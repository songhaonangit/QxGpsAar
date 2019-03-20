#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <android/log.h>
#include <sys/ioctl.h>

//#define SIXLEDS 0x6C
//#define SIXLEDS_IOCTL_SET_LED1		_IOW(SIXLEDS, 0x1, int)
//#define SIXLEDS_IOCTL_SET_LED2		_IOW(SIXLEDS, 0x2, int)
//#define SIXLEDS_IOCTL_SET_LED3		_IOW(SIXLEDS, 0x3, int)
//#define SIXLEDS_IOCTL_SET_LED4		_IOW(SIXLEDS, 0x4, int)
//#define SIXLEDS_IOCTL_SET_LED5		_IOW(SIXLEDS, 0x5, int)
//#define SIXLEDS_IOCTL_SET_LED6		_IOW(SIXLEDS, 0x6, int)

//#define LANKELEDS 0xCC
//#define LANKELEDS_IOCTL_SET_LED1		_IOW(LANKELEDS, 0x1, int)
//#define LANKELEDS_IOCTL_SET_LED2		_IOW(LANKELEDS, 0x2, int)
//#define LANKELEDS_IOCTL_SET_LED3		_IOW(LANKELEDS, 0x3, int)
//#define LANKEBATTERY_IOCTL_GET_STATUS	_IOR(LANKELEDS, 0x4, int)
//#define LANKE_SET_POWER_ON_STM32		_IOW(LANKELEDS, 0x5, int)
//#define LANKE_GET_POWER_ON_STM32		_IOW(LANKELEDS, 0x5, int)


#define MC120M   0x55
/*
#define MC120M_IOCTL_POWER_ON	_IOW(MC120M, 0x1, int)
#define MC120M_IOCTL_SET_DAKA	_IOW(MC120M, 0x2, int)
#define MC120M_IOCTL_SET_WORK	_IOW(MC120M, 0x3, int)

#define MC120M_IOCTL_GET_DAKA	_IOR(MC120M, 0x2, int)
#define MC120M_IOCTL_GET_WORK	_IOR(MC120M, 0x3, int)
#define MC120M_IOCTL_GET_DATA	_IOR(MC120M, 0x3, int)
*/

//#define HALL1   'Z'
//#define HALL1_IOCTL_GET_STATUS   _IOR(HALL1,0x1,int)


#define MC120M_IOCTL_GET_NMEA_GLL   _IOR(MC120M, 0x32, int)
#define MC120M_IOCTL_GET_NMEA_GSA   _IOR(MC120M, 0x33, int)
#define MC120M_IOCTL_GET_NMEA_GXGSV1   _IOR(MC120M, 0x34, int)
#define MC120M_IOCTL_GET_NMEA_GXGSV2   _IOR(MC120M, 0x35, int)
#define MC120M_IOCTL_GET_NMEA_GXGSV3   _IOR(MC120M, 0x36, int)
#define MC120M_IOCTL_GET_NMEA_GXGSV4   _IOR(MC120M, 0x37, int)
#define MC120M_IOCTL_GET_NMEA_GXGSV5   _IOR(MC120M, 0x38, int)
#define MC120M_IOCTL_GET_NMEA_BDGSV1   _IOR(MC120M, 0x39, int)
#define MC120M_IOCTL_GET_NMEA_BDGSV2   _IOR(MC120M, 0x3a, int)
#define MC120M_IOCTL_GET_NMEA_BDGSV3   _IOR(MC120M, 0x3b, int)
#define MC120M_IOCTL_GET_NMEA_BDGSV4   _IOR(MC120M, 0x3c, int)
#define MC120M_IOCTL_GET_NMEA_BDGSV5   _IOR(MC120M, 0x3d, int)
#define MC120M_IOCTL_GET_NMEA_RMC   _IOR(MC120M, 0x3e, int)

#define MC120M_IOCTL_SET_NMEA_ALL   _IOW(MC120M, 0x31, int)
#define MC120M_IOCTL_GET_NMEA_GGA   _IOR(MC120M, 0x3f, int)
#define MC120M_IOCTL_SET_QXSDK_STATUS   _IOW(MC120M, 0x40, int)







static int fd  = -1;
extern "C" JNIEXPORT jint JNICALL

Java_com_getcharmsmart_hn_postion_MainActivity_open(
        JNIEnv *env,
        jobject /* this */) {
    //fd = open("/dev/fourleds", O_RDWR|O_NDELAY );
    fd = open("/dev/mc120m", O_RDWR|O_NDELAY );
   // fd = open("/dev/lanke_leds", O_RDONLY|O_NDELAY );
    //fd = open("/dev/HALL_1", O_RDWR|O_NDELAY );
    return fd;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_getcharmsmart_hn_postion_MainActivity_close(
        JNIEnv *env,
        jobject /* this */) {
    close(fd);
    fd = -1;
    return 0;
}


extern "C" JNIEXPORT jint JNICALL
Java_com_getcharmsmart_hn_postion_MainActivity_ioctl(
        JNIEnv *env,
        jobject /* this */,
        jint cmd,
        jint flag) {
    jint dev_cmd = 0;
    switch (cmd){
        case 1:
            dev_cmd = MC120M_IOCTL_SET_QXSDK_STATUS;
            break;
//        case 2:
//            dev_cmd = MC120M_IOCTL_GET_NMEA_GGA;
//            break;
//        case 3:
//            dev_cmd = MC120M_IOCTL_SET_NMEA_ALL;
//            break;

        default:
            break;
    }
    __android_log_print(ANDROID_LOG_INFO, "DEV_MC120M_CTL", " dev_cmd = 0x%x",dev_cmd);


    int result =  ioctl(fd, dev_cmd, &flag);


    __android_log_print(ANDROID_LOG_INFO, "DEV_MC120M_CTL", "IOCTL + %d",flag);
    __android_log_print(ANDROID_LOG_INFO, "DEV_MC120M_CTL", "IOCTL return  + %d",  result );
//    return flag;

    return result;
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_getcharmsmart_hn_postion_MainActivity_ioctll(
        JNIEnv *env,
        jobject /* this */ ,
        jint cmd,
        jstring flag) {
    jint dev_cmd = 0;
    switch (cmd){
//        case 1:
//            dev_cmd = MC120M_IOCTL_SET_QXSDK_STATUS;
//            break;
        case 2:
            dev_cmd = MC120M_IOCTL_GET_NMEA_GGA;
            break;
        case 3:
            dev_cmd = MC120M_IOCTL_SET_NMEA_ALL;
            break;

        default:
            break;
    }
    __android_log_print(ANDROID_LOG_INFO, "DEV_MC120M_CTL", " dev_cmd = 0x%x",dev_cmd);

    char data[4096];

    int aa =  ioctl(fd, dev_cmd, data);
    __android_log_print(ANDROID_LOG_INFO, "DEV_MC120M_CTL", "IOCTLL+%s",data);




   jstring jstr2 = env->NewStringUTF(data);

    __android_log_print(ANDROID_LOG_INFO, "DEV_MC120M_CTL", "IOCTLL return  + %d",  aa );



//    jclass cls = env->FindClass("java/lang/String");
//    jmethodID ctor = env->GetMethodID(cls, "<init>", "([BLjava/lang/String;)V");
//
//    env->CallVoidMethod(javathis,ctor,data);
//    env->DeleteLocalRef(javathis);


//    return flag;

    return jstr2;
}