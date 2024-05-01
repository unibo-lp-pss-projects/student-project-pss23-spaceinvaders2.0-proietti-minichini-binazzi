package org.example;

/*
 * This class represents the alien entity in the game.
 * Aliens have movement speed and can move horizontally and vertically across the screen.
 * They are shown as a block and are represented by a sprite (image).
 */

public class AlienEntity extends Entity { // AlienEntity inherits all the properties and methods of the Entity class.
    public double moveSpeed = 75; // A public double variable holding the alien's movement speed.
    public Game game; // A public Game object referencing the game instance aliens belong to

    /*
     * Create a new alien entity
     */

    public AlienEntity(Game game, String ref, int x, int y) {
        super(ref, x, y); // the initial position of the alien with the specified x and y coordinates. Ref is the sprite (image) that should be displayed for this alien
        
        this.game = game; // the game in which this entity is being created
        dx = -moveSpeed;
    }

    /*
     * Move this alien according to the amount of time that has passed.
     * The parameter delta is the time that has passed since last move.
     */

    public void move(long delta) {
        // Request a logic update if we have reached the left hand side of the screen and are moving left
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // Request a logic update if we have reached the right hand side of the screen and are moving right
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }

        // proceed with normal move
        super.move(delta);
    }


    /*
     * Update the game logic related to aliens
     */

    public void doLogic() {
        // change the movement from horizontal to slightly down the screen
        dx = -dx;
        y += 10;

        // if the alien has reached the bottom of the screen then the player dies
        if (y > 570) {
            game.notifyDeath();
        }
    }

    /*
     * Notification that this alien has collided with another entity
     */
    
    public void collidedWith(Entity other) {
        
    }
}
