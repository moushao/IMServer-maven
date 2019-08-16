package main.java.com.mous.im.netty;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class ServerHandlerInitializer extends ChannelInitializer<Channel> {
    private IMServer imServer;

    public ServerHandlerInitializer(IMServer imServer) {
        this.imServer = imServer;
    }


    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(ServerHandler.class.getName(), new ServerHandler(imServer));
    }

}
