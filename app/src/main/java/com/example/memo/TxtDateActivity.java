package com.example.memo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class TxtDateActivity extends AppCompatActivity {
    Button txtsave;
    EditText txt_edit, txt_area;
    final String TITLE_TXT = "/title.txt";
    String maintxt = "/main.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClearContent.getInstance().addActivity(this);
        setContentView(R.layout.main_body);
        txtsave = findViewById(R.id.txtsave);
        txt_area = findViewById(R.id.txt_area);
        txt_edit = findViewById(R.id.txt_edit);
        txtsave.setOnClickListener(v -> {
            writeCof(txt_edit.getText().toString());
        });

        if (getIntent().getStringExtra("txttitle")!=null){
            txt_edit.setText(getIntent().getStringExtra("txttitle"));
            txt_area.setText(getIntent().getStringExtra("txmain"));
        }
    }

    String mod;
    public void writeCof(String fs) {
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
}
