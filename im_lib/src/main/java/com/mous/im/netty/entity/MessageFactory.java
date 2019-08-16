package main.java.com.mous.im.netty.entity;


import com.google.gson.Gson;

import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import main.java.com.mous.im.bean.IMContent;
import main.java.com.mous.im.bean.IMMessage;
import main.java.com.mous.im.netty.ChannelContainer;
import main.java.com.mous.im.netty.NettyChannel;

public class MessageFactory {

    private static final MessageFactory instance = new MessageFactory();

    public static MessageFactory getInstance() {
        return instance;
    }

    public void receiveMessage(IMContent msg) {
        reLayMessage(msg);
    }


    private void reLayMessage(IMContent msg) {
        String[] recS = getReceiveIDs(msg.ReceiveID);
        for (String receiveID : recS) {
            NettyChannel ntChannel = ChannelContainer.getInstance().getChannelByAccount(receiveID);
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

    private String getMessage(IMContent msg) {
        IMMessage imMessage = new IMMessage();
        //        imMessage.Command = msg.Command;
        //        imMessage.Entity = new Gson().toJson(msg.MsgBody);
        //        imMessage.ReceiveID = msg.MsgBody.ReceiveID;
        return new Gson().toJson(imMessage);
    }
}
