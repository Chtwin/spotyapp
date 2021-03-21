package eg.hannachi.spotitest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Lyrics extends AsyncTask<String, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private final AppCompatActivity app;

    public Lyrics(AppCompatActivity a){
        this.app = a;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        String j = null;
        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // Create the HTTP connection
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Get the stream and put it in a buffer before reading
                j = readStream(in); // read the data and get the lyrics
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return j;
    }

    @Override
    protected void onPostExecute(String j) {
        super.onPostExecute(j);
        TextView lyr = app.findViewById(R.id.parole); // get the lyrics field in the actvity
        lyr.setText(j); // Set the lyrics
    }

    private String readStream(InputStream is) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            bo.write(i);
            i = is.read();
        }
        // It's all the HTML code of the web page
        String s_original = bo.toString();
        // Only the tag with the lyrics is kept
        int index = s_original.indexOf("<div class=\"lyrics\">")-1;
        String s1 = s_original.substring(index);
        int index2= s1.indexOf("</div>")-1;
        String s2;
        // Only the p tag is kept, because it s the paragraphs with le lyrics
        index = s_original.indexOf("<p>")+3;
        s1 = s_original.substring(index);
        index2= s1.indexOf("</p>");
        s2 = s1.substring(0, index2);
        if (s2.contains("[Paroles")){ // remove the line where the title of the music is specified
            index = s2.indexOf("[Paroles");
            s1 = s2.substring(index);
            index2 = s1.indexOf("]")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "");
        }
        while (s2.contains("<")){ // remove all the html tag in the lyrics
            index = s2.indexOf("<");
            s1 = s2.substring(index);
            index2 = s1.indexOf(">")+1;
            String s3 = s1.substring(0, index2);
            s2 = s2.replace(s3, "\n");
        }
        return s2; // return the clean lyrics
    }
}
