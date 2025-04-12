import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;

public class music_player {
    
    public music_player(){

    }


    public File select_random_track(){

        String tracks_dir_path = "/home/student/Documents/Software-Engineering-Project-LTAG-/photon_tracks/";
        File tracks_dir = new File(tracks_dir_path);

        File[] tracks = tracks_dir.listFiles();

        Random rand = new Random();

        File track = tracks[rand.nextInt(tracks.length)];
        return track;
    }


    public void play_random_track(){

        

        try {

            File track = new File("/home/student/Documents/Software-Engineering-Project-LTAG-/photon_tracks/Track05.mp3");
            
            if(track.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(track);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }

            else{
                System.out.println("Can't find file");
            }
        
            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }






}
