package com.app.pokedex.Export.Excel;

import com.app.pokedex.Pokemon.Pokemon;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PokemonServiceExcel {


    // takes in the userinput to name the file and the pokedex arraylist built upon
    // in game to make an Excel file of the pokemon captured.
    public void exportDataToExcelFile(String fileName, ArrayList<Pokemon> pokedex) {

        String excelFilePath = fileName + ".xlsx";
        int rowNum = 2;

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Pokedex");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Type", "Defense", "Sprite"};

            // creates the first row to name the column categories
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // each new row represents a pokemon and each data falls under
            // its respective column category
            for (Pokemon pokemon : pokedex) {

                Row row = sheet.createRow(rowNum);

                int pokemonId = pokemon.getId();
                String pokemonName = pokemon.getName();
                String pokemonType = pokemon.getType();
                int statDefense = pokemon.getStatDefense();
                String sprite = pokemon.getMainSprite();

                row.createCell(0).setCellValue(pokemonId);
                row.createCell(1).setCellValue(pokemonName);
                row.createCell(2).setCellValue(pokemonType);
                row.createCell(3).setCellValue(statDefense);
                row.createCell(4).setCellValue(sprite);

                rowNum++;

            }

            try (FileOutputStream out = new FileOutputStream(excelFilePath)) {
                workbook.write(out);
                System.out.printf("\n%s created in the root folder of this project\n\n", excelFilePath);
            } catch (IOException ex) {
                System.out.println("An error occurred during I/O operation: " + ex.getMessage());
            }

        } catch (IOException ex) {
            System.out.println("An error occurred during I/O operation: " + ex.getMessage());
        }
    }
}
