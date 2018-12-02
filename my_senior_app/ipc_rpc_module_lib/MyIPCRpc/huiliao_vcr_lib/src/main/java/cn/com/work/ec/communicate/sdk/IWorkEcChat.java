package cn.com.work.ec.communicate.sdk;

/**
 * IM 聊天API
 */

public interface IWorkEcChat {

    /**
     * 跳转到指定好友的聊天界面
     * @param roomName 好友名称、群组名称
     */
    void gotoChatRoom(String roomName);

    /**
     * 发送文本消息
     * @param roomName 好友名称、群组名称，如果为空，则默认发送给当前聊天室
     * @param text 文本消息体
     * @param callback 发送不完毕回调
     */
    void sendMsgText(String roomName, String text, ChatFinishCallback callback);

    /**
     * 拨打语音电话
     * @param roomName 好友名称、群组名称
     * @param callback 电话拨打完毕回调
     */
    void callVoIP(String roomName, ChatFinishCallback callback);
}
