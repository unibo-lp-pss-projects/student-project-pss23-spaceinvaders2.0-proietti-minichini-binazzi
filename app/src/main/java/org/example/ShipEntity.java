package org.example;

/*
 * The entity that represents the players ship
 */

public class ShipEntity extends Entity {
    // The game in which the ship exists
    public Game game;

    /*
     * Constructor method that creates a new entity to represent the players ship. 
     * It takes four parameters: the game in which the ship is being created, the reference to the sprite to show the ship and the initial x and y locations of the player's ship.
     */
    public ShipEntity(Game game, String ref, int x, int y) {
        super(ref, x, y);

        this.game = game; // this allows the Ship to have access to the Game object it belongs to
    }

    /*
     * Request that the ship moves itself based on the amount of time that has passed.
     * "Delta" is the time that has elapsed since last move (ms).
     */
    public void move(long delta) {
        // if the player is moving left and has reached the left hand side of the screen, do not move.
        if ((dx < 0) && (x < 10)) { // "dx < 0" indicates movement to the left. It is negative.
            return; // it returns from the method, preventing further movement to the left
        }
        // if the player is moving right and has reached the right hand side of the screen, do not move.
        if ((dx > 0) && (x > 750)) { // "dx > 0" indicates movement to the right. It is positive.
            return; // it returns from the method, preventing further movement to the right
        }
        // If the above conditions are not met, the ship moves normally according to its velocity and the time elapsed
        super.move(delta);
    }

    /*
     * Notification that the player's ship has collided with an entity.
     * "Other" is the entity with which the ship has collided.
     */
    public void collidedWith(Entity other) {
        // if it is an alien, notify the game that the player is dead
        if (other instanceof AlienEntity || other instanceof ShotEntity) {
            game.notifyDeath(); // the relevant notification window is displayed
        }
    }
}