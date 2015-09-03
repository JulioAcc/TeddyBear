package com.priest.teddybear.navdrawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.priest.teddybear.R;
import com.priest.teddybear.friends.FriendsListFragment;
import com.priest.teddybear.game.factory.CreateGameFragment;
import com.priest.teddybear.lobby.LobbyFragment;
import com.priest.teddybear.login.MainLoginActivity;
import com.priest.teddybear.network.client.ClientActivity;
import com.priest.teddybear.network.server.ServerActivity;
import com.priest.teddybear.parse.ParseConstants;
import com.priest.teddybear.settings.AccountSettingsFragment;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by priest on 8/11/15.
 */
public class NavDrawerFragmentActivity extends FragmentActivity {

    // ---------- Retrieve image from gallery
    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;

    // ------------ Parse user pointer
    private ParseUser parseUser;

    // -----------Navigation drawer
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private Bitmap modifiedAvatar;

    // ------------UI POINTERS

    private LinearLayout accsettLayout, accsettEditLayout;

    private Button accsettChangeAvatar;
    //private TextView

    // ----------- UI CONSTANTS

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navdrawer_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        parseUser = ParseUser.getCurrentUser();

        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.lobby_drawer_list_item, mDrawerTitles));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        accsettChangeAvatar = (Button) findViewById(R.id.accsett_change_avatar_btn);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.tool1);

        setSupportActionBar(toolbar);
        toolbar.setTitle("ToolBar Demo");
        toolbar.setLogo(R.drawable.ic_launcher);*/

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        selectItem(0);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new LobbyFragment();
                break;
            case 1:
                fragment = new CreateGameFragment();
                break;
            case 2:
                fragment = new FriendsListFragment();
                break;
            case 3:
                fragment = new LobbyFragment();
                break;
            case 4:
                fragment = new AccountSettingsFragment();
                break;
            default:
                fragment = new LobbyFragment();
        }
        //Bundle args = new Bundle();
        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        //fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_layout, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void logoutUser(View v) {
        ParseUser.logOut();
        transitionToMainLoginActivity();
    }

    public void transitionToMainLoginActivity() {
        Intent intent = new Intent(this, MainLoginActivity.class);
        startActivity(intent);
    }

    public void linkAccountToFB(View v){
        if(parseUser != null)
            if (!ParseFacebookUtils.isLinked(parseUser)) {
                Collection<String> permissions = Arrays.asList("email", "public_profile", "user_friends");
                ParseFacebookUtils.linkWithReadPermissionsInBackground(parseUser, this, permissions, new SaveCallback() {
                    @Override
                    public void done(ParseException ex) {
                        if (ParseFacebookUtils.isLinked(parseUser)) {
                            Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                        }
                    }
                });
            }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public void saveUserInfo(View v){



        if(modifiedAvatar != null){
            parseUser.put(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE, modifiedAvatar);
            parseUser.saveInBackground();
        }
    }

    public void loadImageFromGallery(View v) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void createGameSession(View v){
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);
    }


    public void joinGameSession(View v){
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.acc_settings_avatar_iv);

                // Set the Image in ImageView after decoding the String
                modifiedAvatar = BitmapFactory.decodeFile(imgDecodableString);

                imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            } else {
                //Toast.makeText(this, "You haven't picked Image",
                //Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
            // .show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

}