package com.priest.teddybear.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.priest.teddybear.R;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by priest on 7/24/15.
 */

public class MainLoginActivity extends Activity {


    public static final int SIGNUP_LAYOUT = 0;
    public static final int LOGIN_LAYOUT = 1;
    public static final int LOGOUT_LAYOUT = 2;
    public static final int PASSWORD_RETRIEVAL_LAYOUT = 3;

    private LinearLayout signupLayout, loginLayout, logoutLayout, passwordRetrievalLayout;
    private EditText emailEditText, pwdEditText, repwdEditText, nameEditText;

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

        parseUser = null;
        try {
            parseUser = ParseUser.getCurrentUser();
        }catch(NullPointerException e){
            e.printStackTrace();
            Log.d("Error","Null pointer exception for currentUser");
        }

        if(parseUser != null){
            setLayoutVisibility(LOGOUT_LAYOUT);
        }else{
            setLayoutVisibility(LOGIN_LAYOUT);
        }
    }

    public void submitSignupForm(View v){
        String email = emailEditText.getText().toString();
        String password = pwdEditText.getText().toString();
        String repassword = repwdEditText.getText().toString();
        String name = nameEditText.getText().toString();

        if(email.equals("") || email==null || password.equals("") || password==null ||
                repassword.equals("") || repassword==null || name.equals("") || name==null) {
            Toast.makeText(this, R.string.loginactivity_main_signup_fail_empty_et, Toast.LENGTH_LONG);
        }else{
            if(!FormValidater.validateEmail(email)){
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_valid_email, Toast.LENGTH_LONG);
            }else if(!FormValidater.validatePassword(password) && !FormValidater.validatePassword(repassword)){
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_password_rules, Toast.LENGTH_LONG);
            }else if(!(password.equals(repassword))){
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_passwords_match, Toast.LENGTH_LONG);
            }else if(!FormValidater.validateName(name)){
                Toast.makeText(this, R.string.loginactivity_main_signup_fail_valid_name, Toast.LENGTH_LONG);
            }else{
                signUpUser(email, password, name);
            }
        }
    }

    public void signUpUser(String email, String password, String name){
        ParseUser user = new ParseUser();
        user.setUsername(name);
        String hashPassword = PasswordValidater.encryptPassword(password, PasswordValidater.STANDARD_HASH_CYCLE);
        user.setPassword(hashPassword);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), R.string.loginactivity_main_signup_successful, Toast.LENGTH_LONG);
                    transitionToLoginLayoutAfterSignup();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.loginactivity_main_signup_failed_parse, Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void transitionToLoginLayoutAfterSignup(){
        setLayoutVisibility(LOGIN_LAYOUT);
        //
    }

    public void transitionToSignupLayout(View v){
        setLayoutVisibility(SIGNUP_LAYOUT);
    }

    public void transitionToPasswordRetrieval(View v){
        setLayoutVisibility(PASSWORD_RETRIEVAL_LAYOUT);
    }

    public void transitionToLobby(View v){
        //Intent menuIntent = new Intent(this, MenuActivity.class);
        //startActivity(menuIntent);
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
                logoutLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
