package main.com.im.netty;

import io.netty.channel.Channel;

public class NettyChannel {

    //当前登陆人账户名称
    private String account;
    //登陆类型 0:移动端 1:PC端
    int type;
    //当前登陆设备码,与登陆类型组成唯一的ChannelID,保证同端登陆设备只有一台
    private String EMIE = "";
    private Channel channel;

    public NettyChannel(String userId, Channel channel) {
        this.account = userId;
        this.channel = channel;
    }

    public String getChannelId() {
        return channel.id().toString();
    }

    public String getAccount() {
        return account;
    }


    public Channel getChannel() {
        return channel;
    }


    public boolean isActive() {
        return channel.isActive();
    }

    public String getUniqChannelID() {
        return account + type + EMIE;
    }
}