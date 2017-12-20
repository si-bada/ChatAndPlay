package com.example.bada.chatandplay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button change_image,change_status;
    private CircleImageView mImage;
    private TextView mPseudo,mStatus;
    private DatabaseReference mDataBase,mStatusDataBase;
    private FirebaseUser mCurrentUser;
    private static final int Gallery_pick = 1;
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        change_image = (Button) findViewById(R.id.btn_change_image);
        change_status = (Button) findViewById(R.id.btn_change_status);
        change_status.setOnClickListener(this);
        change_image.setOnClickListener(this);
        getSupportActionBar().setTitle("Setting");
        mImage = (CircleImageView) findViewById(R.id.setting_image);
        mPseudo = (TextView) findViewById(R.id.tv_name);
        mStatus = (TextView) findViewById(R.id.tv_lipsum);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUID = mCurrentUser.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        mDataBase.keepSynced(true );
        mStatusDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(SettingsActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();
                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                mPseudo.setText(name);
                mStatus.setText(status);
                //Picasso.with(SettingsActivity.this).load(image).into(mImage);
                Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(mImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(SettingsActivity.this).load(image).into(mImage);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if ( v == change_status){
            AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
            alertDialog.setTitle("Your Status");
            final EditText Status = new EditText(SettingsActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            Status.setText(mStatus.getText().toString());
            Status.setLayoutParams(lp);
            alertDialog.setView(Status);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save Changes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String newStatus = Status.getText().toString();
                            mStatusDataBase.child("status").setValue(newStatus);
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        if(v == change_image){
//            Intent Galerie_intent = new Intent();
//            Galerie_intent.setType("image/*");
//            Galerie_intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(Galerie_intent,"Select Image From Gallery"),Gallery_pick);
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
            mProgressDialog = new ProgressDialog(SettingsActivity.this);
            mProgressDialog.setTitle("Uploading image");
            mProgressDialog.setMessage("Please Wait while uploading");
            mProgressDialog.show();
                Uri resultUri = result.getUri();
//                CropImage.activity(resultUri)
//                        .setAspectRatio(16,9)
//                        .setCropShape(CropImageView.CropShape.RECTANGLE)
//                        .start(this);
                String current_user_id = mCurrentUser.getUid();
                StorageReference path = mImageStorage.child("profile_images").child(current_user_id+".jpeg");
                path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            String download_url = task.getResult().getDownloadUrl().toString();
                            mDataBase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SettingsActivity.this,"uploaded",Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(SettingsActivity.this,"error",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == Gallery_pick && resultCode == RESULT_OK){
//            String current_user_id = mCurrentUser.getUid();
//            Uri imageUri = data.getData();
//            StorageReference path = mImageStorage.child("profile_images").child(current_user_id+".jpeg");
//            Toast.makeText(SettingsActivity.this,"okokok",Toast.LENGTH_LONG).show();
//            mProgressDialog = new ProgressDialog(SettingsActivity.this);
//            mProgressDialog.setTitle("Uploading image");
//            mProgressDialog.setMessage("Please Wait while uploading");
//            mProgressDialog.show();
//            path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if(task.isSuccessful()){
//
//                        String download_url = task.getResult().getDownloadUrl().toString();
//                        mDataBase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    Toast.makeText(SettingsActivity.this,"uploaded",Toast.LENGTH_LONG).show();
//                                    mProgressDialog.dismiss();
//
//                                }
//                            }
//                        });
//
//                    }
//                    else
//                    {
//                        Toast.makeText(SettingsActivity.this,"error",Toast.LENGTH_LONG).show();
//
//                    }
//                }
//            });
//
//
//
//        }
//    }

}
