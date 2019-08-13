package main.com.im.netty;


import com.google.gson.Gson;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import main.com.im.netty.bean.MessageContextModel;

class ServerHandler extends ChannelInboundHandlerAdapter {
    public ServerHandler(IMServer imServer) {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Active-收到连接:" + ctx.channel().remoteAddress() + " ChannelId:" + ctx.channel().id()
                .toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (ctx.channel() != null) {
            System.out.print("Inactive-断开连接:" + ctx.channel().remoteAddress()+"   ");
        } else {
            System.out.println("Inactive-断开连接");
        }
        // 用户断开连接后，移除channel
        ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("ServerHandler exceptionCaught()");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
       // System.out.println("ServerHandler userEventTriggered()");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object ob) throws Exception {
        //getJsonIMessage(ob);
        personAuthentication(ctx, null);
        //MessageContextModel msg = getJsonIMessage(ob);
        /*if (msg == null)
            return;

        switch (msg.Command) {
            case 31:
                personAuthentication(ctx, msg);
                break;
            case -1:
                //心跳
                break;
            default:
                MessageFactory.getInstance().receiveMessage(msg);
                break;
        }*/
    }

    //握手认证
    private void personAuthentication(ChannelHandlerContext ctx, final MessageContextModel msg) {
        NettyChannel ntChannel = new NettyChannel(ctx.channel().id().toString(), ctx.channel());
        ChannelContainer.getInstance().saveChannel(ntChannel);
        final InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        final String data = new Gson().toJson(address.getPort() + "已登录");
        ctx.channel()
                .writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8))
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println(address.getPort() + " " + (channelFuture.isSuccess() ? "成功" : "失败"));
                    }
                });
    }
/*
    //握手认证
    private void personAuthentication(ChannelHandlerContext ctx, final MessageContextModel msg) {
        NettyChannel ntChannel = new NettyChannel(msg.LoginBody.AccountName, ctx.channel());
        ChannelContainer.getInstance().saveChannel(ntChannel);
        IMMessage imMessage = new IMMessage();
        imMessage.Command = 31;
        imMessage.Entity = "";
        imMessage.ReceiveID = msg.LoginBody.AccountName;
        imMessage.Flag = true;
        imMessage.Msg = "登录成功";
        final String data = new Gson().toJson(imMessage);
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8)
        ).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println(msg.LoginBody.AccountName + ":登陆" + (channelFuture.isSuccess() ? "成功" : "失败"));
            }
        });
    }
*/


    /*public MessageContextModel getJsonIMessage(Object msg) {
        try {
            ByteBuf s = (ByteBuf) msg;
            byte[] byteArray = new byte[s.writerIndex()];
            s.readBytes(byteArray);
            String result = new String(byteArray, "UTF-8");
            ReferenceCountUtil.release(s);
            System.out.println("接收到消息" + result);
            return new Gson().fromJson(result, MessageContextModel.class);
        } catch (Exception e) {
            System.out.println("消息解析异常" + e.getMessage());
            return null;
        }
    }*/
    public MessageContextModel getJsonIMessage(Object msg) {
        try {
            ByteBuf s = (ByteBuf) msg;
            byte[] byteArray = new byte[s.writerIndex()];
            s.readBytes(byteArray);
            String result = new String(byteArray, "UTF-8");
            ReferenceCountUtil.release(s);
            System.out.println("接收到消息" + result);
            return new Gson().fromJson(result, MessageContextModel.class);
        } catch (Exception e) {
            System.out.println("消息解析异常" + e.getMessage());
            return null;
        }
    }

}
