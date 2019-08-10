package main.com.im.netty.bean;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 类名: {@link LoginBodyModel}
 * <br/> 功能描述:IM登录的实体类
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/4/29
 */
public class LoginBodyModel {
    //登录名称
    public String AccountName;
    //密码
    public String Password;
    //登录时间
    public String LoginTime;

    // 登录人的服务器IP
    public String LoginServerIP;

    // 登录人的服务器端口
    public int LoginServerPort;
    // 登录人设备类型
    public int LoginClientType = 1;
    //备注
    public String Note;

    public LoginBodyModel() {
    }

    public LoginBodyModel(String accountName) {
        AccountName = accountName;
        Password = "123456";
        LoginTime = "";
        LoginServerIP = getLocalIpAddress();
        Note = "";
        LoginServerPort = 1;
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intF = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = intF.getInetAddresses(); enumIpAddress.hasMoreElements
                        (); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}