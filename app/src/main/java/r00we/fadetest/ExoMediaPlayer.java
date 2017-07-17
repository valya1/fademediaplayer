package r00we.fadetest;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;


public class ExoMediaPlayer {

    private SimpleExoPlayer exoPlayer;
    private MediaSource mediaSource;
    private Handler handler;


    public ExoMediaPlayer(@NonNull Context context) {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        handler = new Handler();
    }

    public void loadStreamData(@NonNull Uri uri) {
        if (mediaSource != null) {
            mediaSource.releaseSource();
        }

        String userAgent = "ExoPlayer2";

        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null);

        mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, new DefaultExtractorsFactory(), handler, null);

        exoPlayer.prepare(mediaSource);

    }

    public void seekTo(long position) {
        exoPlayer.seekTo(exoPlayer.getCurrentWindowIndex(), position);
    }

    public void play() {

        exoPlayer.setPlayWhenReady(true);

    }

    public void pause() {
        exoPlayer.setPlayWhenReady(false);
    }

    public void stop() {
        exoPlayer.stop();
        if (mediaSource != null) {
            mediaSource.releaseSource();
        }

    }

    public void release() {
        exoPlayer.release();
    }

    public void addListener(@NonNull ExoPlayer.EventListener trackListener) {
        exoPlayer.addListener(trackListener);
    }

    public int getCurrentWindowIndex() {
        return exoPlayer.getCurrentWindowIndex();
    }

    public long getCurrentPosition() {
        return exoPlayer.getCurrentPosition();
    }

    public void setVolume(float volume) {
        exoPlayer.setVolume(volume);
    }
}
