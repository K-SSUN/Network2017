import java.io.*;
import java.net.*; 

class HTTP_Client {
  public static void main(String[] args) {
    try {
      String host; 
      if (args.length > 0 && args[0] != null) {
    	  host = args[0]; 
      } else {
    	  host = "localhost";
      }
      
      System.out.println("\n======================================");
      System.out.println("1. test for HTTP GET default home page");
      System.out.println("======================================");
      
      Socket defaultHome = new Socket(host, 8080); 
      
      BufferedReader in = new BufferedReader(new InputStreamReader(defaultHome.getInputStream())); 
      PrintWriter out = new PrintWriter(new OutputStreamWriter(defaultHome.getOutputStream())); 

  	  out.println("GET / HTTP/1.0\r\n"); 
  	  out.println("\r\n"); 
  	  out.flush();

      System.out.println("\n(Request)");
  	  System.out.println("GET / HTTP/1.0"); 
  	  System.out.flush();

      System.out.println("\n(Reply)");
      for (;;) {
        String str = in.readLine(); 
        if (str == null) {
          break; 
        } else {
          System.out.println(str); 
        }
      }

     System.out.println("\n======================================");
     System.out.println("2. test for HTTP GET hong home page");
     System.out.println("======================================");
     
     Socket hong = new Socket(host, 8080); 
     
     in = new BufferedReader(new InputStreamReader(hong.getInputStream())); 
     out = new PrintWriter(new OutputStreamWriter(hong.getOutputStream())); 

 	 out.println("GET /hong.html HTTP/1.0\r\n"); 
 	 out.println("\r\n"); 
 	 out.flush();

     System.out.println("\n(Request)");
 	 System.out.println("GET /hong.html HTTP/1.0"); 
 	 System.out.flush();

     System.out.println("\n(Reply)");
     for (;;) {
       String str = in.readLine(); 
       if (str == null) {
         break; 
       } else {
         System.out.println(str); 
       }
     } 
     
    System.out.println("\n======================================");
    System.out.println("3. test for HTTP GET hong2 home page");
    System.out.println("======================================");
    
    Socket hong2 = new Socket(host, 8080); 
    
    in = new BufferedReader(new InputStreamReader(hong2.getInputStream())); 
    out = new PrintWriter(new OutputStreamWriter(hong2.getOutputStream())); 

	out.println("GET /hong2.html HTTP/1.0\r\n"); 
	out.println("\r\n"); 
	out.flush();

    System.out.println("\n(Request)");
	System.out.println("GET /hong2.html HTTP/1.0"); 
	System.out.flush();

    System.out.println("\n(Reply)");
    for (;;) {
      String str = in.readLine(); 
      if (str == null) {
        break; 
      } else {
        System.out.println(str); 
      }
    } 
    
  } catch (Exception e) { 
    System.out.println("Error: " + e); 
  }
    
  }

}