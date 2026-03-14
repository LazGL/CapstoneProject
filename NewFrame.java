import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NewFrame extends JFrame {

    // --- Card names ---
    private static final String SIGNIN  = "signin";
    private static final String SIGNUP  = "signup";
    private static final String MENU    = "menu";
    private static final String SETUP   = "setup";
    private static final String RACE    = "race";
    private static final String HISTORY = "history";

    // --- Layout ---
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // --- State ---
    private String userName = "";
    private Thread raceThread;
    private final Chrono chrono = new Chrono();
    private final RaceHistory raceHistory = new RaceHistory();

    // --- Live race labels (updated from background thread) ---
    private JLabel labelBpm;
    private JLabel labelAvg;
    private JLabel labelDistance;
    private JLabel labelSong;
    private JLabel labelSongBpm;

    // --- Past races table ---
    private DefaultTableModel tableModel;

    // --- Setup input ---
    private JTextField distanceField;

    public NewFrame() {
        setTitle("SoundUp");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel.add(buildSignInPanel(),  SIGNIN);
        mainPanel.add(buildSignUpPanel(),  SIGNUP);
        mainPanel.add(buildMenuPanel(),    MENU);
        mainPanel.add(buildSetupPanel(),   SETUP);
        mainPanel.add(buildRacePanel(),    RACE);
        mainPanel.add(buildHistoryPanel(), HISTORY);

        add(mainPanel);
        showPanel(SIGNIN);
        setLocationRelativeTo(null);
    }

    private void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    // -------------------------------------------------------------------------
    // SIGN IN
    // -------------------------------------------------------------------------
    private JPanel buildSignInPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(0xCC, 0xCC, 0xFF));

        JLabel title = label("Welcome to Sound Up", 28, Font.PLAIN);
        title.setBounds(30, 70, 340, 50);

        JLabel nameLbl = label("Enter your name:", 14, Font.PLAIN);
        nameLbl.setBounds(70, 160, 200, 28);

        JTextField nameField = new JTextField();
        nameField.setFont(font(16));
        nameField.setBounds(70, 192, 260, 40);

        JLabel pwdLbl = label("Enter your password:", 14, Font.PLAIN);
        pwdLbl.setBounds(70, 252, 200, 28);

        JPasswordField pwdField = new JPasswordField();
        pwdField.setFont(font(16));
        pwdField.setBounds(70, 284, 250, 40);

        JButton signInBtn = button("Sign In", 22);
        signInBtn.setBounds(110, 350, 160, 52);
        signInBtn.addActionListener(e -> {
            String n = nameField.getText().trim();
            if (n.isEmpty()) { warn("Please enter a username."); return; }
            userName = n;
            nameField.setText("");
            pwdField.setText("");
            showPanel(MENU);
        });

        JLabel accountLbl = label("Don't have an account yet?", 13, Font.PLAIN);
        accountLbl.setBounds(70, 430, 260, 28);

        JButton signUpBtn = button("Sign Up", 22);
        signUpBtn.setBounds(110, 465, 160, 52);
        signUpBtn.addActionListener(e -> showPanel(SIGNUP));

        add(p, title, nameLbl, nameField, pwdLbl, pwdField,
                signInBtn, accountLbl, signUpBtn);
        return p;
    }

    // -------------------------------------------------------------------------
    // SIGN UP
    // -------------------------------------------------------------------------
    private JPanel buildSignUpPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(0xCC, 0xFF, 0xCC));

        JLabel title = label("Create an account", 24, Font.PLAIN);
        title.setBounds(60, 80, 280, 50);

        JLabel nameLbl = label("Enter your name:", 14, Font.PLAIN);
        nameLbl.setBounds(70, 168, 200, 28);

        JTextField nameField = new JTextField();
        nameField.setFont(font(16));
        nameField.setBounds(70, 200, 260, 40);

        JLabel pwdLbl = label("Enter your password:", 14, Font.PLAIN);
        pwdLbl.setBounds(70, 262, 200, 28);

        JPasswordField pwdField = new JPasswordField();
        pwdField.setFont(font(16));
        pwdField.setBounds(70, 294, 250, 40);

        JButton createBtn = button("Create Account", 20);
        createBtn.setBounds(80, 370, 220, 52);
        createBtn.addActionListener(e -> {
            String n = nameField.getText().trim();
            if (n.isEmpty()) { warn("Please enter a username."); return; }
            userName = n;
            nameField.setText("");
            pwdField.setText("");
            showPanel(MENU);
        });

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(145, 445, 90, 35);
        backBtn.addActionListener(e -> showPanel(SIGNIN));

        add(p, title, nameLbl, nameField, pwdLbl, pwdField, createBtn, backBtn);
        return p;
    }

    // -------------------------------------------------------------------------
    // MAIN MENU
    // -------------------------------------------------------------------------
    private JPanel buildMenuPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(0xEE, 0xEE, 0xFF));

        JLabel title = label("Main Menu", 30, Font.BOLD);
        title.setBounds(105, 120, 200, 50);

        JButton newRaceBtn = button("Start a new race", 18);
        newRaceBtn.setBounds(80, 250, 220, 60);
        newRaceBtn.addActionListener(e -> showPanel(SETUP));

        JButton historyBtn = button("See past races", 18);
        historyBtn.setBounds(80, 350, 220, 60);
        historyBtn.addActionListener(e -> { loadHistoryTable(); showPanel(HISTORY); });

        add(p, title, newRaceBtn, historyBtn);
        return p;
    }

    // -------------------------------------------------------------------------
    // RACE SETUP
    // -------------------------------------------------------------------------
    private JPanel buildSetupPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(0xFF, 0xEE, 0xCC));

        JLabel title = label("Start a new race", 24, Font.PLAIN);
        title.setBounds(70, 90, 260, 50);

        JLabel distLbl = label("Distance to run (km):", 14, Font.PLAIN);
        distLbl.setBounds(70, 190, 230, 28);

        distanceField = new JTextField("5");
        distanceField.setFont(font(16));
        distanceField.setBounds(70, 222, 130, 40);

        JButton goBtn = button("Let's go!", 22);
        goBtn.setBounds(110, 330, 160, 55);
        goBtn.addActionListener(e -> startRace());

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(145, 410, 90, 35);
        backBtn.addActionListener(e -> showPanel(MENU));

        add(p, title, distLbl, distanceField, goBtn, backBtn);
        return p;
    }

    // -------------------------------------------------------------------------
    // DURING RACE
    // -------------------------------------------------------------------------
    private JPanel buildRacePanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(0xCC, 0xFF, 0xEE));

        JLabel header = label("Have a good run!", 22, Font.BOLD);
        header.setBounds(75, 60, 250, 42);

        labelBpm = label("BPM: --", 16, Font.PLAIN);
        labelBpm.setBounds(70, 145, 260, 30);

        labelAvg = label("Average BPM: --", 16, Font.PLAIN);
        labelAvg.setBounds(70, 185, 260, 30);

        labelDistance = label("Distance: 0.00 km", 16, Font.PLAIN);
        labelDistance.setBounds(70, 225, 260, 30);

        JLabel songTitle = label("You are listening:", 14, Font.BOLD);
        songTitle.setBounds(70, 295, 260, 26);

        labelSong = label("--", 14, Font.PLAIN);
        labelSong.setBounds(70, 325, 260, 26);

        labelSongBpm = label("Song BPM: --", 14, Font.PLAIN);
        labelSongBpm.setBounds(70, 355, 260, 26);

        JButton endBtn = button("End of the race", 16);
        endBtn.setBounds(95, 460, 190, 52);
        endBtn.addActionListener(e -> stopRace());

        add(p, header, labelBpm, labelAvg, labelDistance,
                songTitle, labelSong, labelSongBpm, endBtn);
        return p;
    }

    // -------------------------------------------------------------------------
    // RACE HISTORY
    // -------------------------------------------------------------------------
    private JPanel buildHistoryPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(0xFF, 0xCC, 0xEE));

        JLabel title = label("Past Races", 32, Font.PLAIN);
        title.setBounds(90, 60, 220, 55);

        String[] cols = {"Distance (km)", "Time (min)", "Speed (km/h)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(font(13));
        table.getTableHeader().setFont(font(13));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 140, 355, 200);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(145, 370, 90, 35);
        backBtn.addActionListener(e -> showPanel(MENU));

        add(p, title, scroll, backBtn);
        return p;
    }

    // -------------------------------------------------------------------------
    // Race control
    // -------------------------------------------------------------------------
    private void startRace() {
        double distanceKm;
        try {
            distanceKm = Double.parseDouble(distanceField.getText().trim());
            if (distanceKm <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            warn("Please enter a valid distance (e.g. 5).");
            return;
        }

        // Reset live labels
        labelBpm.setText("BPM: --");
        labelAvg.setText("Average BPM: --");
        labelDistance.setText("Distance: 0.00 km");
        labelSong.setText("--");
        labelSongBpm.setText("Song BPM: --");

        showPanel(RACE);
        chrono.start();

        User user = new User(userName, 25);
        Playlist playlist = new Playlist();

        raceThread = new Thread(() -> user.updateBpm(distanceKm, playlist, new RaceListener() {
            @Override
            public void onUpdate(int bpm, int avgBpm, double km, Music song) {
                SwingUtilities.invokeLater(() -> {
                    labelBpm.setText("BPM: " + bpm);
                    labelAvg.setText("Average BPM: " + avgBpm);
                    labelDistance.setText(String.format("Distance: %.2f km", km));
                    if (song != null) {
                        labelSong.setText(song.getName() + " — " + song.getAuthor());
                        labelSongBpm.setText("Song BPM: " + song.getBpm());
                    }
                });
            }

            @Override
            public void onFinished(double totalKm, long elapsedSeconds) {
                chrono.stop();
                raceHistory.save(totalKm, elapsedSeconds);
                SwingUtilities.invokeLater(() -> {
                    double mins = elapsedSeconds / 60.0;
                    double speed = mins > 0 ? totalKm / (mins / 60.0) : 0;
                    JOptionPane.showMessageDialog(NewFrame.this, String.format(
                            "Great job, %s!\nDistance: %.2f km\nTime: %.1f min\nSpeed: %.1f km/h",
                            userName, totalKm, mins, speed));
                    showPanel(MENU);
                });
            }
        }));
        raceThread.setDaemon(true);
        raceThread.start();
    }

    private void stopRace() {
        if (raceThread != null) raceThread.interrupt();
        // onFinished callback handles saving and returning to menu
    }

    // -------------------------------------------------------------------------
    // History table
    // -------------------------------------------------------------------------
    private void loadHistoryTable() {
        tableModel.setRowCount(0);
        List<RaceHistory.RaceRecord> records = raceHistory.load();
        for (RaceHistory.RaceRecord r : records) {
            tableModel.addRow(new Object[]{
                    String.format("%.2f", r.distanceKm),
                    String.format("%.1f", r.durationSeconds / 60.0),
                    String.format("%.1f", r.speedKmh)
            });
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private Font font(int size) {
        return new Font("Comic Sans MS", Font.PLAIN, size);
    }

    private JLabel label(String text, int size, int style) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Comic Sans MS", style, size));
        return l;
    }

    private JButton button(String text, int size) {
        JButton b = new JButton(text);
        b.setFont(new Font("Comic Sans MS", Font.PLAIN, size));
        return b;
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    /** Adds multiple components to a panel in one call. */
    private void add(JPanel panel, java.awt.Component... components) {
        for (java.awt.Component c : components) panel.add(c);
    }
}
