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

function send(data){
    // 使用 send() 方法发送数据
    socket.send(data);
}

function close(){
    // 使用 close() 关闭连接
    socket.close();
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