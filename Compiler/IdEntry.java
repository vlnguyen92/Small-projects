/************************************************************************
*																		*
*	The definition of the IdEntry class									*
*																		*
************************************************************************/

public class IdEntry
{
	public String name;			// Pointer to name in string table
	public int type;			// Type code
	public int block_level;		// Block level
	public int scope;			// Scope
	public int offset;			// Offset in activation frame
	
/************************************************************************
*																		*
*	Function:	toString												*
*																		*
************************************************************************/

	public String toString()
	{
		String s1 = "{name = " + name;
		String s2 = ", type = " + type;
		String s3 = ", block_level = " + block_level;
		String s4 = ", scope = " + scope;
		String s5 = ", offset = " + offset;
		return new String(s1 + s2 + s3 + s4 + s5 + "}");
	}
}
