package com.fiza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class WeatherApp extends JFrame {
    private JComboBox<String> comboBox;
    private Map<String, City> detailsMap;
    private JTable detailsTable;

    public WeatherApp() {
        setTitle("Weather App");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        CityList cityList = new CityList("city.list.json");
        detailsMap = cityList.cities;

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Weather App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Creating combo box with search functionality
        comboBox = new JComboBox<>(detailsMap.keySet().toArray(new String[0]));
        comboBox.setEditable(true);
        comboBox.setPreferredSize(new Dimension(300, 30));
        comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchString = ((JTextField) e.getSource()).getText();
                updateComboBox(searchString);
            }
        });
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDetailsTable((String) comboBox.getSelectedItem());
            }
        });

        // Creating table model with initial data
        DefaultTableModel tableModel = new DefaultTableModel(new String[] { "Parameter", "Value" }, 0);
        detailsTable = new JTable(tableModel);
        detailsTable.setRowHeight(30);

        // Creating scroll pane to hold the table
        JScrollPane scrollPane = new JScrollPane(detailsTable);
        scrollPane.setPreferredSize(new Dimension(600, 250));

        // Creating panel to hold combo box and scroll pane
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(comboBox, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(panel, BorderLayout.CENTER);

        add(contentPanel);
        setVisible(true);
    }

    private void updateComboBox(String searchString) {
        comboBox.removeAllItems();
        for (String option : detailsMap.keySet()) {
            if (option.toLowerCase().contains(searchString.toLowerCase())) {
                comboBox.addItem(option);
            }
        }
        comboBox.setSelectedItem(searchString); // Keep the entered text highlighted
        comboBox.setPopupVisible(true); // Show the popup
    }

    private void updateDetailsTable(String selectedOption) {
        City city = detailsMap.get(selectedOption);
        DefaultTableModel model = (DefaultTableModel) detailsTable.getModel();
        model.setRowCount(0); // Clear existing rows
        Map<String, String> details = city != null ? getAPIResponse(city.getId()) : null;
        if (details != null) {
            model.addRow(new Object[] { "Temperature", details.get("temperature") });
            model.addRow(new Object[] { "Minimum Temperature", details.get("temp_min") });
            model.addRow(new Object[] { "Humidity", details.get("humidity") });
            model.addRow(new Object[] { "Pressure", details.get("pressure") });
            model.addRow(new Object[] { "Feels Like", details.get("feels_like") });
            model.addRow(new Object[] { "Maximum Temperature", details.get("temp_max") });
            model.addRow(new Object[] { "Wind Speed", details.get("wind_speed") });
            model.addRow(new Object[] { "Sunset", details.get("sunset") });
            model.addRow(new Object[] { "Sunrise", details.get("sunrise") });
            model.addRow(new Object[] { "Date Time", details.get("datetime") });
        } else {
            model.addRow(new Object[] { "No details available", "" });
        }
    }

    private Map<String, String> getAPIResponse(Integer cityId) {
        // Replace {city id} and {API key} with your actual values
        try {
            APIResponse apiResponse = new APIResponse(cityId);
            return apiResponse.getApiResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("Parameter", "Value");
        }
    }
}
