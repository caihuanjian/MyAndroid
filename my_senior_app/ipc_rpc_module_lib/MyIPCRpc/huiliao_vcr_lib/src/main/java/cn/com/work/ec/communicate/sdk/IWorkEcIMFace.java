package cn.com.work.ec.communicate.sdk;

/**
 * IM界面跳转
 */
public interface IWorkEcIMFace {

    /**
     * 切换到消息界面
     */
    void gotoWorkEcMsg();

    /**
     * 切换到消息列表界面
     */
    void gotoMsgList();

    /**
     * 切换到好友列表界面
     */
    void gotoFriendList();

    /**
     * 切换到亿咖好友列表界面
     */
    void gotoWorkEcFriendList();

    /**
     * 切换到群聊界面
     */
    void gotoChatGroup();

    /**
     * 切换到新朋友界面
     */
    void gotoNewFriend();

    /**
     * 切换到附近的人界面
     */
    void gotoNearbyFriend();
}
