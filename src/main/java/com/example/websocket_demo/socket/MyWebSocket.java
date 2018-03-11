package com.example.websocket_demo.socket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket {

    //记录当前链接数
    private static int onlineCount = 0;

    //存放每个客户端对应的MyWebSocket对象
    private static CopyOnWriteArraySet<MyWebSocket> myWebSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的链接会话，用来向客户端发送消息
    private Session session;

    //连接建立成功调用的方法
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        myWebSocketSet.add(this);   //加入set中
        addOnlineCount();           //在线人数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    //连接关闭方法
    @OnClose
    public void onClose() {
        myWebSocketSet.remove(this);
    }

    //收到客户端后的调用方法
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来着客户端的消息：" + message);

        //群发消息
        for (MyWebSocket item : myWebSocketSet) {
            try {
                item.senMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //异常处理
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    //发送消息
    public void senMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //群发自定义消息
    public static void sendInfo(String message) {
        for (MyWebSocket item : myWebSocketSet) {
            try {
                item.senMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
}
