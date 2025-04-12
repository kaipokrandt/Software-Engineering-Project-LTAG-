import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.sound.sampled.*;


public class music_player {

    Thread thread = null;
    
    public music_player(Thread music_player_Thread){

        this.thread = music_player_Thread;

    }


    public File select_random_track(){

        String tracks_dir_path = "/home/student/Documents/Software-Engineering-Project-LTAG-/wav_files/";
        File tracks_dir = new File(tracks_dir_path);

        File[] tracks = tracks_dir.listFiles();

        Random rand = new Random();

        File track = tracks[rand.nextInt(tracks.length)];

        System.out.println("Selected " + track.getName());
        return track;
    }


    public void play_random_track(){

        try {

            File track = this.select_random_track();

            
            if(track.exists()){
                System.out.println("Playing music!");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(track);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                this.thread.sleep(396000);
                this.thread.interrupt();
            }

            else{
                System.out.println("Can't find file");
            }
        
            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }






}
