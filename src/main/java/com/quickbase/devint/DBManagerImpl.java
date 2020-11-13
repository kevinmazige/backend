package com.quickbase.devint;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * This DBManager implementation provides a connection to the database containing population data.
 *
 * Created by ckeswani on 9/16/15.
 */
public class DBManagerImpl implements DBManager {
    private Connection getConnection() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:resources/data/citystatecountry.db");
            System.out.println("Opened database successfully");

        } catch (ClassNotFoundException cnf) {
            System.out.println("could not load driver");
        } catch (SQLException sqle) {
            System.out.println("sql exception:" + sqle.getStackTrace());
        }
        return c;
    }
    //TODO: Add a method (signature of your choosing) to query the db for population data by country
    public List<Pair<String, Integer>> getPopulationsByCountry(){
        String sql = "SELECT CountryName,  Sum(Population) AS \"Population\"\n" +
                "FROM ((State \n" +
                "INNER JOIN Country ON State.CountryId = Country.CountryId)\n" +
                "INNER JOIN City ON State.StateId = City.StateId)\n" +
                "GROUP BY CountryName";
        List<Pair<String, Integer>> output = new ArrayList<Pair<String, Integer>>();
        try (
                Connection c = this.getConnection();
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql);){
            while (rs.next()){
                String country = rs.getString("CountryName");
                Integer population = rs.getInt("Population");
                output.add(new ImmutablePair<String, Integer>(country,population));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return output;
    }
}
