package r00we.fadetest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    IPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View play = findViewById(R.id.btnPlay);
        View pause = findViewById(R.id.btnPause);
        View next = findViewById(R.id.btnNext);

        player = new Player(new ExoMediaPlayer(this), new ExoMediaPlayer(this), new Handler());

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.btnPlay:
                    player.play(TrackFactory.getTestTracks());
                    break;
                case R.id.btnPause:
                    player.pause();
                    break;
                case R.id.btnNext:
                    player.nextTrack();
                    break;
            }
        };
        for (View v : Arrays.asList(play, pause, next))
            v.setOnClickListener(onClickListener);
    }
}
