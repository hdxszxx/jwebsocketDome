#一个基于java的webSocket的dome
####服务端项目准备
引入webSocket的jar包
````
<dependency>
    <groupId>javax.websocket</groupId>
    <artifactId>javax.websocket-api</artifactId>
    <version>1.1</version>
</dependency>
````
第一步新建一个类

在类上面加上注解**@ServerEndpoint**其中的参数则是到时候前端访问的地址

以下注解修饰方法则可以进行相应的功能

收到消息触发事件@OnMessage

打开连接触发事件@OnOpen

关闭连接触发事件@OnClose

传输消息错误触发事件@OnError
````
// 收到消息触发事件
@OnMessage
public void onMessage(String message, Session session) throws IOException, InterruptedException {
    ...
}

// 打开连接触发事件
@OnOpen
public void onOpen(Session session, EndpointConfig config, @PathParam("id") String id) {
    ...
}

// 关闭连接触发事件
@OnClose
public void onClose(Session session, CloseReason closeReason) {
    ...
}

// 传输消息错误触发事件
@OnError
public void onError(Throwable error) {
    ...
}
````
发送给客服端数据的方式session.getBasicRemote().sendText(msg);

````
ServerEndpointConfig.Configurator

编写完处理器，你需要扩展 ServerEndpointConfig.Configurator 类完成配置：

public class WebSocketServerConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
````
服务端然后就没有然后了，就是这么简单。
####客服端准备
在此说明这个不是http协议所以需要将地址中的http换成ws，https换成wss

````
第一步直接new一个WebSocket
//初始发一个webSocket
var socket = new WebSocket("ws://localhost:8080/myHandler");
state(socket.readyState);

// 建立 web socket 连接成功触发事件
socket.onopen = function () {
    // 使用 send() 方法发送数据
    socket.send("成功连接");
}

//收到消息进行回调
socket.onmessage = function (data) {
    var mgs = data.data;
    document.getElementById("show").innerHTML = mgs;
    console.info("成功接收到消息内容为："+mgs);
}

//关闭进行回调
socket.onclose = function () {
    console.info("连接已关闭...");
}

//错误了进行回调
socket.onerror = function (event) {
    console.info(event);
}
function state(data){
    switch(data){
        case 0:
            console.info("连接尚未建立");
            break;
        case 1:
            console.info("连接已建立");
            break;
        case 2:
            console.info("连接在关闭");
            break;
        case 3:
            console.info("连接已经关闭或者连接不能打开。");
            break
        default:
            console.info("发生未知错误信息");
            break;
    }
}
````
以上代码中的第一个参数 url, 指定连接的 URL。第二个参数 protocol 是可选的，指定了可接受的子协议。

WebSocket 属性
以下是 WebSocket 对象的属性。假定我们使用了以上代码创建了 Socket 对象：

#####属性描述
######Socket.readyState只读属性 readyState 表示连接状态，可以是以下值：0 - 表示连接尚未建立。1 - 表示连接已建立，可以进行通信。2 - 表示连接正在进行关闭。3 - 表示连接已经关闭或者连接不能打开。
######Socket.bufferedAmount	只读属性 bufferedAmount 已被 send() 放入正在队列中等待传输，但是还没有发出的 UTF-8 文本字节数。
#####WebSocket 事件
以下是 WebSocket 对象的相关事件。假定我们使用了以上代码创建了 Socket 对象：

######事件 	事件处理程序 	描述
######open	Socket.onopen	连接建立时触发
######message	Socket.onmessage	客户端接收服务端数据时触发
######error	Socket.onerror	通信发生错误时触发
######close	Socket.onclose	连接关闭时触发
#####WebSocket 方法
以下是 WebSocket 对象的相关方法。假定我们使用了以上代码创建了 Socket 对象：

######方法	描述
######Socket.send()	使用连接发送数据
######Socket.close()	关闭连接
就是这么简单的
[参考文档](https://www.cnblogs.com/jingmoxukong/p/7755643.html)