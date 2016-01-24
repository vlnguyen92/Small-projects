import java.io.*;
import java.util.*;

public class BackpatchNode
{
	public LinkedList<Integer> true_list;
	public LinkedList<Integer> false_list;
	
	BackpatchNode()
	{
		true_list = null;
		false_list = null;
	}
	
	BackpatchNode(LinkedList<Integer> t_list, LinkedList<Integer> f_list)
	{
		true_list = t_list;
		false_list = f_list;
	}
}