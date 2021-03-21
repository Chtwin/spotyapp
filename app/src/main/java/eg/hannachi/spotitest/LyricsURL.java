package eg.hannachi.spotitest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LyricsURL extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity app;
    ArrayList<String> list = new ArrayList<String>();

    public LyricsURL(AppCompatActivity a){
        this.app = a;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        URL url = null;
        JSONObject j = null;
        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("JJJ", url.toString());
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                j = readStream(in);
                String url_lyrics = j.getJSONObject("response").getJSONArray("hits").getJSONObject(0).getJSONObject("result").getString("url");
                AsyncTask<String, Void, String> task = new Lyrics(this.app);
                task.execute(url_lyrics, null, null);
                //Log.i("JJJ3", j.getString("lyrics"));
                //TextView parolefield = app.findViewById(R.id.paroles);
                //parolefield.setText(j.getString("lyrics"));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject readStream(InputStream is) throws IOException {
        try{
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            //Log.i("JJJ2", bo.toString());
            return new JSONObject(bo.toString());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

}
