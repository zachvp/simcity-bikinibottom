package CommonSimpleClasses.sound;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	private ScheduledExecutorService executor
		= Executors.newSingleThreadScheduledExecutor();

	private static Sound instance;

	public static Sound getInstance() {
		if (instance == null) { instance = new Sound(); }
		return instance;
	}
	
	private Sound() { }
	
	public void playSound(String path) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream((getClass().getResource(path)));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch(Exception ex) {
			System.out.println("Error playing sound.");
			ex.printStackTrace();
		}
	}

}
