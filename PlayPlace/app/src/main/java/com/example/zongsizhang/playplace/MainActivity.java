package com.example.zongsizhang.playplace;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    private EditText in_id;
    private EditText in_age;
    private EditText in_name;
    private GraphView grapher;
    private CheckBox qek_male;
    private CheckBox qek_female;
    private Button b_run;
    private Button b_stop;
    private Button b_upload;
    private Button b_download;
    private boolean onMonitor;

    private DataManager dataManager;

    private PatientData patient;

    private DataBaseService.Binder binder = null;

    private UploadTask mTask;
    private DownloadTask dlTask;

    int serverResponseCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //input-id
        in_id = (EditText)findViewById(R.id.input_pid);
        in_age = (EditText)findViewById(R.id.input_age);
        in_name = (EditText)findViewById(R.id.input_name);
        qek_male = (CheckBox)findViewById(R.id.check_male);
        qek_female = (CheckBox)findViewById(R.id.check_female);
        grapher = (GraphView)findViewById(R.id.graphv);
        b_run = (Button)findViewById(R.id.button_run);
        b_stop = (Button)findViewById(R.id.button_stop);
        b_upload = (Button)findViewById(R.id.button_upload);
        b_download = (Button)findViewById(R.id.button_download);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataManager = new DataManager(this);
        dataManager.startDB();

        qek_male.setOnClickListener(new sexListener());
        qek_female.setOnClickListener(new sexListener());
        qek_female.setChecked(false);
        qek_male.setChecked(true);
        b_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMonitor) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Already on monitor", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                startMonitor();
            }
        });

        b_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!onMonitor) return;
                endMonitor();
            }
        });

        b_upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String path = dataManager.getDBPath();
                mTask = new UploadTask();
                mTask.execute(path);

            }
        });

        b_download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dlTask = new DownloadTask();
                dlTask.execute();
            }
        });

        grapher.init(null, "", null, null, GraphView.LINE);


    }

    public class sexListener implements CheckBox.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.check_male){
                qek_female.setChecked(false);
                qek_male.setChecked(true);
            }else{
                qek_female.setChecked(true);
                qek_male.setChecked(false);
            }
        }
    }

    public void startMonitor(){
        String id = in_id.getText().toString();
        String name = in_name.getText().toString();
        String age = in_age.getText().toString();
        int agendar = -1;
        if(qek_female.isChecked()) agendar = 1;
        else if(qek_male.isChecked()) agendar = 0;
        if("".equals(id) || "".equals(name) || "".equals(age) || -1 == agendar){
            Toast toast=Toast.makeText(getApplicationContext(), "please fill all blanks", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        patient = new PatientData(id,name,age,agendar);
        patient.refreshData();
        grapher.init(patient.getValues(), patient.getTitle(), patient.getHlabels(), patient.getVlabels(), GraphView.LINE);
        grapher.invalidate();
        onMonitor = true;
        Intent intent  = new Intent(MainActivity.this, DataBaseService.class);
        Bundle b = new Bundle();
        //b.putSerializable();
        intent.putExtra("pat", patient);
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    public void endMonitor() {
        grapher.clearGraph();
        in_id.setText("");
        in_name.setText("");
        in_age.setText("");
        qek_male.setChecked(false);
        qek_female.setChecked(false);
        grapher.invalidate();
        onMonitor = false;
        Intent intent  = new Intent(MainActivity.this, DataBaseService.class);
        unbindService(connection);
        dataManager.closeManager();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection connection = new ServiceConnection(){

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DataBaseService.Binder) service;
            binder.getService().setCallBack(new DataBaseService.CallBack(){
                @Override
                public void getServiceData(PatientData data) {
                    grapher.setGraph(data.getMuti_values(), data.getHlabels(), data.getVlabels(), data.getMax(), data.getMin());
                }
            });
        }
    };

    private class UploadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... params) {
            File dataf = new File(params[0]);
            MainActivity.this.uploadFile("https://impact.asu.edu/Appenstance/UploadToServerGPS.php",params[0]);
            return null;
        }
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... params) {
            String filename = "dldb.db";
            String path = MainActivity.this.downloadFile("https://impact.asu.edu/Appenstance/zzspatients.db",filename);
            //System.out.println();
            SQLiteDatabase db_download = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            return null;
        }
    }

    public int uploadFile(String uri, String filepath){
        String fileName = filepath;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        final File sourceFile = new File(fileName);

        //--------------------------------------------------------------------------------------------------
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        //-------------------------------------------------------------------------------------------------------

        if (!sourceFile.isFile()){
            Log.e("uploadFile", "Source File not exist: ");
            return 0;
        }
        else {
            try {
                //open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(uri);
                Log.i("uploadFile", sourceFile.getName());

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("upload_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
                        + fileName + " " + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0){
                    Log.i("uploading", Integer.toString(bytesRead));
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "File Upload success.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "File Upload failed, please check network connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        return serverResponseCode;
    }

    public String downloadFile(String uri, String filename){

        //--------------------------------------------------------------------------------------------------
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        //-------------------------------------------------------------------------------------------------------
        int totalSize = 0;
        int downloadedSize = 0;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File file = new File(SDCardRoot,filename);
            //System.out.println(file.getPath());
            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;

            }
            //close the output stream when complete //
            fileOutput.close();


        } catch (final MalformedURLException e) {
            //showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            //showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "File Upload failed, please check network connection",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        return Environment.getExternalStorageDirectory() + File.separator + filename;
    }
}
