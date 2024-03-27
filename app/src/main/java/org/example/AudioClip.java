package org.example;

/*
 * The AudioClip class represents an audio clip and provides methods for playback control.
 * It utilizes the Java Sound API's Clip class for audio playback.
 * This class allows for starting, stopping, looping, and retrieving the audio clip.
 */


import javax.sound.sampled.Clip; // The Clip interface represents a unique type of data line whose audio data can be loaded before playback, instead of being streamed in real time.

public class AudioClip {
    public Clip clip; // A public attribute named clip of type Clip. This attribute will hold the audio clip that this AudioClip object represents.

    /*
     * When you create a new AudioClip, you give it an actual audio clip to hold onto.
     */
    public AudioClip(Clip clip) { // This is a constructor for the AudioClip class. It takes a Clip object as a parameter and assigns it to the clip attribute of the AudioClip object being constructed.
        this.clip = clip;
    }

    /*
     * This method allows other classes to retrieve the clip attribute of the AudioClip object. 
     */
    public Clip getClip() {
        return clip;
    }

    /*
     * This method makes the audio clip start playing from the beginning. If it was already playing, it stops it first.
     */
    public void play() {
        if (clip != null) {
            clip.stop(); // It stops any previous playback
            clip.setFramePosition(0); // sets the playback position to the beginning (FramePosition(0))
            clip.start(); // then starts playback
        }
    }

    /*
     * This method makes the audio clip play repeatedly in a loop.
     */
    public void loop() { 
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
