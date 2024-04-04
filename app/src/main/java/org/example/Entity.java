package org.example;

/*
 * This class represents an entity, which is any element that appears in the game. 
 * The entity is responsible for resolving collisions and movement based on a set of properties defined either by subclass or externally.
 * This class is abstract, meaning it cannot be instantiated directly, but serves as a base class for other entities in the game.
 */

import java.awt.*;

public abstract class Entity {
    // The current x location of this entity
    public double x;
    public double y;

    // The sprite that represents this entity
    public Sprite sprite;

    // The current speed of this entity horizontally (pixels/sec)
    public double dx;
    public double dy;

    /*
     * Constructs an Entity object with a specified sprite reference (ref) and initial position (x and y).
     * It retrieves the sprite from the SpriteStore using the provided reference and initializes the entity's position.
     */
    public Entity(String ref, int x, int y) {
        this.sprite = SpriteStore.get().getSprite(ref);
        this.x = x;
        this.y = y;
    }

    /*
     * This method updates the location of the entity based on move speeds
     */
    public void move(long delta) {
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    }

    /*
     *  These two methods set the horizontal and vertical speeds of the entity, respectively.
     */
    public void setHorizontalMovement(double dx) {
        this.dx = dx;
    }

    public void setVerticalMovement(double dy) {
        this.dy = dy;
    }

    /*
     * These two methods retrieve the horizontal and vertical speeds of the entity, respectively.
     */
    public double getHorizontalMovement() {
        return dx;
    }

    public double getVerticalMovement() {
        return dy;
    }

    /*
     * Draws the sprite of the entity onto the provided graphics context (g) at its current position (x and y).
     */
    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }

    /*
     * Do the logic associated with this entity. 
     * This method will be called periodically based on game events. 
     * It is intended for subclasses to implement specific logic associated with the entity.
     */
    public void doLogic() {
        
    }

    /*
     * This method gets the x location of this entity
     */
    public int getX() {
        return (int) x;
    }

    /*
     * This method returns the y-coordinate of the entity. It is similar to the previous method.
     */
    public int getY() {
        return (int) y;
    }

    /*
     * Check if this entity collided with another.
     * @return True if the entities collide with each other.
     * It draws rectangles to represent each entity's bounding box and determines whether or not they intersect.
     */
    public boolean collidesWith(Entity other) {
        // The rectangle used for this entity during collisions resolution
        Rectangle me = new Rectangle((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        // The rectangle used for other entities during collision resolution.
        Rectangle him = new Rectangle((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());
        return me.intersects(him);
    }

    /*
     * This method is declared as abstract, meaning it must be implemented by subclasses. 
     * It defines actions to be taken when this entity collides with another entity.
     */
    public abstract void collidedWith(Entity other);
}
