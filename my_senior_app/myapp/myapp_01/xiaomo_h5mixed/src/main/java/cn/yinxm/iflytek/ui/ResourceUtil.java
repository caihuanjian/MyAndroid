package cn.yinxm.iflytek.ui;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iflytek.cloud.thirdparty.aw;
import com.iflytek.msc.MSC;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by yinxm on 2017/1/1.
 * 功能:
 */

public class ResourceUtil {
    private static final String e = MSC.isIflyVersion()?"iflytek/":"cmcc/";
    private static final String f;
    private static HashMap<String, Drawable> g;
    private static HashMap<String, Drawable> h;
    public static int a;
    public static int b;
    public static int c;
    public static int d;

    public static int[] a() {
        return new int[]{-1579033, -9933198};
    }

    public static int[] b() {
        return new int[]{20, 16};
    }

    private static InputStream b(Context var0, String var1) throws IOException {
        InputStream var2 = var0.getAssets().open(var1);
        return var2;
    }

    public static View inflate(Context var0, String var1, ViewGroup var2) throws Exception {
        String var3 = f + var1 + ".xml";
        XmlResourceParser var4 = var0.getAssets().openXmlResourceParser(var3);
        LayoutInflater var5 = (LayoutInflater)var0.getSystemService("layout_inflater");
        return var5.inflate(var4, var2);
    }

    public static synchronized Drawable getDrawble(Context var0, String var1) throws Exception {
        Drawable var2 = (Drawable)g.get(var1);
        if(var2 == null) {
            var2 = c(var0, var1);
            g.put(var1, var2);
        }

        return var2;
    }

    private static Drawable c(Context var0, String var1) throws Exception {
        InputStream var2 = b(var0, e + var1 + ".png");
        TypedValue var3 = new TypedValue();
        var3.density = 240;
        Drawable var4 = null;
        if(Build.VERSION.SDK_INT > a) {
            var4 = aw.a(var0.getResources(), var3, var2, var1, (BitmapFactory.Options)null);
        } else {
            var4 = Drawable.createFromResourceStream(var0.getResources(), var3, var2, var1);
        }

        if(var2 != null) {
            var2.close();
        }

        return var4;
    }

    static {
        f = "assets/" + e;
        g = new HashMap();
        h = new HashMap();
        a = 3;
        b = 4;
        c = 7;
        d = 8;
    }
}
