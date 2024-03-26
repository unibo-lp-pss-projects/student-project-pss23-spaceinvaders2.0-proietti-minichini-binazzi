package org.example;

public class AlienEntity extends Entity {
    public double moveSpeed = 75; // A public double variable holding the alien's movement speed.
    public Game game; // A public Game object, likely referencing the game instance this alien belongs to



    /**
     * Create a new alien entity
     *
     * @param game The game in which this entity is being created
     * @param ref  The sprite (image) which should be displayed for this alien
     * @param x    The initial x location of the alien
     * @param y    The initial y location of the alien
     */

    public AlienEntity(Game game, String ref, int x, int y) {
        super(ref, x, y);
        
        this.game = game;
        dx = -moveSpeed;
    }

    /**
     * Move this alien according to the amount of time that has passed
     *
     * @param delta The time that has passed since last move
     */

    public void move(long delta) {
        // Request a logic update if we have reached the left hand side of the screen and
        // are moving left
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // Request a logic update if we have reached the right hand side of
        // the screen and are moving right
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }

        // proceed with normal move
        super.move(delta);
    }


    /**
     * Update the game logic related to aliens
     */

    public void doLogic() {
        // change the movement from horizontal to slightly down the screen
        dx = -dx;
        y += 10;

        // if we have reached the bottom of the screen then the player dies
        if (y > 570) {
            game.notifyDeath();
        }
    }

    /**
     * Notification that this alien has collided with another entity
     *
     * @param other The other entity
     */
    
    public void collidedWith(Entity other) {
        
    }
}
