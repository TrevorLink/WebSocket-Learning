//聊天室主人
var username;
// 消息接收者
var toName;

//登录后显示用户名和状态
$(function () {
    $.ajax({
        //是否异步,此项目此处必须是false
        async: false,
        //请求方式
        type: 'GET',
        //请求url
        url: "/getUsername",
        success: function (res) {
            username = res;
            //$('#chatMeu').html('<p>用户：' + res + "<span style='float: right;color: greenyellow; height: 20px'>在线</span></p>")
        }
    });
//    创建WebSocket对象
    var ws = new WebSocket("ws://localhost:8080/chat")
    //连接建立成功触发事件
    ws.onopen = function () {
        //在建立连接后我们需要做什么事情？
        //每个客户端建立连接之后需要在页面上显示在线信息
        $('#chatMeu').html('<p>用户：' + username + "<span style='float: right;color: greenyellow; height: 20px'>在线捏</span></p>")
    }
    //客户端收到服务端发送的消息时触发
    ws.onmessage = function (evt) {
        //获取服务端推送的广播数据
        var dataStr = evt.data;
        //转JSON
        var res = JSON.parse(dataStr);
        if (res.system) {
            //是系统推送的广播数据
            var names = res.message;
            var friendsStr="";
            var broadcastStr="";
            //遍历JSON拼接
            for( var name of names){
                if (name!=username) {
                    friendsStr += "<li><a style='text-decoration: underline' onclick='chatWith(\""+ name +"\")'>"+name+"</a></li>";
                    broadcastStr += "<li>宁的好友：" + name + "已上号</li>"
                }
            }
            console.log(friendsStr)
            console.log(broadcastStr)
            //渲染数据
            $("#friendsList").html(friendsStr);
            $("#systemMsg").html(broadcastStr);
        }else {
            //是用户发送的数据
            var data=res.message;
            var cnt = "<div class=\"atalk\"><span id=\"asay\">"+data+"</span></div>"
            //重点！！为了防止第三方乱入，我们需要判断服务端广播过来的数据是否是只有发送方和接收方的
            if (toName==res.fromName) {//只有当和我聊的这个人就是我收到消息的发送的人的时候，我才显示消息
                $("#chatCnt").append(cnt);
            }
            //获取到和发送方的聊天数据
            var chatData = sessionStorage.getItem(res.fromName);
            if (chatData!=null){
                cnt+= chatData;
            }
            //我和发送方的消息记录更新保存
            sessionStorage.setItem(res.fromName,cnt)
        }
    }
    //连接关闭触发事件
    ws.onclose = function () {
        //每个客户端关闭连接之后需要在页面上显示离线信息
        $('#chatMeu').html('<p>用户：'+ username +"<span style='float: right;color: red; height: 20px'>离线了捏</span></p>")
    }

    //当用户点击发送按钮的时候
    $("#submit").click(function () {
        //获取到我们输入的文本内容
        var message = $("#tex_content").val();
        //发送之后我们文本框原先内容就要清空
        $("#tex_content").val("");
        //之后我们就需要前端ws调用send方法主动推送数据到服务端
        //在此之前需要先封装JSON数据
        var clientMessage={"toName":toName,"message":message};
        //调用send方法主动推送
        ws.send(JSON.stringify(clientMessage))
        //与此同时把我们刚发的显示在页面右边
        var cnt = "<div  class=\"btalk\"><span id=\"bsay\">" + message+ "</span></div>";
        $("#chatCnt").append(cnt);
        //获取到我们和这个聊天的人的消息记录
        var chatData = sessionStorage.getItem(toName)
        if (chatData != null) {
            cnt=chatData+cnt;
        }
        //我和接收方的消息记录更新保存
        sessionStorage.setItem(toName,cnt)
    })
})

//用户点击好友名进入聊天
function chatWith(name) {
    toName=name;
    $('#chatMeu').append('<p id="p1" style="text-align: center">正在和<b style="color: #db41ca ">' + name + '</b>聊天</p>');
    $('#chatMain').css("display", "inline");
    //清空聊天区
    $("#chatCnt").html("");
    var chatData = sessionStorage.getItem(toName);
    //只要有聊天记录就展示（不是追加，因为已经清空，这边只是一个查找然后展示）
    if (chatData!=null){
        $("#chatCnt").html(chatData);
    }
}