package main.com.im.netty.bean;

/**
 * 类名: {@link MessageContextModel}
 * <br/> 功能描述:IM消息综合实体类
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/4/29
 */
public class MessageContextModel {
    public int Command;
    // 消息表头
    public MsgHeaderModel Header;
    // 发送消息
    public IMMessageContent MsgBody;
    // 登录体
    public LoginBodyModel LoginBody;

}

