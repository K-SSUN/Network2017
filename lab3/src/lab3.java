package lab3;

import java.io.*;
import java.net.Socket;

public class lab3 {
	
	public static void main(String[] args) {
		try {
			String host;
			if(args.length > 0 && args[0] != null)
				host = args[0];
			else
				host = "localhost";
			
	    Socket socket=new Socket(host, 25);
	    BufferedReader in =new BufferedReader(new InputStreamReader( socket.getInputStream() ) );
	    PrintWriter out =new PrintWriter( socket.getOutputStream(), true );
	    System.out.println("������ ����Ǿ����ϴ�.");	   
			   
	    String line = in.readLine();
	    System.out.println("���� : " + line);
	    
	    System.out.println("helo ����� �����մϴ�.");
	    out.println("helo mydomain.name");
	    line = in.readLine();
	    System.out.println("���� : " + line);
	    
	    System.out.println("mail from ����� �����մϴ�.");
	    out.println("mail from: <soyen_20@naver.com>");
	    line = in.readLine();
	    System.out.println("���� : "+line);
	    
	    System.out.println("rcot ����� �����մϴ�.");
	    out.println("rcpt to: <object1602@gmail.com>");
	    line = in.readLine();
	    System.out.println("����:" + line);
	
	    
	    System.out.println("data ����� �����մϴ�.");
	    out.println("data");
	    line = in.readLine();
	    System.out.println("���� : " + line);
	
	    
	    System.out.println("������ �����մϴ�.");
	    out.println("From: soyen_20@naver.com");
	    out.println("Subject: �輱�� (20143390)");
	    out.println("To: object1602@gmail.com");
	    out.println("hMailServer testing....");
	    out.println(".");
	    line = in.readLine();
	    System.out.println("���� : " + line);
	   
	    
	    System.out.println("���� �����մϴ�.");
		out.println("quit");
		out.close();
		in.close();
		socket.close();
	    
	    System.out.println("==========================");
	    System.out.println("������ ���۵Ǿ����ϴ�.");
	    } catch(Exception e) {
	    	System.out.println("==========================");
	    	System.out.println("������ �߼۵��� �ʾҽ��ϴ�.");
	    	System.out.println(e.toString());
	    	
		}		  
	}
}
