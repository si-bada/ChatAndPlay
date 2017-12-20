package com.example.bada.chatandplay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Bada on 04/12/2017.
 */

class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: RequestFragment requestFragment = new RequestFragment();
                return requestFragment;
            case 1: ChatFragment ChatFragment = new ChatFragment();
                return ChatFragment;
            case 2: FriendsFragment FriendsFragment = new FriendsFragment();
                return FriendsFragment;
            case 3: GameFragment GameFrag = new GameFragment();
                return  GameFrag;

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0 : return "REQUESTS";
            case 1 : return  "CHAT";
            case 2 : return  "FRIENDS";
            default: return  null;
        }
    }
}
