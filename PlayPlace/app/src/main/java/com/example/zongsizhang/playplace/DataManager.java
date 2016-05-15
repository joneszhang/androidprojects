package com.example.zongsizhang.playplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongsizhang on 3/3/16.
 */
public class DataManager implements Serializable{
    private PatientData cur_patient;
    SQLiteDatabase db = null;
    private Context context;

    public DataManager(Context context) {
        cur_patient = new PatientData();
        cur_patient.refreshData();
        this.context = context;
    }

    public void startDB(){
        db = context.openOrCreateDatabase("zzspatients.db", Context.MODE_PRIVATE, null);
    }

    public boolean loadDB(String path){
        db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE,null);
        if(db == null){
            System.out.println("no such file");
            return false;
        }
        else return true;
    }

    public PatientData query_patient(PatientData key) {
        String table_name = key.getTablename();
        if (!IsTableExist(table_name)) db.execSQL("CREATE TABLE " + table_name +" (time DATETIME CURRENT_TIMESTAMP, x float, y float, z float)");
        Cursor c = db.rawQuery("SELECT * FROM "+ table_name + " order by time desc limit 0, 10",null);
        //System.out.println(c.getCount());
        if(c.getCount() == 0) return key;
        String[] times = new String[c.getCount()];
        float[] vls = new float[c.getCount()];
        float[] hls = new float[c.getCount()];
        float[] vs = new float[c.getCount()];
        int i = 0;
        while(c.moveToNext()){
            times[i] = String.valueOf(c.getFloat(0));
            vls[i] = (c.getFloat(1));
            hls[i] = (c.getFloat(2));
            vs[i] = (c.getFloat(3));
            ++i;
        }
        key.setHlabels(times);
        float max = 0;
        float min = vls[0];
        for(int p = 0;p < vls.length; ++p){
            if(max < vls[p]) max = vls[p];
            if(max < hls[p]) max = hls[p];
            if(max < vs[p]) max = vs[p];
            if(min > vls[p]) min = vls[p];
            if(min > hls[p]) min = hls[p];
            if(min > vs[p]) min = vs[p];
        }
        max += 0.1f;
        min -= 0.1f;
        float scale = (max- min) / 9;
        String[] vlhh = new String[vls.length];
        for(int p = 0;p < vls.length; ++p){
            vlhh[p] = String.valueOf(min + p * scale);
        }
        key.setVlabels(vlhh);

        List<float[]> muti_values = new ArrayList<float[]>();
        muti_values.add(vls);
        muti_values.add(hls);
        muti_values.add(vs);
        key.setMuti_values(muti_values);

        key.setMax(max);
        key.setMin(min);
        return key;
    }

    public void push_Data(String table_name, String dt, float x, float y, float z){
        if (!IsTableExist(table_name)) db.execSQL("CREATE TABLE " + table_name +" (time DATETIME CURRENT_TIMESTAMP, x float, y float, Z float)");
        ContentValues cv = new ContentValues();
        cv.put("time", dt);
        cv.put("x", x);
        cv.put("y", y);
        cv.put("z", z);
        db.insert(table_name, null, cv);
    }

    private boolean IsTableExist(String table_name) {
        boolean isTableExist = true;
        Cursor c = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + table_name + "'", null);
        if(c.moveToNext()){
            if (c.getInt(0) == 0) {
                isTableExist = false;
            }
        }
        c.close();
        return isTableExist;
    }

    public String getDBPath(){
        //String packageName = context.getPackageName();
        String DB_PATH = db.getPath();
        //System.out.print(DB_PATH);
        return db.getPath();
    }
    public void closeManager(){
        db.close();
    }

}
