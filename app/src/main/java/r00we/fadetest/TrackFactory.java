package r00we.fadetest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R00We on 09/07/17.
 */

public class TrackFactory {

    public static List<Track> getTestTracks() {
        List<Track> result = new ArrayList<>();
        result.add(new Track("Never Again", "Breaking Benjamin", "https://radio2.zaycev.fm/road/dl/28522", 5.098980, 217.066527, 3.0, 2.0));
        result.add(new Track("Mr. Brightside", "The Killers", "https://radio2.zaycev.fm/road/dl/19141", 3.850050, 214.542999, 3.0, 2.0));
        result.add(new Track("Never Again", "Breaking Benjamin", "https://radio2.zaycev.fm/road/dl/28522", 5.098980, 217.066527, 3.0, 2.0));

        return result;
    }
}
