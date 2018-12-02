package cn.yinxm.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.yinxm.greendao.model.DaoMaster;
import cn.yinxm.greendao.model.DaoSession;
import cn.yinxm.greendao.model.User;
import cn.yinxm.greendao.model.UserDao;

/**
 * 功能：
 * Created by yinxm on 2017/9/13.
 */

public class DBManager {
    private final static String dbName = "test.db";
    private Context mContext;
    private DaoMaster.DevOpenHelper openHelper;
    private DBManager(){
        openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
    }

    public static DBManager getInstance() {
        return DBManagerFactory.instance;
    }

    private static class DBManagerFactory {
        private static DBManager instance = new DBManager();
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param user
     */
    public void insertUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

}
