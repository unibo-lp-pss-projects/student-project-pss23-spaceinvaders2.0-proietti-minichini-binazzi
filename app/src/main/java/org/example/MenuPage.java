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
//import javax.sound.sampled.*;
// import java.awt.event.WindowAdapter;
// import java.awt.event.WindowEvent;
// import java.awt.event.WindowListener;


public class MenuPage extends JFrame {
    private JLabel contentLabel;
    //public AudioStore audioStore = AudioStore.get();
    // private AudioStore audioStore;
    // private AudioClip backgroundMusic;
    
    public MenuPage() {
        setTitle("Menu Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // set a background image 
        JPanel backgroundPanel = new JPanel(){
            private Image backgroundImage;
            {
            try{
                backgroundImage = ImageIO.read(getClass().getResourceAsStream("/sprites/bgimage.png")); // loads the image to set as a background
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected void paintComponent(Graphics g){ // is responsible for painting the component on the screen. It takes a Graphics object g as a parameter, which represents the graphics context for painting.
            super.paintComponent(g); //this calls the paintComponent method of the superclass, which ensures that any default painting behavior provided by the superclass is executed
            if (backgroundImage != null){ // this condition checks if there's a non-null backgroundImage object available
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); 
                // if a background image is available, this line draws the image onto the component's graphics context (g). 
                // it draws the image starting from coordinates (0, 0) and scales it to fit the size of the component, which is determined by getWidth() and getHeight().
            }
        }
                
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setPreferredSize(new Dimension(800,600)); // set the same size of the JFrame

        // Load the audio store
        // audioStore = AudioStore.get();

        // // Get the background music audio clip
        // backgroundMusic = audioStore.getAudio("");
        // backgroundMusic.loop();

        // // Add a window listener to stop the audio when the window is closed
        // addWindowListener(new WindowAdapter() {
        //     @Override
        //     public void windowClosing(WindowEvent e) {
        //         stopBackgroundMusic();
        //     }
        // });

        // create a panel containing the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(300, 200));

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
                dispose();
                // start the game
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
        buttonPanel.add(Box.createVerticalStrut(10)); // it creates an invisible component that acts as a spacer with a specified height (here is set to 10).
        buttonPanel.add(exit);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(start);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(contentLabel);

        buttonPanel.setOpaque(false); // make the panel transparent, allowing the background image to be visible.  

        // set a maximum value of height of the buttons
        commands.setMaximumSize(new Dimension(Integer.MAX_VALUE,10));
        exit.setMaximumSize(new Dimension(Integer.MAX_VALUE,10));
        start.setMaximumSize(new Dimension(Integer.MAX_VALUE,10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1; // set the grid position to row 1 (bottom row)
        gbc.insets = new Insets(80, 0, 0, 0); 
        gbc.anchor = GridBagConstraints.PAGE_END;
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.add(buttonPanel, gbc);

        setVisible(true);
    }

    // private void stopBackgroundMusic() {
    //     if (backgroundMusic != null) {
    //         backgroundMusic.close();
    //     }
    // }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuPage();
            }
        });
    }
}