import javax.swing.SwingUtilities;

public class SoundUp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewFrame().setVisible(true));
    }
}
