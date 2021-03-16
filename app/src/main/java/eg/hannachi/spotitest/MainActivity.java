package eg.hannachi.spotitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "adcfa4bfab734c7aa2bbffb1c63805bd"; //anis
    //private static final String CLIENT_ID = "bc064d26c0c44116be4f4b49da7f0118"; //gab
    private static final String REDIRECT_URI = "http://example.com/callback/";
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
        // End
        text = findViewById(R.id.text);
        img = findViewById(R.id.cover);
        art = findViewById(R.id.album);
        album = findViewById(R.id.artist);
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
                        String titre =  track.name;
                        while (titre.contains("(")){
                            int index = titre.indexOf("(")-1; //F
                            String s1 = titre.substring(index);
                            int index2 = s1.indexOf(")")+1;
                            String s3 = s1.substring(0, index2);
                            titre = titre.replace(s3, "");
                            //Log.i("JJJ sans feat :", " "+ titre);
                        }
                        while (titre.contains("[")){
                            int index = titre.indexOf("[")-1; //F
                            String s1 = titre.substring(index);
                            int index2 = s1.indexOf("]")+1;
                            String s3 = s1.substring(0, index2);
                            titre = titre.replace(s3, "");
                            //Log.i("JJJ sans feat :", " "+ titre);
                        }
                        if (titre.contains("remix")){
                            int index = titre.indexOf("r")-1;
                            String s1 = titre.substring(index);
                            int index2 = s1.indexOf("x")+1;
                            String s3 = s1.substring(0, index2);
                            titre = titre.replace(s3, "");
                            //Log.i("JJJ sans remix :", " "+ titre);
                        }
                        if (titre.contains("Remaster")){
                            int index = titre.indexOf("-");
                            String s1 = titre.substring(index);
                            //int index2 = s1.indexOf(")")+1;
                            String s3 = s1.substring(0);
                            titre = titre.replace(s3, "");
                            //Log.i("JJJ sans Remaster :", " "+ titre);
                        }
                        if (titre.contains("-")){
                            int index = titre.indexOf("-");
                            String s1 = titre.substring(index);
                            //int index2 = s1.indexOf(")")+1;
                            String s3 = s1.substring(0);
                            titre = titre.replace(s3, "");
                            //Log.i("JJJ sans - :", " "+ titre);
                        }
                        //artists(track.artists);
                        Log.d("MainActivity", titre + " by " + track.artist.name);
                        String token = "_ubTsw2W0EC_ER_II73nI8er_Gdi0W65o3ITAP-TzxCLMe4FNUWP9nYaapeaark1";
                        String url = "";
                        if (track.artists.size() > 1){
                            Log.d("artistes", "plusieurs");
                            url = "https://api.genius.com/search?access_token="+token+"&q="+titre+" "+track.artists.get(0).name+" "+track.artists.get(1).name;
                        }
                        else{
                            url = "https://api.genius.com/search?access_token="+token+"&q="+titre+" "+track.artist.name;
                        }
                        LyricsURL task = new LyricsURL(MainActivity.this);
                        task.execute(url, null, null);
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

    private String artists(List<Artist> artists){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < artists.size(); i++){
            s.append(artists.get(i).name).append(" ");
            Log.d("artistes", artists.get(i).name);
        }
        return s.toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}