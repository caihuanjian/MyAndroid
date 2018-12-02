package cn.yinxm.greendao;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import cn.yinxm.greendao.model.User;

public class MainActivity extends Activity {
    private static final String TAG = "yinxm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    public void test () {
        UserDbManager userDbManager = new UserDbManager(getApplicationContext());
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setAge(i * 3);
            user.setName("第" + i + "人");
            userDbManager.insertUser(user);
        }
        List<User> userList = userDbManager.queryUserList();
        Log.e(TAG, "test queryUserList="+userList);
        for (User user : userList) {
            Log.e(TAG, "queryUserList--before-->" + user.getId() + "--" + user.getName() +"--"+user.getAge());
            if (user.getId() == 0) {
                userDbManager.deleteUser(user);
            }
            if (user.getId() == 3) {
                user.setAge(10);
                userDbManager.updateUser(user);
            }
        }
        userList = userDbManager.queryUserList();
        for (User user : userList) {
            Log.e(TAG, "queryUserList--after--->" + user.getId() + "---" + user.getName()+"--"+user.getAge());
        }
    }
}
