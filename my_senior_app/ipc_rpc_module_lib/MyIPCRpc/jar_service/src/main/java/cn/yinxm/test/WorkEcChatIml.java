package cn.yinxm.test;

import android.util.Log;

import cn.com.work.ec.communicate.sdk.ChatFinishCallback;
import cn.com.work.ec.communicate.sdk.IWorkEcChat;

/**
 * Created by yinxm on 2017/1/23.
 * 功能: 聊天消息操作实现
 */

public class WorkEcChatIml implements IWorkEcChat {
    @Override
    public void gotoChatRoom(String roomName) {
    }

    @Override
    public void sendMsgText(String roomName, String text, ChatFinishCallback callback) {
        Log.d("yinxm","消息发送完毕");
    }

    @Override
    public void callVoIP(String roomName, ChatFinishCallback callback) {

    }
}
