import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.lang.*;

public class ChatClient extends JFrame implements ActionListener {
	BufferedReader in = null;
	BufferedWriter out = null;
	Socket socket = null;
	Receiver serverMessage;
	JTextField clientMessage;
	JTextField chatID;
	JTextField ipAddress;
	JTextField portNumber;
	JButton btn= new JButton("connect");
	JScrollPane spane;
	JPanel pan;
	Thread t;
	public ChatClient() {
		setTitle("Ŭ���̾�Ʈ ä�� â"); // ������ Ÿ��Ʋ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //������ ���� ��ư(X)�� Ŭ���ϸ� ���α׷� ����
		setLayout(new BorderLayout()); //BorderLayout ��ġ�������� ���
		
		serverMessage = new Receiver(); // �������� ���� �޽����� ����� ���۳�Ʈ
		serverMessage.setEditable(false); // ���� �Ұ�
		t = new Thread(serverMessage); // �������� �޽��� ������ ���� ������ ����
		clientMessage = new JTextField();
		clientMessage.addActionListener(this);
		
		chatID = new JTextField(10);
		ipAddress = new JTextField(10);
		portNumber = new JTextField(10);
		
		btn.addActionListener(this);
		pan = new JPanel();
		
		pan.add(new JLabel("chatID "));
		pan.add(chatID);
		pan.add(new JLabel("   "));
		
		pan.add(new JLabel("ipAddress "));
		pan.add(ipAddress);
		pan.add(new JLabel("   "));
		
		pan.add(new JLabel("portNumber "));
		pan.add(portNumber);
		
		pan.add(new JLabel("   "));
		pan.add(btn);
		
		spane = new JScrollPane(serverMessage); // �� �ؽ�Ʈ�� ����  ScrollPane���� ǥ��
		add(spane,BorderLayout.CENTER);
		add(clientMessage,BorderLayout.SOUTH);
		add(pan,BorderLayout.NORTH);
		setSize(700, 300); // �� 400 �ȼ�, ���� 200 �ȼ��� ũ��� ������ ũ�� ����
		setVisible(true); // �������� ȭ�鿡 ��Ÿ������ ����

	}
	private void setupConnection() throws IOException {
		String inputIp = ipAddress.getText();
		String inputPortNum = portNumber.getText();
		//System.out.println("Ip Address : " + inputIp);
		//System.out.println("Port Number : " + inputPortNum);
		socket = new Socket(inputIp, Integer.parseInt(inputPortNum)); // ip address , port number Ŭ���̾�Ʈ ���� ����
		System.out.println("�����");
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Ŭ���̾�Ʈ�κ����� �Է� ��Ʈ��
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Ŭ���̾�Ʈ���� ��� ��Ʈ��
//		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")); // Ŭ���̾�Ʈ�κ����� �Է� ��Ʈ��
//		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")); // Ŭ���̾�Ʈ���� ��� ��Ʈ��
	}
	public static void main(String[] args) {
		ChatClient frame = new ChatClient();
	}

	private static void handleError(String string) {
		System.out.println(string);
		System.exit(1);
	}
	
	private class Receiver extends JTextArea implements Runnable {
		@Override
		public void run() {
			String inputMessage=null;
			while (true) {
				try {
					inputMessage = in.readLine(); // Ŭ���̾�Ʈ���� �� ���� ���ڿ� ����
					serverMessage.append("\n" + inputMessage);
//					String convert = new String(inputMessage.getBytes("MS949"), "MS949");
//					String str = convert+"\n";
//					serverMessage.append("\n" + str);
					int pos = serverMessage.getText().length();
					serverMessage.setCaretPosition(pos); // caret �������� ���� ���������� �̵�
				} catch (IOException e) {
					handleError(e.getMessage());
				} 
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { // main Thread ���������� ����
		if (e.getSource() == clientMessage) {
			String outputMessage = clientMessage.getText(); // �ؽ�Ʈ �ʵ忡�� ���ڿ� ����
			try {
				String id = chatID.getText();
				out.write(id + " > " + outputMessage+"\n"); // ���ڿ� ����
//				String str = "client>" + outputMessage+"\n";
//				String convert = new String(str.getBytes("UTF-8"), "UTF-8");
//				out.write(convert); // ���ڿ� ����
				out.flush();
				
				serverMessage.append("\n" + id + " > " + outputMessage);
				int pos = serverMessage.getText().length();
				serverMessage.setCaretPosition(pos); // caret �������� ���� ���������� �̵�
				clientMessage.setText(null); // �Է�â�� ���ڿ� ����
			} catch (IOException e1) {
				handleError(e1.getMessage());
			} 
		}
		else if (e.getSource() == btn) {// ä�� �ʱ�ȭ
			try {
				setupConnection();
			} catch (IOException e2) {
				handleError(e2.getMessage());
			}
			t.start();// 2��° Thread start 2��° Thread�� server���� ���� �޽����� ���
		}
	}
}
