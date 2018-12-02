package cn.yinxm.test;


import cn.com.work.ec.communicate.sdk.WorkEcIMManager;
import cn.com.work.ec.communicate.sdk.IWorkEcChat;
import cn.com.work.ec.communicate.sdk.IWorkEcIMFace;

/**
 * Created by yinxm on 2017/4/24.
 * 功能: 具体业务接口实现，名称不能被混淆
 */

public class WorkEcIMApiIml extends WorkEcIMManager {
    private IWorkEcIMFace workEcIMFace;
    private IWorkEcChat workEcChat;

    public WorkEcIMApiIml() {
        workEcIMFace = new WorkEcIMFaceIml();
        workEcChat = new WorkEcChatIml();
    }

    @Override
    public IWorkEcIMFace getImFace() {
        return workEcIMFace;
    }

    @Override
    public IWorkEcChat getChat() {
        return workEcChat;
    }
}
