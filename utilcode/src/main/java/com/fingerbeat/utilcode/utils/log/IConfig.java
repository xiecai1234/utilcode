package com.fingerbeat.utilcode.utils.log;

/**
 * 作者：XieCaibao
 * 时间：2019/4/16
 * 邮箱：825302814@qq.com
 */

public class IConfig {

    private boolean SHOW_LOG = false;
    private boolean WRITE_LOG = false;

    public static final String FILE_DIR = "/sdcard";//log日志的文件   getAppContext().getFilesDir()
    public static final String FILE_NAME = "log.log";//log日志的文件名称
    public static final String FILE_NAME_LAST = "log_last.log";//达到大小限制时上一次的文件进行备份

    private long fileSize = 100000;//日志文件的大小，默认0.1M

    private String TAG = "TLOG";//Logcat中显示的tag

    private static IConfig instance;

    public static IConfig getInstance() {
        if (instance == null) {
            synchronized (IConfig.class) {
                if (instance == null) {
                    instance = new IConfig();
                }
            }
        }
        return instance;
    }
    public IConfig isShowLog(boolean isShowLog){
        SHOW_LOG = isShowLog;
        return this;
    }

    public boolean getIsShowLog() {
        return SHOW_LOG;
    }

    public IConfig isWriteLog(boolean isWriteLog){
        WRITE_LOG = isWriteLog;
        return this;
    }

    public boolean getIsWriteLog(){
        return WRITE_LOG;
    }

    public IConfig fileSize(long size){
        this.fileSize = size;
        return this;
    }

    public long getFileSize(){
        return this.fileSize;
    }

    public IConfig tag(String tag){
        TAG = tag;
        return this;
    }

    public String getTag(){
        return TAG;
    }

}
