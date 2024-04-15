package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BossEntityTest {
    @Test 
    void testBossEntityInitialization() {
        Game game = new Game();
        BossEntity boss = new BossEntity(game, "sprites/boss.png", 100, 100);
        assertNotNull(boss, "BossEntity instance should not be null");
        assertEquals(100, boss.getX(), "BossEntity initial X position should be 100");
        assertEquals(100, boss.getY(), "BossEntity initial Y position should be 100");
    }
    
    @Test
    void testBossEntityMovement() {
        Game game = new Game();
        BossEntity boss = new BossEntity(game, "sprites/boss.png", 100, 100);
        
        double initialX = boss.getX();
        
        boss.move(100); // Move boss for 100 milliseconds
        
        assertTrue(boss.getX() != initialX, "BossEntity should have moved horizontally");
    }

    @Test
    void testBossEntityReverseDirection() {
        Game game = new Game();
        BossEntity boss = new BossEntity(game, "sprites/boss.png", 700, 100);
        
        double initialDx = boss.getX();
        
        // Move boss to the right edge of the screen
        boss.move(100);
        
        // Boss should reverse direction after hitting the right boundary
        assertEquals(-initialDx, boss.getX(), "BossEntity direction should be reversed after hitting the right boundary");
    }
    
    @Test
    void testBossEntityCollisionWithShot() {
        Game game = new Game();
        BossEntity boss = new BossEntity(game, "sprites/boss.png", 100, 100);
        ShotEntity shot = new ShotEntity(game, "sprites/bullet.png", 100, 100);
        
        // Simulate collision between boss and shot
        boss.collidedWith(shot);
        
        // Boss should notify victory when collided with a shot
        game.notifyWin();
    }
}
