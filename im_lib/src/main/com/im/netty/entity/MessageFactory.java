package main.com.im.netty.entity;


import com.google.gson.Gson;

import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import main.com.im.netty.ChannelContainer;
import main.com.im.netty.NettyChannel;
import main.com.im.netty.bean.IMMessage;
import main.com.im.netty.bean.IMMessageContent;
import main.com.im.netty.bean.MessageContextModel;

public class MessageFactory {

    private static final MessageFactory instance = new MessageFactory();

    public static MessageFactory getInstance() {
        return instance;
    }

    public void receiveMessage(MessageContextModel msg) {
        IMMessageContent msgBody = msg.MsgBody;
        reLayMessage(msg);
      /*  switch (msgBody.MessageType) {
            case 10://点对点文本消息
            case 20:
                sendTextMessage(msg);
                break;
            case 11://点对点文件消息
            case 21:
                sendFileMessage(msg);
                break;
        }*/
    }


    private void reLayMessage(MessageContextModel msg) {
        String[] recS = getReceiveIDs(msg.MsgBody.ReceiveID);
        for (String receiveID : recS) {
            NettyChannel ntChannel = ChannelContainer.getInstance().getChannelByUserId(receiveID);
            if (ntChannel == null || !ntChannel.getChannel().isActive())
                // TODO 说明当前用户不在线,保存消息在服务器
                return;
            String data = getMessage(msg);
            ntChannel.getChannel().writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8));
        }

    }

    private String[] getReceiveIDs(String receivers) {
        String[] recS;
        if (receivers.contains(",")) {
            recS = receivers.split(",");
        } else {
            recS = new String[]{receivers};
        }
        return recS;
    }

    private String getMessage(MessageContextModel msg) {
        IMMessage imMessage = new IMMessage();
        imMessage.Command = msg.Command;
        imMessage.Entity = new Gson().toJson(msg.MsgBody);
        imMessage.ReceiveID = msg.MsgBody.ReceiveID;
        return new Gson().toJson(imMessage);
    }
}
