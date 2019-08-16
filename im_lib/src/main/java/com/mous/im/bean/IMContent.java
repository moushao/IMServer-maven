package main.java.com.mous.im.bean;

/**
 * 类名: {@link IMContent}
 * <br/> 功能描述:IM消息发送体信息
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/4/29
 */
public class IMContent {
    public String MessageID;

    // 发送者ID
    public String SendID;

    // 发送人姓名
    public String SendName;

    // 接受者ID
    public String ReceiveID;

    // 接收人姓名
    public String ReceiveName;


    // 10：点对点文本消息           11：点对点文件消息      12：点对点语音消息      13：点对点视频消息 14：点对点图片
    // 20：群文本消息               21：群文件消息          22：群语音消息         23：群视频消息     24：群图片
    public int MessageType;

    // 1：已发送  2：离线
    public int MessageStatus;

    // 发送事件
    public String SendTime;

    // 内容Text
    public String SendContent = "";

    // 发送类型 1:android 2:PC
    public int SendClientType;

    // 文件名称
    public String SendFileName = "";

    // 文件路径
    public String SendFilePath = "";
    // 添加时间
    public String AddTime;

    // 群组id
    public String GroupID;

    //群名称
    public String GroupName;

}