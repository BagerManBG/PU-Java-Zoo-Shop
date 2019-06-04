package zoo_shop.database.models;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import zoo_shop.database.ConnectionManager;

public class Specie {
    private Integer id;
    private String specie;

    public Specie(Integer id, String specie) {
        this.id = id;
        this.setSpecie(specie);
    }

    public Integer id() {
        return this.id;
    }

    public String getSpecie() {
        return this.specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public Specie save() {
        try {
            Connection con = ConnectionManager.getConnection();

            if (this.id() == null) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO animal_species (`specie`) values(?)");

                stmt.setString(1, this.getSpecie());
                stmt.executeUpdate();

                Statement sel_stmt = con.createStatement();
                ResultSet rs = sel_stmt.executeQuery("SELECT id FROM animal_species ORDER BY id DESC LIMIT 1");

                rs.next();
                this.id = rs.getInt("id");
            } else {
                PreparedStatement stmt = con.prepareStatement("UPDATE animal_species SET `specie` = ? WHERE `id` = ?");

                stmt.setString(1, this.getSpecie());
                stmt.setInt(2, this.id());
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

            PreparedStatement stmt = con.prepareStatement("DELETE FROM animal_species WHERE `id` = ?");

            stmt.setInt(1, this.id());
            stmt.execute();

            con.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static Specie create(String specie) {
        Specie s = new Specie(null, specie);
        s.save();
        return s;
    }

    public static Specie load(Integer id) {
        Specie entity = null;

        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM animal_species WHERE `id`=" + id.toString());

            while (rs.next()) {
                entity = new Specie(rs.getInt("id"), rs.getString("specie"));
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return entity;
    }

    public static Map loadAll() {
        Map<Integer, Specie> map = new HashMap<Integer, Specie>();
        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM animal_species");

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String specie = rs.getString("specie");

                Specie entity = new Specie(id, specie);
                map.put(id, entity);
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return map;
    }
}
