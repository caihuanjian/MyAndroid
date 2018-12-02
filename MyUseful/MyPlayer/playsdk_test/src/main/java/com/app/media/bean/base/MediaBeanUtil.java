package com.app.media.bean.base;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yinxm.playsdk.test.LogUtil;

/**
 * 辅助使用MediaBean的工具
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/22
 */
public class MediaBeanUtil {

    public static final String KEY_MESSAGE_ID = "message_id";

    public static void saveExtJsonKeyValue(MediaBean bean, String key, Object value) {
        if (bean != null
                && !TextUtils.isEmpty(key)
                && value != null) {
            try {
                JSONObject jsonObject;
                if (!TextUtils.isEmpty(bean.getExtJsonStr())) {
                    jsonObject = new JSONObject(bean.getExtJsonStr());
                } else {
                    jsonObject = new JSONObject();
                }
                jsonObject.put(key, value);
                bean.setExtJsonStr(jsonObject.toString());
            } catch (JSONException e) {
                LogUtil.e(e);
            }
        }
    }

    public static Object getExtJsonFromKey(MediaBean bean, String key) {
        Object obj = null;
        if (bean != null && !TextUtils.isEmpty(key)) {
            try {
                JSONObject jsonObject;
                if (!TextUtils.isEmpty(bean.getExtJsonStr())) {
                    jsonObject = new JSONObject(bean.getExtJsonStr());
                } else {
                    jsonObject = new JSONObject();
                }
                obj = jsonObject.opt(key);
            } catch (JSONException e) {
                LogUtil.e(e);
            }
        }
        return obj;
    }
}
