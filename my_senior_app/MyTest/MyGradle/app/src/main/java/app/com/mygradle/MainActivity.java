package app.com.mygradle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import app.com.mylibrary.MyLib;

/**
 *
 *
 * 依赖顺序
 *    APP——》myLib——》InternalLib
 *
 *    结论，
 *
 *    1、app依赖myLib的方式，不会影响myLib内部依赖的工程（internalLib）的访问权限（APP中是否能访问到internalLib中的类）
 *
 *    2、myLib依赖innternalLib的方式，直接影响到innternalLib对外是否可以访问
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        API依赖App ——api/impl—— mylibrary，  mylibrary ——api——  internallibrary
//        InternalLib.getPubStr(); // 可以访问
//        MyLib.getPubStr(); //可以访问

//        API依赖App ——api/impl—— mylibrary，  mylibrary ——[impl]——  internallibrary
//        InternalLib.getPubStr(); // 无法访问
        MyLib.getPubStr();  //可以访问

    }
}
