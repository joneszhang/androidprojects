package com.example.zongsizhang.playplace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongsizhang on 1/28/16.
 */
public class PatientData implements Serializable{
    private String id = "0000";
    private String name = "test";
    private String age = "20";
    private String tablename = null;
    private int agendar = 0; //0 is male, 1 is female
    private float[] values = null;
    private String title = "table";
    private List<float[]> muti_values = new ArrayList<float[]>();
    private float max;
    private float min;
    public List<float[]> getMuti_values() {
        return muti_values;
    }

    public void setMuti_values(List<float[]> muti_values) {
        this.muti_values = muti_values;
    }

    private String[] vlabels;
    private String[] hlabels;


    public PatientData(String pid, String pname, String page, int psex){
        this.id = pid;
        this.name = pname;
        this.agendar = psex;
        this.age = page;
        values = new float[]{0};
    }


    public PatientData(){

    }



    public void reset(String pid, String pname, int psex){
        this.id = pid;
        this.name = pname;
        this.agendar = psex;
    }

    public void refreshData(){
        title = "table";
        values = new float[]{1.2f,3f,3.0f,4.0f,1.2f,3f,3.0f,5.0f,1.2f,3f,3.0f,4.2f,1.2f,3f,3.0f,3.3f};
        vlabels = new String[]{"200","300","400","500"};
        hlabels = new String[]{"100","200","300","400","500"};
    }

    public String getTitle() {
        return title;
    }

    public String[] getVlabels() {
        return vlabels;
    }

    public String[] getHlabels() {
        return hlabels;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAgendar() {
        return agendar;
    }

    public float[] getValues() {
        return values;
    }

    public String getAge() {
        return age;
    }

    public String getTablename() {
        return this.getName() + "_" + this.getId() + "_" + this.getAge() + "_" + this.getAgendar();
    }

    public void setHlabels(String[] hlabels) {
        this.hlabels = hlabels;
    }

    public void setVlabels(String[] vlabels) {
        this.vlabels = vlabels;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }
}
