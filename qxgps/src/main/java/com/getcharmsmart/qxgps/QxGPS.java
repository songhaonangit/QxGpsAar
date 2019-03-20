package com.getcharmsmart.qxgps;

/**
 * Created by root on 19-3-18.
 */

public class QxGPS {

    static {
        System.loadLibrary("qx-native-lib");
    }





    public native int open();

    public native int close();

    public native int ioctl(int cmd, int flag);
    public native String ioctll(int cmd, String flag);
}
