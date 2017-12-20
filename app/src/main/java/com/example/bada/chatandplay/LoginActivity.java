package com.example.bada.chatandplay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.example.bada.chatandplay.R.string.pseudo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email,pass;
    private Button log;
    private ProgressDialog mProgressD;
    private String s_email,s_pass;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.log_email);
        pass = (EditText) findViewById(R.id.login_pass);
        log = (Button) findViewById(R.id.login_btn);
        log.setOnClickListener(this);
        getSupportActionBar().setTitle("");
        mAuth = FirebaseAuth.getInstance();
        mProgressD = new ProgressDialog(this);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    public void onClick(View v) {
        s_email = email.getText().toString();
        s_pass = pass.getText().toString();
        if(!TextUtils.isEmpty(s_email) && !TextUtils.isEmpty(s_pass) ){
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches()){
                email.setError("please enter a valid email !");
            }else{
                mProgressD.setTitle("Registring user");
                mProgressD.setMessage("Please wait while logging to your account !");
                mProgressD.setCanceledOnTouchOutside(false);
                mProgressD.show();

                signIN_user(s_email,s_pass);
            }

    }
        else{

            if(TextUtils.isEmpty(s_email)){
                email.setError("email can't be blank !");

            }
             if(TextUtils.isEmpty(s_pass)){
                pass.setError("Password can't be blank ! ");

            }

        }
}

    private void signIN_user(String ss_email, String ss_pass) {
        ss_email = s_email;
        ss_pass = s_pass;
        mAuth.signInWithEmailAndPassword(ss_email, ss_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("okok", "signInWithEmail:onComplete:" + task.isSuccessful());
                            mProgressD.dismiss();
                            String current_user_id = mAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    }

                                }
                            });
                        }else{
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle("wrong credentials, please check email or password !");
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Lemme try Again ! ",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }



                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("nnoono", "signInWithEmail:failed", task.getException());
                            Log.e("fail beacause ", "onComplete: Failed=" + task.getException().getMessage());

                            Toast.makeText(LoginActivity.this, "fail",
                                    Toast.LENGTH_SHORT).show();
                            mProgressD.hide();

                        }

                        // ...
                    }
                });
    }
    }
