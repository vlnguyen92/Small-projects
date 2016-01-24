public class TreeOps
{
	public static final int LEAF_NODE 		= 0;
	public static final int UNARY_NODE		= 1;
	public static final int BINARY_NODE 	= 2;
		
	int op_code;			// Op code for the operator
	String op_string;	// String version of the operation
	int kind;			// Kind of tree node (leaf, unary, binary)
	
/****************************************************************************
*																			*
*	Function:	TreeOps														*
*																			*
*	Purpose:	To construct a TreeOps object from the specified	 		*
*				operation, string, and kind of node (leaf, unary, or		*
*				binary)														*
*																			*
****************************************************************************/

	public TreeOps(int op, String str, int k)
	{
		op_code = op;
		op_string = str;
		kind = k;
	}
	
/****************************************************************************
*																			*
*	Function:	base_type													*
*																			*
*	Purpose:	To return the base type of the object (int or double)		*
*																			*
****************************************************************************/

	public static int base_type(int type)
	{
	//	Use & operator to strip off PTR part, if present
	
		return type & (Ops.INT|Ops.DOUBLE|Ops.CHAR);
	}
	
/****************************************************************************
*																			*
*	Function:	base_size													*
*																			*
*	Purpose:	To return the size of the base object						*
*																			*
****************************************************************************/

	public static int base_size(int type)
	{
		switch (base_type(type))
		{
			case Ops.INT:
				return 4;
			case Ops.DOUBLE:
				return 8;
			case Ops.CHAR:
				return 4;
			default:
				Err.display_msg(Err.UNRECTYP, Integer.toString(type));
				return 0;
		}
	}
}
