package com.example.bada.chatandplay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText pseudo,email,pass,confirm_pass;
    private Button register,passShow;
    private FirebaseAuth mAuth;
    String s_pseudo,s_email,s_pass,s_confirm;
    private ProgressDialog mProgressD;
    private DatabaseReference mDatabase;
    private boolean hidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hidden = true;
        pseudo = (EditText) findViewById(R.id.reg_pseudo);
        email = (EditText) findViewById(R.id.reg_email);
        confirm_pass = (EditText) findViewById(R.id.reg_confirm);
        passShow = (Button) findViewById(R.id.reg_btn_reg);
        getSupportActionBar().setTitle("Welcome to Chat And Play");
        pass = (EditText) findViewById(R.id.reg_pass);
        register = (Button) findViewById(R.id.reg_show);
        mAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(this);
        passShow.setOnClickListener(this);
        mProgressD = new ProgressDialog(this);
    //    mToolbar = (Toolbar) findViewById(R.id.main_tbar);
  //      setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Create Account");

    }



    @Override
    public void onClick(View v) {
        if(v == register){

                if(hidden == true){
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    hidden = false;
                }else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    hidden = true;
                }

            }
            if(v == passShow){
                s_pseudo = pseudo.getText().toString();
                s_email = email.getText().toString();
                s_pass = pass.getText().toString();
                s_confirm = confirm_pass.getText().toString();
                if(!TextUtils.isEmpty(s_pseudo) && !TextUtils.isEmpty(s_email) && !TextUtils.isEmpty(s_pass) && !TextUtils.isEmpty(s_pass)  ){
                   if(pass.getText().toString().length() < 6 || !android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches() || !s_pass.equals(s_confirm)){
                        if(pass.getText().toString().length() < 6){
                            pass.setError("Password must contain at least 6 caracters");
                        }
                        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches()){
                            email.setError("please enter a valid email !");
                        }
                        if(!s_pass.equals(s_confirm)){
                            pass.setError("Passwords does not match the confirm password");
                        }


                    }  else {
                       mProgressD.setTitle("Registring user");
                       mProgressD.setMessage("Please wait while we create your account !");
                       mProgressD.setCanceledOnTouchOutside(false);
                       mProgressD.show();
                       register_user(s_pseudo,s_email,s_pass);
                   }


                }
            else{
                    if(TextUtils.isEmpty(s_pseudo)){
                        pseudo.setError("pseudo can't be blank !");

                    }
                    if(TextUtils.isEmpty(s_email)){
                        email.setError("email can't be blank !");

                    }
                    if(TextUtils.isEmpty(s_confirm)){
                        confirm_pass.setError("confirm your password please !");

                    } if(TextUtils.isEmpty(s_pass)){
                        pass.setError("Password can't be blank ! ");

                    }

                }
        }

    }

    private void register_user(final String ss_pseudo, String ss_email, String ss_pass) {
        Log.d("okok",ss_pass+ss_email );

        mAuth.createUserWithEmailAndPassword(ss_email,ss_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("okok", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            Toast.makeText(RegisterActivity.this,"mriguel",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            if(current_user != null){
                                String uid = current_user.getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                mDatabase = FirebaseDatabase.getInstance().
                                        getReference().child("Users").child(uid);
                                HashMap<String,String> userMap = new HashMap<String, String>();
                                userMap.put("device_token",deviceToken);
                                userMap.put("name",ss_pseudo);
                                userMap.put("status","Hi there i'm available for Chat And Play");
                                userMap.put("image","default");
                                userMap.put("thumb_image","default");
                                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mProgressD.dismiss();
                                            Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });

                            }
                        }



                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.e("fail beacause ", "onComplete: Failed=" + task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this,"fail",
                                    Toast.LENGTH_SHORT).show();
                            mProgressD.hide();
                        }

                        // ...
                    }
                });
    }
}
