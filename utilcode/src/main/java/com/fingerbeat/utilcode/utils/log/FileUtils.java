package com.fingerbeat.utilcode.utils.log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：XieCaibao
 * 时间：2019/4/16
 * 邮箱：825302814@qq.com
 */

public class FileUtils {

    private static Object obj = new Object();

    public static void writeLogFile(String msg) {
        synchronized (obj) {
            try {
                File file = new File(IConfig.FILE_DIR, IConfig.FILE_NAME);
                FileWriter fw;
                if (file.exists()) {
                    if (file.length() > IConfig.getInstance().getFileSize()) {
//                        File lastFile = new File(App.getAppContext().getFilesDir(), IConfig.FILE_NAME_LAST);
//                        com.blankj.utilcode.util.FileUtils.copyFile(file, lastFile, () -> true);
                        //
                        File newFile = new File(file.getParent() + File.separator + IConfig.FILE_NAME_LAST);
                        if (newFile.exists()) {
                            newFile.delete();
                        }
                       file.renameTo(newFile);
                        fw = new FileWriter(file, false);
                    } else {
                        fw = new FileWriter(file, true);
                    }
                } else {
                    fw = new FileWriter(file, false);
                }
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("MM-dd HH:mm:ss");
                String dateStr = s.format(d);

                fw.write(String.format("%s %d-%d %s", dateStr, android.os.Process.myPid(), Thread.currentThread().getId(), msg));
                fw.write(13);
                fw.write(10);
                fw.flush();
                fw.close();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String readLogText() {
        FileReader fr = null;
        try {
            File file = new File(IConfig.FILE_DIR, IConfig.FILE_NAME);
            if (!file.exists()) {
                return "";
            }
            long n = IConfig.getInstance().getFileSize();
            long len = file.length();
            long skip = len - n;
            fr = new FileReader(file);
            fr.skip(Math.max(0, skip));
            char[] cs = new char[(int) Math.min(len, n)];
            fr.read(cs);
            return new String(cs).trim();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
