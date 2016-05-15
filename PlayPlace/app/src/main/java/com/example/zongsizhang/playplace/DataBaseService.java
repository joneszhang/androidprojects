package com.example.zongsizhang.playplace;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataBaseService extends Service {

    private SensorManager sm;
    static int rate = 1000000;
    private DataManager datam;
    private PatientData pat;
    long lasttime = 0;
    public DataBaseService() {

    }

    @Override
    public void onDestroy() {
        sm.unregisterListener(myAccelerometerListener);
        datam.closeManager();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sm = (SensorManager)getSystemService(this.SENSOR_SERVICE);
        datam = new DataManager(this);
        datam.startDB();
    }



    @Override
    public boolean onUnbind(Intent intent) {
        sm.unregisterListener(myAccelerometerListener);
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        sm.registerListener(myAccelerometerListener,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                this.rate);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        pat = (PatientData)intent.getSerializableExtra("pat");
        sm.registerListener(myAccelerometerListener,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                this.rate);

        return new Binder();
    }

    final SensorEventListener myAccelerometerListener = new SensorEventListener(){

        public void onSensorChanged(SensorEvent sensorEvent){
            long time = System.currentTimeMillis();
            if(time - lasttime < 1000) return;
            lasttime = time;
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                long tn = System.currentTimeMillis();
                //System.out.println("(" + x + "," + y + "," + z + ")");
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = new Date();
                String dt = dateFormat.format(date);
                datam.push_Data(pat.getTablename(), dt, x, y ,z);
                pat = datam.query_patient(pat);
                if(callBack != null)
                    callBack.getServiceData(pat);
                //System.out.println(datam.getDBPath());
            }
        }

        public void onAccuracyChanged(Sensor sensor , int accuracy) {

        }
    };

    public static interface CallBack {
        void getServiceData(PatientData data);
    }

    private CallBack callBack = null;
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public class Binder extends android.os.Binder {
        public void setData(String d) {
            // data 为 Service 类的属性，在内部类中可以很方便的访问到外部的属性

        }

        public DataBaseService getService(){
            return DataBaseService.this;
        }
    }
}
