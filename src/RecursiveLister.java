import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RecursiveLister extends JFrame {

    private JTextArea displayArea;
    private JButton startButton;
    private JButton quitButton;
    private JFileChooser fileChooser;

    public RecursiveLister() {

        setTitle("Recursive Directory Lister");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Recursive File Lister", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        displayArea = new JTextArea(25, 60);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Select Directory & Start");
        quitButton = new JButton("Quit");

        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));


        startButton.addActionListener(e -> selectDirectoryAndList());
        quitButton.addActionListener(e -> System.exit(0));

        pack();
        setLocationRelativeTo(null);
    }

    private void selectDirectoryAndList() {
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            displayArea.setText(""); // Clear previous results
            displayArea.append("--- Starting Traversal of: " + selectedDirectory.getAbsolutePath() + " ---\n\n");

            listFilesRecursively(selectedDirectory, 0);

            displayArea.append("\n--- Traversal Complete ---\n");

        } else if (result == JFileChooser.CANCEL_OPTION) {
            displayArea.append("\nDirectory selection cancelled.\n");
        }
    }

    private void listFilesRecursively(File directory, int level) {

        String indent = "  ".repeat(level);
        File[] contents = directory.listFiles();

        if (contents == null) {
            displayArea.append(indent + "[Error: Could not read contents or directory is empty]\n");
            return;
        }

        for (File item : contents) {
            if (item.isDirectory()) {
                displayArea.append(indent + "[D] " + item.getName() + "\n");
                listFilesRecursively(item, level + 1);
            } else {
                displayArea.append(indent + "[F] " + item.getName() + " (" + item.length() + " bytes)\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecursiveLister().setVisible(true));
    }

}