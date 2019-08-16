package main.java.com.mous.im.bean;

/**
 * Created by MouShao on 2019/4/30.
 */

public class IMMessage {
    // 11:建群 12：删除群 13：退群：14：加群 21：消息 31：登入 32:登出 41:获取离线消息  
    public int Command;
    public boolean Flag;
    public String Code;
    public String Msg = "";
    // 返回实体数据json
    public String Entity;
    public String ReceiveID;
}
