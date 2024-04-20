package org.example;


import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class SpriteTest {

    @Test
    public void testGetWidth() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Sprite sprite = new Sprite(image);
        assertEquals(50, sprite.getWidth());
    }

    @Test
    public void testGetHeight() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Sprite sprite = new Sprite(image);
        assertEquals(50, sprite.getHeight());
    }

    @Test
    public void testDraw() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        Sprite sprite = new Sprite(image);
        sprite.draw(g, 0, 0);
        assertEquals(50, image.getWidth());
        assertEquals(50, image.getHeight());
    }
}
