package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * The main hook of our game. This class with both act as a manager for the
 * display and central mediator for the game logic.
 * Display management will consist of a loop that cycles round all entities
 * in the game asking them to move and then drawing them in the appropriate
 * place. With the help of an inner class it will also allow the player to
 * control the main ship.
 */
public class Game extends Canvas {
    public BufferStrategy strategy;  // The strategy that allows us to use accelerate page flipping
    public boolean gameRunning = true;

    public List<Entity> entities = new ArrayList<Entity>();
    public List<Entity> removeList = new ArrayList<Entity>();

    public Entity ship;  // The entity representing the player
    public double moveSpeed = 300;

    public long lastFire = 0;  // The time at which last fired a shot
    public long firingInterval = 50;

    public int alienCount;
    public BossEntity boss;

    public String Startmessage = "Press any key to start";
    public String Deathmessage = "Oh no! Ti hanno preso!";
    public String Winmessage = "Bravo! hai vinto";
    public boolean waitingForKeyPress = true;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean firePressed = false;
    public boolean logicRequiredThisLoop = false;

    public AudioStore audioStore = AudioStore.get();

    public boolean notificationShowing = false;
    public JFrame notificationFrame;
    public void showNotificationPanel(String message) {
        if (!notificationShowing) {
            JPanel notificationPanel = new JPanel();
            notificationPanel.setLayout(new BorderLayout());
    
            JLabel messageLabel = new JLabel(message);
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
            notificationPanel.add(messageLabel, BorderLayout.CENTER);
    
            JButton closeButton = new JButton("Chiudi il gioco");
            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // Chiude l'applicazione
                }
            });
            notificationPanel.add(closeButton, BorderLayout.SOUTH);
    
            notificationFrame = new JFrame("Notifica");
            notificationFrame.getContentPane().add(notificationPanel);
            notificationFrame.setSize(300, 150);
            notificationFrame.setLocationRelativeTo(null);
            notificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            notificationFrame.setVisible(true);
            
            // Imposta il flag per indicare che la finestra di notifica è aperta
            notificationShowing = true;
        }
    }

    /**
     * Construct our game and set it running.
     */
    public Game() {
        // create a frame to contain our game
        JFrame container = new JFrame("Space Invaders");

        // At the moment of the game's creation, we initialize the sounds.
        AudioClip backgroundMusic = audioStore.getAudio("sound/background.wav");
        backgroundMusic.loop();

        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(null);

        // set up our canvas size and put it into the content of the frame
        setBounds(0, 0, 800, 600);
        panel.add(this);

        // Tell AWT not to repaint since we do that our self in accelerated mode
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

        // add a key input system (defined below) to our canvas, so we can
        // respond to key pressed
        KeyInputHandler keyInputHandler = new KeyInputHandler();

        this.addKeyListener(keyInputHandler);

        this.setFocusable(true);
        //addKeyListener(new KeyInputHandler());

        // request the focus so key events come to us
        requestFocus();

        // create the buffering strategy which will allow AWT to manage our
        // accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialise the entities in our game so there's something to see
        // at startup
        initEntities();
    }

    /**
     * Start a fresh game, this should clear out any old data and
     * create a new set.
     */
    public void startGame() {

        // clear out any existing entities and initialise a new set
        entities.clear();
        initEntities();

        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        firePressed = false;
    }

    /**
     * Initialise the starting state of the entities (ship and aliens). Each
     * entity will be added to the overall list of entities in the game.
     */
    public void initEntities() {
        // create the player ship and place it roughly in the center of the screen
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

    /**
     * Initialise the boss entity.
     */
    public void initBoss() {
        // create the boss ship and place it at the top of the screen
        boss = new BossEntity(this, "sprites/enemy_2.gif", 370, 50);
        entities.add(boss);
    }

    /**
     * Notification from a game entity that the logic of the game
     * should be run at the next opportunity (normally as a result of some
     * game event)
     */
    public void updateLogic() {
        logicRequiredThisLoop = true;
    }

    /**
     * Remove an entity from the game. The entity removed will
     * no longer move or be drawn.
     *
     * @param entity The entity that should be removed
     */
    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    /**
     * Notification that the player has died.
     */
    public void notifyDeath() {
        waitingForKeyPress = true;
        Startmessage = "";
        showNotificationPanel(Deathmessage);
    }
    
    /**
     * Notification that the player has won since all the aliens are dead.
     */
    public void notifyWin() {
        waitingForKeyPress = true;
        Startmessage = "";
        showNotificationPanel(Winmessage);
    }
    
    public void closeNotificationPanel() {
        if (notificationFrame != null) {
            notificationFrame.dispose();
            notificationShowing = false;
        }
    }

    /**
     * Notification that an alien has been killed
     */
    public void notifyAlienKilled() {
        // reduce the alien count, if there are none left, the player has won!
        alienCount--;

        if (alienCount == 0 && boss == null) {
            initBoss();
            boss.fire();
        }

        // if there are still some aliens left then they all need to get faster, so
        // speed up all the existing aliens
        for (Entity entity : entities) {
            if (entity instanceof AlienEntity) {
                // speed up by 2%
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
            }
        }
    }

    /**
     * Attempt to fire a shot from the player. Its called "try"
     * since we must first check that the player can fire at this
     * point, i.e. has he/she waited long enough between shots
     */
    public void tryToFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this, "sprites/shot.gif",
            ship.getX() + 10, ship.getY() - 30);
        entities.add(shot);
    }

    /**
     * The main game loop. This loop is running during all game
     * play as is responsible for the following activities:
     * <p/>
     * - Working out the speed of the game loop to update moves
     * - Moving the game entities
     * - Drawing the screen contents (entities, text)
     * - Updating game events
     * - Checking Input
     * <p/>
     */
    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        // keep looping round til the game ends
        while (gameRunning) {
            // work out how long it has been since the last update, this will be
            // used to calculate how far the entities should move this loop
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // Get hold of a graphics context for the accelerated
            // surface and blank it out
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

            // brute force collisions, compare every entity against every
            // other entity. If any of them collide notify both entities that
            // the collision has occurred
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

            // if a game event has indicated that game logic should be
            // resolved, cycle round every entity requesting that their
            // personal logic should be considered.
            if (logicRequiredThisLoop) {
                for (Entity entity : entities) {
                    entity.doLogic();
                }

                logicRequiredThisLoop = false;
            }

            // if we're waiting for an "any key" press then draw the current
            // message
            if (waitingForKeyPress) {
                g.setColor(Color.white);
                g.drawString(Startmessage,
                    (800 - g.getFontMetrics().stringWidth(Startmessage)) / 2,
                    250);
            }

            // finally, we've completed drawing so clear up the graphics and
            // flip the buffer over
            g.dispose();
            strategy.show();

            // resolve the movement of the ship. First assume the ship isn't
            // moving. If either cursor key is pressed then update the
            // movement appropriately
            ship.setHorizontalMovement(0);

            if ((leftPressed) && (!rightPressed)) {
                ship.setHorizontalMovement(-moveSpeed);
            } else if ((rightPressed) && (!leftPressed)) {
                ship.setHorizontalMovement(moveSpeed);
            }

            // if we're pressing fire, attempt to fire
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

    /**
     * A class to handle keyboard input from the user. The class handles both
     * dynamic input during game play, i.e. left/right and shoot, and more
     * static type input (i.e. press any key to continue)
     * <p/>
     * This has been implemented as an inner class more through habit than
     * anything else. Its perfectly normal to implement this as separate
     * class if slight less convenient.
     */
    public class KeyInputHandler extends KeyAdapter {
//        /**
//         * The number of key presses we've had while waiting for an "any key" press
//         */
        public int pressCount = 1;
//
//        /**
//         * Notification from AWT that a key has been pressed. Note that a key
//         * being pressed is equal to being pushed down but *NOT*
//         * released. That's where keyTyped() comes in.
//         *
//         * @param e The details of the key that was pressed
//         */
        public void keyPressed(KeyEvent e) {
//            // if we're waiting for an "any key" typed then we don't
//            // want to do anything with just a "press"
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
                AudioClip laserSound = audioStore.getAudio("sound/shoot.wav");
                laserSound.play();
            }
        }
//
//        /**
//         * Notification from AWT that a key has been released.
//         *
//         * @param e The details of the key that was released
//         */
        public void keyReleased(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't
            // want to do anything with just a "released"
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

//        /**
//         * Notification from AWT that a key has been typed. Note that typing
//         * a key means to both press and then release it.
//         *
//         * @param e The details of the key that was typed.
//         */
        public void keyTyped(KeyEvent e) {
            // if we're waiting for an "any key" type then check if we've
            // received any recently. We may have had a keyType() event from
            // the user releasing the shoot or move keys, hence the use of
            // the "pressCount" counter.
            if (waitingForKeyPress) {
                if (pressCount == 1) {
                    // since we've now received our key typed
                    // event we can mark it as such and start
                    // our new game
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

    /**
     * The entry point into the game. We'll simply create an instance of class
     * which will start the display and game loop.
     *
     */

    public static void main(String[] args) {

        // Start the main game loop, note: this method will not return until
        // the game has finished running. Hence, we are using the actual main
        // thread to run the game.

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game();
            }
        });

    }
}

