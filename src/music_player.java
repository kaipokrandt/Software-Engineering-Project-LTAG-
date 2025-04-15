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
        
                String tracks_dir_path = "../wav_files";
                File tracks_dir = new File(tracks_dir_path);
        
                System.out.println("Looking for tracks in " + tracks_dir.getAbsolutePath());
                File[] tracks = tracks_dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));

                if (tracks == null || tracks.length == 0) {
                    System.out.println("No tracks found in the directory.");
                    return null;
                }

                Random rand = new Random();
        
                File track = tracks[rand.nextInt(tracks.length)];
        
                System.out.println("Selected " + track.getName());
                return track;
            }
        
        
        public static void play_random_track(Thread thread){
        
            try {
               
                    File track = music_player.select_random_track();
    
                
            if(track.exists() && track != null){
                System.out.println("Playing music!");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(track);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

               

                clip.drain();
                clip.close();
            }

            else{
                System.out.println("Can't find file");
            }
        
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }






}
