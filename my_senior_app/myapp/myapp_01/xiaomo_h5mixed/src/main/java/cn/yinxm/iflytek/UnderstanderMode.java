package cn.yinxm.iflytek;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;

import cn.yinxm.constant.ResultCodeConstant;
import cn.yinxm.h5mixed.R;
import cn.yinxm.iflytek.interfaces.UnderstandResultCallback;
import cn.yinxm.util.LogUtil;
import cn.yinxm.util.StringUtil;

/**
 * Created by yinxm on 2017/1/1.
 * 功能: 通用语义理解
 */

public class UnderstanderMode {
    private Context context;
    private Toast mToast;

    // 语义理解对象（语音到语义）。
    private SpeechUnderstander mSpeechUnderstander;
    // 语义理解对象（文本到语义）。
    private TextUnderstander mTextUnderstander;
    // 语音听写UI
    private RecognizerDialog mIatDialog;

    private UnderstandResultCallback resultCallback;
    int ret;

    public UnderstanderMode(Context context, Toast toast) {
        this.context = context;
        this.mToast = toast;

        /**
         * 申请的appid时，我们为开发者开通了开放语义（语义理解）
         * 由于语义理解的场景繁多，需开发自己去开放语义平台：http://www.xfyun.cn/services/osp
         * 配置相应的语音场景，才能使用语义理解，否则文本理解将不能使用，语义理解将返回听写结果。
         */
        // 初始化对象
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(context, mSpeechUdrInitListener);
        mTextUnderstander = TextUnderstander.createTextUnderstander(context, mTextUdrInitListener);

        mIatDialog = new RecognizerDialog(context, mSpeechUdrInitListener);
        setParam();
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
//        String lang = mSharedPreferences.getString("understander_language_preference", "mandarin");
        String lang = "mandarin";
        if (lang.equals("en_us")) {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lang);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, "2000");

        // 设置标点符号，默认：1（有标点）
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/sud.wav");
    }

    /**
     * 开始语义理解
     *
     * @param type         0:语音转语义；1:文字转语义
     * @param text
     * @param resultCallback
     */
    public void startUnderstand(int type, String text, UnderstandResultCallback resultCallback) {
        this.resultCallback = resultCallback;
        if (type == 1) {
            LogUtil.d("startUnderstand 文字语义理解");
            if (mTextUnderstander.isUnderstanding()) {
                mTextUnderstander.cancel();
                LogUtil.d("startUnderstand 取消");
            } else {
                ret = mTextUnderstander.understandText(text, mTextUnderstanderListener);
                if (ret != 0) {
                    showTip("语义理解失败,错误码:" + ret);
                }
            }
        } else {
            LogUtil.d("startUnderstand 语音语义理解");
//            boolean isShowDialog = true;
//            if (isShowDialog) {
//                // 显示听写对话框
//                mIatDialog.setListener(mSpeechUnderstanderListener);
//                mIatDialog.show();
////            showTip("请开始说话");
//            } else {
//                // 不显示听写对话框
//                ret = mIat.startListening(mRecognizerListener);
//                if (ret != ErrorCode.SUCCESS) {
//                    showTip("听写失败,错误码：" + ret);
//                } else {
////            showTip("请开始说话");
//                }
//            }

            if (mSpeechUnderstander.isUnderstanding()) {// 开始前检查状态
                mSpeechUnderstander.stopUnderstanding();
                showTip("停止录音");
            } else {
//                mIatDialog.show();
                ret = mSpeechUnderstander.startUnderstanding(mSpeechUnderstanderListener);
                if (ret != 0) {
                    showTip("语义理解失败,错误码:" + ret);
                } else {
                    showTip(context.getString(R.string.text_begin));
                }
            }
        }
    }

    /**
     * 停止语义理解
     */
    public void stopUnderstand() {
        try {
            if (mTextUnderstander.isUnderstanding()) {
                mTextUnderstander.cancel();
            }
            if (mSpeechUnderstander.isUnderstanding()) {
                mSpeechUnderstander.stopUnderstanding();
            }
        } catch (Exception e) {
            LogUtil.e("stopUnderstand异常", e);
        }
    }

    /**
     * 执行iat 听写回调
     */
    private void iatResultCallback(String result) {
        LogUtil.d("result=" + result);
        if (resultCallback != null) {
            if (StringUtil.isNotBlank(result)) {
                resultCallback.result(ResultCodeConstant.CODE_SUCCESS, result);
            } else {
                resultCallback.result(ResultCodeConstant.CODE_ERROR, result);
            }
        }
    }


    /**
     * 初始化监听器（语音到语义）。
     */
    private InitListener mSpeechUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtil.d("speechUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };

    /**
     * 初始化监听器（文本到语义）。
     */
    private InitListener mTextUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtil.d("textUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };

    private TextUnderstanderListener mTextUnderstanderListener = new TextUnderstanderListener() {
        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                // 显示
                String text = result.getResultString();
//                if (!TextUtils.isEmpty(text)) {
//                    mUnderstanderText.setText(text);
                iatResultCallback(text);
//                }
            } else {
                LogUtil.d("understander result:null");
                showTip("识别结果不正确。");
                iatResultCallback(null);
            }
        }

        @Override
        public void onError(SpeechError error) {
            // 文本语义不能使用回调错误码14002，请确认您下载sdk时是否勾选语义场景和私有语义的发布
            showTip("onError Code：" + error.getErrorCode());

        }
    };

    /**
     * 语义理解回调。
     */
    private SpeechUnderstanderListener mSpeechUnderstanderListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                LogUtil.d(result.getResultString());

                // 显示
                String text = result.getResultString();
//                if (!TextUtils.isEmpty(text)) {
//                    mUnderstanderText.setText(text);
                iatResultCallback(text);
//                }
            } else {
                showTip("识别结果不正确。");
                iatResultCallback(null);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
//            LogUtil.d(data.length + "");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    private void showTip(final String str) {
        if (mToast != null) {
            mToast.setText(str);
            mToast.show();
        } else {
            LogUtil.i("str=" + str);
        }
    }
}
