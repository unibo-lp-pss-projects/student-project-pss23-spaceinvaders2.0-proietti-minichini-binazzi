package org.example;

import java.awt.*;

/*
 * This class represents a sprite to be displayed on the screen.
 * The image of every component in the game is represented by a sprite.
 * This allows using a single sprite in different places without having to store multiple copies of the image.
 * This eliminates the need to store multiple copies of the image and permits the use of a single sprite in various contexts.
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

    /*
     * Draw the sprite onto the graphics context provided
     */
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }
}
