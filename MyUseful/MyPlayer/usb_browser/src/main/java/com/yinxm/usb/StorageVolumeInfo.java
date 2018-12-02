package com.yinxm.usb;

import android.text.TextUtils;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/10/8
 */
public class StorageVolumeInfo {
    /**
     * 设备名，不一定有值，没有值取path
     */
    private  String mDeviceName;
    /**
     * 挂载路径
     */
    private  String mPath;
    /**
     * 是否可以移除，一般U盘等外部存储设备都无法移除，内部存储设备可以移除
     */
    private  boolean mRemovable;


    public String getDeviceName() {
        if (TextUtils.isEmpty(mDeviceName)) {
            return getPath();
        }
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public boolean isRemovable() {
        return mRemovable;
    }

    public void setRemovable(boolean removable) {
        mRemovable = removable;
    }

    @Override
    public String toString() {
        return "\"StorageVolumeInfo\": {"
                + "\"mDeviceName\": \"" + mDeviceName + '\"'
                + ", \"mPath\": \"" + mPath + '\"'
                + ", \"mRemovable\": \"" + mRemovable
                + '}';
    }
}
