import java.util.*;

public class CodeGenerator
{
	private static int jmp_label = 0;
	private static Stack<Integer> param_stack = new Stack<Integer>();
	private static int param_block_size = 0;
	
/****************************************************************************
*																			*
*	Function:	generate_code												*
*																			*
*	Purpose:	To write assembly code for the abstract syntax tree			*
*																			*
****************************************************************************/

	public static void generate_code(TreeNode t)
	{
	//	Handle the root node in the appropriate way
	
		switch (t.oper)
		{
			case Ops.ALLOC:
			case Ops.FUNC:
			case Ops.FEND:
				generate_node_code(t);
				break;
			case Ops.EQU:
				System.out.println("# Code for EQU");
				System.out.println("    B" + t.left.num + "=L" + t.right.num);
				break;
			case Ops.JUMP:
				System.out.println("# Code for JUMP");
				if (t.left.oper == Ops.BLABEL)
					System.out.println("    jmp   B" + t.left.num);
				else if (t.left.oper == Ops.LABEL)
					System.out.println("    jmp   L" + t.left.num);
				break;
			case Ops.JUMPT:
				System.out.println("# Code for JUMPT");
				traverse_tree(t.right); 	// Traverse the CMPNE tree
				System.out.println("    mov $1,%eax");
				System.out.println("    cmp %eax,%ecx");
				if (t.left.oper == Ops.BLABEL)
					System.out.println("    je B" + t.left.num);
				else
					System.out.println("    je L" + t.left.num);
				break;
			default:
				traverse_tree(t);
		}
		
	//	Pop the final value if necessary
	
		switch (t.oper)
		{
			case Ops.ALLOC:
			case Ops.EQU:
			case Ops.JUMP:
			case Ops.JUMPT:
			case Ops.LABEL:
			case Ops.FUNC:
			case Ops.FEND:
			case Ops.CALL:
			case Ops.RET:
				break;
			default:
				if (t.mode == Ops.INT)
					System.out.println("	add   $4,%esp		# Pop final value");
				else
					System.out.println("    fstpl  -8(%esp)		# Pop final value from FPU stack");
		}
		return;
	}
	
/****************************************************************************
*																			*
*	Function:	traverse_tree												*
*																			*
*	Purpose:	To traverse the abstract syntax tree using a left-to-		*
*				right post-order traversal, writing assembly code as each 	*
*				node is visited												*
*																			*
****************************************************************************/

	public static void traverse_tree(TreeNode t)
	{
		if (t.oper == Ops.CALL)
			generate_node_code(t);
		else
		{
			if (t.left != null)
				traverse_tree(t.left);
			if (t.right != null)
				traverse_tree(t.right);
			generate_node_code(t);
		}
		return;
	}
		
/****************************************************************************
*																			*
*	Function:	generate_node_code											*
*																			*
*	Purpose:	To write assembly code for a particular node in the 		*
*				abstract syntax tree										*
*																			*
****************************************************************************/

	public static void generate_node_code(TreeNode t)
	{
		switch (t.oper)
		{
			case Ops.ERROR:
				System.out.println("# Code for ERROR");
				break;
			case Ops.ALLOC:
				System.out.println("# Code for ALLOC");
				System.out.println("    .comm " + t.left.id.name + "," + t.right.num + "			# Allocate mem for " + t.left.id.name);
				break;
			case Ops.ASSIGN:
				System.out.println("# Code for ASSIGN");
				if (t.mode == Ops.INT)
				{
					System.out.println("    popl  %eax			# Pop value to be assigned");
					System.out.println("    popl  %edx			# Pop destination address");
					System.out.println("    movl  %eax,(%edx)		# Move value to destination address");
					System.out.println("    pushl %eax			# Push value onto stack");
				}
				else
				{
					System.out.println("    popl  %eax			# Pop destination address");
					System.out.println("    fstl  (%eax)		# Store value from FPU");
				}
				break;
			case Ops.CALL:
				System.out.println("# Code for CALL");
				param_stack.push(param_block_size);
				param_block_size = 0;
			//	if (t.right.oper == Ops.APARAM)
				generate_node_code(t.right);		// Process the APARAM (or NULL) tree
				System.out.println("    call  _" + t.left.id.name + "		# Call the function " + t.left.id.name);
				System.out.println("    addl   $" + param_block_size + ",%esp		# Pop the parameters");
				param_block_size = param_stack.pop();
				if (TreeOps.base_type(t.mode) == Ops.INT)
					System.out.println("    push  %eax			# Retrieve return value from eax");
				break;
			case Ops.APARAM:
				System.out.println("# Code for APARAM");
				traverse_tree(t.left);		// Process expr in left subtree
				if (t.left.mode == Ops.DOUBLE)
				{
					System.out.println("    subl   $8,%esp		# Move stack ptr 8 bytes");
					System.out.println("    fstpl  (%esp)		# Push fp value onto stack");
				}
				param_block_size += TreeOps.base_size(t.left.mode);
				generate_node_code(t.right);
				break;
			case Ops.CAST:
				System.out.println("# Code for CAST");
				if (t.mode == Ops.INT)
				{
					System.out.println("    subl  $4,%esp		# Make room on the stack");
					System.out.println("    fisttpl (%esp)		# Store integer from FPU");
				}
				else
				{
					System.out.println("    fild  (%esp)		# Load integer to FPU");
					System.out.println("    addl  $4,%esp		# 'Pop' value from stack");
				}
				break;
			case Ops.CMPEQ:
				System.out.println("# Code for CMPNE");
				System.out.println("    mov $1,%ecx			# Set ecx to true (1)");
				if (t.mode == Ops.INT)
				{
					System.out.println("    pop %eax			# Load right operand");
					System.out.println("    pop %edx			# Load left operand");
					System.out.println("    cmp %eax,%edx		# Compare operands");
				}
				else
				{
				//	System.out.println("    fcompp				# Compare operands and pop both");
					System.out.println("    fcomip				# Compare operands and pop one");
					System.out.println("    fstpl -8(%esp)		# Increment TOP");
				//	System.out.println("    fnstsw %ax			# Move status word to ax");
				//	System.out.println("    sahf				# Store ah in eflags");
				}
				System.out.println("    je L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left != right");
				System.out.println("    dec %ecx			# Set ecx to false (0)");
				System.out.println("L0" + jmp_label + ":");
				System.out.println("    push %ecx			# Push T/F result");
				break;
			case Ops.CMPNE:
				System.out.println("# Code for CMPNE");
				System.out.println("    mov $1,%ecx			# Set ecx to true (1)");
				if (t.mode == Ops.INT)
				{
					System.out.println("    pop %eax			# Load right operand");
					System.out.println("    pop %edx			# Load left operand");
					System.out.println("    cmp %eax,%edx		# Compare operands");
				}
				else
				{
				//	System.out.println("    fcompp				# Compare operands and pop both");
					System.out.println("    fcomip				# Compare operands and pop one");
					System.out.println("    fstpl -8(%esp)		# Increment TOP");
				//	System.out.println("    fnstsw %ax			# Move status word to ax");
				//	System.out.println("    sahf				# Store ah in eflags");
				}
				System.out.println("    jne L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left == right");
				System.out.println("    dec %ecx			# Set ecx to false (0)");
				System.out.println("L0" + jmp_label + ":");
				System.out.println("    push %ecx			# Push T/F result");
				break;
			case Ops.CMPLT:
				System.out.println("# Code for CMPLT");
				System.out.println("    mov $1,%ecx			# Set ecx to true (1)");
				if (t.mode == Ops.INT)
				{
					System.out.println("    pop %eax			# Load right operand");
					System.out.println("    pop %edx			# Load left operand");
					System.out.println("    cmp %eax,%edx		# Compare operands");
					System.out.println("    jl L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left < right");
				}
				else
				{
				//	System.out.println("    fcompp				# Compare operands and pop both");
					System.out.println("    fcomip				# Compare operands and pop one");
					System.out.println("    fstpl -8(%esp)		# Increment TOP");
				//	System.out.println("    fnstsw %ax			# Move status word to ax");
				//	System.out.println("    sahf				# Store ah in eflags");
					System.out.println("    ja L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left < right");
				}
				System.out.println("    dec %ecx			# Set ecx to false (0)");
				System.out.println("L0" + jmp_label + ":");
				System.out.println("    push %ecx			# Push T/F result");
				break;
			case Ops.CMPLE:
				System.out.println("# Code for CMPLE");
				System.out.println("    mov $1,%ecx			# Set ecx to true (1)");
				if (t.mode == Ops.INT)
				{
					System.out.println("    pop %eax			# Load right operand");
					System.out.println("    pop %edx			# Load left operand");
					System.out.println("    cmp %eax,%edx		# Compare operands");
					System.out.println("    jle L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left <= right");
				}
				else
				{
				//	System.out.println("    fcompp				# Compare operands and pop both");
					System.out.println("    fcomip				# Compare operands and pop one");
					System.out.println("    fstpl -8(%esp)		# Increment TOP");
				//	System.out.println("    fnstsw %ax			# Move status word to ax");
				//	System.out.println("    sahf				# Store ah in eflags");
					System.out.println("    jae L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left <= right");
				}
				System.out.println("    dec %ecx			# Set ecx to false (0)");
				System.out.println("L0" + jmp_label + ":");
				System.out.println("    push %ecx			# Push T/F result");
				break;
			case Ops.CMPGT:
				System.out.println("# Code for CMPGT");
				System.out.println("    mov $1,%ecx			# Set ecx to true (1)");
				if (t.mode == Ops.INT)
				{
					System.out.println("    pop %eax			# Load right operand");
					System.out.println("    pop %edx			# Load left operand");
					System.out.println("    cmp %eax,%edx		# Compare operands");
					System.out.println("    jg L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left > right");
				}
				else
				{
				//	System.out.println("    fcompp				# Compare operands and pop both");
					System.out.println("    fcomip				# Compare operands and pop one");
					System.out.println("    fstpl -8(%esp)		# Increment TOP");
				//	System.out.println("    fnstsw %ax			# Move status word to ax");
				//	System.out.println("    sahf				# Store ah in eflags");
					System.out.println("    jb L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left > right");
				}
				System.out.println("    dec %ecx			# Set ecx to false (0)");
				System.out.println("L0" + jmp_label + ":");
				System.out.println("    push %ecx			# Push T/F result");
				break;
			case Ops.CMPGE:
				System.out.println("# Code for CMPGE");
				System.out.println("    mov $1,%ecx			# Set ecx to true (1)");
				if (t.mode == Ops.INT)
				{
					System.out.println("    pop %eax			# Load right operand");
					System.out.println("    pop %edx			# Load left operand");
					System.out.println("    cmp %eax,%edx		# Compare operands");
					System.out.println("    jge L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left >= right");
				}
				else
				{
				//	System.out.println("    fcompp				# Compare operands and pop both");
					System.out.println("    fcomip				# Compare operands and pop one");
					System.out.println("    fstpl -8(%esp)		# Increment TOP");
				//	System.out.println("    fnstsw %ax			# Move status word to ax");
				//	System.out.println("    sahf				# Store ah in eflags");
					System.out.println("    jbe L0" + (++jmp_label) + "				# Jump to L0" + jmp_label + " if left >= right");
				}
				System.out.println("    dec %ecx			# Set ecx to false (0)");
				System.out.println("L0" + jmp_label + ":");
				System.out.println("    push %ecx			# Push T/F result");
				break;
			case Ops.DEREF:
				System.out.println("# Code for DEREF");
				System.out.println("    popl  %eax			# Pop address");
				if (t.mode == Ops.INT)
					System.out.println("    pushl (%eax)		# Push value");
				else
					System.out.println("    fldl  (%eax)		# Load value to FPU");
				break;
			case Ops.DIVIDE:
				System.out.println("# Code for DIVIDE");
				if (t.mode == Ops.INT)
				{
					System.out.println("    popl  %ecx			# Pop divisor");
					System.out.println("    popl  %eax			# Pop dividend");
					System.out.println("    cdq       			# Extend dividend to 64 bits");
					System.out.println("    idiv  %ecx			# Divide");
					System.out.println("    pushl %eax			# Push quotient");
				}
				else
					System.out.println("    fdivrp				# FPU divide");
				break;
			case Ops.FEND:
				System.out.println("# Code for FEND");
				System.out.println("    mov   %ebp,%esp		# Restore stack pointer");
				System.out.println("    pop   %ebp			# Restore base pointer");
				System.out.println("    ret         		# Return to calling procedure");
				break;
			case Ops.FUNC:
				System.out.println("# Code for FUNC");
				String fname = t.left.id.name;
				int size = t.right.num;
				System.out.println("    .text");
				System.out.println("    .globl _" + fname);
				System.out.println("    .def _" + fname + "; .scl 2; .type 32; .endef");
				System.out.println("_" + fname + ":");
				System.out.println("    push  %ebp			# Save base pointer");
				System.out.println("    mov   %esp,%ebp		# Save stack pointer");
				System.out.println("    sub   $" + size + ",%esp		# Reserve space for locals");
				break;
			case Ops.ID:
				System.out.println("# Code for ID, global = " + t.id.name);
				if (t.id.scope == Ops.GLOBAL)
					System.out.println("    lea   " + t.id.name + ",%eax		# Load address");
				else
					System.out.println("    lea   " + t.id.offset + "(%ebp),%eax		# Load addr from stack");
				System.out.println("    pushl %eax			# Push address");
				break;
			case Ops.LABEL:
				System.out.println("# Code for LABEL");
				System.out.println("L" + t.num + ":");
				break;
			case Ops.MINUS:
				System.out.println("# Code for MINUS");
				if (t.mode == Ops.INT)
				{
					System.out.println("    popl  %eax			# pop right operand");
					System.out.println("    popl  %edx  		# pop left operand");
					System.out.println("    sub  %eax,%edx  	# sub right op from left op");
					System.out.println("    pushl %edx			# push result");
				}
				else
					System.out.println("    fsubrp				# FPU subtract");
				break;
			case Ops.MOD:
				System.out.println("# Code for MOD");
				System.out.println("    popl  %ecx			# Pop divisor");
				System.out.println("    popl  %eax			# Pop dividend");
				System.out.println("    cdq       			# Extend dividend to 64 bits");
				System.out.println("    idiv  %ecx			# Divide");
				System.out.println("    pushl %edx			# Push remainder");
				break;
			case Ops.NEGATE:
				System.out.println("# Code for NEGATE");
				if (t.mode == Ops.INT)
					System.out.println("    negl  (%esp)		# Negate the stack value");
				else
					System.out.println("    fchs				# Negate FPU value");
			//	System.out.println("    popl  %eax			# Pop the value");
			//	System.out.println("    neg   %eax			# Negate the value");
			//	System.out.println("    pushl %eax			# Push the value");
				break;
			case Ops.NULL:
				break;
			case Ops.NUM:
				System.out.println("# Code for NUM");
				if (t.mode == Ops.INT)
				{
					System.out.println("    pushl $" + t.num + "       # Push number onto stack");
				}
				else
				{
					System.out.println("    .data");
					System.out.println("L0" + (++jmp_label) + ":  .double " + t.dbl + "		# Double literal");
					System.out.println("    .text");
					System.out.println("    fldl  L0" + jmp_label + "			# Load double to FPU");
				}
				break;
			case Ops.PLUS:
				System.out.println("# Code for PLUS");
				if (t.mode == Ops.INT)
				{
					System.out.println("    popl  %eax			# pop right operand");
					System.out.println("    popl  %edx  		# pop left operand");
					System.out.println("    addl  %edx,%eax  	# add operands");
					System.out.println("    pushl %eax			# push result");
				}
				else
					System.out.println("    faddp				# FPU add");
				break;
			case Ops.PRINT:
				System.out.println("#	Code for PRINT");
				if (t.mode == Ops.INT)
				{
					System.out.println("	.data");
					System.out.println("L0" + (++jmp_label) + ":	.asciz \"" + t.left.left.id.name + " = %d\\n\"	# Format string");
					System.out.println("	.text");
					System.out.println("	leal L0" + jmp_label + ",%eax		# Load address of format string");
					System.out.println("	pushl %eax			# Push address of format string");
					System.out.println("	call _printf		# Call printf()");
					System.out.println("	addl  $8,%esp		# Pop parameters");
					System.out.println("	push  $0#			# Push dummy value");
				}
				else
				{
					System.out.println("   subl  $8,%esp");
					System.out.println("   fstpl (%esp)");
					System.out.println("	.data");
					System.out.println("L0" + (++jmp_label) + ":	.asciz \"" + t.left.left.id.name + " = %lf\\n\"	# Format string");
					System.out.println("	.text");
					System.out.println("	leal L0" + jmp_label + ",%eax		# Load address of format string");
					System.out.println("	pushl %eax			# Push address of format string");
					System.out.println("	call _printf		# Call printf()");
					System.out.println("    addl  $12,%esp		# Pop parameters");
				}
				break;
			case Ops.READ:
				System.out.println("#	Code for READ");
				
			//	Print prompt
			
				System.out.println("    .data");
				System.out.println("L0" + (++jmp_label) + ":  .asciz   \"Enter " + t.left.id.name + ": \"	# Format string");
				System.out.println("    .text");
				System.out.println("    leal L0" + jmp_label + ",%eax		# Load address of format string");
				System.out.println("    pushl %eax			# Push address of format string");
				System.out.println("    call _printf		# Call printf()");
				System.out.println("    addl  $4,%esp		# Pop parameter");
			
			//	Read number
			
				System.out.println("	.data");
				if (TreeOps.base_type(t.mode) == Ops.INT)
				{
					System.out.println("L0" + (++jmp_label) + ":	.asciz \"%d\"	# Format string");
					System.out.println("	.text");
					System.out.println("	leal L0" + jmp_label + ",%eax		# Load address of format string");
					System.out.println("	pushl %eax			# Push address of format string");
				}
				else
				{
					System.out.println("L0" + (++jmp_label) + ":	.asciz \"%lf\"	# Format string");
					System.out.println("	.text");
					System.out.println("	leal L0" + jmp_label + ",%eax		# Load address of format string");
					System.out.println("	pushl %eax			# Push address of format string");
				}
				System.out.println("	call _scanf			# Call scanf()");
				System.out.println("	addl  $8,%esp		# Pop parameters");
				break;
			case Ops.RET:
				System.out.println("# Code for RET");
				if (t.mode == Ops.INT)
					System.out.println("    pop   %eax		# Pop return value into eax");
				System.out.println("    mov   %ebp,%esp		# Restore stack pointer");
				System.out.println("    pop   %ebp			# Restore base pointer");
				System.out.println("    ret         		# Return to calling procedure");
				break;
			case Ops.STR:
				System.out.println("# Code for STR");
				System.out.println("    .data");
				System.out.println("L0" + (++jmp_label) + ":  .asciz " + t.str);
				System.out.println("    .text");
				System.out.println("    lea L0" + jmp_label + ",%eax		# Load string addr");
				System.out.println("    push %eax			# Push string addr");
			case Ops.TIMES:
				System.out.println("# Code for TIMES");
				if (t.mode == Ops.INT)
				{
					System.out.println("    popl   %eax			# Pop right operand");
					System.out.println("    popl   %edx			# Pop left operand");
					System.out.println("    imul   %edx,%eax	# Multiply the operands");
					System.out.println("    pushl  %eax			# Push the product");
				}
				else
					System.out.println("    fmulp				# FPU multiply");
				break;
			default:
				Err.display_msg(Err.ILLOPCOD, Utility.op_info[t.oper].op_string);
				break;
		}
	}
}
