// IRemoteServerInterface.aidl
package cn.yinxm.ipc.aidl;

//即使在同一个包下面，也需要手动import callback
import cn.yinxm.ipc.aidl.IRemoteCallback;
//远程服务
interface IRemoteServerInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,double aDouble, String aString);

    //向服务器传递数据
    void setData(String data);
    //获取服务器数据[非实时,一般是定时器]
    String getData();

    //服务器实时告诉客户端：Binder 里面提供一个注册回调的方法,
    //与单进程不同的是，这个接口需要写成AIDL形式，并提供注册、解注册方法RemoteCallbackList
    void regRemoteCallback(IRemoteCallback callback);
    //解注册回调
    void unregRemoteCallback(IRemoteCallback callback);
}
