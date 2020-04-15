import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.showGui();
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, "Creation failed, " + exc);
        }
    }

    public void showGui() {
        SwingUtilities.invokeLater(() -> {
            JFrame jf = new JFrame("mpp3");
            jf.setPreferredSize(new Dimension(900, 600));
            List<Language> languages = Futil.processDir("training");
            Examiner examiner = new Examiner(languages);
            JTextArea informacje = new JTextArea();
            informacje.setEditable(false);
            JButton test = new JButton("Kliknij, jeżeli chcesz przetestować działanie programu");
            test.addActionListener(e -> {
                String text = JOptionPane.showInputDialog("Podaj swój tekst do testu");
                if (text != null && text != "") {
                    informacje.append("Podany tekst: " + text + "\n");
                    informacje.append(examiner.examin(text) + "\n");
                } else {
                    JOptionPane.showMessageDialog(null, "Podano nie prawidłowy tekst!");
                }
            });
            jf.pack();
            jf.setLayout(new BorderLayout());
            jf.add(informacje,BorderLayout.CENTER);
            jf.add(test,BorderLayout.NORTH);
            jf.setLocationRelativeTo(null);
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jf.setVisible(true);
        });
    }
}
