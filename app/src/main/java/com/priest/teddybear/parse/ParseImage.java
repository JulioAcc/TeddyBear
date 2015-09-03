package com.priest.teddybear.parse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.priest.teddybear.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by priest on 4/10/15.
 */
public class ParseImage {

    public static void saveImage(Bitmap bitmap, ParseUser user) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile file = new ParseFile("profilepicture", data);
        file.saveInBackground();

        user.put(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE, file);
        user.saveInBackground();

    }

    public static ParseFile createParseFileFromBitmap(Bitmap bitmap, String filename) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile file = new ParseFile("photo_"+filename, data);
        file.saveInBackground();

        return file;
    }

    public static void saveImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile file = new ParseFile("profilepicture", data);
        file.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE, file);
        user.saveInBackground();

    }


    public static Bitmap retrieveImage(final ImageButton imageButton, ParseUser user) {

        ParseFile profilePhoto = (ParseFile) user.get(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE);
        profilePhoto.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageButton.setImageBitmap(bMap);
                } else {
                    // something went wrong
                }
            }
        });

        return null;
    }

    public static void retrieveImage(final ImageView imageView, ParseUser user, GetDataCallback callback) {
        ParseFile profilePhoto = (ParseFile) user.get(ParseConstants.USER_CLASS_ATTRIBUTE_PROFILE_PICTURE);
        profilePhoto.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null && data != null) {
                    if(imageView != null) {
                        Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageView.setImageBitmap(bMap);
                    }
                } else {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    public static Bitmap retrieveImage(final ImageView imageView, ParseFile file) {
        if(file != null) {
            file.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageView.setImageBitmap(bMap);
                    } else {
                        // something went wrong
                    }
                }
            });
        }else{
            imageView.setImageResource(R.drawable.generic_avatar);
        }

        return null;
    }

}
