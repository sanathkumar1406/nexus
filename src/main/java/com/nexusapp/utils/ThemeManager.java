package com.nexusapp.utils;

import javafx.scene.Scene;

public class ThemeManager {

    private static final String LIGHT_THEME = "/css/light.css";
    private static final String DARK_THEME = "/css/dark.css";

    private static boolean isDarkMode = false;

    /**
     * Apply the current theme to the given scene.
     *
     * @param scene the JavaFX Scene
     */
    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        if (isDarkMode) {
            scene.getStylesheets().add(ThemeManager.class.getResource(DARK_THEME).toExternalForm());
        } else {
            scene.getStylesheets().add(ThemeManager.class.getResource(LIGHT_THEME).toExternalForm());
        }
    }

    /**
     * Toggle between light and dark mode and apply the theme.
     *
     * @param scene the JavaFX Scene
     */
    public static void toggleTheme(Scene scene) {
        isDarkMode = !isDarkMode;
        applyTheme(scene);
    }

    /**
     * Check if dark mode is currently active.
     *
     * @return true if dark mode is active, false otherwise
     */
    public static boolean isDarkMode() {
        return isDarkMode;
    }

    /**
     * Set the theme explicitly.
     *
     * @param darkMode true for dark mode, false for light mode
     * @param scene    the JavaFX Scene
     */
    public static void setTheme(boolean darkMode, Scene scene) {
        isDarkMode = darkMode;
        applyTheme(scene);
    }
}  
