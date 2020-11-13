package com.quickbase;

import com.quickbase.devint.DBManager;
import com.quickbase.devint.DBManagerImpl;
import com.quickbase.devint.ConcreteStatService;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * The main method of the executable JAR generated from this repository. This is to let you
 * execute something from the command-line or IDE for the purposes of demonstration, but you can choose
 * to demonstrate in a different way (e.g. if you're using a framework)
 */
public class Main {
    public static void main(String[] args) {
        List<Pair<String, Integer>> populationData = aggregatePopData();

        for (Pair<String, Integer> p : populationData) {
            System.out.println("Country: " + p.getLeft() + "- " + p.getRight());
        }

    }

    public static List<Pair<String, Integer>> aggregatePopData(){
        DBManager dbm = new DBManagerImpl();
        ConcreteStatService api = new ConcreteStatService();
        List<Pair<String, Integer>> finalResults = new ArrayList<Pair<String, Integer>>();
        Map<String,Integer> combinedResults = new HashMap<String, Integer>();

        List<Pair<String, Integer>> dbResults = dbm.getPopulationsByCountry();
        List<Pair<String, Integer>> apiResults = api.GetCountryPopulations();

        /*
         * Data returned from the database takes precedence over data returned from the "api". Adding Both sets of data
         * to a hashtable is an efficient way of combining the data from both sources. Database data is added first and
         * "api" data is only added if data for that country is NOT already present.
         */
        for (Pair<String, Integer> p : dbResults) {
            String country = p.getLeft();
            Integer population = p.getRight();
            if (country.equals("U.S.A.")){ //rename data to match "api" reference to the same country
                country = "United States of America";
            }
            combinedResults.put(country, population);
        }

        for (Pair<String, Integer> p:apiResults) {
            //putIfAbsent does NOT replace data if the key is already present
            combinedResults.putIfAbsent(p.getLeft(), p.getRight());

        }

        //convert HashTable data back to List<Pair<String, Integer>>
        Set<String> keySet = combinedResults.keySet();
        for(String s: keySet){
            finalResults.add(new ImmutablePair<String, Integer>(s,combinedResults.get(s)));
        }
        return finalResults;
    }
}