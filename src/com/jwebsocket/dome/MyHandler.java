package com.jwebsocket.dome;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MacFan
 * user:created by MacFan
 * DATE: 2018/8/2
 */
@ServerEndpoint("/myHandler")
public class MyHandler {

    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<MyHandler> connections =  new CopyOnWriteArraySet<MyHandler>();
    private Session session;
    private final String name;

    public MyHandler() {
        this.name = connectionIds.getAndIncrement()+"";
    }

    // 收到消息触发事件
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        this.session =session;
        System.out.println("接收数据成功："+message);
        connections.add(this);
        sendTextAll(message);
    }

    // 打开连接触发事件
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("id") String id) {
        System.out.println("连接成功");
    }

    // 关闭连接触发事件
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("关闭了连接");
    }

    // 传输消息错误触发事件
    @OnError
    public void onError(Throwable error) {
        System.out.println("发生了错误");
    }

    /**
     * 发送消息给全部连接用户
     */
    private static void sendTextAll(String msg){
        for (MyHandler myHandler:connections) {
            try {
                myHandler.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                connections.remove(myHandler);
                try {
                    myHandler.session.close();
                } catch (IOException e1) {

                }
                String message = String.format("* %s %s",
                        myHandler.name, "has been disconnected.");
                sendTextAll(message);
            }
        }
    }

    /**
     * 发送消息给当前用户
     */
    private void sendText(String msg){
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
