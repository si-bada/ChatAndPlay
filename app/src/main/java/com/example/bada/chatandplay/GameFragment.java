package com.example.bada.chatandplay;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    private View mMainView;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    private DatabaseReference mGameDatabase;
    private DatabaseReference mUsersDatabase;
    private ImageView myChoice,vsChoice;
    private Button rock,paper,scissors;

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_chat, container, false);
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        myChoice = (ImageView) mMainView.findViewById(R.id.Game_mychoice);
        vsChoice = (ImageView) mMainView.findViewById(R.id.Game_vschoice);
        rock = (Button) mMainView.findViewById(R.id.game_rock);
        paper = (Button) mMainView.findViewById(R.id.game_paper);
        scissors = (Button) mMainView.findViewById(R.id.game_scissors);
        rock.setOnClickListener(this);
        mGameDatabase = FirebaseDatabase.getInstance().getReference().child("Game").child(mCurrent_user_id);
        mGameDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        return mMainView;
    }

    @Override
    public void onClick(View v) {

    }
}
