package com.cz3002.shopfunding.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;

// Deprecated. Replaced by Picasso API
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    public DownloadImageTask() {}

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
//        ImageView imageView = findViewById(R.id.image_product);
//        imageView.setImageBitmap(result);
    }
}
