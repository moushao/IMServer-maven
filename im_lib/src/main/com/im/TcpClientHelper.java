package main.com.im;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TCP客户端工具类
 */
public class TcpClientHelper {

    private String serverIP;//服务器地址

    private int serverPort;//服务端口

    private Socket socket;//连接

    private Timer timer = null;//定时器

    private boolean isCancelTimer = false;//是否取消定时器

    private boolean isConnect = false;//是否连接

    private MsgReceiveListener listener;

    /**
     * 构造方法
     *
     * @param serverIP
     * @param serverPort
     * @throws IOException
     */
    public TcpClientHelper(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        createSocket();
        isConnect = true;
    }

    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    public void sendMessage(final String msg) {
        //通过客户端的套接字对象Socket方法，获取字节输出流，将数据写向服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream out = null;
                try {
                        out.write(msg.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 启动接收消息线程
     */
    public void startReceive() {

        new Thread(new Runnable() {
            public void run() {
                //读取服务器发回的数据，使用socket套接字对象中的字节输入流6
                InputStream in = null;

                while (isConnect) {
                    try {
                        in = socket.getInputStream();
                        if (in.available() != 0) {
                            byte[] data = new byte[10240];

                            int len = in.read(data);
                            String msg = new String(data, 0, len, "UTF-8");
                            if (listener != null) {
                                listener.receive(msg);
                            }
                            onReceive(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private String onReceive(String msg) {
        //todo
        System.out.println(msg);
        return msg;
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (socket != null && socket.isConnected()) {
                isConnect = false;
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
                stopHeart();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动心跳
     */
    public void startHeart() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int i = 1;

            @Override
            public void run() {

                try {
                    if (isCancelTimer) {
                        timer.cancel();
                        return;
                    }

                    System.out.println(i++);
                    if (socket == null || !socket.isConnected() || socket.isClosed() || isServerClosed()) {
                        createSocket();
                        if (!isServerClosed()) {
                            startReceive();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2000);
    }

    /**
     * 创建连接
     *
     * @return
     */
    private void createSocket() {
        socket = new Socket();
        try {
            socket.setKeepAlive(true);
            //socket.setSoTimeout(5000);//设置读操作超时时间30 s
            //socket.bind(new InetSocketAddress(localPort));
            socket.connect(new InetSocketAddress(serverIP, serverPort));
            isConnect = socket.isConnected();
            isConnect = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止心跳
     */
    public void stopHeart() {
        isCancelTimer = true;
    }

    /**
     * 返回是否连接
     *
     * @return
     */
    public boolean isConnect() {
        return isConnect;
    }

    private boolean isServerClosed() {
        //通过客户端的套接字对象Socket方法，获取字节输出流，将数据写向服务器
        OutputStream out = null;
        try {
            byte data[] = new byte[1];
            out = socket.getOutputStream();
            out.write(data);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public MsgReceiveListener getListener() {
        return listener;
    }

    public void setListener(MsgReceiveListener listener) {
        this.listener = listener;
    }
}

