package org.example;

import java.awt.*;

/**
 * An entity represents any element that appears in the game. 
 * The entity is responsible for resolving collisions and movement based on a set of properties defined either by subclass or externally.
 */

public abstract class Entity {
    // The current x location of this entity
    public double x;
    public double y;
    // The sprite that represents this entity
    public Sprite sprite;
    // The current speed of this entity horizontally (pixels/sec)
    public double dx;
    public double dy;


    public Entity(String ref, int x, int y) {
        this.sprite = SpriteStore.get().getSprite(ref);
        this.x = x;
        this.y = y;
    }

    public void move(long delta) {
        // update the location of the entity based on move speeds
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    }

    public void setHorizontalMovement(double dx) {
        this.dx = dx;
    }

    public void setVerticalMovement(double dy) {
        this.dy = dy;
    }

    public double getHorizontalMovement() {
        return dx;
    }

    public double getVerticalMovement() {
        return dy;
    }

    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }

    /**
     * Do the logic associated with this entity. This method
     * will be called periodically based on game events
     */
    public void doLogic() {
        
    }

    /**
     * Get the x location of this entity
     */
    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    /**
     * Check if this entity collided with another.
     * @return True if the entities collide with each other
     */
    public boolean collidesWith(Entity other) {
        // The rectangle used for this entity during collisions resolution
        Rectangle me = new Rectangle((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        // The rectangle used for other entities during collision resolution.
        Rectangle him = new Rectangle((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());
        return me.intersects(him);
    }

    public abstract void collidedWith(Entity other);
}
