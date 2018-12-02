package cn.yinxm.interfaces.iml;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import cn.yinxm.constant.ResultCodeConstant;
import cn.yinxm.h5mixed.BaseApplication;
import cn.yinxm.iflytek.IflytekManager;
import cn.yinxm.iflytek.interfaces.UnderstandResultCallback;
import cn.yinxm.interfaces.JavaScriptCallAndroidInterace;
import cn.yinxm.util.LogUtil;

/**
 * Created by yinxm on 2017/1/1.
 * 功能:
 */

public class JavaScriptCallAndroidInterfaceIml implements JavaScriptCallAndroidInterace {

    @JavascriptInterface
    public void startIat() {
        IflytekManager.getInstance().startRecord(null);
    }

    @Override
    public void startVoiceUnderstand() {
        IflytekManager.getInstance().startUnderstand(0, null, new UnderstandResultCallback() {
            @Override
            public void result(int code, String json) {
                if (code == ResultCodeConstant.CODE_SUCCESS) {
//                    iatResult
                    LogUtil.d("startVoiceUnderstand webview js execute " + json);
                    WebView webView = BaseApplication.getWebView();//最好的方式是用观察者模式，Activity为被观察对象，初始化显示注册监听器
                    if (webView != null) {
                        webView.loadUrl("javascript:understandResultJson('"+json+"')");
                    }
                }
            }
        });
    }

    @JavascriptInterface
    public void startTts(String text) {
        IflytekManager.getInstance().startTts(text, null);
    }

    @JavascriptInterface
    public void stopTts() {
        IflytekManager.getInstance().stopTts();

    }
}
