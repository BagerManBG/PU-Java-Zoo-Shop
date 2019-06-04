package zoo_shop.database.models;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import zoo_shop.database.ConnectionManager;

public class Animal {
    private Integer id;
    private String name;
    private Integer age;
    private BigDecimal price;
    private Integer specie;

    public Animal(Integer id, String name, Integer age, BigDecimal price, Integer specie) {
        this.id = id;
        this.setName(name);
        this.setAge(age);
        this.setPrice(price);
        this.setSpecie(specie);
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

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;

    }

    public Integer getSpecie() {
        return this.specie;
    }

    public void setSpecie(Integer specie) {
        this.specie = specie;
    }

    public Animal save() {
        try {
            Connection con = ConnectionManager.getConnection();

            if (this.id() == null) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO animals (`name`, `age`, `price`, `specie`) values(?, ?, ?, ?)");

                stmt.setString(1, this.getName());
                stmt.setInt(2, this.getAge());
                stmt.setString(3, this.getPrice().toString());
                stmt.setInt(4, this.getSpecie());
                stmt.executeUpdate();

                Statement sel_stmt = con.createStatement();
                ResultSet rs = sel_stmt.executeQuery("SELECT id FROM animals ORDER BY id DESC LIMIT 1");

                rs.next();
                this.id = rs.getInt("id");
            } else {
                PreparedStatement stmt = con.prepareStatement("UPDATE animals SET `name` = ?, `age` = ?, `price` = ?, `specie` = ? WHERE `id` = ?");

                stmt.setString(1, this.getName());
                stmt.setInt(2, this.getAge());
                stmt.setString(3, this.getPrice().toString());
                stmt.setInt(4, this.getSpecie());
                stmt.setInt(5, this.id());
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

            PreparedStatement stmt = con.prepareStatement("DELETE FROM animals WHERE `id` = ?");

            stmt.setInt(1, this.id());
            stmt.execute();

            con.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static Animal create(String name, Integer age, BigDecimal price, Integer specie) {
        Animal a = new Animal(null, name, age, price, specie);
        a.save();
        return a;
    }

    public static Animal load(Integer id) {
        Animal entity = null;

        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM animals WHERE `id`=" + id.toString());

            while (rs.next()) {
                entity = new Animal(rs.getInt("id"), rs.getString("name"), rs.getInt("age"), new BigDecimal(rs.getString("price")), rs.getInt("specie"));
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return entity;
    }

    public static Map loadAll() {
        Map<Integer, Animal> map = new HashMap<Integer, Animal>();

        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM animals");

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Integer age = rs.getInt("age");
                BigDecimal price = new BigDecimal(rs.getString("price"));
                Integer specie = rs.getInt("specie");

                Animal entity = new Animal(id, name, age, price, specie);
                map.put(id, entity);
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return map;
    }
}
