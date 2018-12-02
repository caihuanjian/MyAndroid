package com.app.media.vcr.iml;

import android.content.Context;
import android.content.Intent;

import com.app.media.manager.UiCallBackManager;
import com.app.media.vcr.VoiceController;

import cn.yinxm.playsdk.test.LogUtil;

import static com.app.media.callback.UiCallBack.SHOW_MSG_NO;


/**
 * 有声
 * Created by yinxuming on 2018/8/1.
 */
public class RadioCmdProcessor extends AbsCmdProcessor {
    @Override
    public void process(Context context, Intent intent) {
        super.process(context, intent);

        LogUtil.d("RadioCmdProcessor.process ");
        final String audioListJson = intent.getStringExtra(VoiceController.STANDARD_DATA_KEY);
        if (audioListJson != null) {
        } else {
            UiCallBackManager.getInstance().getUiCallBackToast().show(SHOW_MSG_NO);
        }
    }
}
