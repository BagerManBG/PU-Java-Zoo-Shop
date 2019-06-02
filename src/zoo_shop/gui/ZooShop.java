package zoo_shop.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;

import zoo_shop.database.models.*;

public class ZooShop {
    private JTabbedPane tabbed_pane_main;
    private JPanel panel_main;
    private JTextField text_field_species_name;
    private JButton button_add_species;
    private JScrollPane scroll_pane_species_list;
    private JTextField text_field_animals_name;
    private JTextField text_field_animals_age;
    private JTextField text_field_animals_price;
    private JComboBox combo_box_animals_species;
    private JButton button_add_animals;
    private JScrollPane scroll_pane_animals_list;
    private JScrollPane scroll_pane_clients_list;
    private JTextField text_field_clients_name;
    private JTextField text_field_clients_email;
    private JTextField text_field_clients_phone;
    private JButton button_add_clients;
    private JScrollPane scroll_pane_adoptions_list;
    private JComboBox combo_box_adoptions_client;
    private JComboBox combo_box_adoptions_animal;
    private JButton button_add_adoptions;

    public ZooShop() {
        // On Tab Change Listener
        tabbed_pane_main.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //JOptionPane.showMessageDialog(new JFrame("ZooShop"), "Hallo");
            }
        });

        // On Add Specie Button Click
        button_add_species.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ZooShop.this.text_field_species_name.getText();
                ZooShop.this.text_field_species_name.setText("");
                Specie.create(name);
            }
        });

        // On Add Animal Button Click
        button_add_animals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // On Add Client Button Click
        button_add_clients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // On Add Adoption Button Click
        button_add_adoptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ZooShop");
        frame.setContentPane(new ZooShop().panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(1000, 800);

        frame.pack();
        frame.setVisible(true);
    }
}
