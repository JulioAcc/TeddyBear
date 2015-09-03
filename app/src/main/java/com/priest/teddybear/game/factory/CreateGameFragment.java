package com.priest.teddybear.game.factory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.priest.teddybear.R;
import com.priest.teddybear.friends.FriendLayout;
import com.priest.teddybear.parse.ParseConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priest on 9/2/15.
 */
public class CreateGameFragment extends Fragment {

    private ParseUser parseUser;

    private LinearLayout friendListLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.game_creation_fragment_layout, container, false);

        parseUser = ParseUser.getCurrentUser();
        friendListLayout = (LinearLayout) rootView.findViewById(R.id.game_creation_friends_layout);

        instantiateFriendList(parseUser);

        return rootView;
    }

    public void instantiateFriendList(ParseUser parseUser){

        ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
        parseUserQuery.include(ParseConstants.USER_CLASS_ATTRIBUTE_FRIENDS_LIST);
        parseUserQuery.whereEqualTo(ParseConstants.CLASS_ATTRIBUTE_OBJECT_ID, parseUser.getObjectId());
        parseUserQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {
                    List<String> friendList = (List<String>)userList.get(0).get(ParseConstants.USER_CLASS_ATTRIBUTE_FRIENDS_LIST);
                    for(int i = 0; i<friendList.size(); i++){
                        System.out.println(friendList.get(i));
                        friendListLayout.addView(new FriendLayout(getActivity(), friendList.get(i), false));
                    }
                } else {
                    Log.d("Size", "0");
                }
            }
        });

    }


}
