package main.com.im.netty.bean;

/**
 * 类名: {@link MsgHeaderModel}
 * <br/> 功能描述:IM消息发送头信息
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/4/29
 */
public class MsgHeaderModel {
    // 消息作者
    public String SpeakID;
    // 消息接收者
    public String ReceiveID;
    // 目标端类型： 1：安卓 2：PC  
    public int SpeakClientType = 1;

    public MsgHeaderModel(String speakID, String receiveID) {
        SpeakID = speakID;
        ReceiveID = receiveID;
    }
}