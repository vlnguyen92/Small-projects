class Warning
{
	public static int count = 0;
	
//	List of recognized warnings

	public static int NULLTREE	= 0;
	public static int MODTOINT   = 1;
	
	public static String msg[] =
	{	
		"Null tree",
		"Mod operands converted to int"
	};
	
//	Display the specified warning

	public static void display_msg(int warn_num)
	{
		System.err.println("Warning: (line " + Yylex.line_num + ") " + msg[warn_num]);
		count++;
		return;
	}
	
//	Display the specified warning with additional text

	public static void display_msg(int warn_num, String text)
	{
		System.err.println("Warning: (line " + Yylex.line_num + ") " + msg[warn_num] + text);
		count++;
		return;
	}
}
