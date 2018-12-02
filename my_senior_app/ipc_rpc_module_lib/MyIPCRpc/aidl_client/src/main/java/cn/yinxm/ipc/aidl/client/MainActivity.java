package cn.yinxm.ipc.aidl.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.yinxm.ipc.aidl.IRemoteCallback;
import cn.yinxm.ipc.aidl.IRemoteServerInterface;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "yinxm";

    Button btnConnect, btnDisConnect;
    Button btnSend, btnReceive;
    TextView mTextView;
    IRemoteServerInterface mRemoteServer;

    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnDisConnect = (Button) findViewById(R.id.btnDisConnect);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnReceive = (Button) findViewById(R.id.btnReceive);

        btnConnect.setOnClickListener(this);
        btnDisConnect.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnReceive.setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.tv);

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick="+v.getId()+", mRemoteServer="+mRemoteServer);
        switch (v.getId()) {
            case R.id.btnConnect:
                Intent remoteServieIntent = new Intent();
                remoteServieIntent.setComponent(new ComponentName("cn.yinxm.ipc.aidl", "cn.yinxm.ipc.aidl.service.RemoteService"));
                bindService(remoteServieIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                Log.d(TAG, "bindService end");
                break;
            case R.id.btnDisConnect:
                try {
                    unbindService(mServiceConnection);
                    mRemoteServer = null;
                }catch (Exception e) {
                    Log.e(TAG, ""+e);
                }
                break;
            case R.id.btnSend:
                if (mRemoteServer != null) {
                    try {
                        mRemoteServer.setData("这是测试数据="+(num++));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btnReceive:
                if (mRemoteServer != null) {
                    try {
                       String  data =  mRemoteServer.getData();
                        mTextView.setText(data);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected name="+name+", service="+service);
            //将远程服务对象转换为AIDL接口对象
            mRemoteServer = IRemoteServerInterface.Stub.asInterface(service);
            Log.d(TAG, "mRemoteServer="+mRemoteServer);
            try {
                mRemoteServer.regRemoteCallback(remoteCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (mTextView != null) {
                mTextView.setText("Service is Connected");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {//注意调用了unbindService，不会回调这个方法
            Log.d(TAG, "onServiceDisconnected name="+name);
            try {
                mRemoteServer.unregRemoteCallback(remoteCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mRemoteServer = null;
            if (mTextView != null) {
                mTextView.setText("Service is DisConnected");
            }
        }
    };

    IRemoteCallback.Stub remoteCallback =  new IRemoteCallback.Stub() {
        @Override
        public void onResponse(final String aString) throws RemoteException {
            Log.d(TAG, "client 端收到Sever端实时消息="+aString+", currentThreadID="+Thread.currentThread().getId());
            mTextView.post(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText("client 端收到Sever端实时消息=\n"+aString);
                }
            });
        }
    };
}
