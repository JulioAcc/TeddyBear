package com.priest.teddybear.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.priest.teddybear.navdrawer.NavDrawerFragmentActivity;
import com.priest.teddybear.R;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.priest.teddybear.parse.ParseConstants;
import com.priest.teddybear.parse.ParseFriendList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by priest on 7/24/15.
 */

public class MainLoginActivity extends Activity {

    public static final String FACEBOOK_ID = "id";
    public static final String FACEBOOK_NAME = "name";
    public static final String FACEBOOK_EMAIL = "email";

    public static final int SIGNUP_LAYOUT = 0;
    public static final int LOGIN_LAYOUT = 1;
    public static final int LOGOUT_LAYOUT = 2;
    public static final int PASSWORD_RETRIEVAL_LAYOUT = 3;

    private LinearLayout signupLayout, loginLayout, logoutLayout, passwordRetrievalLayout;
    private EditText emailEditText, pwdEditText, repwdEditText, nameEditText;
    private EditText loginEmailEditText, loginPwdEditText;
    private ImageView profilePhotoImageView;

    private ParseUser parseUser;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        signupLayout = (LinearLayout) findViewById(R.id.loginactivity_main_signup_layout);
        loginLayout = (LinearLayout) findViewById(R.id.loginactivity_main_login_layout);
        logoutLayout = (LinearLayout) findViewById(R.id.loginactivity_main_logout_layout);
        passwordRetrievalLayout = (LinearLayout) findViewById(R.id.loginactivity_main_password_retrieval_layout);

        emailEditText = (EditText) findViewById(R.id.loginactivity_main_signup_email_et);
        pwdEditText = (EditText) findViewById(R.id.loginactivity_main_signup_password_et);
        repwdEditText = (EditText) findViewById(R.id.loginactivity_main_signup_repassword_et);
        nameEditText = (EditText) findViewById(R.id.loginactivity_main_signup_name_et);

        loginEmailEditText = (EditText) findViewById(R.id.loginactivity_main_login_email_et);
        loginPwdEditText = (EditText) findViewById(R.id.loginactivity_main_login_password_et);

        profilePhotoImageView = (ImageView) findViewById(R.id.profilePhotoImageView);

        if(ParseUser.getCurrentUser().getObjectId() == null){
            setLayoutVisibility(LOGIN_LAYOUT);
        }else{
            transitionToLobby();
        }
    }

    public void facebookLogin(View v){
        Collection<String> permissions = Arrays.asList("email", "public_profile", "user_friends");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                parseUser = user;
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    retrieveFacebookInformation();
                    searchFriendsList();
                    transitionToLobby();
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    retrieveFacebookInformation();
                    searchFriendsList();
                    transitionToLobby();
                }
            }
        });
    }

    public void retrieveFacebookInformation() {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String email = object.getString(FACEBOOK_EMAIL);
                    String name = object.getString(FACEBOOK_NAME);
                    String facebookId = object.getString(FACEBOOK_ID);
                    LoadImageFromUrl loadImage = new LoadImageFromUrl(profilePhotoImageView, facebookId);
                    loadImage.execute();
                    signUpFacebookUser(email, name, facebookId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void searchFriendsList(){

        GraphRequestBatch batch = new GraphRequestBatch(GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                                                            new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray objects, GraphResponse response) {
                String[] facebookFriends = new String[objects.length()];
                for(int i = 0; i < objects.length(); i++){
                    JSONObject friend;
                    try {
                        friend = objects.getJSONObject(i);
                        facebookFriends[i] = friend.getString("id");
                        System.out.println("Friend #"+i+" -> "+ friend.getString("id") +" : "+ friend.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ParseFriendList.searchFacebookFriends(facebookFriends);
            }
        }));
        batch.executeAsync();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture");
    }

    public void submitSignupForm(View v) {
        String email = emailEditText.getEditableText().toString();
        String password = pwdEditText.getEditableText().toString();
        String repassword = repwdEditText.getEditableText().toString();
        String name = nameEditText.getEditableText().toString();

        if (email.equals("") || email == null || password.equals("") || password == null ||
                repassword.equals("") || repassword == null || name.equals("") || name == null) {
            Toast.makeText(this, R.string.loginactivity_main_signup_fail_empty_et, Toast.LENGTH_LONG).show();
        } else {
            if (!FormValidater.validateEmail(email)) {
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_valid_email, Toast.LENGTH_LONG).show();
            } else if (!FormValidater.validatePassword(password) && !FormValidater.validatePassword(repassword)) {
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_password_rules, Toast.LENGTH_LONG).show();
            } else if (!(password.equals(repassword))) {
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_passwords_match, Toast.LENGTH_LONG).show();
            } else if (!FormValidater.validateName(name)) {
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_valid_name, Toast.LENGTH_LONG).show();
            } else {
                signUpParseUser(email, password, name);
            }
        }
    }

    public void signUpParseUser(String email, String password, String name){
        ParseUser user = new ParseUser();
        user.put(ParseConstants.USER_CLASS_ATTRIBUTE_NAME, name);
        String hashPassword = PasswordValidater.encryptPassword(password, PasswordValidater.STANDARD_HASH_CYCLE);
        user.setPassword(hashPassword);
        user.setEmail(email);
        user.setUsername(email);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), R.string.loginactivity_main_signup_successful, Toast.LENGTH_LONG).show();
                    transitionToLoginLayoutAfterSignup();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.loginactivity_main_signup_failed_parse, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signUpFacebookUser(String email, String name, String facebookId){
        try{
            System.out.println(email + name + facebookId);
            parseUser.put(ParseConstants.USER_CLASS_ATTRIBUTE_EMAIL, email);
            parseUser.put(ParseConstants.USER_CLASS_ATTRIBUTE_NAME, name);
            parseUser.put(ParseConstants.USER_CLASS_ATTRIBUTE_FACEBOOK_ID, facebookId);
            parseUser.saveInBackground();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public void submitLoginForm(View v){
        String email = loginEmailEditText.getEditableText().toString();
        String password = loginPwdEditText.getEditableText().toString();

        if (email.equals("") || email == null || password.equals("") || password == null) {
            Toast.makeText(this, R.string.loginactivity_main_signup_fail_empty_et, Toast.LENGTH_LONG).show();
        } else {
            if (!FormValidater.validateEmail(email)) {
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_valid_email, Toast.LENGTH_LONG).show();
            } else {
                loginUser(email, password);
            }
        }
    }

    public void loginUser(String email, String password){
        String hashPassword = PasswordValidater.encryptPassword(password, PasswordValidater.STANDARD_HASH_CYCLE);
        ParseUser.logInInBackground(email, hashPassword, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    transitionToLobby();
                } else {
                    Log.e("LOGIN ERROR", e.getMessage());
                }
            }
        });
    }

    public void logoutUser(View v){
        ParseUser.logOut();
        transitionToLoginLayoutAfterLogout();
    }

    public void transitionToLoginLayoutAfterSignup(){
        setLayoutVisibility(LOGIN_LAYOUT);
    }

    public void transitionToLoginLayoutAfterLogout(){
        setLayoutVisibility(LOGIN_LAYOUT);
    }

    public void transitionToSignupLayout(View v){
        setLayoutVisibility(SIGNUP_LAYOUT);
    }

    public void transitionToPasswordRetrieval(View v){
        setLayoutVisibility(PASSWORD_RETRIEVAL_LAYOUT);
    }

    public void transitionToLobby(){
        Intent intent = new Intent(this, NavDrawerFragmentActivity.class);
        startActivity(intent);
    }

    public void setLayoutVisibility(int visibleLayout){
        signupLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.GONE);
        logoutLayout.setVisibility(View.GONE);
        passwordRetrievalLayout.setVisibility(View.GONE);

        switch(visibleLayout){
            case SIGNUP_LAYOUT:
                signupLayout.setVisibility(View.VISIBLE);
                break;
            case LOGIN_LAYOUT:
                loginLayout.setVisibility(View.VISIBLE);
                break;
            case LOGOUT_LAYOUT:
                logoutLayout.setVisibility(View.VISIBLE);
                break;
            case PASSWORD_RETRIEVAL_LAYOUT:
                passwordRetrievalLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
