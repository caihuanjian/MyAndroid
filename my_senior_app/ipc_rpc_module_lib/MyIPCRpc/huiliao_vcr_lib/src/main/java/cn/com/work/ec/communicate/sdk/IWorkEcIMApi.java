package cn.com.work.ec.communicate.sdk;

/**
 * IM支持的API
 */
public interface IWorkEcIMApi {

    /**
     * 获取IM界面跳转API
     * @return IWorkEcIMFace
     */
    IWorkEcIMFace getImFace();

    /**
     * 获取IM聊天API
     * @return IWorkEcChat
     */
    IWorkEcChat getChat();
}
