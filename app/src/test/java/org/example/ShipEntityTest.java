package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShipEntityTest {
    @Test
    void testShipEntityInitialization() {
        Game game = new Game();
        ShipEntity ship = new ShipEntity(game, "sprites/ship.gif", 100, 100);
        // Assert that the ship instance is not null
        assertNotNull(ship, "ShipEntity instance should not be null");
        // Assert initial position
        assertEquals(100, ship.getX(), "ShipEntity initial X position should be 100");
        assertEquals(100, ship.getY(), "ShipEntity initial Y position should be 100");
    }

    @Test
    void testShipEntityLeftBoundary() {
        Game game = new Game();
        ShipEntity ship = new ShipEntity(game, "sprites/ship.gif", 10, 100);
        double initialX = ship.getX();
        // Move the ship to the left boundary
        ship.move(100); // Move the ship 
        // Assert that ship remains at the left boundary
        assertEquals(initialX, ship.getX(), "ShipEntity should not move beyond the left boundary");
    }

    @Test
    void testShipEntityRightBoundary() {
        Game game = new Game();
        ShipEntity ship = new ShipEntity(game, "sprites/ship.gif", 750, 100);
        double initialX = ship.getX();
        // Move the ship to the right boundary
        ship.move(100); // Move the ship
        // Assert that ship remains at the right boundary
        assertEquals(initialX, ship.getX(), "ShipEntity should not move beyond the right boundary");
    }


    @Test
    void testShipEntityCollisionWithAlien() {
        Game game = new Game();
        ShipEntity ship = new ShipEntity(game, "sprites/ship.gif", 100, 100);
        AlienEntity alien = new AlienEntity(game, "sprites/alien.gif", 100, 100);

        // Simulate collision between ship and alien
        ship.collidedWith(alien);
        
        // Verify that the game is notified of the ship's death or take appropriate action
    }


}
