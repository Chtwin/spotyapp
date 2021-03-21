package eg.hannachi.spotitest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

@SuppressLint("StaticFieldLeak")
public class LyricsURL extends AsyncTask<String, Void, JSONObject> {

    private final AppCompatActivity app;

    public LyricsURL(AppCompatActivity a){
        this.app = a;
    }

    @SuppressLint("WrongThread")
    @Override
    protected JSONObject doInBackground(String... strings) {
        URL url = null;
        JSONObject j = null;
        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // Create the HTTP connection
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Get the stream and put it in a buffer before reading
                j = readStream(in);
                String url_lyrics = j.getJSONObject("response").getJSONArray("hits").getJSONObject(0).getJSONObject("result").getString("url");
                AsyncTask<String, Void, String> task = new Lyrics(this.app);
                task.execute(url_lyrics, null, null); // performs the task that will retrieve the lyrics
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

    // This method convert the data of the bufferes in JsonObject
    private JSONObject readStream(InputStream is) throws IOException {
        try{
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return new JSONObject(bo.toString());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

}
