public class Utility
{
//	Label number

	public static int label = 0;
	
//	Array of information about of tree node operations

	public static TreeOps[] op_info = 
	{
		new TreeOps(Ops.ERROR, "ERROR", 0),
		new TreeOps(Ops.ALLOC, "ALLOC", TreeOps.BINARY_NODE),
		new TreeOps(Ops.ID, "ID", TreeOps.LEAF_NODE),
		new TreeOps(Ops.NUM, "NUM", TreeOps.LEAF_NODE),
		new TreeOps(Ops.PLUS, "PLUS", TreeOps.BINARY_NODE),
		new TreeOps(Ops.MINUS, "MINUS", TreeOps.BINARY_NODE),
		new TreeOps(Ops.TIMES, "TIMES", TreeOps.BINARY_NODE),
		new TreeOps(Ops.DIVIDE, "DIVIDE", TreeOps.BINARY_NODE),
		new TreeOps(Ops.MOD, "MOD", TreeOps.BINARY_NODE),
		new TreeOps(Ops.NEGATE, "NEGATE", TreeOps.UNARY_NODE),
		new TreeOps(Ops.ASSIGN, "ASSIGN", TreeOps.BINARY_NODE),
		new TreeOps(Ops.DEREF, "DEREF", TreeOps.UNARY_NODE),
		new TreeOps(Ops.READ, "READ", TreeOps.UNARY_NODE),
		new TreeOps(Ops.PRINT, "PRINT", TreeOps.UNARY_NODE),
		new TreeOps(Ops.CAST, "CAST", TreeOps.UNARY_NODE),
		new TreeOps(Ops.LABEL, "LABEL", TreeOps.LEAF_NODE),
		new TreeOps(Ops.BLABEL, "BLABEL", TreeOps.LEAF_NODE),
		new TreeOps(Ops.JUMP, "JUMP", TreeOps.UNARY_NODE),
		new TreeOps(Ops.JUMPT, "JUMPT", TreeOps.BINARY_NODE),
		new TreeOps(Ops.EQU, "EQU", TreeOps.BINARY_NODE),
		new TreeOps(Ops.CMPEQ, "CMPEQ", TreeOps.BINARY_NODE),
		new TreeOps(Ops.CMPNE, "CMPNE", TreeOps.BINARY_NODE),
		new TreeOps(Ops.CMPLT, "CMPLT", TreeOps.BINARY_NODE),
		new TreeOps(Ops.CMPLE, "CMPLE", TreeOps.BINARY_NODE),
		new TreeOps(Ops.CMPGT, "CMPGT", TreeOps.BINARY_NODE),
		new TreeOps(Ops.CMPGE, "CMPGE", TreeOps.BINARY_NODE),
		new TreeOps(Ops.FUNC, "FUNC", TreeOps.BINARY_NODE),
		new TreeOps(Ops.RET, "RET", TreeOps.UNARY_NODE),
		new TreeOps(Ops.FEND, "FEND", TreeOps.UNARY_NODE),
		new TreeOps(Ops.LIST, "LIST", TreeOps.BINARY_NODE),
		new TreeOps(Ops.CALL, "CALL", TreeOps.BINARY_NODE),
		new TreeOps(Ops.STR, "STR", TreeOps.LEAF_NODE),
		new TreeOps(Ops.NULL, "NULL", TreeOps.LEAF_NODE),
		new TreeOps(Ops.APARAM, "APARAM", TreeOps.BINARY_NODE)
	};
	
/************************************************************************
*																		*
*	Function:	print_tree												*
*																		*
*	Purpose:	To draw the abstract syntax tree						*2
*																		*
************************************************************************/

	public static void print_tree(TreeNode t)
	{
	//	System.out.println("print_tree()");
		
		if (mycompiler.tree)
		{
			print_tree(t, 0);
			//System.out.println("DONE");
		}
		
		if (mycompiler.code)
			CodeGenerator.generate_code(t);
		
		if (mycompiler.tree)
			System.out.println();
		//System.out.println("Leaving print-tree");
		return;
	}
	
/************************************************************************
*																		*
*	Function:	print_tree												*
*																		*
*	Purpose:	To draw the abstract syntax tree recursively			*
*																		*
************************************************************************/

	public static void print_tree(TreeNode node, int indent)
	{
		if (node != null)
		{
		//	Indent
		
			for (int i = 0; i < indent; i++)
				System.out.print("   ");
		
		//	Print current node and increase indentation
		
			print_node(node);
			indent++;
		
		//	Print left and right subtrees for a binary node
		
			if (op_info[node.oper].kind == TreeOps.BINARY_NODE)
			{
				//System.out.println("Print left subtree");
				print_tree(node.left, indent);
				//System.out.println("Print right subtree");
				print_tree(node.right, indent);
			}
		
		//	Print left subtree for a unary node
		
			else if (op_info[node.oper].kind == TreeOps.UNARY_NODE)
				print_tree(node.left, indent);
		}
		else
			Warning.display_msg(Warning.NULLTREE);
		return;
	}
	
/************************************************************************
*																		*
*	Function:	print_node												*
*																		*
*	Purpose:	To draw a single node of the abstract syntax tree		*
*																		*
************************************************************************/

	public static void print_node(TreeNode node)
	{
	//	Print node operation

		System.out.print(op_info[node.oper].op_string + " ");
	
	//	Print node mode

		print_type(node.mode);

		if (op_info[node.oper].kind == TreeOps.LEAF_NODE)
		{
			switch (node.oper)
			{
				case Ops.ID:
					System.out.print(" value=\"" + node.id.name + "\"");
					break;
				case Ops.NUM:
					if (node.mode == Ops.INT)
						System.out.print(" value=" + node.num);
					else
						System.out.print(" value=" + node.dbl);
					break;
				case Ops.STR:
					System.out.print(" value = " + node.str);
					break;
				case Ops.BLABEL:
					System.out.print(" blabel = " + node.num);
					break;
				case Ops.LABEL:
					System.out.print(" label = " + node.num);
					break;
				case Ops.NULL:
					break;
				default:
					Err.display_msg(Err.ILLOPCOD, op_info[node.oper].op_string);
					System.exit(1);
			}
		}
		System.out.println();
	}
	
/************************************************************************
*																		*
*	Function:	print_type												*
*																		*
*	Purpose:	To print the data type of a node						*
*																		*
************************************************************************/

	public static void print_type(int type)
	{
//		System.err.println("print_type()");

		if ((type & Ops.PTR) != 0)
			System.out.print("PTR|");
		
		if ((type & Ops.PROC) != 0)
			System.out.print("PROC|");
			
		if ((type & Ops.INT) != 0)
			System.out.print("INT");
		else if ((type & Ops.DOUBLE) != 0)
			System.out.print("DBL");
		else if ((type & Ops.CHAR) != 0)
			System.out.print("CHAR");
	}
}
