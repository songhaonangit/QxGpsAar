package com.getcharmsmart.qxgps;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by root on 2018/11/14.
 * GPS消息读取线程
 */

public abstract class GPSReadThread extends Thread {

    public abstract void onDataReceived(String str);

    private static final String TAG = GPSReadThread.class.getSimpleName();
    QxGPSManager qxGPSManager;
    private String mStr= "";
    private int cmd;
    private boolean isStop = false;
    int size = 0;
    public GPSReadThread(String str) {
        mStr = str;
    }
    public GPSReadThread(int mCmd,String str) {
        cmd = mCmd;
        mStr = str;
        qxGPSManager = new QxGPSManager();
    }
    @Override
    public void run() {
        super.run();

        while (!isInterrupted()) {
            if(isStop){
                return;
            }

//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            mStr = qxGPSManager.getData(cmd,"");

            Log.d(TAG, "run: ");
            if(TextUtils.isEmpty(mStr)){
                continue;
            }

            onDataReceived(mStr);


        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * 关闭线程 释放资源
     */
    public void release() {
        interrupt();

        if (null != mStr) {
                mStr = null;
        }
        isStop= true;

    }
}
