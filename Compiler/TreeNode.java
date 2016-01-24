public class TreeNode
{
//	Data members

	int oper;
	int mode;

//	Data members for interior nodes

	TreeNode left;
	TreeNode right;
	
//	Data members for leaf nodes

	IdEntry id;
	int num;
	double dbl;
	String str;
	
/****************************************************************************
*																			*
*	Function:	TreeNode													*
*																			*
*	Purpose:	To create an empty node										*
*																			*
****************************************************************************/

public TreeNode()
{
	oper = Ops.NULL;
	mode = 0;
	left = null;
	right = null;
	id = null;
	num = 0;
	dbl = 0.0;
	str = null;
	return;
}

/****************************************************************************
*																			*
*	Function:	TreeNode(int, int, TreeNode, TreeNode)						*
*																			*
*	Purpose:	To create a node for a specified operation, mode, and		*
*				with specified left and right children						*
*																			*
****************************************************************************/

public TreeNode(int op, int md, TreeNode lf, TreeNode rt)
{
	oper = op;
	mode = md;
	left = lf;
	right = rt;
	num = 0;
	dbl = 0.0;
	str = null;
	id = null;
	return;
}

/****************************************************************************
*																			*
*	Function:	TreeNode(int)												*
*																			*
*	Purpose:	To create a node for a number								*
*																			*
****************************************************************************/

public TreeNode(int n)
{
	oper = Ops.NUM;
	mode = Ops.INT;
	left = null;
	right = null;
	num = n;
	dbl = 0.0;
	id = null;
	str = null;
	return;
}

/****************************************************************************
*																			*
*	Function:	TreeNode(String)											*
*																			*
*	Purpose:	To create a node for a string								*
*																			*
****************************************************************************/

public TreeNode(String s)
{
	oper = Ops.STR;
	mode = Ops.PTR|Ops.CHAR;
	left = null;
	right = null;
	num = 0;
	dbl = 0.0;
	id = null;
	str = s;
	return;
}

/****************************************************************************
*																			*
*	Function:	TreeNode(double)											*
*																			*
*	Purpose:	To create a node for a double								*
*																			*
****************************************************************************/

public TreeNode(double d)
{
	oper = Ops.NUM;
	mode = Ops.DOUBLE;
	left = null;
	right = null;
	num = 0;
	dbl = d;
	id = null;
	str = null;
	return;
}

/****************************************************************************
*																			*
*	Function:	TreeNode(op, label)											*
*																			*
*	Purpose:	To create a node for a specified IdEntry					*
*																			*
****************************************************************************/

	public TreeNode(int op, int labl)
	{
		oper = op;
		mode = Ops.INT;
		num = labl;
		return;
	}
	
/****************************************************************************
*																			*
*	Function:	TreeNode(IdEntry)											*
*																			*
*	Purpose:	To create a node for a specified IdEntry					*
*																			*
****************************************************************************/

	public TreeNode(IdEntry i)
	{
		oper = Ops.ID;
		mode = i.type | Ops.PTR;
		id = i;
		return;
	}
	
/****************************************************************************
*																			*
*	Function:	toString													*
*																			*
*	Purpose:	To convert a TreeNode into a String							*
*																			*
****************************************************************************/

	public String toString()
	{
		String s1 = new String("{oper = " + oper);
		String s2 = new String(", mode = " + mode);
		String s3 = null;
		String s4 = null;
	
	//	If node is interior, print left and right children
	
		if (left != null || right != null)
		{
			s3 = new String(", left = " + left);
			s4 = new String(", right = " + right + "}");
			return new String(s1 + s2 + s3 + s4);
		}
	
	//	If node is terminal, print node info
	
		else
		{
			if (id != null)
				s3 = new String(", id = " + id);
			else if (str != null)
				s3 = new String(", str = " + str);
			else
				s3 = new String(", num = " + num + "}");
			return new String(s1 + s2 + s3);
		}
	}
}
