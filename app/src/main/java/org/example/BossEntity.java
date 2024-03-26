package org.example;

/*
 * This class contains the creation of the BossEntity, the entity that appears when all the aliens are killed.
 * The Boss can move from one side of the screen to another without being able to move down the screen.
 * It can shoot like the ShipEntity, so the player must avoid being hit while trying to eliminate the Boss.
 */

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

//import org.newdawn.spaceinvaders.ShotEntity;
//import org.newdawn.spaceinvaders.Game;

public class BossEntity extends Entity {

    private static final double SPEED = 150; // A private static variable holding the boss's movement speed.
    private static final int BOSS_WIDTH = 64; // A private static variable holding the boss's width
    private static final int BOSS_HEIGHT = 64; // A private static variable holding the boss's height

    //private int health;

    private Game game;
    private ShotEntity shot;
    private Timer shotTimer;

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
        }, 1000, 1000);
    }

    @Override
    public void move(long delta) {
        // Move boss horizontally
        super.move(delta);
    
        // Reverse direction if boss hits the screen boundaries
        if (x <= 0 || x >= 800 - BOSS_WIDTH) {
            dx = -dx;
        }
    }

    public void fire() {
        // Create a new shot fired by the boss
        int shotX = (int) (x + (BOSS_WIDTH / 2)); // X position of the shot (center of the boss)
        int shotY = (int) (y + BOSS_HEIGHT); // Y position of the shot (bottom of the boss)
        shot = new ShotEntity(game, "sprites/bullet.png", shotX, shotY); // Assign a sprite (image) to this shot
        shot.dy = -shot.moveSpeed; // Set the shot's vertical speed to move upwards
        game.entities.add(shot);
    }
    
    

    //@Override
    public void draw(Graphics2D g) {
        super.draw(g); // Draw the sprite of the boss
    }

    @Override
    public void collidedWith(Entity other) {
        // Collision logic with other entities
        if (other instanceof ShotEntity) {
            game.notifyWin();
        }
    }

    @Override
    public void doLogic() {

    }
}
