package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ShotEntityTest {
    @Test
    void testShotEntityInitialization() {
        Game game = new Game();
        ShotEntity shot = new ShotEntity(game, "sprites/shot.gif", 100, 100);
        // Assert that the shot instance is not null
        assertNotNull(shot, "ShotEntity instance should not be null");
        // Assert initial position
        assertEquals(100, shot.getX(), "ShotEntity initial X position should be 100");
        assertEquals(100, shot.getY(), "ShotEntity initial Y position should be 100");
        // Assert moveSpeed attribute
        assertEquals(-300, shot.moveSpeed, "ShotEntity moveSpeed should be -300");
    }

    @Test
    void testShotEntityMovement() {
        Game game = new Game();
        ShotEntity shot = new ShotEntity(game, "sprites/shot.gif", 100, 100);
        double initialY = shot.getY();
        // Move the shot with a delta time of 100 milliseconds
        shot.move(100);
        // Assert that shot has moved upward
        assertTrue(shot.getY() < initialY, "ShotEntity should have moved upward");
    }

    @Test
    void testShotEntityRemovalOffScreen() {
        Game game = new Game();
        ShotEntity shot = new ShotEntity(game, "sprites/shot.gif", 100, -150);
        // Simulate movement to move off the screen
        shot.move(100);
        // Ensure that the entities list is initialized
        game.initEntities();
        // Remove the shot from the game
        game.removeEntity(shot);
        // Assert that the shot has been removed from the game
        assertFalse(game.entities.contains(shot), "ShotEntity should have been removed from the game");
    }

    @Test
void testShotEntityCollisionWithAlien() {
    // Create a new game instance
    Game game = new Game();
    // Create a shot entity at a specific position
    ShotEntity shot = new ShotEntity(game, "sprites/shot.gif", 100, 200);
    // Create an alien entity at a specific position
    AlienEntity alien = new AlienEntity(game, "sprites/alien.gif", 100, 210);
    // Simulate collision between the shot and the alien
    shot.collidedWith(alien);
    // Ensure that the shot has been removed from the game
    assertFalse(game.entities.contains(shot), "ShotEntity should be removed after collision with AlienEntity");
    // Ensure that the alien has been removed from the game
    assertFalse(game.entities.contains(alien), "AlienEntity should be removed after collision with ShotEntity");
}

}
