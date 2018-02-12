package com.android.base.util;

import android.content.Context;
import android.content.res.Resources;

import com.android.base.R;
import com.android.base.constant.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 拷贝数据库到手机存储空间
 *
 * @Title:
 * @Description:
 * @Author:08861
 * @Since:2015年1月26日
 * @Version:1.0.0
 */
public class CheckAndTransportFile implements Runnable {

    private Context context;

    public CheckAndTransportFile(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        moveFilePos();
    }

    /**
     * 移动数据库的位置
     */
    private void moveFilePos() {
        File dir = new File(Constant.FILE_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        File file = new File(dir, Constant.BAIDU_MAP_NAME);

        if (file.exists()) {
            file.delete();
        }
        FileUtils.loadDbFile(R.raw.custom_config_roadcolor, file, context.getResources(), context.getPackageName());
    }

    /**
     * 移动地图数据位置
     */
    private void moveMapData() {

    }

}

class FileUtils {
    public static void loadDbFile(int rawId, File file, Resources res, String pkgname) {
        InputStream dbInputStream = null;
        FileOutputStream fos = null;
        try {
            dbInputStream = res.openRawResource(R.raw.custom_config_roadcolor);
            fos = new FileOutputStream(file);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = dbInputStream.read(bytes)) > 0) {
                fos.write(bytes, 0, length);
            }
            fos.close();
            dbInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void moveFile(InputStream from, File toFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(toFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        moveFile(from, fos);
    }

    public static void moveFile(InputStream from, OutputStream to) {

        try {
            byte[] bytes = new byte[1024];
            int length;
            while ((length = from.read(bytes)) > 0) {
                to.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                from.close();
                to.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}