package org.example;

import javax.sound.sampled.Clip;

public class AudioClip {
    public Clip clip;

    public AudioClip(Clip clip) {
        this.clip = clip;
    }

    public Clip getClip() {
        return clip;
    }

    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
