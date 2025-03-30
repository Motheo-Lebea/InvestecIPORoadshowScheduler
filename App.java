import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class App {
    private static final RoadshowScheduler scheduler = new RoadshowScheduler();
    private static final DefaultListModel<String> scheduleListModel = new DefaultListModel<>();
    private static final JList<String> scheduleList = new JList<>(scheduleListModel);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Investec IPO Roadshow Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 221, 222));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Investce IPO Roadshow Scheduler");

        titleLabel.setFont(new Font("SF Pro", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Schedule Presentation Tab
        JPanel schedulePanel = createSchedulePanel();
        tabbedPane.addTab("Schedule Presentation", schedulePanel);

        // View Schedule Tab
        JPanel viewPanel = createViewPanel();
        tabbedPane.addTab("View Schedule", viewPanel);

        // Check Availability Tab
        JPanel availabilityPanel = createAvailabilityPanel();
        tabbedPane.addTab("Check Availability", availabilityPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields
        JTextField companyField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField dateField = new JTextField(10);
        JTextField startTimeField = new JTextField(5);
        JTextField endTimeField = new JTextField(5);

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Company Name:"), gbc);
        gbc.gridx = 1;
        panel.add(companyField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Start Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        panel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("End Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        panel.add(endTimeField, gbc);

        // Submit button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Schedule Presentation");
        submitButton.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime startTime = LocalTime.parse(startTimeField.getText());
                LocalTime endTime = LocalTime.parse(endTimeField.getText());

                Presentation presentation = new Presentation(
                        companyField.getText(),
                        locationField.getText(),
                        date,
                        startTime,
                        endTime
                );

                scheduler.addPresentation(presentation);
                JOptionPane.showMessageDialog(panel, "Presentation scheduled successfully!");
                updateScheduleList();

                // Clear fields
                companyField.setText("");
                locationField.setText("");
                dateField.setText("");
                startTimeField.setText("");
                endTimeField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(submitButton, gbc);

        return panel;
    }

    private static JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Schedule list
        updateScheduleList();
        scheduleList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(scheduleList);

        // Filter controls
        JPanel filterPanel = new JPanel();
        JTextField locationFilter = new JTextField(15);
        JButton filterButton = new JButton("Filter by Location");
        filterButton.addActionListener(e -> {
            String location = locationFilter.getText();
            if (location.isEmpty()) {
                updateScheduleList();
            } else {
                List<Presentation> filtered = scheduler.getPresentationsByLocation(location);
                scheduleListModel.clear();
                filtered.forEach(p -> scheduleListModel.addElement(p.toString()));
            }
        });

        filterPanel.add(new JLabel("Filter by Location:"));
        filterPanel.add(locationFilter);
        filterPanel.add(filterButton);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createAvailabilityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields
        JTextField locationField = new JTextField(20);
        JTextField dateField = new JTextField(10);
        JTextField startTimeField = new JTextField(5);
        JTextField endTimeField = new JTextField(5);
        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Start Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        panel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("End Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        panel.add(endTimeField, gbc);

        // Check button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton checkButton = new JButton("Check Availability");
        checkButton.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime startTime = LocalTime.parse(startTimeField.getText());
                LocalTime endTime = LocalTime.parse(endTimeField.getText());

                Presentation testPresentation = new Presentation(
                        "Test",
                        locationField.getText(),
                        date,
                        startTime,
                        endTime
                );

                try {
                    scheduler.addPresentation(testPresentation);
                    resultArea.setText("This time slot is available!");
                    scheduler.removePresentation(testPresentation);
                } catch (IllegalArgumentException ex) {
                    resultArea.setText("Time slot not available:\n" + ex.getMessage());
                }
            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        });
        panel.add(checkButton, gbc);

        // Result area
        gbc.gridy++;
        panel.add(new JScrollPane(resultArea), gbc);

        return panel;
    }

    private static void updateScheduleList() {
        scheduleListModel.clear();
        List<Presentation> schedule = scheduler.getSchedule();
        if (schedule.isEmpty()) {
            scheduleListModel.addElement("No presentations scheduled");
        } else {
            schedule.forEach(p -> scheduleListModel.addElement(p.toString()));
        }
    }
}