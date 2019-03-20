package com.getcharmsmart.qxgps;

/**
 * Created by root on 19-3-18.
 */

public interface OnGpsDataListener {
    /**
     * 数据接收
     *
     * @param str 接收到的数据
     */
    void onDataReceived(String str);
}
