package com.fiza;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> new WeatherApp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
