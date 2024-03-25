package org.example;

import java.awt.*;

/**
 * A sprite to be displayed on the screen. Note that a sprite contains no
 * state information, i.e. its just the image and not the location. This
 * allows us to use a single sprite in lots of different places without
 * having to store multiple copies of the image.
 */
public class Sprite {
    // The image to be drawn for this sprite
    public Image image;

    public Sprite(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    /**
     * Draw the sprite onto the graphics context provided
     */
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }
}
