import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;

public class ChatServer {
	
	ServerSocket serverSocket;
	Socket client;
	private Thread thread;
	String receiveMessage, sendMessage;
	
	public ChatServer(int port) {
		
		// 전송
		try {
			serverSocket = new ServerSocket(port);
			
			System.out.println("--------Chat Server Running.--------");
			while(true){
	            Socket s1 = serverSocket.accept();
	            Socket s2 = serverSocket.accept();
	            new ClientThread(s1, s2).start();
	            new ClientThread(s2, s1).start();
	            
	        }
		}
		catch (Exception e) {
			PrintDebugMessage.print(e);
		}
		
	}
	
	
	public static void main(String[] args) {
		ChatServer server = new ChatServer(9090);

	}

	// 클라이언트 쓰레드 
	class ClientThread extends Thread{
		
		Socket client1;
		Socket client2;
		
		public ClientThread(Socket client1, Socket client2) {
			this.client1 = client1;
			this.client2 = client2;
		}
		public void run() {
			try {
				InputStream istream = client1.getInputStream();
				OutputStream ostream = client2.getOutputStream();
				PrintWriter pwrite = new PrintWriter(ostream, true);
				
				while(true) {
					BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
					String msg = receiveRead.readLine();
					if(msg != null)
					{
						pwrite.println(msg);
						pwrite.flush();
					}
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}	
		}
	}
}


