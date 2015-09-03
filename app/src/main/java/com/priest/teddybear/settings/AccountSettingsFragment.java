package com.priest.teddybear.settings;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.priest.teddybear.R;
import com.priest.teddybear.parse.ParseConstants;

/**
 * Created by priest on 8/11/15.
 */
public class AccountSettingsFragment extends Fragment {

    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;

    private ParseUser parseUser;

    private ImageView avatarImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.account_settings_fragment_layout, container, false);

        parseUser = ParseUser.getCurrentUser();

        avatarImageView = (ImageView) rootView.findViewById(R.id.acc_settings_avatar_iv);

        return rootView;
    }

    //public voi

    public void setProfilePhoto(){
        ParseFile profilePhoto = (ParseFile) parseUser.get(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE);
        if (profilePhoto != null) {
            profilePhoto.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null && data != null) {
                        if (getAvatarImageView() != null) {
                            Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            getAvatarImageView().setImageBitmap(bMap);
                        }
                    } else {
                        System.out.println(e.getMessage());
                    }
                }
            });
        } else {
            getAvatarImageView().setImageResource(R.drawable.generic_avatar);
        }
    }

    public ImageView getAvatarImageView(){
        return avatarImageView;
    }
}
