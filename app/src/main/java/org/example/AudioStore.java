package org.example;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.HashMap;

public class AudioStore {
    public static final AudioStore SINGLE_STORE = new AudioStore();
    public HashMap<String, AudioClip> audioClips = new HashMap<>();

    public AudioStore() {}

    public static AudioStore get() {
        return SINGLE_STORE;
    }

    public AudioClip getAudio(String ref) {
        if (audioClips.containsKey(ref)) {
            return audioClips.get(ref);
        }

        URL url = this.getClass().getClassLoader().getResource(ref);
        if (url == null) {
            fail("Can't find ref: " + ref);
        }

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            AudioClip audioClip = new AudioClip(clip);
            audioClips.put(ref, audioClip);
            return audioClip;
        } catch (Exception e) {
            fail("Failed to load audio: " + ref);
            return null;
        }
    }

    public void fail(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
