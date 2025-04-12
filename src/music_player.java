import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.sound.sampled.*;


public class music_player {

        
        public music_player(){
        }
    
    
        public static File select_random_track(){
        
                String tracks_dir_path = "/home/student/Documents/Software-Engineering-Project-LTAG-/wav_files/";
                File tracks_dir = new File(tracks_dir_path);
        
                File[] tracks = tracks_dir.listFiles();
        
                Random rand = new Random();
        
                File track = tracks[rand.nextInt(tracks.length)];
        
                System.out.println("Selected " + track.getName());
                return track;
            }
        
        
            public static void play_random_track(Thread thread){
        
                try {
        
                    File track = music_player.select_random_track();
    
                
                if(track.exists()){
                    System.out.println("Playing music!");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(track);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    thread.sleep(396000);
                    thread.interrupt();
            }

            else{
                System.out.println("Can't find file");
            }
        
            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }






}
