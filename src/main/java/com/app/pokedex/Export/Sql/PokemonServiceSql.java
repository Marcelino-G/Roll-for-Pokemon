package com.app.pokedex.Export.Sql;

import com.app.pokedex.Pokemon.Pokemon;
import com.app.pokedex.Export.Sql.DAO.PokemonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonServiceSql {

    @Autowired
    private PokemonDao pokemonDao;

    public void createTable() {
        pokemonDao.createTable();
    }

    public void dropTable() {
        pokemonDao.dropTable();
    }

    public void addPokemon(Pokemon pokemon) {
        pokemonDao.addPokemon(pokemon);
    }

    public void exportTableToSqlFile(String fileName) {
        pokemonDao.exportTableToSqlFile(fileName);
        System.out.printf("\n%s.sql created in the root folder of this project\n\n", fileName);
    }

}
