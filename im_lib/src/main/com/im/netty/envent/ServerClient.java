package main.com.im.netty.envent;

/**
 * 类名: {@link ServerClient}
 * <br/> 功能描述:ims抽象接口，需要切换到其它方式实现im功能，实现此接口即可
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/9
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public interface ServerClient {
    /**
     * 初始化IM
     */
    void init();

    /**
     * 关闭IM
     */
    void close();
}
