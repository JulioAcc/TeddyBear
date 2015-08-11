package com.priest.teddybear.lobby;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.priest.teddybear.R;
import com.priest.teddybear.parse.ParseConstants;

/**
 * Created by priest on 8/9/15.
 */
public class Lobby extends FragmentActivity {

    TextView userNameTextView;
    ParseUser parseUser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobbyactivity_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        parseUser = ParseUser.getCurrentUser();

        userNameTextView = (TextView) findViewById(R.id.lobbyactivity_main_name_tv);

        userNameTextView.setText("Welcome, " + parseUser.getString(ParseConstants.USER_CLASS_ATTRIBUTE_NAME));

    }
}
