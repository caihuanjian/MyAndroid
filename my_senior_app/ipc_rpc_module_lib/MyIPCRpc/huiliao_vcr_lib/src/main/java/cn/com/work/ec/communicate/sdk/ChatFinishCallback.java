package cn.com.work.ec.communicate.sdk;

import java.util.Map;

/**
 *  聊天消息发送完毕回调
 */
public interface ChatFinishCallback {
    /**
     *
     * @param code 执行结果状态
     * @param params 返回信息
     */
    void finish(FinishCode code, Map<String, Object> params);

    enum FinishCode{
        SUCCESS,
        FAIL;
    }
}


