package cn.yinxm.app.applist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AutoBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.d("yinxm","AutoBootReceiver.ACTION_BOOT_COMPLETED");
            // u can start your service here
            Toast.makeText(context, "收到开机广播", Toast.LENGTH_LONG).show();
            context.startService(new Intent(context, MyService.class));
            return;
        }
    }
}
