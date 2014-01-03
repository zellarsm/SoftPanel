package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	
	private InetAddress serverIP;
	
	public UDPClient(InetAddress serverIP) {
		this.serverIP = serverIP;
		
		System.out.println(serverIP);
	}
	
	public String transmit(String data) throws IOException {
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		// Create the socket to send and receive data on.
		DatagramSocket clientSocket = new DatagramSocket();
		
		// Prepare data to be sent
		sendData = data.getBytes();
		
		// Create the send packet and transmit it.
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, 9876);
		clientSocket.send(sendPacket);
		
		// Create the receive packet and receive it.
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		
		String response = new String(receivePacket.getData()).replaceAll("[^a-zA-Z0-9]", "");
		System.out.println("FROM SERVER: " + response);
		
		// Close the socket.
		clientSocket.close();
		
		return response;
	}
}
