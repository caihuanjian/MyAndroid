// IRemoteService.aidl
package cn.yinxm.app.server;

import cn.yinxm.app.server.IRemoteCallback;
// Declare any non-default types here with import statements

interface IRemoteService {
    void sendCmdToServer(String pkg, String cmd, String action, String data);
    void regCallback(IRemoteCallback callback);
    void unregCallback(IRemoteCallback callback);
}
