package com.sky.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {

    private static Map<String,Session> sessions = new ConcurrentHashMap<>();
    public WebSocketServer() {
        log.info("WebSocketServer 构造函数被调用");
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        sessions.put(sid,session);
        log.info("客户端：{}建立连接",sid);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来着客户端{}的信息：{}",sid,message);
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        sessions.remove(sid);
        log.info("断开连接：{}",sid);
    }

    public void sendMessage(String message) {
      if(!sessions.isEmpty()){
         for(Session session : sessions.values()){
             session.getAsyncRemote().sendText(message);
         }
      }

    }

}
