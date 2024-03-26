package org.example;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.HashMap;

public class AudioStore {
    public static final AudioStore SINGLE_STORE = new AudioStore(); // This is a single instance of the AudioStore class.  It's static, so there is only one copy shared among all instances of AudioStore.
    public HashMap<String, AudioClip> audioClips = new HashMap<>(); // This is a map that holds audio clips (AudioClip objects) with associated keys (references).

    /*
     * This is an empty constructor. It doesn't have any explicit functionality but is implicitly called when a new AudioStore object is created.
     */
    public AudioStore() {}

    /*
     * This method returns the single instance of the AudioStore class (SINGLE_STORE). 
     * It's a way to get access to the AudioStore instance without needing to create a new one.
     */
    public static AudioStore get() {
        return SINGLE_STORE;
    }

    /*
     * This method retrieves an audio clip based on a given reference (ref). It checks if the requested audio clip is already stored in the audioClips collection.
     * If it is, it returns it immediately. 
     * If not, it retrieves the audio file from a specified resource, loads it into memory as a Clip object and creates a new AudioClip instance with this Clip.
     * Then, it stores the audio file in the audioClips collection for future use, and returns it. 
     * 
     * To sum up, it serves as a method to efficiently fetch and manage audio clips, either from memory or by loading them from external resources when needed.
     */
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

    /*
     * This method prints an error message to the standard error stream (System.err) and exits the program with an exit code of 1.
     * It is useful because it provides a mechanism to handle errors and terminate the program in case of failures.
     */
    public void fail(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
