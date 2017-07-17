package r00we.fadetest;

import android.net.Uri;


public class Track {
    private String title;
    private String artist;
    private Uri url;
    private double start;
    private double next;
    private double fadeIn;
    private double fadeOut;

    public Track(String title, String artist, String url, double start, double next, double fadeIn, double fadeOut) {
        this.title = title;
        this.artist = artist;
        this.url = Uri.parse(url);
        this.start = start;
        this.next = next;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
    }

    public Uri getUrl() {
        return url;
    }

    public double getStart() {
        return start;
    }

    public double getNext() {
        return next;
    }

    public double getFadeIn() {
        return fadeIn;
    }

    public double getFadeOut() {
        return fadeOut;
    }
}
