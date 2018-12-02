package com.app.media.vcr;

import android.content.Context;
import android.content.Intent;

import com.app.media.vcr.iml.NextCmdProcessor;
import com.app.media.vcr.iml.PreviousCmdProcessor;
import com.app.media.vcr.iml.QueryCmdProcessor;
import com.app.media.vcr.iml.RadioCmdProcessor;


/**
 * 语音控制
 * Created by yinxuming on 2018/8/1.
 */
public class VoiceController {

    public static final String TAG = "vcr";

    /**
     * 标准数据格式，包含的关键字
     */
    public static final String STANDARD_DATA_KEY = "audio_list";


    private VoiceController() {
    }

    public static VoiceController getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final VoiceController INSTANCE = new VoiceController();
    }

    /**
     * 是否是标准数据格式
     *
     * @return
     */
    public boolean isStandardData(Intent intent) {
        return intent != null && intent.hasExtra(STANDARD_DATA_KEY);
    }

    /**
     * 处理通过广播发过来的语音控制指令
     *
     * @param intent
     */
    public void processVcrCmd(Context context,  String action, Intent intent) {
        switch (action) {
            case VoiceCmdConstant.NEXT:
                new NextCmdProcessor().process(context, intent);
                break;
            case VoiceCmdConstant.PREVIOUS:
                new PreviousCmdProcessor().process(context, intent);
                break;
            case VoiceCmdConstant.QUERY:
                new QueryCmdProcessor().process(context, intent);
                break;
            case VoiceCmdConstant.RADIO:
                new RadioCmdProcessor().process(context, intent);
                break;
            default:
                break;

        }
    }

}
