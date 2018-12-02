package cn.yinxm.screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yinxm.lib.screen.DimenAdaptUtil;
import cn.yinxm.lib.screen.ScreenAdapter;
import cn.yinxm.lib.utils.log.LogUtil;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView tv;
    View mWindowTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        tv = (TextView) findViewById(R.id.tv);
        mWindowTip = findViewById(R.id.windowTip);
        LogUtil.setLogEnabled(true);

        ScreenAdapter.getInstance().setUiDesign(1080, 1920, 1);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //测试将图片放不同路径下，得到的宽高是否有变化 【缩放处理】
            String str = "原图144*144——》宽："+img.getWidth()+", 高："+img.getHeight();
            tv.setText(str);
            LogUtil.e(str);
//            设备信息：1080*1920，density=3, xxhdpi，densityDPI=480dp，
//            原图144*144，放mdpi， density=1
//            实际显示：432*432 = 144*3倍，结论，xxhdpi去mdpi里面找图，会放大3倍

            LogUtil.e("mWindowTip w=" + mWindowTip.getWidth() + ", h=" + mWindowTip.getHeight());
        }
    }

    public void genDimensFile() {
        //文件路径，最小宽度限定符 swdp
        DimenAdaptUtil.genDimens(
                "D:\\androidCode\\BanTing2.0_Git\\BanTing\\huiting\\src\\main\\res\\values-sw720dp-land\\dimens.xml", 720,
                "D:\\dimens.xml", 811
        );
    }

    public void showWindowManger(View view) {
        UsbTipWindowManager.getInstance().showDiscoveryUsb(getApplicationContext());
    }
}
