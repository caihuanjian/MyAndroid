package cn.yinxm.h5mixed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cn.yinxm.constant.ResultCodeConstant;
import cn.yinxm.iflytek.IflytekManager;
import cn.yinxm.iflytek.interfaces.UnderstandResultCallback;
import cn.yinxm.interfaces.iml.JavaScriptCallAndroidInterfaceIml;
import cn.yinxm.util.LogUtil;

public class MainActivity extends AppCompatActivity {
    public static final int MSG_CALL_JS_UNDERSTAND_RESULT = 1;
    ViewGroup llUrl;
    EditText etUrl;
    Button btnOpen;
    WebView webview;
    ImageView img_speech_input;

    public Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            LogUtil.d("handler msg="+msg+", what="+msg.what);
            switch (msg.what) {
                case MSG_CALL_JS_UNDERSTAND_RESULT:
                    if (webview != null) {
                        // TODO: 2017/1/25
                        webview.loadUrl("");
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llUrl = (ViewGroup) findViewById(R.id.llUrl);
        etUrl = (EditText) findViewById(R.id.etUrl);
        btnOpen = (Button) findViewById(R.id.btnOpen);
        webview = (WebView) findViewById(R.id.webview);
        BaseApplication.setWebView(webview);
        img_speech_input = (ImageView) findViewById(R.id.img_speech_input);
        initData();
        IflytekManager.getInstance().init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        closeInput(etUrl);
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String url = sp.getString("url", null);
        if (url != null && !"".equals(url)) {
            etUrl.setText(url);
            changeUrl();
        }

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUrl();
//                android.R.layout.navigation_bar;
//                img_speech_input.performClick();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                    img_speech_input.performClick();
//                    img_speech_input.performLongClick()
//                    img_speech_input.callOnClick();
//                }
//                IflytekManager.getInstance().startTts("小聪聪，今天星期几", null);

            }
        });
        img_speech_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "点记录语音按钮", Toast.LENGTH_SHORT).show();
                recordVoice();
            }
        });

        initWebView();

    }

    private void initWebView() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        //允许js调用
        webview.getSettings().setJavaScriptEnabled(true);
        // TODO: 2017/1/2  
//        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);

        //允许js调用android的接口,有注入漏洞风险，api 17已禁用
        webview.addJavascriptInterface(new JavaScriptCallAndroidInterfaceIml(), "jsm");

        //允许网页alert弹窗，默认不允许
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹出窗口
        webview.setWebChromeClient(new WebChromeClient() {
            //Sets the chrome handler. This is an implementation of WebChromeClient
            // for use in handling JavaScript dialogs, favicons, titles, and the progress. This will replace the current handler.
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.alert_title).setMessage(message)

                        .setPositiveButton("确认",
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                        // MyWebView.this.finish();
                                    }
                                });

                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                LogUtil.d("onJsConfirm url="+url+", message="+message+", result="+result);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                LogUtil.d("onJsPrompt url="+url+", message="+message+", defaultValue="+defaultValue+", result="+result);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

        //启用mixedContent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        //处理加载https问题
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed();
                //handleMessage(Message msg); 其他处理
            }
        });

    }

    //改变url加载数据
    public void changeUrl() {
        String url = etUrl.getText().toString().trim();
        Log.d("yinxm", "url=" + url);
        if (url != null && !"".equals(url)) {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            sp.edit().putString("url", url).commit();
            webview.loadUrl(url);
        }
        closeInput(etUrl);
    }

    //录音
    public void recordVoice() {
//        IflytekManager.getInstance().startRecord(new IatResultCallback() {
//            @Override
//            public void result(int code, String str) {
////                LogUtil.d("recordVoice code="+code+", str="+str);
//                if (code == ResultCodeConstant.CODE_SUCCESS) {
////                    iatResult
//                    LogUtil.d("webview js execute " + str);
////                    webview.loadUrl("javascript:alt");
//                    webview.loadUrl("javascript:iatResult('"+str+"')");
//                }
//            }
//        });

        IflytekManager.getInstance().startUnderstand(0, null, new UnderstandResultCallback() {
            @Override
            public void result(int code, String json) {
                if (code == ResultCodeConstant.CODE_SUCCESS) {
//                    iatResult
                    LogUtil.d("webview js execute " + json);
//                    webview.loadUrl("javascript:alt");
                    webview.loadUrl("javascript:understandResultJson('"+json+"')");
                }
            }
        });
    }

    /**
     * 关闭键盘
     *
     * @param
     */
    public void closeInput(EditText view) {
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            Log.d("yinxm", "keycode=" + event.getKeyCode());
            closeInput(etUrl);
            changeUrl();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * 这里监控的是物理返回或者设置了该接口的点击事件
         * 当按钮事件为返回时，且WebView可以返回，即触发返回事件
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                String url = webview.getUrl();
                LogUtil.d("返回上一个网页url="+url);//不是上一个网页的地址
                // TODO: 2017/1/1
//                etUrl.setText(url);
                //拦截事件
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
