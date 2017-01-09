import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Title:		PingClient
 * Description:	This client makes UDP ping request to a IP through a port
 *		The IP is hardcoded into the source code and can 
 * 		be modified at any time. The Post number must be 
 * 		provided when running this client
 * Auther:		Jhan Perera
 * Date: 		April 1 2016
 * Note:	Run this appliction via the following command 
 *		      java PingClient <port>
 * ****************************************************************************/
public class PingClient
{
	
	public static void main(String[] args) throws Exception
	{
		// Get intput from the command-line args
		if (args.length != 1) 
		{
			System.out.println("Required arguments: port");
			return;
		}	
		// Save the port number from argument 0
		int port = Integer.parseInt(args[0]);
		
		// Instruction and prompt the user
		System.out.println("Command Syntax: ping <destination addr> <destination port>");
		System.out.println("use CTRL^C to quit");	
		
		//read the input from the system.in
		Scanner keyboard = new Scanner(System.in);
		String pingDummy = keyboard.next();
		if (!pingDummy.equals("ping"))
		{
			System.out.println("Invalid Command");
			return;
		}
		String destination_address = keyboard.next();
		int destination_port = keyboard.nextInt();


		// Get the internet address (IPv4) of the specified host
      		Inet4Address address = (Inet4Address)Inet4Address.getByName(destination_address.trim());


		// Create a datagram socket to send/receive a packet through. 
     		DatagramSocket dsocket = new DatagramSocket(port);
		
	
		// Create 10 PING datagrams to send to the server
		for( int sequence_number = 0; sequence_number < 10; sequence_number++)
		{	
			// Get the current date and time
			String current_time = new Date().toString();

			// Create the payload: PING <sequence_number> <timestamp> \n
			String datagram_string = "PING " + sequence_number + " " 
								+ current_time + "\n";

			byte[] message = datagram_string.getBytes();

			DatagramPacket packet = new DatagramPacket(message, 
								   message.length, 
								   address, 
								   destination_port);
			
			// Send the packet via the datagram soocket
			dsocket.send(packet);
	
			//Set timeout
			dsocket.setSoTimeout(10000);

			// Create a datagram packet to hold incoming UDP packet.
			DatagramPacket recieved = new DatagramPacket(new byte[1024], 1024);
			
			// Reply from server?
			try
			{
				dsocket.receive(recieved);
				printData(recieved);	
			} //Catch the timeout exception to display the timeout message
			catch(SocketTimeoutException e)
			{
				//timeout message
				System.out.println("PING " + sequence_number + 
							" Request timed out");
			}
			// Delay for 1 second then send next packet
			Thread.sleep(1000);				
		}
		// Close the port
		dsocket.close();
	}

	/*
	* Print Server data to the standard output stream.
	*/
	private static void printData(DatagramPacket request) throws Exception
	{
		// Obtain references to the packet's array of bytes.
		byte[] buf = request.getData();

		// Wrap the bytes in a byte array input stream,
		// so that you can read the data as a stream of bytes.
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);

		// Wrap the byte array output stream in an input stream reader,
		// so you can read the data as a stream of characters.
		InputStreamReader isr = new InputStreamReader(bais);

		// Wrap the input stream reader in a bufferred reader,
		// so you can read the character data a line at a time.
		// (A line is a sequence of chars terminated by any combination of \r and \n.)
		BufferedReader br = new BufferedReader(isr);

		// The message data is contained in a single line, so read this line.
		String line = br.readLine();

		// Print host address and data received from it.
		System.out.println(
				"Received from " +
				request.getAddress().getHostAddress() +
				": " +
				new String(line).trim() );		
	}	
}
