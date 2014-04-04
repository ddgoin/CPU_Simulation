import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;
import java.util.Random;
public class cpu
{
	public static void main(String args[])
	{
		
		try
		{
			int PC = 0; //Process Counter
			int SP = 999; //Stack Pointer
			int IR = 0; //Instruction Register
			int AC = 0; //Accumulator
			int X  = 0;  //X Register
			int Y  = 0;  //Y Register
			int SysStack = 1999;
			int count = 0;
			Random rand = new Random();
			Runtime rt = Runtime.getRuntime();
			//reads program file name and interrupt timer from cmd line args
			String fname = args[0];
			int itimer = Integer.parseInt(args[1]);	
			
			//initialize memory process
			System.out.println("running command \"java memory " + fname + "\""); 
			Process proc = rt.exec("java memory " + fname);
			
			InputStream is = proc.getInputStream();
			OutputStream os = proc.getOutputStream();
			Scanner sc = new Scanner(is);
			PrintWriter pw = new PrintWriter(os);
			
			
			if(count >= itimer) 
			{
				SysStack--;
				Write(SysStack, PC, sc, pw);
				SysStack--;
				Write(SysStack, SP, sc, pw);
				SysStack--;
				Write(SysStack, IR, sc, pw);
				SysStack--;
				Write(SysStack, AC, sc, pw);
				SysStack--;
				Write(SysStack, X, sc, pw);
				SysStack--;
				Write(SysStack, Y, sc, pw);
				PC = 1000;
				count = itimer;
			}
			while (IR != 50) //main fetch/execute loop
			{
				//System.out.println("Fetching instruction " + PC);
				//-----Fetch-----
				IR = Read(PC,sc,pw); //reads in next instruction
				PC++;
				
				//-----Execute (going to be really long! ughh)-----
				int val = Read(PC,sc,pw); //grabs the value for ease
				//System.out.println("Executing instruction " + IR + " with val = " + val);
				
				count++;
					switch(IR)
					{					
						case 1:	AC = val;
								PC++;
								break; //Load value
							
						case 2: AC = Read(val,sc,pw);
								PC++;
								break; //Load addr
								
						case 3: val = Read(val, sc, pw);
								AC = Read(val, sc, pw);
								PC++;
								break; //LoadInd addr
								
						case 4: AC = Read(val+X, sc, pw);
								PC++;
								break; //LoadIdxX addr
								
						case 5: AC = Read(val+Y, sc, pw);
								PC++;
								break; //LoadIdxY addr
								
						case 6: AC = Read(SP+X, sc, pw);
								break; //LoadSpX
								
						case 7: Write(val, AC, sc, pw);
								PC++;
								break; //Store addr
						
						case 8: AC = rand.nextInt(100)+1;
								break; //Get (random int 1-100)
						
						case 9: if(val == 1) {System.out.print(AC);}
								else if(val == 2) {System.out.print(Character.toChars(AC));}
								PC++;
								break; //Put port
								
						case 10: AC += X;
								break; //AddX
								
						case 11: AC += Y;
								break; //AddY
								
						case 12: AC -= X;
								break; //SubX
						
						case 13: AC -= Y;
								break; //SubY
															
						case 14: X = AC;
								break; //CopyToX
								
						case 15: AC = X;
								break; //CopyFromX
								
						case 16: Y = AC;
								break; //CopyToY
								
						case 17: AC = Y;
								break; //CopyFromY
								
						case 18: SP = AC;
								break; //CopyToSp
								
						case 19: AC = SP;
								break; //CopyFromSp
								
						case 20: PC = val;
								break; //Jump addr
								
						case 21: if (AC == 0) {PC = val;} 
								else PC++;
								break; //JumpIfEqual addr
								
						case 22: if (AC != 0) {PC = val;}
								else PC++;
								break; //JumpIfNotEqual addr
								
						case 23: SP--;
								Write(SP, PC, sc, pw);
								PC = val;
								break; //Call addr
								
						case 24: PC = Read(SP, sc, pw);
								SP++;
								PC++;
								break; //Ret
								
						case 25: X++;
								break; //IncX
								
						case 26: X--;
								break; //DecX
								
						case 27: SP--;
								Write(SP, AC, sc, pw);
								break; //Push
								
						case 28: AC = Read(SP, sc, pw);
								SP++;
								break; //Pop
								
						case 29:SysStack--;
								Write(SysStack, PC, sc, pw);
								SysStack--;
								Write(SysStack, SP, sc, pw);
								SysStack--;
								Write(SysStack, IR, sc, pw);
								SysStack--;
								Write(SysStack, AC, sc, pw);
								SysStack--;
								Write(SysStack, X, sc, pw);
								SysStack--;
								Write(SysStack, Y, sc, pw);
								
								PC = 1500;
								//NOT IMPLEMENTED YET
								break; //Int
								
						case 30:Y = Read(SysStack++, sc, pw);
								X = Read(SysStack++, sc, pw);
								AC = Read(SysStack++, sc, pw);
								IR = Read(SysStack++, sc, pw);
								SP = Read(SysStack++, sc, pw);
								PC = Read(SysStack++, sc, pw);
								count = itimer;
								break; //IRet
								
						case 50: Exit(sc, pw);
								break; //end
								
								
					}

			}
						
			proc.waitFor();
			
			System.out.println("\nCPU finished running\n");	
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}	
	}
	public static int Read(int addr, Scanner sc, PrintWriter pw)
	{
		pw.printf("r\n" + addr + "\n");
		pw.flush(); //send read command
		while (!sc.hasNextInt());
		return sc.nextInt(); //reads in next instruction
		
	}
	public static void Write(int addr, int val, Scanner sc, PrintWriter pw)
	{
		pw.printf("w\n" + addr + " " + val + "\n");
		pw.flush();
		while(!sc.hasNextInt());
		sc.nextInt();
	}
	public static void Exit(Scanner sc, PrintWriter pw)
	{
		pw.printf("x\n");
		pw.flush();
		while(!sc.hasNextInt());
		sc.nextInt();
	}
}
