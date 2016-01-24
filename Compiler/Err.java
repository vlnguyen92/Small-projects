class Err
{
	public static int count = 0;
	
	public static int UNDECVAR	= 0;
	public static int MULTDECV	= 1;
	public static int MULTDECL  = 2;
	public static int UNRECTYP	= 3;
	public static int ILLCHAR   = 4;
	public static int ILLOPCOD	= 5;
	public static int SYNTAX    = 6;
	public static int FNCNOTGLB	= 7;
	public static int ARGMULTDF = 8;
	
	public static String msg[] =
	{	
		"Undeclared variable ",
		"Multiply declared variable ",
		"Multiply declared variable",
		"Unrecognized type ",
		"Illegal character",
		"Illegal op code",
		"Syntax error",
		"Function not global",
		"Argument multiply defined"
	};
	
	public static void display_msg(int err_num)
	{
		System.err.println("Error: (line " + Yylex.line_num + ") " + msg[err_num]);
		count++;
		return;
	}

//	Display the specified error message with additional text

	public static void display_msg(int err_num, String text)
	{
		System.err.println("Error: (line " + Yylex.line_num + ") " + msg[err_num] + ": " + text);
		count++;
		return;
	}
}