package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.sampled.*;

public class AudioClipTest {

    private AudioClip audioClip;
    private Clip clip;

    @BeforeEach
    public void setUp() throws Exception {
        // Creating a Mock Clip for testing (A mock is a simulated object that imitates the behavior of a real object in a test context)
        clip = AudioSystem.getClip();
        audioClip = new AudioClip(clip);
    }

    @AfterEach //used to free resources and reset state after each test method runs
    public void tearDown() throws Exception {
        audioClip = null;
        clip = null;
    }

    @Test
    public void testGetClip() {
        assertEquals(clip, audioClip.getClip());
    }

    @Test
    public void testPlay() {
        // Check that the play() method does not raise exceptions
        try {
            audioClip.play();
        } catch (Exception e) {
            fail("play() method threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoop() {
        // Check that the loop() method does not raise exceptions
        try {
            audioClip.loop();
        } catch (Exception e) {
            fail("loop() method threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testStop() {
        // Check that the stop() method does not raise exceptions
        try {
            audioClip.stop();
        } catch (Exception e) {
            fail("stop() method threw an exception: " + e.getMessage());
        }
    }
}
