package com.app.pokedex.Export.Sql.DAO;

import com.app.pokedex.Pokemon.Pokemon;

import java.util.ArrayList;

public interface PokemonDao {

    void createTable();
    void dropTable();
    void addPokemon(Pokemon pokemon);
    void exportTableToSqlFile(String fileName);

}
