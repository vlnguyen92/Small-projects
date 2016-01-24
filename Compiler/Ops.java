/************************************************************************
*	Purpose:	This file defines the Ops class.  The Ops class 		*
*				contains the symbolic constants representing the 		*
*				actions represented by the nodes of the tree.			*
*																		*
************************************************************************/

public class Ops
{
//	Types of node

	public static final int ERROR   = 0;
	public static final int ALLOC   = 1;
	public static final int ID	    = 2;
	public static final int NUM	    = 3;
	public static final int PLUS	= 4;
	public static final int MINUS	= 5;
	public static final int TIMES	= 6;
	public static final int DIVIDE  = 7;
	public static final int MOD	    = 8;
	public static final int NEGATE  = 9;
	public static final int ASSIGN  = 10;
	public static final int DEREF	= 11;
	public static final int READ	= 12;
	public static final int PRINT	= 13;
	public static final int CAST    = 14;
	public static final int LABEL   = 15;
	public static final int BLABEL  = 16;
	public static final int JUMP    = 17;
	public static final int JUMPT   = 18;
	public static final int EQU		= 19;
	public static final int CMPEQ	= 20;
	public static final int CMPNE	= 21;
	public static final int CMPLT	= 22;
	public static final int CMPLE	= 23;
	public static final int CMPGT	= 24;
	public static final int CMPGE	= 25;
	public static final int FUNC    = 26;
	public static final int RET		= 27;
	public static final int FEND	= 28;
	public static final int LIST	= 29;
	public static final int CALL	= 30;
	public static final int STR 	= 31;
	public static final int NULL	= 32;
	public static final int APARAM	= 33;
	
//	Scope constants

	public static final int GLOBAL 	= 0;
	public static final int LOCAL 	= 1;
	public static final int PARAM 	= 2;

//	Data type constants

	public static final int INT = 1 << 0;	// Set bit 0
	public static final int PTR = 1 << 1;	// Set bit 1
	public static final int DOUBLE = 1 << 2;	// Set bit 2
	public static final int PROC = 1 << 3;	// Set bit 3
	public static final int CHAR = 1 << 4;	// Set bit 4
	
//	Data type names

	public static String[] type_name = 
	{
		"INT",
		"PTR",
		"DBL"
	};
}
