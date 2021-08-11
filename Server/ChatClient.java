import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class ChatClient extends JFrame implements Runnable, ActionListener {

	private BufferedReader br;
	PrintWriter pw;
	private static JPanel output;
	private static JPanel sub1, sub2;
	private JTextField input;
	private JLabel label;
	JButton send;
	private static JScrollPane scrollpane;
	private static JScrollBar vertical;
	private Thread thread;
	private String host;
	Socket socket;
	static Color bg, mymsg, recmsg;

	public ChatClient(String server) {
		super("Chat Chat");
		host = server;
		try {
			socket = new Socket(host, 9090);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		bg = new Color(155, 187, 212);
		mymsg = new Color(254, 240, 27);
		recmsg = new Color(255, 255, 255);
		
		output = new JPanel(); sub1 = new JPanel(); sub2 = new JPanel();
		scrollpane = new JScrollPane(output);
		vertical = scrollpane.getVerticalScrollBar();
		sub1.setLayout(new BoxLayout(sub1, BoxLayout.Y_AXIS));
		sub2.setLayout(new BoxLayout(sub2, BoxLayout.Y_AXIS));
		sub1.setOpaque(true);
		sub1.setBackground(bg);
		sub2.setOpaque(true);
		sub2.setBackground(bg);
		
		getContentPane().add(scrollpane, "Center");
		output.setLayout(new BorderLayout());
		output.add("West", sub1);
		output.add("East", sub2);
		output.setOpaque(true);
		output.setBackground(bg);

		Panel bottom = new Panel(new BorderLayout());

		send = new JButton("전송");
		input = new JTextField();

		// 전송
		send.addActionListener(this);
		input.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == '\n') {
					try {
						OutputStream os = socket.getOutputStream();
						pw = new PrintWriter(new OutputStreamWriter(os));
						pw.println((input.getText()).toString());
						newMessage(input.getText().toString(), true);
						vertical.setValue(vertical.getMaximum()+1);
						input.setText("");
						pw.flush();
					}
					catch(Exception ex2) {
						ex2.printStackTrace();
					}
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		input.requestFocus();
		bottom.add(send, "East");
		bottom.add(input, "Center");

		getContentPane().add(bottom, "South");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 300);
		setVisible(true);
		run();
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			new ChatClient(args[0]);
		} else {
			new ChatClient("192.168.0.3");
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == send) {
			try {
				OutputStream os = socket.getOutputStream();
				pw = new PrintWriter(new OutputStreamWriter(os));
				pw.println((input.getText()).toString());
				newMessage(input.getText().toString(), true);
				vertical.setValue(vertical.getMaximum()+1);
				input.setText("");
				pw.flush();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		try {
			// 서버에서 메시지 받기
			InputStream is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		
			while (true) {
				String msg = br.readLine();
				if(msg != null) {
					System.out.println(msg);
					newMessage(msg, false);
				}
			}
			
		} catch (Exception ex) {
			PrintDebugMessage.print(ex);
		}

	}

	public static void newMessage(String msg, boolean b) {
		JLabel nmsg = new JLabel();
		JLabel pad = new JLabel();
		nmsg.setText(msg);
		if(b) {
			nmsg.setForeground(Color.BLACK);
			nmsg.setOpaque(true); 
			nmsg.setBackground(mymsg);
			nmsg.setBorder(BorderFactory.createLineBorder(mymsg, 3));
			pad.setBorder(BorderFactory.createEmptyBorder(10,10,5,5));
			sub2.add("East",nmsg);
			sub2.add("East", pad);
		}
		else {
			nmsg.setForeground(Color.BLACK);
			nmsg.setOpaque(true); 
			nmsg.setBackground(recmsg);
			pad.setBorder(BorderFactory.createEmptyBorder(10,10,5,5));
			nmsg.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
			sub1.add("West", pad);
			sub1.add("West", nmsg);
		}
		
		output.repaint();
		output.revalidate();
		nmsg.setVisible(true);
		vertical.setValue(vertical.getMaximum());

	}

}
