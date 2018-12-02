// IRemoteCallback.aidl
package cn.yinxm.ipc.aidl;

//server端通过接口回调进行跨进程通信，此时接口回调必须声明为AIDL形式
interface IRemoteCallback {
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,double aDouble, String aString);
    void onResponse(String aString);
}
