package com.getcharmsmart.qxgps;


import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by root on 19-3-18.
 */

public class QxGPSManager extends QxGPS{
    private static final String TAG = QxGPSManager.class.getSimpleName();
    private OnGpsDataListener mOnGpsDataListener;
    private GPSReadThread mGPSReadThread;
    private  String str="";
    int result = 0;
    public QxGPSManager setmOnGpsDataListener(OnGpsDataListener listener){
        mOnGpsDataListener = listener;
        return this;
    }
    public boolean openGps(int cmd){
        result = open();
        Log.d(TAG,"--openGps-result--"+result);
        if (result > 0){

           int setVal = ioctl(1,1);

            Log.d(TAG,"--openGps-setVal--"+setVal);

            startReadThread(cmd);

            return  true;
        }else {
            return false;
        }


    }

    public void closeGps(){
        ioctl(1,0);
        close();
        stopReadThread();
        mOnGpsDataListener = null;
    }


    /**
     * 开启接收消息的线程
     */
    private void startReadThread(int cmd) {
        mGPSReadThread = new GPSReadThread(cmd,str) {
            @Override
            public void onDataReceived(String str) {
                if (null != mOnGpsDataListener) {
                    mOnGpsDataListener.onDataReceived(str);
                }
            }
        };
        mGPSReadThread.start();
    }

    /**
     * 停止接收消息的线程
     */
    private void stopReadThread() {
        if (null != mGPSReadThread) {
            mGPSReadThread.release();
        }
    }

    public String getData(int cmd,String flag){
        String str = ioctll(cmd,flag);
        return str;
}

}
