package com.example.bada.chatandplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    private DatabaseReference mGameDatabase;
    private DatabaseReference mUsersDatabase,mRivalChoice;
    private ImageView myChoice,vsChoice;
    private Button rock,paper,scissors;
    private DatabaseReference mRootRef;
    private FirebaseUser mCurrent_user;
    private String user_id;
    private String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
         user_id = getIntent().getStringExtra("user_id");
        final String user_name = getIntent().getStringExtra("user_name");
        mCurrent_state = "Nothing";

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        myChoice = (ImageView) findViewById(R.id.Game_mychoice);
        vsChoice = (ImageView) findViewById(R.id.Game_vschoice);
        rock = (Button) findViewById(R.id.game_rock);
        paper = (Button) findViewById(R.id.game_paper);
        scissors = (Button) findViewById(R.id.game_scissors);
        rock.setOnClickListener(this);
        paper.setOnClickListener(this);
        scissors.setOnClickListener(this);
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mGameDatabase = FirebaseDatabase.getInstance().getReference().child("Game").child(mCurrent_user_id);
        mGameDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onClick(View v) {
        Map GameMap = new HashMap();

            if(v == rock){

                GameMap.put("Game/" + mCurrent_user.getUid() + "/" + user_id + "/Choice", "Rock");
                GameMap.put("Game/" + user_id  + "/" + mCurrent_user.getUid() + "/Choice", "nothing");
                mRootRef.updateChildren(GameMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mCurrent_state = "Played";
                    }
                });
                myChoice.setImageResource(R.drawable.rock);
                Toast.makeText(GameActivity.this,"Waiting for the other player to choose",
                        Toast.LENGTH_SHORT).show();
                mRivalChoice =FirebaseDatabase.getInstance().getReference().child("Game").child(user_id).child(mCurrent_user.getUid());
                mRivalChoice.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Rock")){
                            vsChoice.setImageResource(R.drawable.rock_inv);
                            Toast.makeText(GameActivity.this,"You Draw",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Paper")){
                            vsChoice.setImageResource(R.drawable.paper_inv);
                            Toast.makeText(GameActivity.this,"You Lose",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Scissors")){
                            vsChoice.setImageResource(R.drawable.scissors_inv);
                            Toast.makeText(GameActivity.this,"You Win",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            if(v == paper){
                GameMap.put("Game/" + mCurrent_user.getUid() + "/" + user_id + "/Choice", "Paper");
                GameMap.put("Game/" + user_id  + "/" + mCurrent_user.getUid() + "/Choice", "nothing");
                mRootRef.updateChildren(GameMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mCurrent_state = "Played";
                    }
                });
                myChoice.setImageResource(R.drawable.paper);
                mRivalChoice =FirebaseDatabase.getInstance().getReference().child("Game").child(user_id).child(mCurrent_user.getUid());
                mRivalChoice.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Rock")){
                            vsChoice.setImageResource(R.drawable.rock_inv);
                            Toast.makeText(GameActivity.this,"You Win",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Paper")){
                            vsChoice.setImageResource(R.drawable.paper_inv);
                            Toast.makeText(GameActivity.this,"You draw",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Scissors")){
                            vsChoice.setImageResource(R.drawable.scissors_inv);
                            Toast.makeText(GameActivity.this,"You Lose",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(v == scissors){
                GameMap.put("Game/" + mCurrent_user.getUid() + "/" + user_id + "/Choice", "Scissors");
                GameMap.put("Game/" + user_id  + "/" + mCurrent_user.getUid() + "/Choice", "nothing");
                mRootRef.updateChildren(GameMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mCurrent_state = "Played";
                    }
                });
                myChoice.setImageResource(R.drawable.scissors);

                mRivalChoice =FirebaseDatabase.getInstance().getReference().child("Game").child(user_id).child(mCurrent_user.getUid());
                mRivalChoice.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Rock")){
                            vsChoice.setImageResource(R.drawable.rock_inv);
                            Toast.makeText(GameActivity.this,"You Lose",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Paper")){
                            vsChoice.setImageResource(R.drawable.paper_inv);
                            Toast.makeText(GameActivity.this,"You Win",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(dataSnapshot.child("Choice").getValue().toString().equals("Scissors")){
                            vsChoice.setImageResource(R.drawable.scissors_inv);
                            Toast.makeText(GameActivity.this,"You Draw",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }





    }
}
