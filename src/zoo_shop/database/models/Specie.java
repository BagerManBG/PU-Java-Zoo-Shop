package zoo_shop.database.models;

import java.sql.*;
import zoo_shop.database.ConnectionManager;

public class Specie {
    private Integer id;
    private String specie;

    public Specie (Integer id, String specie) {
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

    public static void create(String specie) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO animal_species (`specie`) values(?)");

            stmt.setString(1,specie);
            stmt.executeUpdate();

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
