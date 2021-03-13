package eg.hannachi.spotitest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Lyrics extends AsyncTask<String, Void, String> {
    private AppCompatActivity app;
    ArrayList<String> list = new ArrayList<String>();

    public Lyrics(AppCompatActivity a){
        this.app = a;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        String j = null;
        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("JJJ", url.toString());
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                j = readStream(in);
                //Log.i("JJJ3", j.toString());
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return j;
    }

    @Override
    protected void onPostExecute(String j) {
        super.onPostExecute(j);
        TextView parolefield = app.findViewById(R.id.parole);
        parolefield.setText(j);
    }

    private String readStream(InputStream is) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            bo.write(i);
            i = is.read();
        }
        String s_original = bo.toString(); //tt le code html
        //on garde que la balise lyrics
        Log.i("JJJ2 avant ", ""+s_original.length());
        int index = s_original.indexOf("<div class=\"lyrics\">")-1;
        String s1 = s_original.substring(index);
        int index2= s1.indexOf("</div>")-1;
        String s2 = s1.substring(0, index2);

        //on garde que le paragraphe p qui a les paroles
        index = s_original.indexOf("<p>")+3;
        s1 = s_original.substring(index);
        index2= s1.indexOf("</p>")-1;
        s2 = s1.substring(0, index2);
        //Log.i("JJJ2 pendant <p>", " "+ s2);

        //retirer les <a href=>
        while (s2.contains("<a href=")){
            index = s2.indexOf("<a href=");
            s1 = s2.substring(index);
            index2= s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "\n");
            //Log.i("JJJ2 pendant <a href :", " "+ s2.length());
        }
        //retirer les <br>
        while (s2.contains("<br>")){
            index = s2.indexOf("<br>");
            s1 = s2.substring(index);
            index2 = s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "\n");
            //Log.i("JJJ2 pendant <br> :", " "+ s2.length());
        }
        //retirer les </a>
        while (s2.contains("</a>")){
            index = s2.indexOf("</a>");
            s1 = s2.substring(index);
            index2 = s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "");
            //Log.i("JJJ2 pendant </a> :", " "+ s2.length());
        }
        //retirer les <i>
        while (s2.contains("<i>")){
            index = s2.indexOf("<i");
            s1 = s2.substring(index);
            index2 = s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "");
            //Log.i("JJJ2 pendant <i> :", " "+ s2.length());
        }
        //retirer les </i>
        while (s2.contains("</i>")){
            index = s2.indexOf("</i>");
            s1 = s2.substring(index);
            index2 = s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "");
            //Log.i("JJJ2 pendant </i> :", " "+ s2.length());
        }
        while (s2.contains("</b>")){
            index = s2.indexOf("</b>");
            s1 = s2.substring(index);
            index2 = s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "");
            //Log.i("JJJ2 pendant </i> :", " "+ s2.length());
        }
        while (s2.contains("<b>")){
            index = s2.indexOf("<b>");
            s1 = s2.substring(index);
            index2 = s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "");
            //Log.i("JJJ2 pendant </i> :", " "+ s2.length());
        }
        //retirer les >
        //exactement nos paroles
        //Log.i("JJJ2 fin", " "+ s2);
        return s2;
    }
}
