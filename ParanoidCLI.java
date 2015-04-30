import java.util.Scanner;

public class ParanoidCLI implements chatUI
{	
	public static void main(String args[])
	{
		Backend backend=new Backend();
		Scanner keyb = new Scanner(System.in);
		String input = new String();
		
		backend.bind();
		
		while(true)
		{
			//System.out.print("> ");
			input=keyb.nextLine();
			
			if(input.equalsIgnoreCase("Connect"))
			{
				System.out.print("Enter remote host: ");
				input=keyb.nextLine();
				backend.Connect(input);
			}
			else
			{
				backend.sendMessage(input);
			}
		}
	}
	
	public void printMessage(String message)
	{
		System.out.println("Message: " + message);
	}
	
	public void printAlert(String alert)
	{
		System.out.println("ALERT: " + alert);
	}
}
