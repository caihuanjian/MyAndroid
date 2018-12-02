package org.greenrobot.greendao.example.model;

import java.util.List;

/**
 * Created by yinxm on 2017/12/24.
 */

public interface INoteDataSource <T>  extends IBaseDataSource <T>  {
    void insertBatch(List<T> datas);//批量插入
    void delBatch(List<T> datas);//批量删除,传入数据为空，清空数据
    List<T> queryAll();//查询全部
    void insertOrReplace(T data);//存在记录update，否则直接insert
}
