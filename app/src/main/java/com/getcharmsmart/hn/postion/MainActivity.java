package com.getcharmsmart.hn.postion;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bcgtgjyb.huanwen.customview.mylibrary.view58.Loding58View;
import com.getcharmsmart.hn.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    //MEMA 数据的文件地址
   // public final String FILE_NAME = Environment.getExternalStorageDirectory().getPath()+"/qxwz/gps_data.txt";

    public static final String TAG = "MainActivity";
    Button bt_open,bt_close,bt_gga,bt_nmea;
    TextView result;
    String mes = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_used_main);
        result =  (TextView)findViewById(R.id.tv_result);

        bt_open = (Button)findViewById(R.id.bt_open);
        bt_close = (Button)findViewById(R.id.bt_close);
        bt_gga = (Button)findViewById(R.id.bt_gga);
        bt_nmea = (Button)findViewById(R.id.bt_nmea);

        open();

        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ioctl(1,1);
            Toast.makeText(getApplicationContext(), "open...", Toast.LENGTH_SHORT).show();
            }
        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ioctl(1,0);
                Toast.makeText(getApplicationContext(), "close...", Toast.LENGTH_SHORT).show();
            }
        });

        bt_gga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ioctll(2,mes);

                Log.d("songhaonan","------------"+str);
                if(!TextUtils.isEmpty(str)){
                    //result.setText("GGA打卡成功\n"+ str);
                    result.setText( "gga:" +str);
                }


            }
        });

        bt_nmea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = ioctll(3,mes);
                if(!TextUtils.isEmpty(str1)){
                    result.setText("NMEA:"+ str1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 设置为竖屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        /**
         *检查网络状态
         */

        if (!NetWorkUtils.isNetworkAvailable(this)) {
            showSetNetworkUI(this);
        } else {

            if(NetWorkUtils.netCanUse(this)){
                Toast.makeText(this, "网络已连接可用...", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "当前网络不可用，请尝试更换网络连接方式，或者换个地方尝试使用...", Toast.LENGTH_SHORT).show();
            }


        }

        //检查权限
        verifyStoragePermissions(this);
    }

     /*
     * 打开设置网络界面
     */
    public void showSetNetworkUI(final Context context) {
    // 提示对话框
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("网络设置提示")
            .setMessage("网络连接不可用,是否进行设置?")
    .setPositiveButton("设置", new DialogInterface.OnClickListener() {

    @Override
    public void onClick(DialogInterface dialog, int which) {
    // TODO Auto-generated method stub
    Intent intent = null;
    // 判断手机系统的版本 即API大于10 就是3.0或以上版本
    if (android.os.Build.VERSION.SDK_INT > 10) {

        intent = new Intent(Settings.ACTION_SETTINGS);

    }else{

        intent = new Intent();
        ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(component);
        intent.setAction("android.intent.action.VIEW");
    }
     context.startActivity(intent);

    }
    }).create();

    builder.setCancelable(false).show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
    }



    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听 打卡的按键
        if (keyCode == 444) {
            Log.i(TAG, "---onKeyDown--keycode-------" + keyCode);
            Toast.makeText(getApplicationContext(),"打卡成功",Toast.LENGTH_SHORT).show();

            return true;
        }
        return false;
    }

    public native int open();

    public native int close();

    public native int ioctl(int cmd, int flag);
    public native String ioctll(int cmd, String flag);

}
