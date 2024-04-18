package org.example;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    public void testStartGame() {
        Game game = new Game();
        game.startGame();
        assertTrue(game.entities.size() > 0); // Check that there are entities in the game after launching
    }

    @Test
    public void testInitEntities() {
        Game game = new Game();
        game.initEntities();
        assertTrue(game.entities.size() > 0); // Check that there are entities initialized correctly
    }

    @Test
    public void testNotifyDeath() {
        Game game = new Game();
        game.notifyDeath();
        assertTrue(game.waitingForKeyPress); // Verify that the game waits for a keypress after the player dies
    }

    @Test
    public void testNotifyWin() {
        Game game = new Game();
        game.notifyWin();
        assertTrue(game.waitingForKeyPress); // Verify that the game is waiting for a button press after the player wins
    }

    @Test
    public void testCloseNotificationPanel() {
        Game game = new Game();
        game.closeNotificationPanel();
        assertFalse(game.notificationShowing); // Check that the notification panel is no longer shown after closing
    }
}