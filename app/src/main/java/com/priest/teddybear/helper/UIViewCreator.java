package com.priest.teddybear.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by priest on 4/11/15.
 */
public class UIViewCreator {

    public static LinearLayout.LayoutParams createLinearLayoutParams(int width, int height, int leftMargin,
                                                                     int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        return params;
    }

    public static LinearLayout createLinearLayout(Context context, int orientation, int leftMargin, int topMargin, int rightMargin,
                                                  int bottomMargin, int height, int width) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(orientation);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(width, height);
        rowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        layout.setLayoutParams(rowParams);

        return layout;
    }

    public static ImageView createLinearImageView(Context context, int resourceId, int leftMargin, int topMargin, int rightMargin,
                                                  int bottomMargin, int height, int width, int gravity) {
        ImageView image = new ImageView(context);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(width, height);

        //imageParams.weight = weight;
        imageParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        imageParams.gravity = gravity;
        image.setLayoutParams(imageParams);
        image.setImageResource(resourceId);

        return image;
    }

    public static ImageView createLinearImageView(Context context, int leftMargin, int topMargin, int rightMargin,
                                                  int bottomMargin, int height, int width, int gravity) {
        ImageView image = new ImageView(context);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(width, height);

        //imageParams.weight = weight;
        imageParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        imageParams.gravity = gravity;
        image.setLayoutParams(imageParams);

        return image;
    }

    public static ImageView createLinearImageView(Context context, int resourceId, int leftMargin, int topMargin, int rightMargin,
                                                  int bottomMargin, int height, int width, int gravity, int weight) {
        ImageView image = new ImageView(context);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(width, height);

        imageParams.weight = weight;
        imageParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        imageParams.gravity = gravity;
        image.setLayoutParams(imageParams);
        image.setImageResource(resourceId);

        return image;
    }

    public static ImageView createLinearImageView(Context context, Bitmap bitmap, int leftMargin, int topMargin, int rightMargin,
                                                  int bottomMargin, int height, int width, int gravity) {
        ImageView image = new ImageView(context);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(width, height);

        //imageParams.weight = weight;
        imageParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        imageParams.gravity = gravity;
        image.setLayoutParams(imageParams);
        image.setImageBitmap(bitmap);

        return image;
    }

    public static TextView createLinearTextView(Context context, String text, int leftMargin, int topMargin, int rightMargin,
                                                int bottomMargin, int height, int width, int gravity) {
        TextView textView = new TextView(context);

        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(width, height);

        //imageParams.weight = weight;
        textViewParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        textViewParams.gravity = gravity;
        textView.setLayoutParams(textViewParams);
        textView.setText(text);

        return textView;
    }

    public static ImageButton createLinearImageButton(Context context, int weight,
                                                      int height, int width) {
        ImageButton image = new ImageButton(context);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(width, height);

        imageParams.weight = weight;
        image.setLayoutParams(imageParams);

        return image;
    }

    public static Button createLinearButton(Context context, int stringResourceId, int width, int height) {
        Button button = new Button(context);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(width,
                height);
        imageParams.gravity = 1;
        imageParams.weight = 1;
        button.setText(stringResourceId);

        return button;
    }


    public static HorizontalScrollView createLinearHorizontalScrollView(Context context, int leftMargin, int topMargin, int rightMargin,
                                                                        int bottomMargin, int height, int width) {

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        horizontalScrollView.setLayoutParams(layoutParams);

        return horizontalScrollView;
    }

    public static RelativeLayout createRelativeLayout(Context context, int leftMargin, int topMargin, int rightMargin,
                                                                        int bottomMargin, int height, int width) {

        RelativeLayout layout = new RelativeLayout(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        layout.setLayoutParams(params);

        return layout;
    }

    public static TextView createRelativeTextView(Context context, String text, int leftMargin, int topMargin, int rightMargin,
                                                int bottomMargin, int height, int width, List<Integer> rules) {
        TextView textView = new TextView(context);

        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(width, height);

        //imageParams.weight = weight;
        for(int rule : rules){
            textViewParams.addRule(rule);
        }

        textViewParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        textView.setLayoutParams(textViewParams);
        textView.setText(text);

        return textView;
    }

    public static ImageView createRelativeImageView(Context context, int resourceId, int leftMargin, int topMargin, int rightMargin,
                                                  int bottomMargin, int height, int width, List<Integer> rules) {
        ImageView image = new ImageView(context);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(width, height);

        for(int rule : rules){
            imageParams.addRule(rule);
        }

        imageParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        image.setLayoutParams(imageParams);
        image.setImageResource(resourceId);

        return image;
    }

}