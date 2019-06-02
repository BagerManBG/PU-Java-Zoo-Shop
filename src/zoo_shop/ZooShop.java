package zoo_shop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZooShop {
    private JTabbedPane tabbed_pane_main;
    private JPanel panel_main;
    private JTextField text_field_species_name;
    private JButton button_add_species;
    private JScrollPane scroll_pane_species_list;

    public ZooShop() {
        button_add_species.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ZooShop.this.text_field_species_name.getText();
                JOptionPane.showMessageDialog(new JFrame("ZooShop"), name);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ZooShop");
        frame.setContentPane(new ZooShop().panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 800);

        frame.pack();
        frame.setVisible(true);
    }
}
