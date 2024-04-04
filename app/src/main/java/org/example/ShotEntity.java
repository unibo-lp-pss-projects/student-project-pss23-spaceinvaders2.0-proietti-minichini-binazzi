package org.example;

/*
 * This entity represents a shot fired by both the player's ship and the boss.
 * In this ShotEntity class, the moveSpeed attribute represents the vertical speed of the shot. 
 * A negative value is used to indicate upward movement, aligning with the shooting direction of the player's ship. 
 * However, since this class is shared between the ship and the boss, where shots move in opposite directions, 
 * the negative sign signifies movement in the opposite direction for the boss's shots (which is from top to the bottom of the screen).
 */

public class ShotEntity extends Entity { // ShotEntity inherits all the properties and methods of the Entity class.
    // The shot vertical speed
    public double moveSpeed = -300;

    // The game in which this entity exists
    public Game game;

    // True if this shot has been "used", for example if it hit something
    public boolean used = false;

    /*
     * Create a new shot
     *
     */
    public ShotEntity(Game game, String sprite, int x, int y) {
        super(sprite, x, y); // initialize the sprite and position of the shot entity.

        this.game = game; // The game in which the shot has been created

        dy = moveSpeed; // it sets the vertical speed (dy) of the shot to moveSpeed
    }

    /*
     * Request that this shot moved based on the amount of time that has passed.
     * It also handles the shot entity removal if it has move off the screen.
     * This prevents unused shots from accumulating indefinitely in memory.
     *
     */
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);

        // If the shot has moved off the screen, this requests the game to remove the shot entity. 
        if (y < -100) {
            game.removeEntity(this);
        }
    }

    /*
     * This method handles collision events between the shot and other entities. 
     * It ensures that only one collision event is processed for each shot and proceeds to remove the entities 
     * and to notify the game when the shot collides with an alien.
     * The param "other" represents the other entity with which the shot has collided
     */
    public void collidedWith(Entity other) {
        // prevent double kills checking the "used" flag. If the shot already hit something, do not collide. 
        if (used) {
            return;
        }

        // An alien will be eliminated if the shot hits it.
        if (other instanceof AlienEntity) {
            // remove both the shot and the alien entity from the game 
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyAlienKilled();
            used = true;
        }
    }
}