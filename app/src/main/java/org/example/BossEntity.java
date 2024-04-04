package org.example;

/*
 * This class represents the BossEntity, the entity that appears when all the aliens are killed.
 * The Boss can move from side to side on the screen, but not downward. 
 * It has the ability to shoot like the ShipEntity, so the player must avoid being hit while trying to eliminate the Boss.
 */

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

public class BossEntity extends Entity { // BossEntity inherits all the properties and methods of the Entity class.

    private static final double SPEED = 150; // A private static variable holding the boss's movement speed.
    private static final int BOSS_WIDTH = 64; // A private static variable holding the boss's width
    private static final int BOSS_HEIGHT = 64; // A private static variable holding the boss's height

    private Game game; // a private game referencing the game instance the boss belongs to.
    private ShotEntity shot; // a private shot referencing the shot fired by the boss (in this case).
    private Timer shotTimer; // a private shotTimer representing the timer used to control the boss's firing rate.

    /*
     * This constructor creates a new instance of the BossEntity class.
     * It initializes the boss's sprite (image), position and movement speed.
     * It also schedules a timer task to repeatedly fire shots at a fixed interval.
     */
    public BossEntity(Game game, String sprite, int x, int y) {
        super(sprite, x, y);
        this.dx = SPEED;
        this.game = game;
        this.shotTimer = new Timer();
        this.shotTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fire();
            }
        }, 1000, 1000); //  it will execute after a delay of 1 second and then repeat every 1 second thereafter.
    }

    /*
     * This method moves the boss horizontally, ensuring it stays within the screen boundaries. 
     * If the boss hits the left or right boundary, its movement direction is reversed to prevent it from moving off-screen.
     */
    @Override
    public void move(long delta) {
        // Move boss horizontally
        super.move(delta);
    
        // Reverse direction if boss hits the screen boundaries
        if (x <= 0 || x >= 800 - BOSS_WIDTH) { // if the boss entity's position (x) is <= 0 (left boundary) or >= 800 minus the boss's width (right boundary), it means the boss has hit the screen edges.
            dx = -dx; // this reverses the boss horizontal movement direction (dx).
        }
    }

    /*
     * This method creates and launches a shot from the boss entity towards the player's direction.
     */

    public void fire() {
        // Create a new shot fired by the boss
        int shotX = (int) (x + (BOSS_WIDTH / 2)); // X position of the shot (center of the boss)
        int shotY = (int) (y + BOSS_HEIGHT); // Y position of the shot (bottom of the boss)
        shot = new ShotEntity(game, "sprites/bullet.png", shotX, shotY); // Assign a sprite (image) to this shot
        shot.dy = -shot.moveSpeed; // dy is the vertical speed and direction of the shot. Here, it adds a negative sign to reverse its direction, so the shot moves now towards the bottom of the screen
        game.entities.add(shot);
    }
    
    /*
     * This method is responsible for rendering the boss entity on the game screen.
     */
    public void draw(Graphics2D g) {
        super.draw(g);
    }

    /*
     * This method handles collision events between the boss entity and other entities, specifically responding to collisions with shots fired by the player.
     */

    @Override
    public void collidedWith(Entity other) {
        if (other instanceof ShotEntity) {
            game.notifyWin(); // A notification of victory appears if the player defeats the boss.
        }
    }

    @Override
    public void doLogic() {

    }
}
