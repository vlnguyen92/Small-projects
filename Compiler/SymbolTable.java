/************************************************************************
*																		*
*	Program:	SymbolTable.java										*
*																		*
*	Author:		Robb T. Koether											*
*																		*
*	Date:		Feb 21, 2004											*
*																		*
*	Purpose:	This file contains several functions used to manage		*
*				the symbol table										*
*																		*
************************************************************************/

import java.io.*;
import java.util.*;

/************************************************************************
*																		*
*	The definition of the SymbolTable class								*
*																		*
************************************************************************/

public class SymbolTable
{
//	Constants
	
	public static final int RESWORD = 1;
	public static final int GLOBAL = 2;
	public static final int LOCAL = 3;

//	Class variables

	public static LinkedList<Hashtable<String, IdEntry>> id_table;
											// List of tables of identifiers
											// from high block level to low
	public static int level = 0;			// Current block level
	
	public static int f_dcl_size = 0;			// No. of bytes in local variables
	public static int f_arg_size = 0;		// No. of bytes in arg list
	public static int ret_type;				// Function return type

	/************************************************************************
*																		*
*	Function:	init													*
*																		*
*	Purpose:	To initialize the symbol table							*
*																		*
************************************************************************/

	public static void init()
	{
		id_table = new LinkedList<Hashtable<String, IdEntry>>();
		id_table.addLast(null);		// Entry 0
		return;
	}
	
/************************************************************************
*																		*
*	Function:	init_keywords											*
*																		*
*	Purpose:	To create a new hash table for the identifier strings	*
*																		*
************************************************************************/
	
	public static void init_keywords()
	{
	//	System.out.println("Store key words");
		install_keyword(sym.INT, "int");
		install_keyword(sym.DOUBLE, "double");
		install_keyword(sym.IF, "if");
		install_keyword(sym.ELSE, "else");
		install_keyword(sym.RETURN, "return");
		install_keyword(sym.READ, "read");
		install_keyword(sym.PRINT, "print");
		install_keyword(sym.WHILE, "while");
		install_keyword(sym.FOR, "for");
		return;
	}

/************************************************************************
*																		*
*	Function:	install_keyword											*
*																		*
*	Purpose:	To install a keyword in the symbol table				*
*																		*
************************************************************************/

	public static void install_keyword(int t, String s)
	{
		IdEntry p = install(s, 1);
		p.type = t;
	}

/************************************************************************
*																		*
*	Function:	id_lookup												*
*																		*
*	Purpose:	To lookup an identifier by name and block level and 	*
*				return a reference to it								*
*																		*
*	Note:		If the specified block level is 0, then the function	*
*				will search all block levels							*
*																		*
************************************************************************/
	
	public static IdEntry id_lookup(String s, int blk_lev)
	{
		IdEntry id = null;
	
	// Search all hashtables for entry, starting with top level
	
		if (blk_lev == 0)
		{
			for (int i = id_table.size() - 1; i > 0; i--)
			{
				Hashtable<String, IdEntry> t = id_table.get(i);
				id = t.get(s);
				if (id != null)
					break;
			}
		}
	
	//	Search only the specified table
	
		else
		{
			Hashtable<String, IdEntry> t = id_table.get(blk_lev);
			id = t.get(s);
		}
		return id;
	}
	
/************************************************************************
*																		*
*	Function:	install													*
*																		*
*	Purpose:	To install a name at a specified block level in the 	*
*				symbol table and then return a reference to it			*
*																		*
*	Note:		If the specified block level is negative, then the 		*
*				identifier will be installed at the current block level	*
*																		*
************************************************************************/

	public static IdEntry install(String s, int blk_lev)
	{
	//	Use current level if blk_lev == 0
	
		if (blk_lev == 0)
			blk_lev = level;
		
	//	Get hashtable at specified level
	
		Hashtable<String, IdEntry> t = id_table.get(blk_lev);
			
	//	Install entry in hashtable at this level
	
		IdEntry id = new IdEntry();
		id.name = s;
		id.block_level = blk_lev;
		t.put(s, id);
	//	System.out.println("Identifier " + s + " installed in id_table at level " + blk_lev);
		
		return id;
	}

/************************************************************************
*																		*
*	Function:	enter_block												*
*																		*
*	Purpose:	To create a new hash table for identifiers at the next	*
*				level													*
*																		*
*	Note:		This hash table will be linked at the head of the list	*
*				of hash tables											*
*																		*
************************************************************************/
	
	public static void enter_block()
	{
		level++;									// Increase current level
	//	System.out.println("level = " + level);
	
	//	Create new hashtable at top level
	
		id_table.addLast(new Hashtable<String, IdEntry>());			// Create hashtable at top level
		return;
	}

/************************************************************************
*																		*
*	Function:	leave_block												*
*																		*
*	Purpose:	To delete the hash table of the identifiers at the 		*
*				current highest block level								*
*																		*
************************************************************************/
	
	public static void leave_block()
	{
		if (level > 0)
		{
			id_table.removeLast();	// Remove top-level hashtable
			level--;				// Reduce current level
	//		System.out.println("level = " + level);
		}
	}

/************************************************************************
*																		*
*	Function:	id_dump													*
*																		*
*	Purpose:	To print a list of all identifiers						*
*																		*
************************************************************************/
	
	public static void id_dump()
	{
		System.out.println("Dumping the identifier table");
		for (int i = 0; i < id_table.size(); i++)
		{
			Hashtable<String, IdEntry> ht = id_table.get(i);
			if (ht != null)
			{
				System.err.println("Symbol Table Level " + i);
				for (Enumeration<IdEntry> e = ht.elements(); e.hasMoreElements();)
					System.err.println(e.nextElement());
			}
		}
		return;
	}
}
