package cn.yinxm.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.com.work.ec.communicate.sdk.WorkEcIMManager;
import cn.com.work.ec.communicate.sdk.IWorkEcIMFace;

/**
 * Created by yinxm on 2017/4/19.
 * 功能: 消息界面跳转实现
 */

public class WorkEcIMFaceIml implements IWorkEcIMFace {
    @Override
    public void gotoWorkEcMsg() {
        WorkEcIMManager workEcIMManager =  WorkEcIMManager.getInstance();
        Log.d("yinxm", "jarService WorkEcIMFaceIml.gotoWorkEcMsg getInstance="+workEcIMManager);

        Context context = WorkEcIMManager.getContext();
        Log.d("yinxm", "context="+context);
        if (context != null) {
//            Intent intent = new Intent("cn.yinxm.test", "cn.yinxm.test.ui.MainActivity");
            Intent intent = new Intent();
            intent.setClassName("cn.yinxm.test", "cn.yinxm.test.ui.MainActivity");//直接到达需要传递参数的界面
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putInt("opt", 10);
            bundle.putString("ext", "小明");
            intent.putExtra("action_huiliao_vcr", bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public void gotoMsgList() {
        Log.d("yinxm", "jarService WorkEcIMFaceIml.gotoMsgList");
        Context context = WorkEcIMManager.getContext();
        Log.d("yinxm", "context="+context);
        if (context != null) {
//            Intent intent = new Intent("cn.yinxm.test", "cn.yinxm.test.ui.MainActivity");
            Intent intent = new Intent();
            intent.setClassName("cn.yinxm.test", "cn.yinxm.test.ui.MainActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putInt("opt", 11);
            bundle.putString("ext", "gotoMsgList 测试");
            intent.putExtra("action_huiliao_vcr", bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public void gotoFriendList() {
//        Context context = BaseApplication.getContext();
//        LogUtil.d("WorkEcIMFaceIml.gotoFriendList context="+context);
//        if (context != null) {
//            context.startActivity(new Intent("com.work.ec.btphone.sample.test.CommunicateActivity"));
//        }
//        ToastUtil.showTextToast("打开好友列表");

    }

    @Override
    public void gotoWorkEcFriendList() {
        Log.d("yinxm","gotoWorkEcFriendList");

    }

    @Override
    public void gotoChatGroup() {

    }

    @Override
    public void gotoNewFriend() {

    }

    @Override
    public void gotoNearbyFriend() {

    }
}
