package com.example.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class TxtDateActivity extends AppCompatActivity {
    Button txtsave,cx,cx2;
    EditText txt_edit, txt_area;
    final String TITLE_TXT = "/title.txt";
    String maintxt = "/main.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClearContent.getInstance().addActivity(this);
        setContentView(R.layout.main_body);
        txtsave = findViewById(R.id.txtsave);
//        cx2 = findViewById(R.id.cx2);
        cx = findViewById(R.id.cx);
        txt_area = findViewById(R.id.txt_area);
        txt_edit = findViewById(R.id.txt_edit);
        TxtCheXiao txtCheXiao = new TxtCheXiao(txt_area);
//        txt_area.setOnKeyListener((v, keyCode, event) -> {
//            if (event.getAction() == KeyEvent.ACTION_UP && KeyEvent.KEYCODE_ENTER == keyCode){
//                txt_area.setText(txt_area.getText()+"\r\n");
//            }
//            return true;
//        });
        txtsave.setOnClickListener(v -> {
            if (!txt_edit.getText().toString().equals("")){
                writeCof(txt_edit.getText().toString());
            }else {
                Toast.makeText(this,"标题为空！",Toast.LENGTH_LONG).show();
            }
        });

        if (getIntent().getStringExtra("txttitle")!=null){
            txt_edit.setText(getIntent().getStringExtra("txttitle"));
            txt_area.setText(getIntent().getStringExtra("txmain"));
        }
        cx.setOnClickListener(v -> {
            txtCheXiao.undo();
        });
//        cx2.setOnClickListener(v -> {
//            txtCheXiao.redo();
//        });
    }

    String mod;
    public void writeCof(String fs) {
        if (getIntent().getStringExtra("txttitle")!=null){
            new File(this.getFilesDir()+"/" + getIntent().getStringExtra("txttitle")+".txt").delete();
        }
        Log.e("L2og", this.getFilesDir() + fs+".txt");
        File file = new File(this.getFilesDir()+"/" + fs+".txt");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(txt_area.getText().toString().getBytes("UTF-8"));
            fos.close();
        } catch (Exception e) {
            Log.e("Log", e + "");
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setMessage("确定要退出吗？")
                    .setNegativeButton("退出", (dialog, which) -> {
                        this.finish();
            })
                    .setNeutralButton("取消", (dialog, which) -> {

                    })
                    .setPositiveButton("保存并退出", (dialog, which) -> {
                        if (!txt_edit.getText().toString().equals("")){
                            writeCof(txt_edit.getText().toString());
                            this.finish();
                        }else {
                            Toast.makeText(this,"标题为空！",Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
