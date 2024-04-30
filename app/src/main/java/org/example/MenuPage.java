package org.example;
/*
 * This is the Menu page. The Menu is the first element to open when running the space invaders game.
 * In the Menu we have multiple buttons, each one with a specific function.
 * The first one shows the commands the player needs to follow to play the game correctly.
 * The second one simply closes the page if the player doesn't want to play anymore.
 * The last one starts the game itself.
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;


public class MenuPage extends JFrame {
    public JLabel contentLabel;
    public AudioStore audioStore = AudioStore.get();
    public AudioClip menuMusic;
    
    public MenuPage() {
        /*
         * Create the Menu frame
         */
        setTitle("Menu Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        /*
         * Set a background image and music (sound)
         */
        JPanel backgroundPanel = new JPanel(){
            private Image backgroundImage;
            {
            try{
                backgroundImage = ImageIO.read(getClass().getResourceAsStream("/sprites/bgimage.png")); // loads the image
                menuMusic = audioStore.getAudio("sound/menuMusic.wav"); // loads the audio 
                menuMusic.loop(); // the music continues until the frame is closed
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        /*
         * Customize the painting behavior of the background panel by drawing a background image, if available.
         * It takes a Graphics object g as a parameter, which represents the graphics context for painting.
         */
        @Override
        protected void paintComponent(Graphics g){
            // Calls the paintComponent method of the superclass, which ensures that any default painting behavior provided by the superclass is executed
            super.paintComponent(g); 
            // Check if a background image is available
            if (backgroundImage != null){
                // Draw the background image onto the panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); 
            }
        }
                
        };

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setPreferredSize(new Dimension(800,600));

        /*
         * Create the three buttons contained in the menu.
         * First, the button panel is created.
         */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(300, 200));

        /*
         * Create a new label that displays the game commands for the player
         */
        contentLabel = new JLabel("<html><body>Press space bar to shoot and arrows to move</body></html>");
        contentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentLabel.setVisible(false); // initially is not visible
        
        contentLabel.setPreferredSize(new Dimension(200, 50));
        contentLabel.setFont(contentLabel.getFont().deriveFont(Font.BOLD, 14));
        contentLabel.setForeground(Color.WHITE);
        contentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentLabel.setVerticalAlignment(SwingConstants.CENTER);

        buttonPanel.add(contentLabel);

        // commands button, it shows the commands used to play the game
        JButton commands = new JButton("Commands");
        commands.addActionListener(new ActionListener() {
            // variable to track the visibility of the HTML content
            boolean contentVisible = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                // toggle visibility of HTML content when button is pressed
                contentVisible = !contentVisible;
                contentLabel.setVisible(contentVisible);
                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        });
        commands.setAlignmentX(Component.CENTER_ALIGNMENT);
        commands.setBorder(BorderFactory.createRaisedBevelBorder());
        commands.setBackground(Color.WHITE);
        commands.setForeground(Color.BLACK);
        commands.setFont(commands.getFont().deriveFont(Font.BOLD,16));
        commands.setPreferredSize(new Dimension(200, 50));

        // exit button, it closes the menu page
        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // closes the menu page
            }
        });
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setBorder(BorderFactory.createRaisedBevelBorder());
        exit.setBackground(Color.WHITE);
        exit.setForeground(Color.BLACK);
        exit.setFont(commands.getFont().deriveFont(Font.BOLD,16));
        exit.setPreferredSize(new Dimension(200, 50));

        // start button, it opens the game
        JButton start = new JButton("Start Game");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // close the menu page
                dispose();
                /*
                 * Start the game. Firstly, the background music stops and then the game window opens
                 */
                
                menuMusic.stop();
                Game game = new Game();
                game.startGame();
                // start the game loop
                new Thread(() -> {
                    game.gameLoop();
                }).start();
            }
        });
        
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setBorder(BorderFactory.createRaisedBevelBorder());
        start.setBackground(Color.WHITE);
        start.setForeground(Color.BLACK);
        start.setFont(commands.getFont().deriveFont(Font.BOLD,16));
        start.setPreferredSize(new Dimension(200, 50));

        add(backgroundPanel);

        buttonPanel.add(Box.createVerticalGlue()); // adds a vertical glue component to the buttonPanel. Vertical glue will cause the components to be spaced out vertically, pushing them to the top and bottom edges of the container.
        buttonPanel.add(commands);

        // Create an invisible component that acts as a spacer with a specified height
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(exit);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(start);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(contentLabel);

        // Make the panel transparent, allowing the background image to be visible.  
        buttonPanel.setOpaque(false); 

        // Set a maximum value of height of the buttons
        commands.setMaximumSize(new Dimension(Integer.MAX_VALUE,10));
        exit.setMaximumSize(new Dimension(Integer.MAX_VALUE,10));
        start.setMaximumSize(new Dimension(Integer.MAX_VALUE,10));

        /*
         * Create a new instance of GridBagConstraints. This object is used to specify constraints for how components are placed within a GridBagLayout.
         */
        GridBagConstraints gbc = new GridBagConstraints();
        /*
         * The gridx and gridy properties determine the row and column indices where the component will be placed within the grid. 
         * Here, "gridx" is set to 0, indicating the first column, and "gridy" is set to 1, indicating the second row (bottom row).
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        // The insets property specifies the external padding (top, left, bottom, right) around the component.
        gbc.insets = new Insets(80, 0, 0, 0); 
        // The anchor property specifies where the component should be positioned within its display area if it doesn't fill the entire space allocated to it. In this case, the component is allocated to the bottom of its display area.
        gbc.anchor = GridBagConstraints.PAGE_END;
        // The buttonPanel is added to the backgroundPanel using the specified GridBagConstraints
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.add(buttonPanel, gbc);

        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuPage();
            }
        });
    }
}