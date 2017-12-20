package com.example.bada.chatandplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private Button RegBtn,Connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        RegBtn = (Button) findViewById(R.id.start_ref_btn);
        RegBtn.setOnClickListener(this);
        Connect = (Button) findViewById(R.id.start_log_btn);
        Connect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == RegBtn){
            Intent reg_intent = new Intent(StartActivity.this,RegisterActivity.class);
            startActivity(reg_intent);
        }
        if(v == Connect){
            Intent login_intent = new Intent(StartActivity.this,LoginActivity.class);
            startActivity(login_intent);
        }

    }
}
