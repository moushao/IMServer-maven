package main.com.im.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ChannelContainer {

    private ChannelContainer() {

    }

    private static final ChannelContainer INSTANCE = new ChannelContainer();

    public static ChannelContainer getInstance() {
        return INSTANCE;
    }

    private final Map<String, NettyChannel> CHANNELS = new ConcurrentHashMap<>();

    public void saveChannel(NettyChannel channel) {
        if (channel == null) {
            return;
        }

        checkIsNewEquipment(channel);
        CHANNELS.put(channel.getAccount(), channel);
    }

    private void checkIsNewEquipment(NettyChannel channel) {
        //如果包含,就说明之前登陆过
        if (CHANNELS.containsKey(channel.getAccount())) {
            NettyChannel beforeChannel = CHANNELS.get(channel.getAccount());
            //判断当前登陆,是否和之前登陆的是否为同端同一设备
            if (channel.getUniqChannelID().equals(beforeChannel.getUniqChannelID())) {
                //TODO 如果当前登陆设备,与之前不是同端同一个登陆设备,则提示"被踢下线"
            }
            destroyNettyChannel(beforeChannel);
        }

    }

    /**
     * <br/> 方法名称: destroyNettyChannel
     * <br/> 方法详述:通过账户删除NettyChannel和Channel
     */
    private void destroyNettyChannel(NettyChannel channel) {
        if (channel == null)
            return;
        NettyChannel ch;
        Channel unUseChannel;
        try {
            ch = CHANNELS.remove(channel.getAccount());
            unUseChannel = ch.getChannel();
            unUseChannel.disconnect();
            unUseChannel.close();
            unUseChannel.eventLoop().shutdownGracefully();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            unUseChannel = null;
            ch = null;
        }
    }

    /**
     * <br/> 方法名称: removeChannelIfConnectNoActive
     * <br/> 方法详述: 删除断开的连接
     * <br/> 参数:channel:被断开的连接
     */
    public void removeChannelIfConnectNoActive(Channel channel) {
        if (channel == null) {
            return;
        }
        String channelId = channel.id().toString();
        NettyChannel nettyChannel = getChannelByChannelID(channelId);
        destroyNettyChannel(nettyChannel);
    }

    public NettyChannel getChannelByChannelID(String channelId) {
        for (Map.Entry<String, NettyChannel> entry : CHANNELS.entrySet()) {
            if (entry.getValue().getChannelId().equals(channelId)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * <br/> 方法名称: getChannelByAccount
     * <br/> 方法详述: 通过账户获取Channel
     * <br/> 参数: Account 账户名称
     */
    public NettyChannel getChannelByAccount(String Account) {
        NettyChannel channel = CHANNELS.get(Account);
        if (channel != null && channel.isActive()) {
            return channel;
        }
        return null;
    }
}
