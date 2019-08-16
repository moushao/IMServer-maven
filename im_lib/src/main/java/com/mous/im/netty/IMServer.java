package main.java.com.mous.im.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import main.java.com.mous.im.netty.envent.ServerClient;

public class IMServer implements ServerClient {
    private static volatile IMServer instance;
    //boss线程监听端口，worker线程负责数据读写
    EventLoopGroup boss, worker;
    private ServerBootstrap bootstrap;
    private Channel channel;
    //是否让主线程等待,默认为false
    private boolean isWait;

    public IMServer() {
    }

    public static IMServer getInstance() {
        if (instance == null) {
            synchronized (IMServer.class) {
                if (instance == null) {
                    instance = new IMServer();
                }
            }
        }
        return instance;
    }

    public IMServer isWait(boolean wait) {
        isWait = wait;
        return instance;
    }

    @Override
    public void init() {
        try {
            boss = new NioEventLoopGroup(/*2, new DefaultThreadFactory("server1", true)*/);
            worker = new NioEventLoopGroup(/*4, new DefaultThreadFactory("server1", true)*/);
            //辅助启动类
            bootstrap = new ServerBootstrap();
            //设置线程池
            bootstrap.group(boss, worker);
            //设置socket工厂
            bootstrap.channel(NioServerSocketChannel.class);
            //设置管道工厂
            bootstrap.childHandler(new ServerHandlerInitializer(this));
            //设置TCP参数
            //1.链接缓冲池的大小（ServerSocketChannel的设置）
            bootstrap.option(ChannelOption.SO_BACKLOG, 5024);
            //维持链接的活跃，清除死链接(SocketChannel的设置)
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //关闭延迟发送
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            System.out.println("server start ...... ");
            //绑定需要监听的端口
            bootstrap.bind(9090);
            ChannelFuture future = bootstrap.bind(9090).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        channel = channelFuture.channel();
                        System.out.println("服务已启动,成功监听端口");
                    }
                }
            });
            if (isWait)//是否开启主线程等待
                //取消端口监听,退出子线程,回到主线程,使用sync表示主线程等待
                future.sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        } finally {
            if (isWait)
                close();
        }
    }

    private void shutdownNioEvenLootGroup() {
        //优雅退出，释放线程池资源
        worker.shutdownGracefully();
        boss.shutdownGracefully();
        boss = null;
        worker = null;
    }

    @Override
    public void close() {
        try {
            if (bootstrap != null) {
                if (channel != null) {
                    channel.closeFuture();
                    channel.pipeline().remove(ServerHandler.class.getName());
                }
                shutdownNioEvenLootGroup();
                bootstrap.group().shutdownGracefully();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            channel = null;
            bootstrap = null;
        }
    }
}
