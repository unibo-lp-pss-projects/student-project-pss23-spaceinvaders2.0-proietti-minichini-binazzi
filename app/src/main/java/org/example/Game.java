package org.example;

/*
 * This is the main part of the game. This class will serve as the game logic's central mediator in addition to managing the display.
 * Display management will consist of a loop that cycles round each entity asking them to move and then drawing them in the right place.
 * With the assistance of an inner class, it will also allow the player to control the ship.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas {
    public BufferStrategy strategy;  // The strategy that enables the use of rapid page flipping
    public boolean gameRunning = true;

    public List<Entity> entities = new ArrayList<Entity>();
    public List<Entity> removeList = new ArrayList<Entity>();

    public Entity ship;  // The entity representing the player (the ship)
    public double moveSpeed = 300;

    public long lastFire = 0;
    public long firingInterval = 50;

    public int alienCount;
    public BossEntity boss; // The entity representing the boss

    public String message = "Press any key to start"; // This string will be displayed once the game page is open 
    public boolean waitingForKeyPress = true;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean firePressed = false;
    public boolean logicRequiredThisLoop = false;

    public AudioStore audioStore = AudioStore.get();
    public AudioClip backgroundMusic;
    public AudioClip laserSound;

    public boolean notificationShowing = false;
    public JFrame notificationFrame;

    /*
     * This creates a notification panel which shows a message when the player finishes the game.
     * Whether the player wins or loses, a different message will be displayed.
     */

    public void showNotificationPanel(Sprite imageSprite) {
        if (!notificationShowing) {
            JPanel notificationPanel = new JPanel();
            notificationPanel.setLayout(new BorderLayout());
    
            ImageIcon imageIcon = new ImageIcon(imageSprite.image);
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(300, 150, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(scaledImageIcon);
            notificationPanel.add(imageLabel, BorderLayout.CENTER);
    
            JButton closeButton = new JButton("Close the game");
            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // closes the application
                }
            });
            notificationPanel.add(closeButton, BorderLayout.SOUTH);
    
            notificationFrame = new JFrame("Notification");
            notificationFrame.getContentPane().add(notificationPanel);
            notificationFrame.pack(); // Size the window according to the elements inside
            notificationFrame.setLocationRelativeTo(null);
            notificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            notificationFrame.setVisible(true);
    
            // Set the flag to show that the notification window is open
            notificationShowing = true;
        }
    } 
    
    /*
     * Construct the game and set it running.
     */

    public Game() {
        // create a frame to contain the game
        JFrame container = new JFrame("Space Invaders");

        // at the moment of the game's creation, initialize the background music (from a sound folder).
        backgroundMusic = audioStore.getAudio("sound/background.wav");
        backgroundMusic.loop(); // the music keeps playing (loop)

        // obtain the content of the frame and set up the game's resolution
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(null);

        // set up the canvas size and put it into the content of the frame
        setBounds(0, 0, 800, 600);
        panel.add(this);

        // tell AWT not to repaint since we do that in accelerated mode
        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // add a listener to exit the game when the user closes the window
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // add a key input system (defined below) to the canvas, so we can respond to key pressed
        KeyInputHandler keyInputHandler = new KeyInputHandler();

        this.addKeyListener(keyInputHandler);

        this.setFocusable(true);

        // request the focus so key events come to us
        requestFocus();

        // create the buffering strategy which will allow AWT to manage the accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialise the entities in the game so the player can see something at startup
        initEntities();
    }

    /*
     * Start a fresh game, this should clear out any old data and create a new set.
     */

    public void startGame() {

        entities.clear();
        initEntities();

        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        firePressed = false;
    }

    /*
     * Initialise the starting state of the entities (ship and aliens). 
     * Each entity will be added to the overall list of entities in the game.
     */

    public void initEntities() {
        // create the player ship and place it in the center of the screen
        ship = new ShipEntity(this, "sprites/ship.gif", 370, 550);
        entities.add(ship);

        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        alienCount = 0;
        for (int row = 0; row < 5; row++) {
            for (int x = 0; x < 12; x++) {
                Entity alien = new AlienEntity(this, "sprites/alien.gif",
                    100 + (x * 50), (50) + row * 30);
                entities.add(alien);
                alienCount++;
            }
        }
    }

    /*
     * Initialise the boss entity.
     */

    public void initBoss() {
        // create the boss entity and place it at the top of the screen
        boss = new BossEntity(this, "sprites/boss.gif", 370, 50);
        entities.add(boss);
    }

    /*
     * Notification from a game entity that the logic of the game should be run at the next opportunity (normally as a result of some game event)
     */
    public void updateLogic() {
        logicRequiredThisLoop = true;
    }

    /*
     * Remove an entity from the game. 
     * The entity removed will no longer move or be drawn.
     */
    public void removeEntity(Entity entity) { // the parameter entity is the entity that should be removed
        removeList.add(entity);
    }

    /*
     * Notification that the player has died.
     */
    public void notifyDeath() {
        waitingForKeyPress = true;
        message = "";
        backgroundMusic.stop();
        Sprite loseSprite = SpriteStore.get().getSprite("sprites/lose.jpg"); // an image will be shown
        showNotificationPanel(loseSprite);
    }    
    
    /*
     * Notification that the player has won since all the aliens and the boss have died.
     */
    public void notifyWin() {
        waitingForKeyPress = true;
        message = "";
        backgroundMusic.stop();
        Sprite winSprite = SpriteStore.get().getSprite("sprites/win.jpg"); // an image will be shown
        showNotificationPanel(winSprite);
    }
    
    /*
     * Close the notification panel
     */
    public void closeNotificationPanel() {
        if (notificationFrame != null) {
            notificationFrame.dispose();
            notificationShowing = false;
        }
    }

    /*
     * Notification that an alien has been killed
     */
    public void notifyAlienKilled() {
        // reduce the alien count, if there are none left, the player has won the level
        alienCount--;

        // when all the aliens are removed, the boss shows up
        if (alienCount == 0 && boss == null) {
            initBoss();
            boss.fire();
        }

        // if there are still some aliens left then they all need to get faster. 
        for (Entity entity : entities) {
            if (entity instanceof AlienEntity) {
                // speed up by 2%
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
            }
        }
    }

    /*
     * Attempt to fire a shot from the player. 
     * Its called "try" since we must first check that the player can fire. 
     * In order to fire, the player has to wait long enough between shots.
     */
    public void tryToFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if the player waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this, "sprites/shot.gif",
            ship.getX() + 10, ship.getY() - 30);
        entities.add(shot);
    }

    /*
     * The main game loop. This loop is running during all game play as it is responsible for the following activities:
     * - Working out the speed of the game loop to update moves
     * - Moving the game entities
     * - Drawing the screen contents (entities, text)
     * - Updating game events
     * - Checking Input
     */
    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        // keep looping round until the game ends
        while (gameRunning) {
            // work out how long it has been since the last update.
            // This will be used to calculate how far the entities should move this loop
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // Get hold of a graphics context for the accelerated surface and blank it out
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 800, 600);

            // cycle round asking each entity to move itself
            if (!waitingForKeyPress) {
                for (Entity entity : entities) {
                    entity.move(delta);
                }
            }

            // cycle round drawing all the entities we have in the game
            for (Entity entity : entities) {
                entity.draw(g);
            }

            // brute force collisions, compare every entity against every other entity.
            // If any of them collide notify both entities that the collision has occurred
            for (int p = 0; p < entities.size(); p++) {
                for (int s = p + 1; s < entities.size(); s++) {
                    Entity me = entities.get(p);
                    Entity him = entities.get(s);

                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    }

                    // Check collision between shots fired by the boss and other entities
                    if ((me instanceof ShotEntity && him instanceof BossEntity) ||
                        (him instanceof ShotEntity && me instanceof BossEntity)) {
                        if (me.collidesWith(him)) {
                            me.collidedWith(him);
                            him.collidedWith(me);
                        }
                    }
                }
            }

            // remove any entity that has been marked for clear up
            entities.removeAll(removeList);
            removeList.clear();

            // if a game event has indicated that game logic should be resolved, cycle round every entity requesting that their personal logic should be considered.
            if (logicRequiredThisLoop) {
                for (Entity entity : entities) {
                    entity.doLogic();
                }

                logicRequiredThisLoop = false;
            }

            // if we are waiting for an "any key" press then draw the current message
            if (waitingForKeyPress) {
                g.setColor(Color.white);
                g.drawString(message,
                    (800 - g.getFontMetrics().stringWidth(message)) / 2,
                    250);
            }

            // finally, we have completed drawing so clear up the graphics and flip the buffer over
            g.dispose();
            strategy.show();

            // resolve the movement of the ship. 
            // First assume the ship is not moving. If either cursor key is pressed then update the movement appropriately
            ship.setHorizontalMovement(0);

            if ((leftPressed) && (!rightPressed)) {
                ship.setHorizontalMovement(-moveSpeed);
            } else if ((rightPressed) && (!leftPressed)) {
                ship.setHorizontalMovement(moveSpeed);
            }

            // if we are pressing fire, attempt to fire
            if (firePressed) {
                tryToFire();
            }

            // finally pause for a bit. Note: this should run us at about
            // 100 fps but on windows this might vary each loop due to
            // a bad implementation of timer
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Thread.interrupted();
                return;
            }
        }
    }

    /*
     * A class to handle keyboard input from the user. The class handles both
     * dynamic input during game play, i.e. left/right and shoot, and more
     * static type input (i.e. press any key to continue).
     */
    public class KeyInputHandler extends KeyAdapter {
       /*
        * The number of key presses we have had while waiting for an "any key" press
        */
        public int pressCount = 1;

       /*
        * Notification from AWT that a key has been pressed. Note that a key
        * being pressed is equal to being pushed down but not released. That's where keyTyped() comes in.
        * The parameter e is the details of the key that was pressed.
        */
        public void keyPressed(KeyEvent e) {
           // if we are waiting for an "any key" typed then we do not want to do anything with just a "press"
            if (waitingForKeyPress) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
                laserSound = audioStore.getAudio("sound/shoot.wav");
                laserSound.play();
            }
        }

       /*
        * Notification from AWT that a key has been released.
        * The parameter e is the details of the key that was released.
        */
        public void keyReleased(KeyEvent e) {
            // if we're waiting for an "any key" typed then we do not want to do anything with just a "released"
            if (waitingForKeyPress) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
        }

       /*
        * Notification from AWT that a key has been typed. 
        * Note that typing a key means to both press and then release it.
        * The parameter e is the details of the key that was typed.
        */
        public void keyTyped(KeyEvent e) {
            // if we're waiting for an "any key" type then check if we have received any recently. We may have had a keyType() event from
            // the user releasing the shoot or move keys, hence the use of the "pressCount" counter.
            if (waitingForKeyPress) {
                if (pressCount == 1) {
                    // since we have now received our key typed event we can mark it as such and start our new game
                    waitingForKeyPress = false;
                    startGame();
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }
          // if we hit escape, then quit the game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

    /*
     * The entry point into the game. 
     * We will simply create an instance of class which will start the display and game loop.
     */

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game();
            }
        });

    }
}

