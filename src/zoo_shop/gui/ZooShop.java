package zoo_shop.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import zoo_shop.database.ConnectionManager;
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
    private JTextField species_search_name;
    private JButton button_species_search;
    private JTextField animals_search_name;
    private JButton button_animals_search;
    private JTextField clients_search_name;
    private JButton button_clients_search;
    private JButton button_adoptions_search;
    private JTextField adoptions_search_client;
    private JTextField global_search_animal_name;
    private JTextField global_search_client_name;
    private JButton button_global_search;
    private JPanel panel_global_search;
    private JFrame frame;

    public ZooShop(JFrame frame) {
        this.frame = frame;
        this.switchTabSpecies();
        // On Tab Change Listener.
        tabbed_pane_main.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = tabbed_pane_main.getSelectedIndex();

                switch (index) {
                    case 0:
                        ZooShop.this.switchTabSpecies();
                        break;
                    case 1:
                        ZooShop.this.switchTabAnimals();
                        break;
                    case 2:
                        ZooShop.this.switchTabClients();
                        break;
                    case 3:
                        ZooShop.this.switchTabAdoptions();
                        break;
                    case 4:
                        ZooShop.this.switchTabGlobalSearch();
                        break;
                    default:
                        break;
                }
            }
        });

        // On Add Specie Button Click.
        button_add_species.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ZooShop.this.text_field_species_name.getText();
                ZooShop.this.text_field_species_name.setText("");

                Specie.create(name);
                ZooShop.this.switchTabSpecies();
            }
        });

        // On Add Animal Button Click.
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

        // On Add Client Button Click.
        button_add_clients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ZooShop.this.text_field_clients_name.getText();
                ZooShop.this.text_field_clients_name.setText("");

                String email = ZooShop.this.text_field_clients_email.getText();
                ZooShop.this.text_field_clients_email.setText("");

                String phone = ZooShop.this.text_field_clients_phone.getText();
                ZooShop.this.text_field_clients_phone.setText("");

                Client.create(name, email, phone);
                ZooShop.this.switchTabClients();
            }
        });

        // On Add Adoption Button Click.
        button_add_adoptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object client_obj = ZooShop.this.combo_box_adoptions_client.getSelectedItem();
                Integer client = Integer.parseInt(((ComboItem) client_obj).getValue());
                ZooShop.this.combo_box_adoptions_client.setSelectedIndex(0);

                Object animal_obj = ZooShop.this.combo_box_adoptions_animal.getSelectedItem();
                Integer animal = Integer.parseInt(((ComboItem) animal_obj).getValue());
                ZooShop.this.combo_box_adoptions_animal.setSelectedIndex(0);

                Adoption.create(client, animal);
                ZooShop.this.switchTabAdoptions();
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
        ArrayList<JPanel> containers = new ArrayList<>();

        this.species_search_name.setText("");

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
            containers.add(container);

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

        // Species Search.
        button_species_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search_name = ZooShop.this.species_search_name.getText();

                for (JPanel container : containers) {
                    container.setVisible(true);
                }

                if (!search_name.equals("")) {
                    int index = 0;
                    for(Map.Entry<Integer, Specie> row : species.entrySet()) {
                        Integer id = row.getKey();
                        Specie specie = row.getValue();

                        if (!specie.getSpecie().contains(search_name)) {
                            containers.get(index).setVisible(false);
                        }
                        index++;
                    }
                }

                ZooShop.this.panel_species_list.revalidate();
                ZooShop.this.panel_species_list.repaint();
            }
        });

        this.panel_species_list.revalidate();
        this.panel_species_list.repaint();
    }

    // Create the list of animals.
    private void switchTabAnimals() {
        Map<Integer, Animal> animals = Animal.loadAll();
        Map<Integer, Specie> species = Specie.loadAll();
        ArrayList<JPanel> containers = new ArrayList<>();

        this.animals_search_name.setText("");

        this.panel_animals_list.removeAll();
        this.panel_animals_list.setLayout(new BoxLayout(this.panel_animals_list, BoxLayout.Y_AXIS));

        combo_box_animals_species.removeAllItems();

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
            containers.add(container);

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

            // Animals Search.
            button_animals_search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String search_name = ZooShop.this.animals_search_name.getText();

                    for (JPanel container : containers) {
                        container.setVisible(true);
                    }

                    if (!search_name.equals("")) {
                        int index = 0;
                        for(Map.Entry<Integer, Animal> row : animals.entrySet()) {
                            Integer id = row.getKey();
                            Animal animal = row.getValue();

                            if (!animal.getName().contains(search_name)) {
                                containers.get(index).setVisible(false);
                            }
                            index++;
                        }
                    }

                    ZooShop.this.panel_animals_list.revalidate();
                    ZooShop.this.panel_animals_list.repaint();
                }
            });
        }

        this.panel_animals_list.revalidate();
        this.panel_animals_list.repaint();
    }

    // Create the list of clients
    private void switchTabClients() {
        Map<Integer, Client> clients = Client.loadAll();
        ArrayList<JPanel> containers = new ArrayList<>();

        this.clients_search_name.setText("");

        this.panel_clients_list.removeAll();
        this.panel_clients_list.setLayout(new BoxLayout(this.panel_clients_list, BoxLayout.Y_AXIS));

        for(Map.Entry<Integer, Client> row : clients.entrySet()) {
            Integer id = row.getKey();
            Client client = row.getValue();

            JPanel container = new JPanel();
            JTextField name_text_field = new JTextField();
            JTextField email_text_field = new JTextField();
            JTextField phone_text_field = new JTextField();

            JCheckBox trigger_editable = new JCheckBox("Enable Update");
            JButton update_button = new JButton("Update");
            JButton delete_button = new JButton("Delete");

            update_button.setEnabled(false);

            name_text_field.setEditable(false);
            name_text_field.setText(client.getName());
            name_text_field.setMaximumSize(new Dimension(255, 30));

            email_text_field.setEditable(false);
            email_text_field.setText(client.getEmail());
            email_text_field.setMaximumSize(new Dimension(255, 30));

            phone_text_field.setEditable(false);
            phone_text_field.setText(client.getPhone());
            phone_text_field.setMaximumSize(new Dimension(255, 30));

            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
            container.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(name_text_field, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(email_text_field, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(phone_text_field, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(trigger_editable, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(update_button, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(delete_button, Component.LEFT_ALIGNMENT);

            this.panel_clients_list.add(container, Component.LEFT_ALIGNMENT);
            containers.add(container);

            trigger_editable.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    update_button.setEnabled(!update_button.isEnabled());
                    name_text_field.setEditable(!name_text_field.isEditable());
                    email_text_field.setEditable(!email_text_field.isEditable());
                    phone_text_field.setEditable(!phone_text_field.isEditable());
                }
            });

            update_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String cl_name = name_text_field.getText();
                    client.setName(cl_name);

                    String cl_email = email_text_field.getText();
                    client.setEmail(cl_email);

                    String cl_phone = phone_text_field.getText();
                    client.setPhone(cl_phone);

                    client.save();
                    ZooShop.this.switchTabClients();
                }
            });

            delete_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.delete();
                    ZooShop.this.switchTabClients();
                }
            });

            // Clients Search.
            button_clients_search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String search_name = ZooShop.this.clients_search_name.getText();

                    for (JPanel container : containers) {
                        container.setVisible(true);
                    }

                    if (!search_name.equals("")) {
                        int index = 0;
                        for(Map.Entry<Integer, Client> row : clients.entrySet()) {
                            Integer id = row.getKey();
                            Client client = row.getValue();

                            if (!client.getName().contains(search_name)) {
                                containers.get(index).setVisible(false);
                            }
                            index++;
                        }
                    }

                    ZooShop.this.panel_clients_list.revalidate();
                    ZooShop.this.panel_clients_list.repaint();
                }
            });
        }

        this.panel_clients_list.revalidate();
        this.panel_clients_list.repaint();
    }

    // Create the list of adoptions
    private void switchTabAdoptions() {
        Map<Integer, Adoption> adoptions = Adoption.loadAll();
        Map<Integer, Client> clients = Client.loadAll();
        Map<Integer, Animal> animals = Animal.loadAll();
        ArrayList<JPanel> containers = new ArrayList<>();

        this.adoptions_search_client.setText("");

        this.panel_adoptions_list.removeAll();
        this.panel_adoptions_list.setLayout(new BoxLayout(this.panel_adoptions_list, BoxLayout.Y_AXIS));

        combo_box_adoptions_client.removeAllItems();
        combo_box_adoptions_animal.removeAllItems();

        for(Map.Entry<Integer, Client> row : clients.entrySet()) {
            Integer id = row.getKey();
            Client client = row.getValue();

            this.combo_box_adoptions_client.addItem(new ComboItem(client.getName(), client.id().toString()));
        }

        for(Map.Entry<Integer, Animal> row : animals.entrySet()) {
            Integer id = row.getKey();
            Animal animal = row.getValue();

            this.combo_box_adoptions_animal.addItem(new ComboItem(animal.getName(), animal.id().toString()));
        }

        for(Map.Entry<Integer, Adoption> row : adoptions.entrySet()) {
            Integer id = row.getKey();
            Adoption adoption = row.getValue();

            Integer client_ref_index = null;
            Integer animal_ref_index = null;

            JPanel container = new JPanel();

            JComboBox client_ref = new JComboBox();
            JComboBox animal_ref = new JComboBox();

            JCheckBox trigger_editable = new JCheckBox("Enable Update");
            JButton update_button = new JButton("Update");
            JButton delete_button = new JButton("Delete");

            update_button.setEnabled(false);

            for (int i = 0; i < this.combo_box_adoptions_client.getItemCount(); i++) {
                ComboItem item = (ComboItem) this.combo_box_adoptions_client.getItemAt(i);
                client_ref.addItem(item);

                if (Integer.parseInt(item.getValue()) == adoption.getClient()) {
                    client_ref_index = i;
                }
            }

            client_ref.setEditable(false);
            client_ref.setSelectedIndex(client_ref_index);
            client_ref.setMaximumSize(new Dimension(150, 30));

            for (int i = 0; i < this.combo_box_adoptions_animal.getItemCount(); i++) {
                ComboItem item = (ComboItem) this.combo_box_adoptions_animal.getItemAt(i);
                animal_ref.addItem(item);

                if (Integer.parseInt(item.getValue()) == adoption.getAnimal()) {
                    animal_ref_index = i;
                }
            }

            animal_ref.setEditable(false);
            animal_ref.setSelectedIndex(animal_ref_index);
            animal_ref.setMaximumSize(new Dimension(150, 30));

            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
            container.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(client_ref, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(animal_ref, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(trigger_editable, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(update_button, Component.LEFT_ALIGNMENT);

            container.add(Box.createRigidArea(new Dimension(10,0)));
            container.add(delete_button, Component.LEFT_ALIGNMENT);

            this.panel_adoptions_list.add(container, Component.LEFT_ALIGNMENT);
            containers.add(container);

            trigger_editable.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    update_button.setEnabled(!update_button.isEnabled());
                    client_ref.setEditable(!client_ref.isEditable());
                    animal_ref.setEditable(!animal_ref.isEditable());
                }
            });

            update_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object ad_client_obj = client_ref.getSelectedItem();
                    Integer ad_client = Integer.parseInt(((ComboItem) ad_client_obj).getValue());

                    Object ad_animal_obj = animal_ref.getSelectedItem();
                    Integer ad_animal = Integer.parseInt(((ComboItem) ad_animal_obj).getValue());

                    adoption.setClient(ad_client);
                    adoption.setAnimal(ad_animal);

                    adoption.save();
                    ZooShop.this.switchTabAdoptions();
                }
            });

            delete_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    adoption.delete();
                    ZooShop.this.switchTabAdoptions();
                }
            });

            // Adoptions Search.
            button_adoptions_search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String search_name = ZooShop.this.adoptions_search_client.getText();

                    for (JPanel container : containers) {
                        container.setVisible(true);
                    }

                    if (!search_name.equals("")) {
                        int index = 0;
                        for(Map.Entry<Integer, Adoption> row : adoptions.entrySet()) {
                            Integer id = row.getKey();
                            Adoption adoption = row.getValue();

                            if (!clients.get(adoption.getClient()).getName().contains(search_name)) {
                                containers.get(index).setVisible(false);
                            }
                            index++;
                        }
                    }

                    ZooShop.this.panel_adoptions_list.revalidate();
                    ZooShop.this.panel_adoptions_list.repaint();
                }
            });
        }

        this.panel_adoptions_list.revalidate();
        this.panel_adoptions_list.repaint();
    }

    private void switchTabGlobalSearch() {
        this.panel_global_search.removeAll();
        this.panel_global_search.setLayout(new BoxLayout(this.panel_global_search, BoxLayout.Y_AXIS));

        button_global_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ZooShop.this.panel_global_search.removeAll();

                String search_animal = ZooShop.this.global_search_animal_name.getText();
                String search_client = ZooShop.this.global_search_client_name.getText();

                boolean search_animal_empty = search_animal.equals("");
                boolean search_client_empty = search_client.equals("");

                if (!search_animal_empty || !search_client_empty) {

                    String query = "SELECT * FROM adoptions";
                    query += " INNER JOIN animals ON adoptions.animal = animals.id";
                    query += " INNER JOIN clients ON adoptions.client = clients.id";
                    query += " INNER JOIN animal_species ON animals.specie = animal_species.id";
                    query += " WHERE 1=1";

                    if (!search_animal_empty)
                        query += " AND animals.name LIKE '%" + search_animal + "%'";

                    if (!search_client_empty)
                        query += " AND clients.name LIKE '%" + search_client + "%'";

                    try {
                        Connection con = ConnectionManager.getConnection();
                        Statement stmt = con.createStatement();

                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            String a_name = rs.getString("animals.name");
                            String a_price = rs.getString("animals.price");

                            String as_specie = rs.getString("animal_species.specie");

                            String c_name = rs.getString("clients.name");
                            String c_phone = rs.getString("clients.phone");

                            JPanel container = new JPanel();
                            JTextField a_name_text_field = new JTextField();
                            JTextField a_price_text_field = new JTextField();
                            JTextField as_specie_text_field = new JTextField();
                            JTextField c_name_text_field = new JTextField();
                            JTextField c_phone_text_field = new JTextField();

                            a_name_text_field.setEditable(false);
                            a_name_text_field.setText("Animal: " + a_name);
                            a_name_text_field.setMaximumSize(new Dimension(200, 30));

                            as_specie_text_field.setEditable(false);
                            as_specie_text_field.setText("Specie: " + as_specie);
                            as_specie_text_field.setMaximumSize(new Dimension(200, 30));

                            a_price_text_field.setEditable(false);
                            a_price_text_field.setText("Price: " + a_price);
                            a_price_text_field.setMaximumSize(new Dimension(200, 30));

                            c_name_text_field.setEditable(false);
                            c_name_text_field.setText("Client: " + c_name);
                            c_name_text_field.setMaximumSize(new Dimension(200, 30));

                            c_phone_text_field.setEditable(false);
                            c_phone_text_field.setText("Phone: " + c_phone);
                            c_phone_text_field.setMaximumSize(new Dimension(200, 30));

                            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
                            container.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

                            container.add(Box.createRigidArea(new Dimension(10,0)));
                            container.add(a_name_text_field, Component.LEFT_ALIGNMENT);

                            container.add(Box.createRigidArea(new Dimension(10,0)));
                            container.add(a_price_text_field, Component.LEFT_ALIGNMENT);

                            container.add(Box.createRigidArea(new Dimension(10,0)));
                            container.add(as_specie_text_field, Component.LEFT_ALIGNMENT);

                            container.add(Box.createRigidArea(new Dimension(10,0)));
                            container.add(c_name_text_field, Component.LEFT_ALIGNMENT);

                            container.add(Box.createRigidArea(new Dimension(10,0)));
                            container.add(c_phone_text_field, Component.LEFT_ALIGNMENT);

                            ZooShop.this.panel_global_search.add(container, Component.LEFT_ALIGNMENT);
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }

                ZooShop.this.panel_global_search.revalidate();
                ZooShop.this.panel_global_search.repaint();
            }
        });
    }
}
