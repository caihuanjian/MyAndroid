/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package cn.yinxm.app.sdk;

import android.os.RemoteException;

import cn.yinxm.app.server.IRemoteCallback;
import cn.yinxm.app.server.IRemoteService;

/**
 * Created by yinxuming on 2018/6/3.
 * RemoteChannelService的binder实现类，主要的通信接口在此实现
 */
public class RemoteChannelBinder extends IRemoteService.Stub {

    @Override
    public void sendCmdToServer(String pkg, String cmd, String action, String data) throws RemoteException {

    }

    @Override
    public void regCallback(IRemoteCallback callback) throws RemoteException {

    }

    @Override
    public void unregCallback(IRemoteCallback callback) throws RemoteException {

    }
}
