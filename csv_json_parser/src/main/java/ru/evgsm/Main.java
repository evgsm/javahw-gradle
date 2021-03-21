package ru.evgsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.collections.iterators.EmptyOrderedIterator;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main (String [] arg) {

        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"));
                FileWriter fileWriter = new FileWriter("data.json")) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id","firstName","lastName","country","age");

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> listEmployeeCsv = csv.parse();
            //listEmployeeCsv.forEach(System.out::println);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();

            for (Employee emp: listEmployeeCsv) {
                fileWriter.write(gson.toJson(emp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
