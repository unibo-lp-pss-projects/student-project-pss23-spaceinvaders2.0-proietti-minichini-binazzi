package org.example;

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

//import org.newdawn.spaceinvaders.ShotEntity;
//import org.newdawn.spaceinvaders.Game;

public class BossEntity extends Entity {

    private static final double SPEED = 150;
    private static final int BOSS_WIDTH = 64;
    private static final int BOSS_HEIGHT = 64;

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
        shot = new ShotEntity(game, "sprites/bullet.png", shotX, shotY);
        shot.dy = -shot.moveSpeed; // Set the shot's vertical speed to move upwards
        game.entities.add(shot);
    }
    
    

    //@Override
    public void draw(Graphics2D g) {
        super.draw(g); // Draw boss sprite
    }

    @Override
    public void collidedWith(Entity other) {
        // Collision logic with other entities can be implemented here
        if (other instanceof ShotEntity) {
            game.notifyWin();
        }
    }

    @Override
    public void doLogic() {
        // Boss logic can be implemented here
    }
}
