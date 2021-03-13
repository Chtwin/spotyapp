package eg.hannachi.spotitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "adcfa4bfab734c7aa2bbffb1c63805bd";
    private static final String REDIRECT_URI = "http://example.com/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;
    private String artist;
    private String music;
    private TextView text;
    private ImageView img;

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
        text = findViewById(R.id.text);
        img = findViewById(R.id.cover);
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
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                        artist = track.artist.name;
                        music = track.name;
                        String token = "_ubTsw2W0EC_ER_II73nI8er_Gdi0W65o3ITAP-TzxCLMe4FNUWP9nYaapeaark1";
                        String url = "https://api.genius.com/search?access_token="+token+"&q="+artist+"%20"+music;
                        Log.i("ez", track.imageUri.toString());
                        LyricsURL task = new LyricsURL(MainActivity.this);
                        task.execute(url, null, null);
                        text.setText(music + " de " + artist);
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

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}