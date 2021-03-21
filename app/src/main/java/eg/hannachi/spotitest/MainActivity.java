package eg.hannachi.spotitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "adcfa4bfab734c7aa2bbffb1c63805bd"; // anis
    //private static final String CLIENT_ID = "bc064d26c0c44116be4f4b49da7f0118"; //gab
    private static final String REDIRECT_URI = "http://example.com/callback/";
    private final String token = "_ubTsw2W0EC_ER_II73nI8er_Gdi0W65o3ITAP-TzxCLMe4FNUWP9nYaapeaark1"; // Token that we use for the connection to the Genius API
    private SpotifyAppRemote mSpotifyAppRemote;
    private TextView text;
    private ImageView img;
    private TextView art;
    private TextView album;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Night mode
        final AppCompatDelegate delegate = getDelegate();
        delegate.installViewFactory();
        delegate.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        delegate.applyDayNight();
        // Declare of the module of the UI
        text = findViewById(R.id.text);
        img = findViewById(R.id.cover);
        art = findViewById(R.id.album);
        album = findViewById(R.id.artist);
        Button act = findViewById(R.id.act);
        act.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Activity2.class))); // Clicking on the button redirect to another activity
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the parameter to create the connection with spotify server
        ConnectionParams connectionParams =
        new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();
        // Create the connection with the spotify server and store the data in the mSpotifyAppRemote attribut.
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        connected();
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void connected() {
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track; // We get the track
                    if (track != null) {    // Check if a music is playing
                        String titre = parseTitle(track.name);
                        String url = "https://api.genius.com/search?access_token="+token+"&q="+titre+" "+track.artist.name;  // We create the URL to get the music with the API
                        LyricsURL task = new LyricsURL(MainActivity.this);
                        task.execute(url, null, null); // get the music lyrics
                        // We display the data on the app
                        text.setText(track.name);
                        art.setText(track.artist.name);
                        album.setText(track.album.name);
                        mSpotifyAppRemote
                                .getImagesApi()
                                .getImage(track.imageUri, Image.Dimension.LARGE)
                                .setResultCallback(
                                        bitmap -> {
                                            img.setImageBitmap(bitmap);
                                        });
                    }
                });
    }

    private String parseTitle(String titre){
        List<String> l = Arrays.asList("(","(",")","[","[","]","remix","r","x","-","-","-"); // The character that we want to remove with their content
        int index, index2;
        String s3, s1;
        for (int i = 0; i < l.size(); i += 3){
            if (i<=6){
                //removes the characters for which we know the beginning and the end
                while (titre.contains(l.get(i))){
                    index = titre.indexOf(l.get(i+1))-1;
                    s1 = titre.substring(index);
                    index2 = s1.indexOf(l.get(i+2))+1;
                    s3 = s1.substring(0, index2);
                    titre = titre.replace(s3, "");
                }
            }
            else if (titre.contains(l.get(i))){
                //removes the characters whose beginning we know and from which we want to remove all the characters that follow
                index = titre.indexOf(l.get(i+1))-1;
                s1 = titre.substring(index);
                index2 = s1.indexOf(l.get(i+1))+1;
                s3 = s1;
                titre = titre.replace(s3, "");
            }
        }
        return titre; // return the parse title
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}