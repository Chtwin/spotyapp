package eg.hannachi.spotitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "adcfa4bfab734c7aa2bbffb1c63805bd";
    private static final String REDIRECT_URI = "http://example.com/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;
    private String artist;
    private String music;

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
        // End
        final TextView text = findViewById(R.id.text);
        final Button button = findViewById(R.id.get);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi()
                        .subscribeToPlayerState()
                        .setEventCallback(playerState -> {
                            final Track track = playerState.track;
                            if (track != null) {
                                Log.d("MainActivity", track.name + " by " + track.artist.name);
                                artist = track.artist.name;
                                music = track.name;
                            }
                        });
                // Gabiboux
                if (artist != null)
                {
                    Lyrics task = new Lyrics(MainActivity.this);
                    String url = "https://api.lyrics.ovh/v1/"+artist+"/"+music+"";
                    task.execute(url, null, null);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
        new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void connected() {
        // Then we will write some more code here.
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}