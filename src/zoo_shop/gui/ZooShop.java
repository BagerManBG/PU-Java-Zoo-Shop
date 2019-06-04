package zoo_shop.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import javafx.scene.layout.Border;
import zoo_shop.database.models.*;
import zoo_shop.tools.ComboItem;

public class ZooShop {
    private JTabbedPane tabbed_pane_main;
    private JPanel panel_main;
    private JTextField text_field_species_name;
    private JButton button_add_species;
    private JTextField text_field_animals_name;
    private JTextField text_field_animals_age;
    private JTextField text_field_animals_price;
    private JComboBox combo_box_animals_species;
    private JButton button_add_animals;
    private JTextField text_field_clients_name;
    private JTextField text_field_clients_email;
    private JTextField text_field_clients_phone;
    private JButton button_add_clients;
    private JComboBox combo_box_adoptions_client;
    private JComboBox combo_box_adoptions_animal;
    private JButton button_add_adoptions;
    private JPanel panel_species_list;
    private JPanel panel_animals_list;
    private JPanel panel_clients_list;
    private JPanel panel_adoptions_list;
    private JPanel pane_species_list;
    private JFrame frame;

    public ZooShop(JFrame frame) {
        this.frame = frame;
        this.switchTabSpecies();
        // On Tab Change Listener
        tabbed_pane_main.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer index = tabbed_pane_main.getSelectedIndex();

                switch (index) {
                    case 0:
                        ZooShop.this.switchTabSpecies();
                        break;
                    case 1:
                        ZooShop.this.switchTabAnimals();
                        break;
                    default:
                        break;
                }
//                JOptionPane.showMessageDialog(new JFrame("ZooShop"), index);
            }
        });

        // On Add Specie Button Click
        button_add_species.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ZooShop.this.text_field_species_name.getText();
                ZooShop.this.text_field_species_name.setText("");

                Specie.create(name);
                ZooShop.this.switchTabSpecies();
            }
        });

        // On Add Animal Button Click
        button_add_animals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ZooShop.this.text_field_animals_name.getText();
                ZooShop.this.text_field_animals_name.setText("");

                Integer age = Integer.parseInt(ZooShop.this.text_field_animals_age.getText());
                ZooShop.this.text_field_animals_age.setText("");

                BigDecimal price = new BigDecimal(ZooShop.this.text_field_animals_price.getText());
                ZooShop.this.text_field_animals_price.setText("");

                Object specie_obj = ZooShop.this.combo_box_animals_species.getSelectedItem();
                Integer specie = Integer.parseInt(((ComboItem) specie_obj).getValue());
                ZooShop.this.combo_box_animals_species.setSelectedIndex(0);

                Animal.create(name, age, price, specie);
                ZooShop.this.switchTabAnimals();
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
        frame.setContentPane(new ZooShop(frame).panel_main);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setSize(1200, 800);

        frame.pack();
        frame.setVisible(true);
    }

    // Create the list of species.
    private void switchTabSpecies() {
        Map<Integer, Specie> species = Specie.loadAll();

        this.panel_species_list.removeAll();
        this.panel_species_list.setLayout(new BoxLayout(this.panel_species_list, BoxLayout.Y_AXIS));

        for(Map.Entry<Integer, Specie> row : species.entrySet()) {
            Integer id = row.getKey();
            Specie specie = row.getValue();

            JPanel container = new JPanel();
            JTextField specie_text_field = new JTextField();

            JCheckBox trigger_editable = new JCheckBox("Enable Update");
            JButton update_button = new JButton("Update");
            JButton delete_button = new JButton("Delete");

            update_button.setEnabled(false);

            specie_text_field.setEditable(false);
            specie_text_field.setText(specie.getSpecie());
            specie_text_field.setMaximumSize(new Dimension(255, 30));

            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
            container.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(specie_text_field, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(trigger_editable, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(update_button, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(delete_button, Component.LEFT_ALIGNMENT);

            this.panel_species_list.add(container, Component.LEFT_ALIGNMENT);

            trigger_editable.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    update_button.setEnabled(!update_button.isEnabled());
                    specie_text_field.setEditable(!specie_text_field.isEditable());
                }
            });

            update_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sp_text = specie_text_field.getText();
                    specie.setSpecie(sp_text);
                    specie.save();
                    ZooShop.this.switchTabSpecies();
                }
            });

            delete_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    specie.delete();
                    ZooShop.this.switchTabSpecies();
                }
            });
        }

        this.panel_species_list.revalidate();
        this.panel_species_list.repaint();
    }

    // Create the list of animals.
    private void switchTabAnimals() {
        Map<Integer, Animal> animals = Animal.loadAll();
        Map<Integer, Specie> species = Specie.loadAll();

        this.panel_animals_list.removeAll();
        this.panel_animals_list.setLayout(new BoxLayout(this.panel_animals_list, BoxLayout.Y_AXIS));

        for(Map.Entry<Integer, Specie> row : species.entrySet()) {
            Integer id = row.getKey();
            Specie specie = row.getValue();

            this.combo_box_animals_species.addItem(new ComboItem(specie.getSpecie(), specie.id().toString()));
        }

        for(Map.Entry<Integer, Animal> row : animals.entrySet()) {
            Integer id = row.getKey();
            Animal animal = row.getValue();

            Integer specie_ref_index = null;

            JPanel container = new JPanel();

            JTextField animal_name_text = new JTextField();
            JTextField animal_age_text = new JTextField();
            JTextField animal_price_text = new JTextField();
            JComboBox animal_specie_ref = new JComboBox();

            JCheckBox trigger_editable = new JCheckBox("Enable Update");
            JButton update_button = new JButton("Update");
            JButton delete_button = new JButton("Delete");

            update_button.setEnabled(false);

            animal_name_text.setEditable(false);
            animal_name_text.setText(animal.getName());
            animal_name_text.setMaximumSize(new Dimension(150, 30));

            animal_age_text.setEditable(false);
            animal_age_text.setText(animal.getAge().toString());
            animal_age_text.setMaximumSize(new Dimension(150, 30));

            animal_price_text.setEditable(false);
            animal_price_text.setText(animal.getPrice().toString());
            animal_price_text.setMaximumSize(new Dimension(150, 30));

            for (int i = 0; i < this.combo_box_animals_species.getItemCount(); i++) {
                ComboItem item = (ComboItem) this.combo_box_animals_species.getItemAt(i);
                animal_specie_ref.addItem(item);

                if (Integer.parseInt(item.getValue()) == animal.getSpecie()) {
                    specie_ref_index = i;
                }
            }

            animal_specie_ref.setEditable(false);
            animal_specie_ref.setSelectedIndex(specie_ref_index);
            animal_specie_ref.setMaximumSize(new Dimension(150, 30));

            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
            container.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(animal_name_text, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(animal_age_text, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(animal_price_text, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(animal_specie_ref, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(trigger_editable, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(update_button, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(delete_button, Component.LEFT_ALIGNMENT);

            this.panel_animals_list.add(container, Component.LEFT_ALIGNMENT);

            trigger_editable.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    update_button.setEnabled(!update_button.isEnabled());
                    animal_name_text.setEditable(!animal_name_text.isEditable());
                    animal_age_text.setEditable(!animal_age_text.isEditable());
                    animal_price_text.setEditable(!animal_price_text.isEditable());
                    animal_specie_ref.setEditable(!animal_specie_ref.isEditable());
                }
            });

            update_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String an_name = animal_name_text.getText();
                    Integer an_age = Integer.parseInt(animal_age_text.getText());
                    BigDecimal an_price = new BigDecimal(animal_price_text.getText());

                    Object an_obj = animal_specie_ref.getSelectedItem();
                    Integer an_specie = Integer.parseInt(((ComboItem) an_obj).getValue());

                    animal.setName(an_name);
                    animal.setAge(an_age);
                    animal.setPrice(an_price);
                    animal.setSpecie(an_specie);
                    animal.save();
                    ZooShop.this.switchTabAnimals();
                }
            });

            delete_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animal.delete();
                    ZooShop.this.switchTabAnimals();
                }
            });
        }

        this.panel_animals_list.revalidate();
        this.panel_animals_list.repaint();
    }
}
