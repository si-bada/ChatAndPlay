package com.example.bada.chatandplay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {
    Toolbar mToolbar;
    RecyclerView mUserlist;
    private DatabaseReference mUsersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
       // mToolbar = (Toolbar) findViewById(R.id.main_tbar);
        //setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUserlist = (RecyclerView) findViewById(R.id.users_list);
        mUserlist.setHasFixedSize(true);
        mUserlist.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UserViewHolder.class,
                mUsersDatabase

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setImage(model.getImage(),getApplicationContext());

                final String user_id =getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profile_intent = new Intent(UserActivity.this,ProfileActivity.class);
                        profile_intent.putExtra("userID",user_id);
                        startActivity(profile_intent);


                    }
                });
            }
        };
        mUserlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {

            TextView UsernameView = (TextView) mView.findViewById(R.id.user_single_name);
            UsernameView.setText(name);
        }

        public void setStatus(String status) {
            TextView UserStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            UserStatusView.setText(status);
        }

        public void setImage(String image, Context applicationContext) {
            CircleImageView user_imageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(applicationContext).load(image).placeholder(R.drawable.bada).into(user_imageView);

        }
    }
}
