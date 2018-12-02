package cn.yinxm.device.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import cn.yinxm.device.R;
import cn.yinxm.lib.utils.NetworkUtil;
import cn.yinxm.lib.utils.log.LogUtil;
import cn.yinxm.lib.utils.net.MobileSiginal;

/**
 * 网络信息测试
 */
public class NetInfoActivity extends Activity {
    TextView tv_net_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_info);

        tv_net_info = (TextView) findViewById(R.id.tv_net_info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetInfo();
    }

    public void getNetInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isNetworkConnected=").append(NetworkUtil.isNetworkConnected(getApplicationContext())).append(", ");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            stringBuilder.append("networkInfo=").append(networkInfo);
        }

        tv_net_info.setText(stringBuilder.toString());
    }

    public void clickReqNet(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

//                    Response response =  OkHttpUtil.getFormResponse("http://61.182.139.3:8088/music/857458.mp3?qcode=werd3819dkeK", null, null);
//                    LogUtil.d("response="+response);
//                    if (response != null) {
//                        LogUtil.d("headers="+response.headers().toString());
//                        LogUtil.d("body="+response.body().string());
//                    }

//                    URL url = new URL("http://61.182.139.3:8088/music/857458.mp3?qcode=werd3819dkeK");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setInstanceFollowRedirects(false);
//                    conn.connect();
//                    int resCode =  conn.getResponseCode();// trigger server redirect
//                    String location = conn.getHeaderField("Location");
//                    String realURL = conn.getURL().toString();
//                    LogUtil.d("location="+location+", realURL="+realURL+", resCode="+resCode);
//                    testConnection();
                } catch (Exception e) {
                    LogUtil.e(e);
                }
            }
        }).start();
        getPhoneState(getApplicationContext());
        MobileSiginal.getPhoneState(getApplicationContext(), new MobileSiginal.Callback() {
            @Override
            public void callback(String msg) {
                tv_net_info.append("\n"+msg);
            }
        });

    }


    private static void testConnection() throws Exception {
        String url = "http://61.182.139.3:8088/music/857458.mp3?qcode=werd3819dkeK";
        URL serverUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
        LogUtil.d("conn=" + conn);
        conn.setRequestMethod("GET");
        //必须设置false，否则会自动redirect到重定向后的地址
        conn.setInstanceFollowRedirects(false);
        conn.addRequestProperty("Accept-Charset", "UTF-8;");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
        conn.addRequestProperty("Referer", "http://matols.com/");
        conn.connect();
        Map<String, List<String>> map = conn.getHeaderFields();
        for (String key : map.keySet()) {
            LogUtil.d(key + "=" + map.get(key));
        }
        if (conn.getResponseCode() == 302) {
            String location = conn.getHeaderField("Location");
            LogUtil.d("location=" + location);
        }
        conn.disconnect();

        LogUtil.d(null);

    }


    public static void getPhoneState(Context context) {
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener MyPhoneListener = new PhoneStateListener() {
            @Override
            //获取对应网络的ID，这个方法在这个程序中没什么用处
            public void onCellLocationChanged(CellLocation location) {
                if (location instanceof GsmCellLocation) {
                    int CID = ((GsmCellLocation) location).getCid();
                } else if (location instanceof CdmaCellLocation) {
                    int ID = ((CdmaCellLocation) location).getBaseStationId();
                }
            }

            //系统自带的服务监听器，实时监听网络状态
            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
            }

            //这个是我们的主角，就是获取对应网络信号强度
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                //这个ltedbm 是4G信号的值
                String signalinfo = signalStrength.toString();
                String[] parts = signalinfo.split(" ");
                int ltedbm = Integer.parseInt(parts[9]);
                //这个dbm 是2G和3G信号的值
                int asu = signalStrength.getGsmSignalStrength();
                int dbm = -113 + 2 * asu;

                if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                   LogUtil.d("网络：LTE 信号强度：" + ltedbm + "======Detail:" + signalinfo);
                } else if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    String bin;
                    if (dbm > -75) {
                        bin = "网络很好";
                    } else if (dbm > -85) {
                        bin = "网络不错";
                    } else if (dbm > -95) {
                        bin = "网络还行";
                    } else if (dbm > -100) {
                        bin = "网络很差";
                    } else {
                        bin = "网络错误";
                    }
                   LogUtil.d("网络：WCDMA 信号值：" + dbm + "========强度：" + bin + "======Detail:" + signalinfo);
                } else {
                    String bin;
                    if (asu < 0 || asu >= 99) bin = "网络错误";
                    else if (asu >= 16) bin = "网络很好";
                    else if (asu >= 8) bin = "网络不错";
                    else if (asu >= 4) bin = "网络还行";
                    else bin = "网络很差";
                   LogUtil.d("网络：GSM 信号值：" + dbm + "========强度：" + bin + "======Detail:" + signalinfo);
                }
                super.onSignalStrengthsChanged(signalStrength);
            }
        };
        telephonyManager.listen(MyPhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

}
