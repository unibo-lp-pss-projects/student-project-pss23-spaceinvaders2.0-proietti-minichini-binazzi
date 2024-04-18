package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MenuPageTest {
    private MenuPage menuPage;

    @BeforeEach
    void setUp() {
        menuPage = new MenuPage();
    }

    @AfterEach
    void tearDown() {
        menuPage.dispose(); // Closes the menuPage window after each test
    }

    @Test
    void testTitle() {
        assertEquals("Menu Page", menuPage.getTitle());
    }

    @Test
    void testContentLabelVisibility() {
        assertFalse(menuPage.contentLabel.isVisible());
    }

    @Test
    void testExitButtonAction() {
        JButton exitButton = null;
        Component[] components = menuPage.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component innerComponent : panel.getComponents()) {
                    if (innerComponent instanceof JButton && ((JButton) innerComponent).getText().equals("Exit")) {
                        exitButton = (JButton) innerComponent;
                        break;
                    }
                }
                if (exitButton != null) {
                    exitButton.doClick(); // Simulate clicking the "Exit" button
                    assertFalse(menuPage.isVisible()); // Verify that the menuPage window has been closed
                }
            }
        }
    }


    @Test
    void testStartButtonAction() {
        JButton startButton = null;
        Component[] components = menuPage.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JButton && ((JButton) component).getText().equals("Start Game")) {
                startButton = (JButton) component;
                break;
            }    
        }
        if (startButton != null) {
            startButton.doClick(); // Simulate clicking the "Start Game" button
            assertFalse(menuPage.isVisible()); // Verify that the menuPage window has been closed
        }
    }
}
