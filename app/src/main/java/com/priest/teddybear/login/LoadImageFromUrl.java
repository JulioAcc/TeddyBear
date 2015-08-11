package com.priest.teddybear.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.priest.teddybear.parse.ParseImage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by priest on 8/8/15.
 */
public class LoadImageFromUrl extends AsyncTask<Void, Void, Bitmap> {

    private ImageView imgView;
    private String userId;
    //private Resources resources;

    public LoadImageFromUrl(ImageView imgView, String userId){
        this.imgView = imgView;
        this.userId = userId;
        //this.resources = resources;
    }

    protected Bitmap doInBackground(Void... p) {

        String urlStr = "http://graph.facebook.com/" + userId + "/picture?type=large";
        Bitmap img = null;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlStr);
        HttpResponse response;
        try {
            response = (HttpResponse)client.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
            InputStream inputStream = bufferedEntity.getContent();
            img = BitmapFactory.decodeStream(inputStream);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    protected void onPostExecute(Bitmap bm) {

        imgView.setImageBitmap(bm);
        ParseImage.saveImage(bm);
        //Drawable drawable = new BitmapDrawable(resources, bm);
        //imgView.setImageDrawable(drawable);
    }

}