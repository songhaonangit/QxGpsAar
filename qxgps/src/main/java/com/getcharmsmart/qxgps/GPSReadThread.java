package com.getcharmsmart.qxgps;

import android.util.Log;

/**
 * Created by root on 2018/11/14.
 * GPS消息读取线程
 */

public abstract class GPSReadThread extends Thread {

    public abstract void onDataReceived(String str);

    private static final String TAG = GPSReadThread.class.getSimpleName();
    QxGPSManager qxGPSManager;
    private String mStr;
    private int cmd;
    public GPSReadThread(String str) {
        mStr = str;
    }
    public GPSReadThread(int mCmd,String str) {
        cmd = mCmd;
        mStr = str;
    }
    @Override
    public void run() {
        super.run();
        qxGPSManager = new QxGPSManager();
        while (!isInterrupted()) {


                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            mStr = qxGPSManager.getData(cmd,"");

                Log.i(TAG, "run: ");
                int size = mStr.length();

                if (-1 == size || 0 >= size) {
                    return;
                }

               Log.i(TAG, "run: readBytes length= " + size);
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

    }
}
