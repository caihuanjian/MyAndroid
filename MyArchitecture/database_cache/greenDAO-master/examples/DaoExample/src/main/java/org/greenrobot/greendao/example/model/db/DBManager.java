package org.greenrobot.greendao.example.model.db;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.example.model.INoteDataSource;
import org.greenrobot.greendao.example.model.db.dao.DaoMaster;
import org.greenrobot.greendao.example.model.db.dao.DaoMaster.DevOpenHelper;
import org.greenrobot.greendao.example.model.db.dao.DaoSession;
import org.greenrobot.greendao.example.model.db.entity.Note;


/**
 * Created by yinxm on 2017/12/24.
 */

public class DBManager {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    private INoteDataSource<Note> noteDataSource;

    private static DBManager instance;

    public static DBManager getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                }
            }
        }
        return instance;
    }

    private DBManager() {}

    public DBManager initDb(Context context) {
        //只在主线程初始化一次
        DevOpenHelper helper = new DevOpenHelper(context, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        noteDataSource = new NoteDbDataSource();
        return getInstance();
    }

    /**
     * 供页面查询使用
     * @return
     */
    public INoteDataSource<Note> getNoteDataSource() {
        if (noteDataSource == null) {
            noteDataSource = new NoteDbDataSource();
        }
        return noteDataSource;
    }

    /**
     * 供DataSource层使用，也可以供页面自定义查询使用
     * @return
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
