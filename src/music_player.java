import java.io.File;
import java.util.Random;
import javax.sound.sampled.*;

public class music_player {

    private static Clip currentClip = null;

    public music_player() {}

    public static File select_random_track() {
        String tracks_dir_path = "wav_files/";
        File tracks_dir = new File(tracks_dir_path);
        File[] tracks = tracks_dir.listFiles();

        if (tracks == null || tracks.length == 0) {
            System.out.println("No tracks found in " + tracks_dir_path);
            return null;
        }

        Random rand = new Random();
        File track = tracks[rand.nextInt(tracks.length)];
        System.out.println("Selected " + track.getName());
        return track;
    }
         
    // find, select, and play the music
    public static void play_random_track() {
        try {
            File track = music_player.select_random_track();
    
            if (track.exists()) {
                System.out.println("Playing Music!");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(track);
                currentClip = AudioSystem.getClip();
                currentClip.open(audioStream);
                currentClip.start();
    
                // Wait for 6 minutes unless interrupted
                for (int i = 0; i < 360; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        currentClip.stop();
                        currentClip.close();  // ensuring the clip closes after stopping
                        System.out.println("Music interrupted and stopped!");
                        return;
                    }
                    Thread.sleep(1000);
                }
    
                currentClip.stop();
                currentClip.close(); // Close clip after playback ends
            } else {
                System.out.println("Can not  find file!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopPlayback() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
            System.out.println("Music has  Stopped.");
        }
    }
}