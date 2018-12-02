package com.yinxm.puzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private EditText et;
    private Button btnStart;
    JigsawPuzzlePanel puzzlePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (puzzlePanel != null) {
                    String text = et.getText().toString();
                    int level = 0;
                    if (TextUtils.isEmpty(text)) {
                        level = puzzlePanel.getRandomLevel();
                    } else {
                        level = Integer.valueOf(text);
                    }
                    level = puzzlePanel.startGame(level);
                    et.setText(""+level);
                }
            }
        });

        puzzlePanel = (JigsawPuzzlePanel)findViewById(R.id.puzzlePanel);

        puzzlePanel.setRowColums(3, 3);

//        puzzlePanel.post(new Runnable() {
//            @Override
//            public void run() {
//
//                int viewWidth = puzzlePanel.getWidth();
//                int viewHeight = puzzlePanel.getHeight();
//                LogUtil.d("MainActivity width=" + viewWidth + ", height=" + viewHeight);
//            }
//        });



    }
}
