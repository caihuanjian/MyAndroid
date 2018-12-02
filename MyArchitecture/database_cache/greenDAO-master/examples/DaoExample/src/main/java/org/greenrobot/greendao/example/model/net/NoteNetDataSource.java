package org.greenrobot.greendao.example.model.net;

import org.greenrobot.greendao.example.model.INoteDataSource;
import org.greenrobot.greendao.example.model.db.entity.Note;

import java.util.List;

/**
 * Created by yinxm on 2017/12/24.
 * 网络数据
 */

public class NoteNetDataSource implements INoteDataSource <Note>{

    @Override
    public void insert(Note data) {

    }

    @Override
    public void delete(Note data) {

    }

    @Override
    public void update(Note data) {

    }

    @Override
    public List<Note> query(int page, int size) {
        return null;
    }

    @Override
    public void insertBatch(List<Note> datas) {

    }

    @Override
    public void delBatch(List<Note> datas) {

    }

    @Override
    public List<Note> queryAll() {
        return null;
    }

    @Override
    public void insertOrReplace(Note data) {

    }
}
