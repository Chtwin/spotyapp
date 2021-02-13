package eg.hannachi.spotitest;

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

public class Lyrics extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity app;
    ArrayList<String> list = new ArrayList<String>();

    public Lyrics(AppCompatActivity a){
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
                Log.i("JJJ3", j.getString("lyrics"));
                //TextView parolefield = app.findViewById(R.id.parole);
                //parolefield.setText(j.getString("lyrics"));
                //run(j);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return j;
    }

    /*public void run(JSONObject j) {
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView parolefield = app.findViewById(R.id.parole);
                try {
                    parolefield.setText(j.getString("lyrics"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }*/

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        TextView parolefield = app.findViewById(R.id.parole);
        try {
            parolefield.setText(jsonObject.getString("lyrics"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONObject readStream(InputStream is) throws IOException {
        try{
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            Log.i("JJJ2", bo.toString());
            return new JSONObject(bo.toString());
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

}
