import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;

public class memory
{
	public static void main(String args[])
	{
		try
		{
			String filename = args[0]; //loading in filename from the command line argument passed by main
			int mem[] = initialize(filename); // 0-999 is for user program, 1000-1999 for system code
			char cmdType='c';
				
			Scanner sc = new Scanner(System.in);
			String cmdIn = null;
			
			while(cmdType != 'x')
			{
				if (sc.hasNext())
				{	
					cmdType = (sc.nextLine()).charAt(0); //grabs the first char r = read, w = write
					
					if (cmdType == 'r')
					{	
						int addr = sc.nextInt();
						System.out.println(mem[addr]);
						cmdType = 'c';
						sc.nextLine();
					}
					else if (cmdType == 'w')
					{
						int addr = sc.nextInt();
						int val = sc.nextInt();
						mem[addr]=val;
						System.out.println(-1); //tells the cpu that the store command is complete
						cmdType = 'c';
						sc.nextLine();
					}
					else if (cmdType == 'x')
					{
						System.out.println("2");
						cmdType = 'x';
					}
					
				}
			}
				
		
			//System.out.println("MEMORY finished running\n");
		
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}	
	public static int[] initialize(String fname)
	{
		int mem[] = new int[2000];
		//open file 
		try
		{
			//System.out.println("\nAttempting to open file \"" + fname + "\"\n");
			
			String temp = "";
			Scanner fs = new Scanner(new File(fname));
			Scanner sc;
			int i = 0;
			
			while(fs.hasNext())
			{
				temp = fs.nextLine();
				sc = new Scanner(temp);
				if (temp.length() > 0) // ignore blank lines
				{
					if (temp.charAt(0) == '.')
					{
						sc = new Scanner(temp.substring(1));
						i = sc.nextInt();
					}
					else if(sc.hasNextInt())
					{
						mem[i++] = sc.nextInt();
					}
				}
				else
				{
					//i++;
				}
			}
			//printMem(mem);
		}
		catch (Throwable t) {t.printStackTrace();}
		
		return (mem);
	}
	
	public static void printMem(int[] mem)
	{
			for (int i = 0; i < 2000; i++)
			{
				System.out.print("  mem[" + i + "] = " + mem[i]);
				if ((i+1)%10 == 0)
				{
					System.out.print("\n");
				}			
			}
	}
}
