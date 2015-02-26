package client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Sound {
	
    public Sound() {}
    
	public void playSound(String url) {
		
		String fileName = "/Users/williamsneddon/Documents/workspace/PongGame/src/res/";   
		//String fileName = "res/bounce.wav";

		File file = new File(fileName+url);
	    AudioInputStream stream;
	    AudioFormat format;
	    DataLine.Info info;
	    Clip clip;
		
		try {
		    stream = AudioSystem.getAudioInputStream(file);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		}
		catch (Exception e) {}
	}
}