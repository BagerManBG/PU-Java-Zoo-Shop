package zoo_shop.database.models;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import zoo_shop.database.ConnectionManager;

public class Client {
    private Integer id;
    private String name;
    private String email;
    private String phone;

    public Client(Integer id, String name, String email, String phone) {
        this.id = id;
        this.setName(name);
        this.setEmail(email);
        this.setPhone(phone);
    }

    public Integer id() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Client save() {
        try {
            Connection con = ConnectionManager.getConnection();

            if (this.id() == null) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO clients (`name`, `email`, `phone`) values(?, ?, ?)");

                stmt.setString(1, this.getName());
                stmt.setString(2, this.getEmail());
                stmt.setString(3, this.getPhone());
                stmt.executeUpdate();

                Statement sel_stmt = con.createStatement();
                ResultSet rs = sel_stmt.executeQuery("SELECT id FROM clients ORDER BY id DESC LIMIT 1");

                rs.next();
                this.id = rs.getInt("id");
            } else {
                PreparedStatement stmt = con.prepareStatement("UPDATE clients SET `name` = ?, `email` = ?, `phone` = ? WHERE `id` = ?");

                stmt.setString(1, this.getName());
                stmt.setString(2, this.getEmail());
                stmt.setString(3, this.getPhone());
                stmt.setInt(4, this.id());
                stmt.executeUpdate();
            }
            con.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return this;
    }

    public void delete() {
        try {
            Connection con = ConnectionManager.getConnection();

            PreparedStatement stmt = con.prepareStatement("DELETE FROM clients WHERE `id` = ?");

            stmt.setInt(1, this.id());
            stmt.execute();

            con.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static Client create(String name, String email, String phone) {
        Client c = new Client(null, name, email, phone);
        c.save();
        return c;
    }

    public static Client load(Integer id) {
        Client entity = null;

        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM clients WHERE `id`=" + id.toString());

            while (rs.next()) {
                entity = new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"));
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return entity;
    }

    public static Map loadAll() {
        Map<Integer, Client> map = new HashMap<Integer, Client>();
        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                Client entity = new Client(id, name, email, phone);
                map.put(id, entity);
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return map;
    }
}
