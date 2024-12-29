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
//        System.out.println("Pokedex table created");
    }

    public void dropTable() {
        pokemonDao.dropTable();
//        System.out.println("Pokedex table dropped");
    }

    public void addPokemon(Pokemon pokemon) {
        pokemonDao.addPokemon(pokemon);
//        System.out.printf("\n%s added to pokedex table", pokemon.getName());
    }

    public void exportTableToSqlFile(String fileName){
        pokemonDao.exportTableToSqlFile(fileName);
        System.out.printf("\n%s.sql created in the root folder of this project\n\n", fileName);
    }

//    public void exportTableToWordFile(String fileName, ArrayList<Pokemon> pokedex){
//        pokemonDao.exportTableToWordFile(fileName, pokedex);
//    }

}
