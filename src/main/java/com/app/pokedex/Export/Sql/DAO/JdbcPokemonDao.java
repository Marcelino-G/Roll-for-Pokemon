package com.app.pokedex.Export.Sql.DAO;

import com.app.pokedex.Pokemon.Pokemon;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class JdbcPokemonDao implements PokemonDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private int pokemonId = 0;

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

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

    private String sqlDropTable = """
                DROP TABLE IF EXISTS pokedex;
                
                """;

    private String sqlSelectAll = "SELECT * FROM pokedex";

    @Override
    public void createTable() {
        jdbcTemplate.execute(sqlCreateTable);
    }

    @Override
    public void dropTable() {
        this.pokemonId = 0;
        jdbcTemplate.update(sqlDropTable);
    }

    @Override
    public void addPokemon(Pokemon pokemon) {

        String pokemon_name = pokemon.getName();
        String pokemon_type = pokemon.getType();
        int stat_defense = pokemon.getStatDefense();
        String sprite = pokemon.getMainSprite();

        this.pokemonId = this.pokemonId + 1;

        String sqlInsertPokemon = """
                INSERT INTO pokedex (pokemon_id, pokemon_name, pokemon_type, stat_defense, sprite)
                VALUES (?, ?, ?, ?, ?);
                """;
        jdbcTemplate.update(sqlInsertPokemon, this.pokemonId, pokemon_name, pokemon_type, stat_defense, sprite);
    }

    @Override
    public void exportTableToSqlFile(String fileName) {

        String sqlFilePath = fileName + ".sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFilePath))) {

            writer.write(sqlDropTable);
            writer.write(sqlCreateTable);


            jdbcTemplate.query(sqlSelectAll, (ResultSet rs) -> {

                while (rs.next()) {

                    int pokemonId = rs.getInt("pokemon_id");
                    String pokemonName = rs.getString("pokemon_name");
                    String pokemonType = rs.getString("pokemon_type");
                    int statDefense = rs.getInt("stat_defense");
                    String sprite = rs.getString("sprite");

                    try {
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
            System.out.println(ex.getMessage());
        }
    }

//    @Override
//    public void exportTableToWordFile(String fileName, ArrayList<Pokemon> pokedex) {
//
//        String excelFilePath = fileName + ".xlsx";
//        int startPokemonId = 1;
//        int rowNum = 2;
//
//        try(Workbook workbook = new XSSFWorkbook()){
//
//            Sheet sheet = workbook.createSheet("Pokedex");
//
//            Row headerRow = sheet.createRow(0);
//            String[] headers = {"ID", "Name", "Type", "Defense", "Sprite"};
//
//            for(int i =0; i < headers.length; i++){
//
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(headers[i]);
//
//
//            }
//
//
//
//            for(Pokemon pokemon : pokedex){
//
//
//
//                Row row = sheet.createRow(rowNum);
//
//                int pokemonId = startPokemonId;
//                String pokemonName = pokemon.getName();
//                String pokemonType = pokemon.getType();
//                int statDefense = pokemon.getStatDefense();
//                String sprite = pokemon.getMainSprite();
//
//                row.createCell(0).setCellValue(pokemonId);
//                row.createCell(1).setCellValue(pokemonName);
//                row.createCell(2).setCellValue(pokemonType);
//                row.createCell(3).setCellValue(statDefense);
//                row.createCell(4).setCellValue(sprite);
//
//
//                startPokemonId++;
//                rowNum++;
//
//
//
////                run.setText("%d\t%s\t%s\t%d\t%s".formatted(pokemonId,pokemonName,pokemonType,statDefense,sprite));
//
//
//            }
//
//
////            XWPFParagraph title = document.createParagraph();
////            XWPFRun titleRun = title.createRun();
////            titleRun.setText("pokedex table export");
////            titleRun.setBold(true);
////            titleRun.setFontSize(16);
////
////            XWPFParagraph header = document.createParagraph();
////            XWPFRun headerRun = header.createRun();
////            headerRun.setBold(true);
////            headerRun.setText("ID\tName\tType\tDefense\tSprite");
//
//
//
//
//
////            jdbcTemplate.query(sqlSelectAll, (ResultSet rs) -> {
////
////                while(rs.next()){
////
////                    XWPFParagraph paragraph = document.createParagraph();
////                    XWPFRun run = paragraph.createRun();
////
////
////                    int pokemonId = rs.getInt("pokemon_id");
////                    String pokemonName = rs.getString("pokemon_name");
////                    String pokemonType = rs.getString("pokemon_type");
////                    int statDefense = rs.getInt("stat_defense");
////                    String sprite = rs.getString("sprite");
////
////                    run.setText("%d\t%s\t%s\t%d\t%s".formatted(pokemonId,pokemonName,pokemonType,statDefense,sprite));
////
////                }
////
////                return null;
////
////            });
//
//            try(FileOutputStream out = new FileOutputStream(excelFilePath)){
//                workbook.write(out);
//                System.out.println("word created");
//            } catch(IOException ex){
//                System.out.println(ex.getMessage());
//            }
//
//
//
//
//        } catch(IOException ex){
//            System.out.println(ex.getMessage());
//        }
//
//
//
//    }


}
