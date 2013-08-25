package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class CryptoClient {

	public static void main(String[] args) throws Exception {

		String serverName = "192.168.2.12";
		int serverPort = 5001;

		// Create an input scanner
		Scanner input = new Scanner(System.in);

		// Create a connection to the server
		final Socket clientSocket = new Socket(serverName, serverPort);

		// Create the input and the output stream

		DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());
		final BufferedReader inFromServer = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));

		// Create a new thread that reads everything from the Server
		clientReadingThread temp = new clientReadingThread(System.out,
				inFromServer, clientSocket);

		while (true) {
			String line = input.nextLine();
			if (line.equals("\\LOGOUT")) {
				outToServer.writeBytes(line + '\n');
				break;
			}
			outToServer.writeBytes(line + '\n');
		}

	}
}

// Separate thread class for the client to receive messages.
class clientReadingThread extends Thread {

	PrintStream out;
	BufferedReader inFromServer;
	Socket clientSocket = null;

	public clientReadingThread(PrintStream out, BufferedReader inFromServer,
			Socket clientSocket) {
		this.out = out;
		this.inFromServer = inFromServer;
		this.start();
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String line = inFromServer.readLine();
				if (line.equals("LOGOUTGOODBYE")) {
					System.out.println("Good Bye");
					clientSocket.close();
					break;

				}
				System.out.println(line);
			} catch (Exception e) {
				System.out.println("Disconnected from Server.");
				System.exit(1);
			}
		}
	}
}
