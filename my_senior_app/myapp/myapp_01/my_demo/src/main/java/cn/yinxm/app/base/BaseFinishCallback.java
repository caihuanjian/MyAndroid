package cn.yinxm.app.base;

/**
 * 功能：操作完成回调
 * Created by yinxm on 2017/6/16.
 */

public interface BaseFinishCallback<T> {
    /**
     *
     * @param finishCode 执行状态
     * @param t  返回值
     */
    void finish(FinishCode finishCode, T t);

    enum FinishCode {
        SUCCESS,
        FAILED
    }
}
