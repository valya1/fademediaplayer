package mihail.fade;

import android.os.Handler;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.Iterator;
import java.util.List;


public class Player implements IPlayer {

    public static final int PAUSE_MODE = 10;
    public static final int STOP_MODE = 11;
    private final ExoMediaPlayer mMediaPlayer1;
    private final ExoMediaPlayer mMediaPlayer2;
    private final Handler handler;
    private Iterator<Track> mTracksIterator;
    private Track mCurrentTrack;
    private List<Track> trackList;
    private float mFadeInCriteria;
    private float mFadeOutCriteria;
    private Boolean mMediaPlayerIndicator;


    public Player(ExoMediaPlayer exoMediaPlayer1, ExoMediaPlayer exoMediaPlayer2, Handler handler) {

        this.mMediaPlayer1 = exoMediaPlayer1;
        this.mMediaPlayer2 = exoMediaPlayer2;
        this.handler = handler;
        switchPlayers();

        this.mMediaPlayer1.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (isLoading) {
                    mMediaPlayer1.setVolume(0.0f);
                    mMediaPlayer1.play();
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                    fadeIn(mCurrentTrack, exoMediaPlayer1);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });


        this.mMediaPlayer2.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (isLoading) {
                    mMediaPlayer2.setVolume(0.0f);
                    mMediaPlayer2.play();
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                    fadeIn(mCurrentTrack, exoMediaPlayer2);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
    }

    @Override
    public void play(List<Track> tracks) {

        if (tracks == null)
            return;

        if (trackList == null) {
            mTracksIterator = tracks.listIterator();
            trackList = tracks;

            if (mTracksIterator.hasNext()) {
                mCurrentTrack = mTracksIterator.next();
                playCurrentTrack(getMainMediaPlayer());
            } else
                return;
        } else
            getMainMediaPlayer().play();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getMainMediaPlayer().getCurrentPosition() >= Math.round(mCurrentTrack.getNext() * 1000)) {
                    nextTrack();
                }
                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    @Override
    public void pause() {
        fadeOut(mCurrentTrack, getMainMediaPlayer(), PAUSE_MODE);
    }

    @Override
    public void nextTrack() {
        switchPlayers();
        fadeOut(mCurrentTrack, getSecondaryMediaPlayer(), STOP_MODE);
        if (!mTracksIterator.hasNext()) {
            mTracksIterator = trackList.iterator();
        }
        mCurrentTrack = mTracksIterator.next();
        playCurrentTrack(getMainMediaPlayer());
    }

    private void fadeIn(Track track, ExoMediaPlayer exoMediaPlayer) {

        final long steps = Math.round(10 * track.getFadeOut());
        final double stepWidth = (double) 1 / steps;

        mFadeInCriteria = 0;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFadeInCriteria += stepWidth;
                exoMediaPlayer.setVolume(Double.valueOf(1 - Math.pow(mFadeInCriteria - 1, 2)).floatValue());
                if (mFadeInCriteria >= 1) {
                    mFadeInCriteria = 0;
                    handler.removeCallbacks(this);
                } else
                    handler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void fadeOut(Track track, ExoMediaPlayer exoMediaPlayer, int mode) {

        final long steps = Math.round(10 * track.getFadeOut());
        final double stepWidth = (double) 1 / steps;

        mFadeOutCriteria = 0;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFadeOutCriteria += stepWidth;
                exoMediaPlayer.setVolume(Double.valueOf(Math.pow(mFadeOutCriteria - 1, 2)).floatValue());

                if (mFadeOutCriteria >= 1) {
                    if (mode == PAUSE_MODE) {
                        exoMediaPlayer.pause();
                    } else {
                        exoMediaPlayer.stop();
                    }
                    mFadeOutCriteria = 0;
                    handler.removeCallbacks(this);
                } else
                    handler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void playCurrentTrack(ExoMediaPlayer exoMediaPlayer) {

        exoMediaPlayer.seekTo(Double.valueOf(mCurrentTrack.getStart() - mCurrentTrack.getFadeIn() * 1000).longValue());
        exoMediaPlayer.loadStreamData(mCurrentTrack.getUrl());
    }

    private ExoMediaPlayer getMainMediaPlayer() {
        return mMediaPlayerIndicator ? mMediaPlayer1 : mMediaPlayer2;
    }

    private ExoMediaPlayer getSecondaryMediaPlayer() {
        return mMediaPlayerIndicator ? mMediaPlayer2 : mMediaPlayer1;
    }

    private void switchPlayers() {
        mMediaPlayerIndicator = mMediaPlayerIndicator == null || !mMediaPlayerIndicator;
    }
}
