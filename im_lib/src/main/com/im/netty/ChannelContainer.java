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
     * <br/> 方法详述:通过id删除NettyChannel和Channel
     * <br/> 参数:
     * <br/> 返回值:
     * <br/> 异常抛出 Exception:
     * <br/> 异常抛出 NullPointerException:
     */
    private void destroyNettyChannel(NettyChannel channel) {
        NettyChannel ch = CHANNELS.remove(channel.getAccount());
        Channel channel1 = ch.getChannel();
        try {
            channel1.disconnect();
            channel1.close();
            channel1.eventLoop().shutdownGracefully();
        } catch (Exception e) {

        } finally {
            channel1 = null;
            ch = null;
        }
        CHANNELS.remove(channel.getAccount());
    }

    public NettyChannel removeChannelIfConnectNoActive(Channel channel) {
        if (channel == null) {
            return null;
        }

        String channelId = channel.id().toString();
        return removeChannelIfConnectNoActive(channelId);
    }

    public NettyChannel removeChannelIfConnectNoActive(String channelId) {
        if (CHANNELS.containsKey(channelId) && !CHANNELS.get(channelId).isActive()) {
            return CHANNELS.remove(channelId);
        }

        return null;
    }

    public String getUserIdByChannel(Channel channel) {
        return getUserIdByChannel(channel.id().toString());
    }

    public String getUserIdByChannel(String channelId) {
        if (CHANNELS.containsKey(channelId)) {
            return CHANNELS.get(channelId).getAccount();
        }
        return null;
    }

    public NettyChannel getChannelByUserId(String userId) {
        /*for (Map.Entry<String, NettyChannel> entry : CHANNELS.entrySet()) {
            if (entry.getValue().getUserId().equals(userId) && entry.getValue().isActive()) {
                return entry.getValue();
            }
        }*/

        NettyChannel channel = CHANNELS.get(userId);
        if (channel != null && channel.isActive()) {
            return channel;
        }
        return null;
    }
}
