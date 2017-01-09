import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

/*
 * Title:		NosyServer
 * Description:	This application will sit on a server and process user request
 * 		send back appropriate information
 * Auther:		Jhan Perera
 * Date: 		April 3 2016
 * Note:	Run this appliction via the following command 
 *		      java NosyServer <post>
 * ****************************************************************************/
class NosyServer
{
   	public static void main(String args[]) throws Exception
	{
	 	String clientSentence;
	   	String outSentence;

		// Get intput from the command-line args
		if (args.length != 1) 
		{
			System.out.println("Required arguments: port");
			return;
		}	
		// Save the port number from argument 0
		int port = Integer.parseInt(args[0]);

		//Set the socet with port number
	   	ServerSocket welcomeSocket = new ServerSocket(port);
	   	//Accept the connection from the client
	   	Socket connectionSocket = welcomeSocket.accept();
	   	//Get a buffere for the input.
		BufferedReader inFromClient = 
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		//Obtain the dataOutupt from the client
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		//Listen for client requests
		while(true)
		{
			// While we still have more to read from the client
			while ((clientSentence = inFromClient.readLine()) != null)
			{
				System.out.println("Received: " + clientSentence);
				//Inital prompt from the client
				if (clientSentence.trim().equals("menu"))
				{
					outToClient.writeBytes("= = = = = = = = = = = = = = = = Menu = = = = = = = = = = = = = = = =" + '\n');
					outToClient.writeBytes("   date --  print the date and time of server's system" + '\r');
					outToClient.writeBytes("   timezone --  print the time zone of server's system " + '\r');
					outToClient.writeBytes("   OSname --  print the name of server's operating system (OS)" + '\r');
					outToClient.writeBytes("   OSversion --  print the of version number of server's OS" + '\r');
					outToClient.writeBytes("   user -- print the name of the user logged onto (i.e. running) the server" + '\r');
					outToClient.writeBytes("   exit --  exit the program " + '\r');
					outToClient.writeBytes("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =  " + '\r');
				}
				//If we get "exit" we need to close te connection
				else if (clientSentence.equals("exit"))
				{
					outSentence = "Termianting Connection" + '\n';
					outToClient.writeBytes(outSentence);
				}
				// If we get "date" as an argument
				else if (clientSentence.equals("date"))
				{
					outSentence = new Date().toString() + '\n';
					outToClient.writeBytes(outSentence);
				}
				//if we get "timezone" as an argument
				else if (clientSentence.equals("timezone"))
				{
					outSentence = TimeZone.getDefault().getDisplayName() + '\n';
					outToClient.writeBytes(outSentence);
				}
				// If we get the "OSname" as an argument
				else if (clientSentence.equals("OSname"))	
				{
					outSentence = System.getProperty("os.name") + '\n';
					outToClient.writeBytes(outSentence);
				}
				// If we get "OSVersion" as an argument
				else if (clientSentence.equals("OSversion"))
				{
					outSentence = System.getProperty("os.version") + '\n';
					outToClient.writeBytes(outSentence);
				}
				// If we get "user" as an argument
				else if (clientSentence.equals("user"))	
				{
					outSentence = System.getProperty("user.name")+ '\n';
					outToClient.writeBytes(outSentence);
				}
				// If we get other arguments then we send this. 
				else
				{
					outSentence = "Command not recognized" + '\n';
					outToClient.writeBytes(outSentence);
				} 
				//Close the connection and termiante
				welcomeSocket.close();
			}	
		}
	}
}
