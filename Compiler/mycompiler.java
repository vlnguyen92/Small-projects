import java.io.*;
import java_cup.runtime.*;

/************************************************************************
*																		*
*	Definition of the mycompiler class									*
*																		*
************************************************************************/

public class mycompiler
{
	public static boolean tree = false;
	public static boolean code = false;
	
/************************************************************************
*																		*
*	Function:	main													*
*																		*
************************************************************************/

	public static void main(String[] args) throws IOException
	{
	//	Process command-line arguments
	
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-t"))
				tree = true;
			else if (args[i].equals("-c"))
				code = true;
		}

	//	Initialize the symbol table
	
		SymbolTable.init();
		
	//	Move to level 1 and register reserved words in the symbol table
	
		SymbolTable.enter_block();
		SymbolTable.init_keywords();
	
	//	Move to level 2 and create a parser
	
		SymbolTable.enter_block();
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		parser p = new parser(new Yylex(br));
	
	//	Parse the program
	
		try
		{
			p.parse();
		}
		catch (Exception e)
		{
			Err.display_msg(Err.SYNTAX);
		}

	//	Report the number of errors and warnings

		if (Err.count > 0)
			System.err.println("There were " + Err.count + " error(s) reported");
		if (Warning.count > 0)
			System.err.println("There were " + Warning.count + " warning(s) reported");
	}
}
