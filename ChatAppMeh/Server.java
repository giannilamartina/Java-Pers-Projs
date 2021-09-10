/**
 * https://medium.com/nerd-for-tech/create-a-chat-app-with-java-sockets-8449fdaa933
 * https://github.com/JiheneBarhoumi/ChatApp/blob/main/src/Server.java
 * @author Gianni LaMartina
 */
package ChatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	public static void main(String[] args) {
		final ServerSocket serverSocket;
		final Socket clientSocket;
		final BufferedReader in; // Used to read data from the clientSocket object
		final PrintWriter out; // Used to write data into the clientSocket object
		final Scanner sc = new Scanner(System.in);

		try {
			serverSocket = new ServerSocket(5000);
			clientSocket = serverSocket.accept();
			out = new PrintWriter(clientSocket.getOutputStream()); // out is ready to write data into clientSocket
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // instantiates the in object
																							// used to read from
																							// clientSocket
			// new InputStreamReader(clientSocket.getInputStream() creates a stream reader
			// for the socket
			// but this stream reader only reads data as bytes; must be passed to
			// BufferedReader to be converted into characters

			Thread sender = new Thread(new Runnable() {
				String msg; // variable that will contain data written by user

				@Override // override the run method
				public void run() {
					while (true) {
						msg = sc.nextLine(); // reads data from user
						out.println(msg); // write data stored in msg in the clientSocket
						out.flush(); // forces the sending of the data
					}
				}
			});
			sender.start();

			Thread receive = new Thread(new Runnable() {
				String msg;

				@Override
				public void run() {
					try {
						msg = in.readLine(); // Read data sent from the client using in object associated to
						// clientSocket using readLine()
						while (msg != null) { // if null is returned, client is not connected.
							// Doesn't mean client didn't send anything; client can be connected to the
							// server still.
							System.out.println("Client : " + msg);
							msg = in.readLine();
						}

						System.out.println("Client disconnected");

						out.close(); // Client is no longer connected, so we close sockets and streams
						clientSocket.close();
						serverSocket.close();
					} catch (IOException e) { // Try/catch is used to print any error related to reading data or closing
												// sockets/streams
						e.printStackTrace();
					}
				}
			});
			receive.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
