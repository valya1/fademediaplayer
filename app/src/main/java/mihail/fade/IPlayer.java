package mihail.fade;

import java.util.List;


public interface IPlayer {

    void play(List<Track> tracks);

    void pause();

    void nextTrack();
}
