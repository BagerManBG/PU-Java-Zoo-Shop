package zoo_shop.database.models;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import zoo_shop.database.ConnectionManager;

public class Adoption {
    private Integer id;
    private Integer client;
    private Integer animal;

    public Adoption(Integer id, Integer client, Integer animal) {
        this.id = id;
        this.setClient(client);
        this.setAnimal(animal);
    }

    public Integer id() {
        return this.id;
    }

    public Integer getClient() {
        return this.client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getAnimal() {
        return this.animal;
    }

    public void setAnimal(Integer animal) {
        this.animal = animal;
    }

    public Adoption save() {
        try {
            Connection con = ConnectionManager.getConnection();

            if (this.id() == null) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO adoptions (`client`, `animal`) values(?, ?)");

                stmt.setInt(1, this.getClient());
                stmt.setInt(2, this.getAnimal());
                stmt.executeUpdate();

                Statement sel_stmt = con.createStatement();
                ResultSet rs = sel_stmt.executeQuery("SELECT id FROM adoptions ORDER BY id DESC LIMIT 1");

                rs.next();
                this.id = rs.getInt("id");
            } else {
                PreparedStatement stmt = con.prepareStatement("UPDATE adoptions SET `client` = ?, `animal` = ? WHERE `id` = ?");

                stmt.setInt(1, this.getClient());
                stmt.setInt(2, this.getAnimal());
                stmt.setInt(3, this.id());
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

            PreparedStatement stmt = con.prepareStatement("DELETE FROM adoptions WHERE `id` = ?");

            stmt.setInt(1, this.id());
            stmt.execute();

            con.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static Adoption create(Integer client, Integer animal) {
        Adoption a = new Adoption(null, client, animal);
        a.save();
        return a;
    }

    public static Adoption load(Integer id) {
        Adoption entity = null;

        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM adoptions WHERE `id`=" + id.toString());

            while (rs.next()) {
                entity = new Adoption(rs.getInt("id"), rs.getInt("client"), rs.getInt("animal"));
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return entity;
    }

    public static Map loadAll() {
        Map<Integer, Adoption> map = new HashMap<Integer, Adoption>();

        try {
            Connection con = ConnectionManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM adoptions");

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer client = rs.getInt("client");
                Integer animal = rs.getInt("animal");

                Adoption entity = new Adoption(id, client, animal);
                map.put(id, entity);
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return map;
    }
}
