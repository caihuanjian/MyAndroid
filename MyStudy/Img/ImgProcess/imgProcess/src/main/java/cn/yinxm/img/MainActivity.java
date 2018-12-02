package cn.yinxm.img;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

     public void clickPrimaryColor(View view) {
        PrimaryColorActivity.startActivity(this);
    }

    public void clickColorMatrix(View view) {
        ColorMatrixActivity.startActivity(this);
    }

    public void clickPixelProcess(View view) {
        PixelProcessActivity.startActivity(this);
    }
}
