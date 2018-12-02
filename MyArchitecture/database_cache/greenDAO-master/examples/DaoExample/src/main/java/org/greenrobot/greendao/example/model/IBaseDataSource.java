package org.greenrobot.greendao.example.model;

import java.util.List;

/**
 * Created by yinxm on 2017/12/24.
 */

public interface IBaseDataSource <T> {
    void insert(T data);
    void delete(T data);
    void update(T data);
    List<T> query(int page, int size);
}
