package com.app.pokedex.Export.Sql.DAO;

import com.app.pokedex.Pokemon.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;

// handles sql related operations

@Repository
public class JdbcPokemonDao implements PokemonDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String sqlCreateTable = """
            CREATE TABLE IF NOT EXISTS pokedex(
            pokemon_id int,
            pokemon_name varchar(20),
            pokemon_type varchar(20),
            stat_defense int,
            sprite varchar(2083),
            CONSTRAINT PK_pokemon_id PRIMARY KEY (pokemon_id)
            );
                        
            """;

    private final String sqlDropTable = """
            DROP TABLE IF EXISTS pokedex;
                            
            """;

    private final String sqlSelectAll = "SELECT * FROM pokedex";

    @Override
    public void createTable() {
        jdbcTemplate.execute(sqlCreateTable);
    }

    @Override
    public void dropTable() {
        jdbcTemplate.update(sqlDropTable);
    }

    // takes in a pokemon and inserts it's "get" values into the pokedex table with INSERT statements
    @Override
    public void addPokemon(Pokemon pokemon) {

        int pokemonId = pokemon.getId();
        String pokemon_name = pokemon.getName();
        String pokemon_type = pokemon.getType();
        int stat_defense = pokemon.getStatDefense();
        String sprite = pokemon.getMainSprite();

        String sqlInsertPokemon = """
                INSERT INTO pokedex (pokemon_id, pokemon_name, pokemon_type, stat_defense, sprite)
                VALUES (?, ?, ?, ?, ?);
                """;
        jdbcTemplate.update(sqlInsertPokemon, pokemonId, pokemon_name, pokemon_type, stat_defense, sprite);
    }

    // prints out the sql operations that DROP and CREATE the pokedex table
    // and then prints out the 1+ INSERT statements that add pokemon to the pokedex table.
    // SQL SCRIPT FOR A SQL FILE ^^^
    //
    // a SELECT * is used to get all the pokemon from the pokedex to loop through.
    // each iteration creates an INSERT statement.
    @Override
    public void exportTableToSqlFile(String fileName) {

        String sqlFilePath = fileName + ".sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFilePath))) {

            // part of sql script
            writer.write(sqlDropTable);
            // part of sql script
            writer.write(sqlCreateTable);


            jdbcTemplate.query(sqlSelectAll, (ResultSet rs) -> {

                while (rs.next()) {

                    int pokemonId = rs.getInt("pokemon_id");
                    String pokemonName = rs.getString("pokemon_name");
                    String pokemonType = rs.getString("pokemon_type");
                    int statDefense = rs.getInt("stat_defense");
                    String sprite = rs.getString("sprite");

                    try {
                        // part of sql script
                        writer.write("""
                                INSERT INTO pokedex (pokemon_id, pokemon_name, pokemon_type, stat_defense, sprite)
                                VALUES (%d, '%s', '%s', %d, '%s'); \n
                                """.formatted(pokemonId, pokemonName, pokemonType, statDefense, sprite));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            });
        } catch (IOException ex) {
            System.out.println("An error occurred during I/O operation: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("A runtime error occurred: " + ex.getMessage());
        }
    }
}
