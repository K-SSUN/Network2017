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
	    System.out.println("서버에 연결되었습니다.");	   
			   
	    String line = in.readLine();
	    System.out.println("응답 : " + line);
	    
	    System.out.println("helo 명령을 전송합니다.");
	    out.println("helo mydomain.name");
	    line = in.readLine();
	    System.out.println("응답 : " + line);
	    
	    System.out.println("mail from 명령을 전송합니다.");
	    out.println("mail from: <soyen_20@naver.com>");
	    line = in.readLine();
	    System.out.println("응답 : "+line);
	    
	    System.out.println("rcot 명령을 전송합니다.");
	    out.println("rcpt to: <object1602@gmail.com>");
	    line = in.readLine();
	    System.out.println("응답:" + line);
	
	    
	    System.out.println("data 명령을 전송합니다.");
	    out.println("data");
	    line = in.readLine();
	    System.out.println("응답 : " + line);
	
	    
	    System.out.println("본문을 전송합니다.");
	    out.println("From: soyen_20@naver.com");
	    out.println("Subject: 김선영 (20143390)");
	    out.println("To: object1602@gmail.com");
	    out.println("hMailServer testing....");
	    out.println(".");
	    line = in.readLine();
	    System.out.println("응답 : " + line);
	   
	    
	    System.out.println("접속 종료합니다.");
		out.println("quit");
		out.close();
		in.close();
		socket.close();
	    
	    System.out.println("==========================");
	    System.out.println("메일이 전송되었습니다.");
	    } catch(Exception e) {
	    	System.out.println("==========================");
	    	System.out.println("메일이 발송되지 않았습니다.");
	    	System.out.println(e.toString());
	    	
		}		  
	}
}
