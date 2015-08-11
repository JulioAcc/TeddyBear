package com.priest.teddybear.parse;

import android.util.Log;

import com.facebook.internal.CollectionMapper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by priest on 8/9/15.
 */
public class ParseFriendList {

    public static void searchFacebookFriends(final String[] friendsFacebookId){

        Collection<String> userCollection = Arrays.asList(friendsFacebookId);
        ParseQuery<ParseUser> parseFriendsQuery = ParseUser.getQuery();
        parseFriendsQuery.whereContainedIn(ParseConstants.USER_CLASS_ATTRIBUTE_FACEBOOK_ID, userCollection);
        parseFriendsQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> friendsList, ParseException e) {
                if (e == null) {
                    ParseUser parseUser = ParseUser.getCurrentUser();
                    ArrayList<String> friendsObjectIdList = new ArrayList<String>();
                    for (int i = 0; i < friendsList.size(); i++)
                        friendsObjectIdList.add(friendsList.get(i).getObjectId());
                    parseUser.put(ParseConstants.USER_CLASS_ATTRIBUTE_FRIENDS_LIST, friendsObjectIdList);
                    parseUser.saveInBackground();
                } else {
                    Log.d("Size", "0");
                }
            }
        });
    }

}
