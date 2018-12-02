package cn.yinxm.iflytek.interfaces;

/**
 * Created by yinxm on 2017/1/1.
 * 功能: 语义理解完成回调
 */

public interface UnderstandResultCallback {
    /**
     * 语义理解返回结果
     * @param json
     */
    public void result(int code, String json);

}
