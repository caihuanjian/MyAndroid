package org.greenrobot.greendao.example.model.db;

import org.greenrobot.greendao.example.model.INoteDataSource;
import org.greenrobot.greendao.example.model.db.dao.NoteDao;
import org.greenrobot.greendao.example.model.db.entity.Note;

import java.util.List;

/**
 * Created by yinxm on 2017/12/24.
 * 本地数据
 */

public class NoteDbDataSource  implements INoteDataSource <Note>{

    @Override
    public void insert(Note data) {
        DBManager.getInstance().getDaoSession().insert(data);
    }

    @Override
    public void delete(Note data) {
        DBManager.getInstance().getDaoSession().getNoteDao().delete(data);
    }

    @Override
    public void update(Note data) {
        DBManager.getInstance().getDaoSession().getNoteDao().update(data);
    }

    @Override
    public List<Note> query(int page, int size) {
        return DBManager.getInstance().getDaoSession().getNoteDao().queryBuilder()
                .limit(size).offset(page*size)  //分页查询
                .orderDesc(NoteDao.Properties.Date) //按时间倒序排列
                .list();
    }

    @Override
    public void insertBatch(List<Note> datas) {
        DBManager.getInstance().getDaoSession().getNoteDao().insertInTx(datas);
    }

    @Override
    public void delBatch(List<Note> datas) {
        if (datas != null && datas.size() > 0) {
            DBManager.getInstance().getDaoSession().getNoteDao().deleteInTx(datas);
        }else {
            //清空数据
            DBManager.getInstance().getDaoSession().getNoteDao().deleteAll();
        }
    }

    @Override
    public List<Note> queryAll() {
//        return DBManager.getInstance().getDaoSession().getNoteDao().loadAll();
        return DBManager.getInstance().getDaoSession().getNoteDao().queryBuilder().orderDesc(NoteDao.Properties.Date).list();
    }

    @Override
    public void insertOrReplace(Note data) {
        DBManager.getInstance().getDaoSession().getNoteDao().insertOrReplace(data);
    }
}
