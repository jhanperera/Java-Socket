import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Title:		NosyClient
 * Description:	This applicaton will connect to a server and request information
 * Auther:		Jhan Perera
 * Date: 		April 3 2016
 * Note:	Run this appliction via the following command 
 *		      java NosyClient <host> <port>
 * ****************************************************************************/

class NosyClient
{
	public static void main(String args[]) throws Exception
	{
		String sentence;
		String modifiedSentence;

		// Get intput from the command-line args
		if (args.length != 2) 
		{
			System.out.println("Required arguments: hostname and port");
			return;
		}
		// Save the host name from argument 0
		String host = args[0];

		// Save the port number from argument 1
		int port = Integer.parseInt(args[1]);
		
		//Set the clientSocket to connect to the host and port
		Socket clientSocket = new Socket(host, port);
		
		//Get the output stream of the server
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		// Get the input stream from the server
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		//Send the inital prompt
		sentence = "menu";
		outToServer.writeBytes(sentence + '\n');

		//Get the inital promps from the server *The menu" of all commands about 7 lines of text
		for( int i = 0; i < 8 && (modifiedSentence = inFromServer.readLine()) != null ; i ++)
		{
			System.out.println(modifiedSentence);	
		}

		//Prompt the user
		System.out.print("Enter Command > ");

		//Read user input
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));

		//While the server is sending more info
		while((sentence = inFromUser.readLine()) != null)
		{
			//if we get exit then we want to close this conenction as well
			if (sentence.trim().equals("exit"))
			{
				outToServer.writeBytes(sentence + '\n');
				modifiedSentence = inFromServer.readLine();
				System.out.println(modifiedSentence);
				clientSocket.close();
				return;
			}
			//Else we send and listen for more data to come back
			else
			{
				outToServer.writeBytes(sentence + '\n');
				modifiedSentence = inFromServer.readLine();
				System.out.println(modifiedSentence);
				clientSocket.close();
				return;
			}
		}
	}
}
