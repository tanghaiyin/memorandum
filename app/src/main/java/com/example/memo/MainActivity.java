package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView txt_list_view;
    private Button exitapp2, exitapp;
    private List<String> txtList;
    private TxtListAdapter txtListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClearContent.getInstance().addActivity(this);
        txt_list_view = findViewById(R.id.relist);
        exitapp = findViewById(R.id.exitapp);
        exitapp2 = findViewById(R.id.exitapp2);
        exitapp2.setOnClickListener(v -> {
            Intent intent = new Intent(this, TxtDateActivity.class);
            startActivity(intent);
        });
        exitapp.setOnClickListener(v -> {
            ClearContent.getInstance().exit();
        });
        if (applyPermission(10086)) {
            initList();
//            Log.e("Lo2gs", readFileLine(getFilesDir() + "/title.txt").get(0) + "");
            Log.e("Lo2g", getFilesDir() + "/title.txt" + "");
        }
    }

    AlertDialog al;
    private void initList() {
        txtList = new ArrayList<>();
        List<String> ls = new ArrayList<>();
        File[] files = getFilesDir().listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".txt")) {
                Log.e("外面2", files[i].getName());
                ls.add(files[i].getName());
            }
        }
        txtList=ls;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        txt_list_view.setLayoutManager(linearLayoutManager);
        txtListAdapter = new TxtListAdapter(txtList, this);
        txt_list_view.setAdapter(txtListAdapter);
        txtListAdapter.setOnItemClickListener(position -> {
            Log.e("读",readFile(getFilesDir() + "/" + files[position].getName()));
            Intent intentread = new Intent(MainActivity.this, TxtDateActivity.class);
            intentread.putExtra("txttitle", files[position].getName().substring(0, files[position].getName().lastIndexOf(".")));
            intentread.putExtra("txmain", readFile(getFilesDir() + "/" + files[position].getName()));
            MainActivity.this.startActivity(intentread);
        });
        txtListAdapter.setOnItemLongClickListener(position -> {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.delete_log, null);
            Button buttontxt = view.findViewById(R.id.deletxt);
            buttontxt.setOnClickListener(v -> {
                if (new File(getFilesDir() + "/" + files[position].getName()).exists()){
                    Log.e("TAG23", ""+new File(getFilesDir() + "/" + files[position].getName()).delete());
                    txtList.remove(position);
                    al.dismiss();
                    txtListAdapter.changeSelected();
                }
            });
            ab.setView(view);
            al = ab.show();
            Log.e("TAG2", "The File doesn't not exist.");
        });
    }
    public static String readFile(String filename) {
        StringBuilder fileContent = new StringBuilder("");
        File file = new File(filename);
        BufferedReader bufferedReader = null;
        String str = null;
        try {
            if (file.exists()) {
                bufferedReader = new BufferedReader(new FileReader(filename));
                while ((str = bufferedReader.readLine()) != null) {
                    if (!fileContent.toString().equals("")) {
                        fileContent.append("\r\n");
                    }
                    fileContent.append(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return fileContent.toString();
    }
    public static String readTxt(String path){
        String str = "";
        try {
            File urlFile = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String mimeTypeLine = null ;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str+mimeTypeLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  str;
    }

    public String readFileLine(String strFilePath) {
        //判断文件是否存在
        if (new File(strFilePath).exists()) {
            List<String> txtList = new ArrayList<>();
            File file = new File(strFilePath);
            String content = "";
            if (file.isDirectory()) {
                Log.d("TAG", "The File doesn't not exist.");
            } else {
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader = new InputStreamReader(instream);
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line;
                        //逐行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line;
                        }
                        instream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return content;
        } else {
            return null;
        }

    }


    public boolean applyPermission(int requestCode) {
        boolean isPermission;
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int code = checkCallingOrSelfPermission(permissions[0]);
        if (code == PackageManager.PERMISSION_GRANTED) {
            isPermission = true;
        } else {
            isPermission = false;
            ActivityCompat.requestPermissions(MainActivity.this, permissions, requestCode);
        }
        return isPermission;

    }
}