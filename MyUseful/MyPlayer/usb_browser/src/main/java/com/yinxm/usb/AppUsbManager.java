package com.yinxm.usb;

import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/10/6
 */
public class AppUsbManager {
    private static final String TAG = "AppUsbManager";

    private Context mContext;
    private StorageManager mStorageManager;

    /**
     * 在多媒体页面，插入USB后，扫描完成后，自动播放
     */
    private boolean isAutoPlay = false;

    private AppUsbManager() {
    }

    public static AppUsbManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static AppUsbManager INSTANCE = new AppUsbManager();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
    }

    public List<StorageVolumeInfo> getVolumeList() {
        List<StorageVolumeInfo> storageVolumeInfoList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // api 24
            List<StorageVolume> storageVolumeList = mStorageManager.getStorageVolumes();

            for (StorageVolume storageVolume : storageVolumeList) {
                StorageVolumeInfo info = transform(storageVolume);
                if (info != null) {
                    info.setDeviceName(storageVolume.getDescription(mContext));
                    storageVolumeInfoList.add(info);
                }
            }
        } else {
            try {
                Class<?>[] paramClasses = {};
                Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
                Object[] params = {};
                Object[] invokes = (Object[]) getVolumeList.invoke(mStorageManager, params);

                if (invokes != null) {
                    for (Object obj : invokes) {
                        StorageVolumeInfo info = transform(obj);
                        if (info != null) {
                            storageVolumeInfoList.add(info);
                        }
                    }

                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        Log.d(TAG, "storageVolumeInfoList=" + storageVolumeInfoList);
        return storageVolumeInfoList;
    }

    public StorageVolumeInfo transform(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            StorageVolumeInfo info = new StorageVolumeInfo();

            Method getPath = obj.getClass().getMethod("getPath", new Class[0]);
            info.setPath((String) getPath.invoke(obj, new Class[0]));

            Method isRemovable = obj.getClass().getMethod("isRemovable", new Class[0]);
            info.setRemovable((boolean) isRemovable.invoke(obj, new Class[0]));

            return info;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
