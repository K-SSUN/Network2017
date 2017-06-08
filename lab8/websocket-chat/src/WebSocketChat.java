/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.IOException;
import java.util.Set;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint(value = "/chat")
public class WebSocketChat {

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<WebSocketChat> connections =
            new CopyOnWriteArraySet<>();
    private static String[] colorarray = { "red", "green", "blue", "magenta", "purple", "plum", "orange" };
    private static Vector<String> colors = new Vector<String>();

    private String nickname;
    private Session session;
    private String userName = "";
    private String userColor = "";
    private JSONObject jsonMsg, jsonMsg2;
    String jsonInfo;

    public WebSocketChat() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
        colors.addAll(Arrays.asList(colorarray));
    }


    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
        String message = String.format("* %s %s", nickname, "has joined.");
        broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* %s %s",
                nickname, "has disconnected.");
        broadcast(message);
    }


    @OnMessage
    public void incoming(String message) {
    	//System.out.println(message); 
    	String[] splitedMsg = splitMessage(message);
    	
    	if(splitedMsg[1].equals(new String("message"))){
	        // Never trust the client
	        if (userName.equals("")) { // first message sent by user is their name
	            // remember user name
	        	userName = splitedMsg[3];
	            // get random color and send it back to the user
	            userColor = colors.remove(0);
	            jsonMsg  = new JSONObject();
	            jsonMsg.put("type", "color");
	            jsonMsg.put("data", userColor);
	            jsonInfo = jsonMsg.toJSONString();
	            ppsend(jsonInfo, this);
	        } else { 
	            jsonMsg  = new JSONObject();
	            jsonMsg.put("time",  (new Date()).getTime());
	            jsonMsg.put("text", splitedMsg[3]);
	            jsonMsg.put("author", userName);
	            jsonMsg.put("color", userColor);
	
	            jsonMsg2  = new JSONObject();
	            jsonMsg2.put("type", "message");
	            jsonMsg2.put("data", jsonMsg);
	            jsonInfo = jsonMsg2.toJSONString();        	
	
	            broadcast(jsonInfo);
        	} 
    	} else{
    		jsonMsg  = new JSONObject(); 
			jsonMsg.put("width", splitedMsg[14]);
			jsonMsg.put("color", splitedMsg[12]);
			jsonMsg.put("x1", splitedMsg[4]);
			jsonMsg.put("y1", splitedMsg[6]);
			jsonMsg.put("x2", splitedMsg[8]);
			jsonMsg.put("y2", splitedMsg[10]);
			
			jsonMsg2  = new JSONObject();
			jsonMsg2.put("type", splitedMsg[1]);
			jsonMsg2.put("data", jsonMsg);
			jsonInfo = jsonMsg2.toJSONString();        	
			
			broadcast(jsonInfo);
    	}
        
    }
    
    // Split message function
    public String[] splitMessage(String message){
    	String[] splitedMsg = new String(message).split("\\}");
    	String msg= Arrays.toString(splitedMsg).replace('[', ' ').replace(']', ' ').replace(',', ' ').trim();
    	splitedMsg = msg.split("\\{");
    	msg = Arrays.toString(splitedMsg).replace('[', ' ').replace(']', ' ').replace(',', ' ').trim();
    	msg = msg.replaceAll("\"", "");
    	msg = msg.replace(" ", ":");
    	splitedMsg = msg.split(":");
    	
    	if(splitedMsg[1].equals(new String("message"))){
    		String msgText="";
    		if(splitedMsg.length > 4){
	    		for(int i=3; i<splitedMsg.length; i++)
	    			msgText = msgText + splitedMsg[i]+ " ";
	    		splitedMsg[3] = msgText;
    		}
    		ArrayList<String> modifiedMsg = new ArrayList<String>(Arrays.asList(splitedMsg));
    		splitedMsg = modifiedMsg.subList(0,4).toArray(new String[modifiedMsg.subList(0,4).size()]);// 0~3까지 자름
    	}else{// draw
    		ArrayList<String> modifiedMsg = new ArrayList<String>(Arrays.asList(splitedMsg));
    		modifiedMsg.remove("");
    		modifiedMsg.remove("");
    		splitedMsg = modifiedMsg.toArray(new String[modifiedMsg.size()]);
    	}
    	return splitedMsg;
    }
    
    @OnError
    public void onError(Throwable t) throws Throwable {
//        log.error("Chat Error: " + t.toString(), t);
    }


    private static void ppsend(String msg, WebSocketChat client) {
            try {
                synchronized (client) {
                	client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
            }
    }

        
    private static void broadcast(String msg) {
        for (WebSocketChat client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
//                log.debug("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                broadcast(message);
            }
        }
    }
}
