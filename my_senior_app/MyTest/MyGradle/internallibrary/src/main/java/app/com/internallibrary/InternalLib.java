/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package app.com.internallibrary;

/**
 * Created by yinxuming on 2018/7/6.
 */
public class InternalLib {

    public static String getPubStr() {
        return "this is InternalLib.getPubStr";
    }

    private static String getPrivateStr() {
        return "this is InternalLib.getPrivateStr";
    }
}
