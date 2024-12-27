package com.app.pokedex.SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class PokemonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTable() {
        dropTable();

        String sql = "CREATE TABLE IF NOT EXISTS pokemon (\n"
                + " id INTEGER PRIMARY KEY,\n"
                + " name TEXT NOT NULL,\n"
                + " type TEXT NOT NULL\n"
                + ");";
        jdbcTemplate.execute(sql);
        System.out.println("Table created successfully.");
        addPokemon(5, "jerry", "normal");
    }

    public void dropTable() {
        String dropTableSql = "DROP TABLE IF EXISTS pokemon";
        jdbcTemplate.execute(dropTableSql);
        System.out.println("Table 'pokemon' has been dropped.");
    }

    public void addPokemon(int id, String name, String type) {
        String sql = "INSERT INTO pokemon (id, name, type) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, id, name, type);
        System.out.println("Pokemon added: " + name);
    }


    public void exportTableToFile() {
        String query = "SELECT * FROM pokemon";
        String filePath = "exported_pokemon.sql"; // Update with your actual username

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the CREATE TABLE statement at the top
            String createTableStatement = "CREATE TABLE IF NOT EXISTS pokemon (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + " name TEXT NOT NULL,\n"
                    + " type TEXT NOT NULL\n"
                    + ");\n";
            writer.write(createTableStatement);

            // Write the INSERT statements for each row
            jdbcTemplate.query(query, (ResultSet rs) -> {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String type = rs.getString("type");

                    String insertStatement = String.format("INSERT INTO pokemon (id, name, type) VALUES (%d, '%s', '%s');\n", id, name, type);
                    try {
                        writer.write(insertStatement);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            });

            System.out.println("Table data exported to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
