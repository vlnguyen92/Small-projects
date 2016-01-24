import java.util.*;

public class SemanticAction
{
	public static int label_num = 0;
	
/************************************************************************
*																		*
*	Function:	and_op													*
*																		*
*	Purpose:	To create a node the 'and' operator						*
*																		*
************************************************************************/

public static BackpatchNode and_op(BackpatchNode c1, Integer m1, BackpatchNode c2)
{
	backpatch(c1.true_list, m1);
	return new BackpatchNode(c2.true_list, merge(c1.false_list, c2.false_list));
}

/************************************************************************
*																		*
*	Function:	or_op													*
*																		*
*	Purpose:	To create a node the 'or' operator						*
*																		*
************************************************************************/

public static BackpatchNode or_op(BackpatchNode c1, Integer m1, BackpatchNode c2)
{
	backpatch(c1.false_list, m1);
	return new BackpatchNode(merge(c1.true_list, c2.true_list), c2.false_list);
}

/************************************************************************
*																		*
*	Function:	not_op													*
*																		*
*	Purpose:	To create a node the 'not' operator						*
*																		*
************************************************************************/

public static BackpatchNode not_op(BackpatchNode c)
{
	return new BackpatchNode(c.false_list, c.true_list);
}

/************************************************************************
*																		*
*	Function:	arith													*
*																		*
*	Purpose:	To create a node for a binary arithmetic operator		*
*																		*
************************************************************************/

public static TreeNode arith(int op, TreeNode t1, TreeNode t2)
{
	if (t1.mode == t2.mode)
		return new TreeNode(op, t1.mode, t1, t2);
	else
		return new TreeNode(op, Ops.DOUBLE, cast(t1, Ops.DOUBLE), cast(t2, Ops.DOUBLE));
}
	
/************************************************************************
*																		*
*	Function:	arg														*
*																		*
*	Purpose:	To process a formal argument of a function				*
*																		*
************************************************************************/

	public static void arg(String s, Integer type)
	{
		// System.out.println("arg()");
	
		IdEntry id = SymbolTable.id_lookup(s, SymbolTable.LOCAL);
		if (id != null)
			Err.display_msg(Err.ARGMULTDF, "argument name " + s);
		else
		{
			id = SymbolTable.install(s, SymbolTable.level);
			id.type = Ops.PTR|type.intValue();
			id.scope = Ops.PARAM;
			id.offset = SymbolTable.f_arg_size;
			SymbolTable.f_arg_size += TreeOps.base_size(type);
		}
	}
	
/************************************************************************
*																		*
*	Function:	assign													*
*																		*
*	Purpose:	To create a node for an assignment operator				*
*																		*
************************************************************************/

public static TreeNode assign(TreeNode v, TreeNode e)
{
	int t = TreeOps.base_type(v.mode);
	return new TreeNode(Ops.ASSIGN, t, v, cast(e, t));
}

/************************************************************************
*																		*
*	Function:	backpatch												*
*																		*
*	Purpose:	To backpatch a list of backpatch labels to an actual 	*
*				label													*
*																		*
************************************************************************/

public static void backpatch(LinkedList<Integer> b, Integer labl)
{
	//System.out.println("backpatch()");
	for (int i = 0; i < b.size(); i++)
	{
		TreeNode blab_tree = new TreeNode(Ops.BLABEL, b.get(i));
		TreeNode lab_tree = new TreeNode(Ops.LABEL, labl);
		TreeNode equ_tree = new TreeNode(Ops.EQU, Ops.INT, blab_tree, lab_tree);
		Utility.print_tree(equ_tree);
	}
	return;
}

/************************************************************************
*																		*
*	Function:	call													*
*																		*
*	Purpose:	To create a node for a function call					*
*																		*
************************************************************************/

public static TreeNode call(String s, TreeNode e)
{
	// System.out.println("Semantic action call()");
	
	IdEntry id = SymbolTable.id_lookup(s, SymbolTable.GLOBAL);
	if (id == null)
	{
		id = SymbolTable.install(s, SymbolTable.GLOBAL);
		id.type = Ops.PTR|Ops.PROC|Ops.INT;
		id.scope = Ops.GLOBAL;
	}
	TreeNode t = new TreeNode(id);
	if (e == null)
		e = new TreeNode();
	return new TreeNode(Ops.CALL, TreeOps.base_type(id.type), t, e);
}

/************************************************************************
*																		*
*	Function:	cast													*
*																		*
*	Purpose:	To create a node for a cast operation					*
*																		*
************************************************************************/

public static TreeNode cast(TreeNode e, int t)
{
	if (TreeOps.base_type(e.mode) != TreeOps.base_type(t))
		return new TreeNode(Ops.CAST, t, e, null);
	else
		return e;
}

/************************************************************************
*																		*
*	Function:	dcl														*
*																		*
*	Purpose:	To process a declaration								*
*																		*
************************************************************************/

	public static IdEntry dcl(String s, Integer type)
	{
		// System.out.println("dcl()");
	
		IdEntry id = SymbolTable.id_lookup(s, SymbolTable.level);
		if (id != null)
		{
			Err.display_msg(Err.MULTDECV, s);
			return id;
		}
		else
		{
			id = SymbolTable.install(s, SymbolTable.level);
			id.type = type.intValue();
		}
	
	//	Declaration is global
	
		if (SymbolTable.level == SymbolTable.GLOBAL)
		{
			id.scope = Ops.GLOBAL;
		
		//	Create and print an allocation tree

			TreeNode t1 = new TreeNode(id);
			TreeNode t2 = new TreeNode(TreeOps.base_size(id.type));			
			TreeNode t3 = new TreeNode(Ops.ALLOC, id.type, t1, t2);
			Utility.print_tree(t3);
		}
	
	//	Declaration is local
	
		else
		{
			id.scope = Ops.LOCAL;
			SymbolTable.f_dcl_size += TreeOps.base_size(id.type);
			id.offset = -SymbolTable.f_dcl_size;
		}

		return id;
	}
	
/************************************************************************
*																		*
*	Function:	deref													*
*																		*
*	Purpose:	To create a node for a dereference operation			*
*																		*
************************************************************************/

public static TreeNode deref(TreeNode v)
{
	return new TreeNode(Ops.DEREF, TreeOps.base_type(v.mode), v, null);
}

/************************************************************************
*																		*
*	Function:	expr_to_cexpr											*
*																		*
*	Purpose:	To build a JUMPT tree from an expression				*
*																		*
************************************************************************/

public static BackpatchNode expr_to_cexpr(TreeNode e)
{
	TreeNode zero_tree;
	if (e.mode == Ops.INT)
		zero_tree = new TreeNode(0);
	else
		zero_tree = new TreeNode(0.0);
	TreeNode cmp_tree = new TreeNode(Ops.CMPNE, e.mode, zero_tree, e);
	int true_labl = new_label();
	int false_labl = new_label();
	TreeNode jmpt_tree = new TreeNode(Ops.JUMPT, Ops.INT, new TreeNode(Ops.BLABEL, true_labl), cmp_tree);
	Utility.print_tree(jmpt_tree);
	TreeNode jmpf_tree = new TreeNode(Ops.JUMP, Ops.INT, new TreeNode(Ops.BLABEL, false_labl), null);
	Utility.print_tree(jmpf_tree);
	return new BackpatchNode(make_list(true_labl), make_list(false_labl));
}

/************************************************************************
*																		*
*	Function:	fbeg													*
*																		*
*	Purpose:	To enter a function name in the symbol table			*
*																		*
************************************************************************/

	public static IdEntry fbeg(IdEntry id)
	{
		// System.out.println("fbeg()");

		TreeNode t1 = new TreeNode(id);
		TreeNode t2 = new TreeNode(SymbolTable.f_dcl_size);
		TreeNode t = new TreeNode(Ops.FUNC, TreeOps.base_type(id.type), t1, t2);
		Utility.print_tree(t);
		return id;
	}
	
/************************************************************************
*																		*
*	Function:	fname													*
*																		*
*	Purpose:	To enter a function name in the symbol table			*
*																		*
************************************************************************/

	public static IdEntry fname(String s, Integer type)
	{
		// System.out.println("fname()");

		IdEntry id = null;
		if (SymbolTable.level != SymbolTable.GLOBAL)
			Err.display_msg(Err.FNCNOTGLB, "function name = " + s);
		else
		{
			id = SymbolTable.id_lookup(s, SymbolTable.level);
			if (id == null)
			{
				id = SymbolTable.install(s, SymbolTable.level);
				id.scope = Ops.GLOBAL;
				id.type = Ops.PTR|Ops.PROC|type.intValue();
				SymbolTable.f_arg_size = 8;
				SymbolTable.f_dcl_size = 0;
				SymbolTable.ret_type = type.intValue();
				SymbolTable.enter_block();
			}
		}

		return id;
	}
	
/************************************************************************
*																		*
*	Function:	func													*
*																		*
*	Purpose:	To backpatch the last statement in a function			*
*																		*
************************************************************************/

public static void func(IdEntry id, LinkedList<Integer> s, Integer m1)
{
	// System.out.println("func()");
	
	TreeNode ret_tree;
	if (TreeOps.base_type(id.type) == Ops.INT)
		ret_tree = new TreeNode(Ops.RET, Ops.INT, new TreeNode(0), null);
	else
		ret_tree = new TreeNode(Ops.RET, Ops.DOUBLE, new TreeNode(0.0), null);
	Utility.print_tree(ret_tree);

	TreeNode t1 = new TreeNode(id);
	TreeNode t = new TreeNode(Ops.FEND, SymbolTable.ret_type, t1, null);
	Utility.print_tree(t);
	SymbolTable.ret_type = 0;
	backpatch(s, m1);
	SymbolTable.leave_block();
	return;
}

/************************************************************************
*																		*
*	Function:	id														*
*																		*
*	Purpose:	To create a node for an identifier						*
*																		*
************************************************************************/

	public static TreeNode id(String s)
	{
		// System.err.println("id()");

		IdEntry p = SymbolTable.id_lookup(s, 0);
		if (p == null)
		{
			Err.display_msg(Err.UNDECVAR, s);
			p = SymbolTable.install(s, SymbolTable.level);		// Put s in the symbol table
			p.type = Ops.INT;
			p.scope = Ops.GLOBAL;
		}

		return new TreeNode(p);
	}

/************************************************************************
*																		*
*	Function:	if_stmt													*
*																		*
*	Purpose:	To build a tree from an if statement					*
*																		*
************************************************************************/

public static LinkedList<Integer> if_stmt(BackpatchNode c, Integer m1, 
LinkedList<Integer> s)
{
	backpatch(c.true_list, m1);
	merge(s, c.false_list);
	return new LinkedList<Integer>(s);
}

/************************************************************************
*																		*
*	Function:	if_else_stmt											*
*																		*
*	Purpose:	To build a tree from an if-else statement				*
*																		*
************************************************************************/

public static LinkedList<Integer> if_else_stmt(BackpatchNode c, Integer m1, 
LinkedList<Integer> s1, LinkedList<Integer> n1, Integer m2, LinkedList<Integer> s2)
{
	backpatch(c.true_list, m1);
	backpatch(c.false_list, m2);
	merge(s2, merge(s1, n1));
	return new LinkedList<Integer>(s2);
}

/************************************************************************
*																		*
*	Function:	m														*
*																		*
*	Purpose:	To create a label_num									*
*																		*
************************************************************************/

	public static Integer m()
	{
		TreeNode t = new TreeNode(Ops.LABEL, new_label());
		Utility.print_tree(t);
		return new Integer(label_num);
	}
	
/************************************************************************
*																		*
*	Function:	make_list												*
*																		*
*	Purpose:	To create a LinkedList of labels, initialized with a 	*
*				specified label											*
*																		*
************************************************************************/

	public static LinkedList<Integer> make_list(int labl)
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(new Integer(labl));
		return list;
	}
	
/************************************************************************
*																		*
*	Function:	merge													*
*																		*
*	Purpose:	To merge two LinkedLists into one LinkedList		*
*																		*
************************************************************************/

	public static LinkedList<Integer> merge(LinkedList<Integer> b1, LinkedList<Integer> b2)
	{
		b1.addAll(b2);
		return b1;
	}
	
/************************************************************************
*																		*
*	Function:	mod														*
*																		*
*	Purpose:	To create a node for a binary arithmetic operator		*
*																		*
************************************************************************/

public static TreeNode mod(TreeNode e1, TreeNode e2)
{
	if (TreeOps.base_type(e1.mode) != Ops.INT || TreeOps.base_type(e2.mode) != Ops.INT)
	{
		Warning.display_msg(Warning.MODTOINT);
		return new TreeNode(Ops.MOD, Ops.INT, cast(e1, Ops.INT), cast(e2, Ops.INT));
	}
	else
		return new TreeNode(Ops.MOD, Ops.INT, e1, e2);
}

/************************************************************************
*																		*
*	Function:	n														*
*																		*
*	Purpose:	To create a jump tree									*
*																		*
************************************************************************/

	public static LinkedList<Integer> n()
	{
		int labl = new_label();
		TreeNode t1 = new TreeNode(Ops.BLABEL, labl);
		TreeNode t2 = new TreeNode(Ops.JUMP, Ops.INT, t1, null);
		Utility.print_tree(t2);
		return make_list(labl);
	}
	
/************************************************************************
*																		*
*	Function:	negate													*
*																		*
*	Purpose:	To create a node for the negation operator				*
*																		*
************************************************************************/

public static TreeNode negate(TreeNode e)
{
	return new TreeNode(Ops.NEGATE, e.mode, e, null);
}

/************************************************************************
*																		*
*	Function:	new_label												*
*																		*
*	Purpose:	To increment the label_num class variable and return 	*
*				its value												*
*																		*
************************************************************************/

public static int new_label()
{
	return ++label_num;
}

/************************************************************************
*																		*
*	Function:	print													*
*																		*
*	Purpose:	To create a node for calling the printf() function		*
*																		*
************************************************************************/

public static void print(TreeNode v)
{
	TreeNode t1 = deref(v);
	TreeNode t = new TreeNode(Ops.PRINT, t1.mode, t1, null);
	Utility.print_tree(t);
	return;
}

/************************************************************************
*																		*
*	Function:	read													*
*																		*
*	Purpose:	To create a node for calling the scanf() function		*
*																		*
************************************************************************/

public static void read(TreeNode v)
{
	TreeNode t = new TreeNode(Ops.READ, v.mode, v, null);
	Utility.print_tree(t);
	return;
}
/************************************************************************
*																		*
*	Function:	rel_op													*
*																		*
*	Purpose:	To create a node for a relative operator				*
*																		*
************************************************************************/

public static BackpatchNode rel_op(int op, TreeNode e1, TreeNode e2)
{
	TreeNode t;
	if (e1.mode == Ops.INT && e2.mode == Ops.INT)
		t = new TreeNode(op, Ops.INT, e1, e2);
	else
		t = new TreeNode(op, Ops.DOUBLE, cast(e1, Ops.DOUBLE), cast(e2, Ops.DOUBLE));
	return rel_op_to_cexpr(t);
}

/************************************************************************
*																		*
*	Function:	rel_op_to_cexpr											*
*																		*
*	Purpose:	To create a the jump trees for a relative operator		*
*																		*
************************************************************************/

public static BackpatchNode rel_op_to_cexpr(TreeNode t)
{
	int true_labl = new_label();
	int false_labl = new_label();
	TreeNode jmpt_tree = new TreeNode(Ops.JUMPT, Ops.INT, new TreeNode(Ops.BLABEL, true_labl), t);
	Utility.print_tree(jmpt_tree);
	TreeNode jmpf_tree = new TreeNode(Ops.JUMP, Ops.INT, new TreeNode(Ops.BLABEL, false_labl), null);
	Utility.print_tree(jmpf_tree);
	return new BackpatchNode(make_list(true_labl), make_list(false_labl));
}

/************************************************************************
*																		*
*	Function:	ret														*
*																		*
*	Purpose:	To create a node for return statement					*
*																		*
************************************************************************/

	public static LinkedList<Integer> ret(TreeNode e)
	{
		// System.out.println("ret()");

		TreeNode t = new TreeNode(Ops.RET, SymbolTable.ret_type, cast(e, SymbolTable.ret_type), null);
		Utility.print_tree(t);
		return new LinkedList<Integer>();
	
	}
/************************************************************************
*																		*
*	Function:	stmts													*
*																		*
*	Purpose:	To backpatch stmts										*
*																		*
************************************************************************/

public static LinkedList<Integer> stmts(LinkedList<Integer> s1, Integer m1, LinkedList<Integer> s2)
{
	backpatch(s1, m1);
	return s2;
}

/************************************************************************
*																		*
*	Function:	str														*
*																		*
*	Purpose:	To handle a string tree node							*
*																		*
************************************************************************/

public static TreeNode str(String s)
{
	return new TreeNode();
}

/************************************************************************
*																		*
*	Function:	while_stmt												*
*																		*
*	Purpose:	To backpatch branches in a while statement				*
*																		*
************************************************************************/

public static LinkedList<Integer> while_stmt(Integer m1, BackpatchNode c, Integer m2, 
LinkedList<Integer> s1, LinkedList<Integer> n1)
{
	backpatch(c.true_list, m2);
	backpatch(merge(s1, n1), m1);
	return new LinkedList<Integer>(c.false_list);
}
/************************************************************************
*																		*
*	Function:	for_stmt												*
*																		*
*	Purpose:	To backpatch branches in a for statement				*
*																		*
************************************************************************/

public static LinkedList<Integer> for_stmt(Integer m1, BackpatchNode c,
Integer m2, Integer m3, LinkedList<Integer> s, LinkedList<Integer> n2)
{
	backpatch(c.true_list, m3);
	backpatch(s, m2);
	backpatch(n2, m1);
	return new LinkedList<Integer>(c.false_list);
//	backpatch(c.true_list, m3);
//	backpatch(merge(s, n2), m2);
//	backpatch(n1, m1);
//	return new LinkedList<Integer>(c.false_list);
}

}
