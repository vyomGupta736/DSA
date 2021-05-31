import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
public class FabricBreakup{	
	public static void main(String args[])
	{
		try
		{
			File file = new File(args[0]); 
			Scanner sc = new Scanner(file); 
			StackInterface myStack = new Stack();
			StackInterface track = new Stack();
			while (sc.hasNextLine()) 
			{
				String s = sc.nextLine().trim();
				int index = s.trim().indexOf(' ');
				if(index != -1)
				{
					String no = s.substring(0, s.indexOf(' '));
					char oper = s.charAt(s.indexOf(' ')+1);
					if(oper == '1')
					{
						int shirtNumber = Integer.parseInt(s.substring(s.lastIndexOf(' ')+1));
						if(myStack.size() == 0)
						{
							track.push(shirtNumber);
							myStack.push(shirtNumber);
						}
						else
						{
							myStack.push(shirtNumber);
							try
							{
								if((int)track.top() > shirtNumber)
								{
									track.push((int)track.top());
								}
								else
								{
									track.push(shirtNumber);
								}
							}catch(EmptyStackException e)
							{
								System.out.println("Empty stack");
							}
						}
					}
					else if(oper == '2')
					{
						int toppled = 0;
						if(myStack.size() == 0)
						{
							System.out.println(no+" "+"-1");
						}
						else
						{
							try
							{
								if((int)myStack.top() == (int)track.top())
								{
									myStack.pop();
									track.pop();
									System.out.println(no+" "+0);
								}else
								{
									while(myStack.size() > 0 && ((int)myStack.top() != (int)track.top()))
									{
										myStack.pop();
										track.pop();
										toppled++;
									}
									myStack.pop();
									track.pop();
									System.out.println(no+" "+toppled);
								}
							}catch(EmptyStackException e)
							{
								System.out.println("Empty stack");
							}
						}
					}
				}
			}
		}catch(FileNotFoundException e)
		{
            System.out.println("File not found");
		}
		
	}
}
