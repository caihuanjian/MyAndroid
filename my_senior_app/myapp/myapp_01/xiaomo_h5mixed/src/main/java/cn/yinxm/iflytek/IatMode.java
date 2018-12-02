package cn.yinxm.iflytek;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import cn.yinxm.constant.ResultCodeConstant;
import cn.yinxm.iflytek.interfaces.IatResultCallback;
import cn.yinxm.iflytek.util.JsonParser;
import cn.yinxm.util.LogUtil;
import cn.yinxm.util.StringUtil;

/**
 * Created by yinxm on 2017/1/1.
 * 功能: 录音听写模块
 */

public class IatMode {

    private Context context;

    private Toast mToast;
    //    private SharedPreferences mSharedPreferences;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    int ret = 0; // 函数调用返回值
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 听写结果的一个暂时存储
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //听写完毕后回调
    private IatResultCallback iatResultCallback;

    public IatMode(Context context, Toast toast) {
        this.context = context;
        this.mToast = toast;

//        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(context, mInitListener);

//        mSharedPreferences = context.getSharedPreferences("iflytek_config", Activity.MODE_PRIVATE);
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        setIatParam();
    }

    public IatMode(Context context) {
        this(context, null);
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            LogUtil.d("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void setIatParam() {
//        mIat.setParameter(SpeechConstant.PARAMS, null);
//        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//        String lag = "mandarin";
//        if (lag.equals("en_us")) {
//            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
//        } else {
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//            mIat.setParameter(SpeechConstant.ACCENT, lag);
//        }
//        mIat.setParameter(SpeechConstant.VAD_BOS, "3000");
//        mIat.setParameter(SpeechConstant.VAD_EOS, "3000");
//        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

//        String lag = mSharedPreferences.getString("iat_language_preference",
//                "mandarin");
//        if (lag.equals("en_us")) {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
//        } else {
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
//        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "2000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

    }

    public void startRecord(IatResultCallback iatResultCallback) {
        if (iatResultCallback != null) {
            this.iatResultCallback = iatResultCallback;
        }
        mIatResults.clear();
        boolean isShowDialog = true;
        if (isShowDialog) {
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
//            showTip("请开始说话");
        } else {
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("听写失败,错误码：" + ret);
            } else {
//            showTip("请开始说话");
            }
        }
    }


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            addIatResult(results);
            if (isLast) {
                iatResultCallback();
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            LogUtil.d("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            LogUtil.d(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            LogUtil.d("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            addIatResult(results);
            if (isLast) {
                iatResultCallback();
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            LogUtil.d("当前正在说话，音量大小：" + volume);
//            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 添加每次的识别结果
     * @param results
     */
    private void addIatResult(RecognizerResult results) {
        if (results == null) {
            return;
        }
        LogUtil.d("results="+results);
        String text = JsonParser.parseIatResult(results.getResultString());
        if (StringUtil.isBlank(text)) {
            return;
        }
        LogUtil.d("IflytekManager.addIatResult text="+text);
        String sn = null;
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            LogUtil.e("解析听写结果异常", e);
        }
        mIatResults.put(sn, text);
    }

    //得到iat结果
    public String getIatResutl() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mIatResults != null && !mIatResults.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = mIatResults.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                stringBuilder.append(entry.getValue());
            }
        }
        return stringBuilder.toString();
    }


    /**
     * 执行iat 听写回调
     */
    private void iatResultCallback() {
        String result = getIatResutl();
        LogUtil.d("result="+result);
        if (iatResultCallback != null) {
            iatResultCallback.result(ResultCodeConstant.CODE_SUCCESS, result);
        }
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            LogUtil.d("InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
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
            LogUtil.i("str="+str);
        }
    }
}
