import javax.swing.*;

public class TestFileChooser {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                System.out.println("Selected file: " + fileChooser.getSelectedFile().getName());
            }
        });
    }
}