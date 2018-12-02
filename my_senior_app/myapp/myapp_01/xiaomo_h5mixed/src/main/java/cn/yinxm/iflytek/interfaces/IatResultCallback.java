package cn.yinxm.iflytek.interfaces;

/**
 * Created by yinxm on 2017/1/1.
 * 功能:
 */

public interface IatResultCallback {
    /**
     *
     * @param code ResultCodeConstant.CODE_SUCCESS
     * @param str 识别后的字符串
     */
    public void result(int code, String str);
}
