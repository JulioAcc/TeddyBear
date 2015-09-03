package com.priest.teddybear.friends;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.priest.teddybear.R;
import com.priest.teddybear.helper.UIViewCreator;
import com.priest.teddybear.parse.ParseConstants;
import com.priest.teddybear.parse.ParseImage;

import java.util.List;

/**
 * Created by priest on 9/2/15.
 */
public class FriendLayout extends LinearLayout {

    private Context context;

    private String userId;
    private ParseUser user;
    private boolean isFriend, isInstantiatedByAndroid, hasBeenInstantiated;

    private LinearLayout infoLayout, statusLayout;
    private ImageView avatarImageView;
    private TextView nameTextView, onlineStatusTextView;
    private Button addFriendButton;

    public FriendLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.isInstantiatedByAndroid = true;
        this.hasBeenInstantiated = false;

    }

    public void instantiateByAndroid(String userId, boolean isFriend){
        if(isInstantiatedByAndroid && !hasBeenInstantiated) {
            this.userId = userId;
            this.isFriend = isFriend;

            instantiateUI();
            retrieveUser(userId);

            this.hasBeenInstantiated = true;
        }
    }


    public FriendLayout(Context context, String userId, boolean isFriend) {
        super(context);

        this.setLayoutParams(UIViewCreator.createLinearLayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
        this.setOrientation(LinearLayout.HORIZONTAL);

        this.context = context;
        this.userId = userId;
        this.isFriend = isFriend;
        this.isInstantiatedByAndroid = false;

        instantiateUI();
        retrieveUser(userId);
    }

    public FriendLayout(Context context, ParseUser user, boolean isFriend) {
        super(context);

        this.setLayoutParams(UIViewCreator.createLinearLayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
        this.setOrientation(LinearLayout.HORIZONTAL);

        this.context = context;
        this.user = user;
        this.isFriend = isFriend;
        this.isInstantiatedByAndroid = false;

        instantiateUI();
        retrieveUser(user);
    }

    public void instantiateUI(){

        avatarImageView = UIViewCreator.createLinearImageView(context, 0, 0, 0, 0,
                (int) getResources().getDimension(R.dimen.friend_layout_avatar_height),
                (int) getResources().getDimension(R.dimen.friend_layout_avatar_height), Gravity.CENTER_VERTICAL);

        infoLayout = UIViewCreator.createLinearLayout(context, LinearLayout.VERTICAL, 0, 0, 0, 0,
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        infoLayout.setGravity(Gravity.CENTER);

        nameTextView = UIViewCreator.createLinearTextView(context, "", 0, 0, 0, 0,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        nameTextView.setTextSize(context.getResources().getDimension(R.dimen.friend_layout_textsize));

        statusLayout = UIViewCreator.createLinearLayout(context, LinearLayout.VERTICAL, 0, 0, 0, 0,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        statusLayout.setGravity(Gravity.CENTER);
        statusLayout.setOrientation(LinearLayout.HORIZONTAL);

        onlineStatusTextView = UIViewCreator.createLinearTextView(context, "", 0, 0, 0, 0,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        onlineStatusTextView.setTextSize(context.getResources().getDimension(R.dimen.friend_layout_textsize));

        addFriendButton = UIViewCreator.createLinearButton(context, R.string.friend_layout_add_button,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addFriendButton.setTextSize(context.getResources().getDimension(R.dimen.friend_layout_textsize));
        addFriendButton.setVisibility(View.GONE);

        statusLayout.addView(onlineStatusTextView);
        statusLayout.addView(addFriendButton);

        infoLayout.addView(nameTextView);
        infoLayout.addView(statusLayout);

        this.addView(avatarImageView);
        this.addView(infoLayout);
    }

    public void retrieveUser(String userId){
        ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
        parseUserQuery.whereEqualTo(ParseConstants.CLASS_ATTRIBUTE_OBJECT_ID, userId);
        parseUserQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {
                    ParseUser user = userList.get(0);
                    ParseImage.retrieveImage(avatarImageView, user.getParseFile(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE));
                    nameTextView.setText(user.getString(ParseConstants.USER_CLASS_ATTRIBUTE_NAME));
                    if(user.getBoolean(ParseConstants.USER_CLASS_ATTRIBUTE_STATUS_ONLINE))
                        onlineStatusTextView.setText(R.string.friend_layout_online_status);
                    else
                        onlineStatusTextView.setText(R.string.friend_layout_offline_status);

                    if(!isFriend){
                        addFriendButton.setVisibility(View.VISIBLE);
                    }else{
                        addFriendButton.setVisibility(View.GONE);
                    }
                } else {
                    e.printStackTrace();
                    Log.d("Size", "0");
                }
            }
        });
    }

    public void retrieveUser(ParseUser user){
        ParseImage.retrieveImage(avatarImageView, user.getParseFile(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE));
        nameTextView.setText(user.getString(ParseConstants.USER_CLASS_ATTRIBUTE_NAME));
        if(user.getBoolean(ParseConstants.USER_CLASS_ATTRIBUTE_STATUS_ONLINE))
            onlineStatusTextView.setText(R.string.friend_layout_online_status);
        else
            onlineStatusTextView.setText(R.string.friend_layout_offline_status);

        if(!isFriend){
            addFriendButton.setVisibility(View.VISIBLE);
        }else{
            addFriendButton.setVisibility(View.GONE);
        }
    }

}
