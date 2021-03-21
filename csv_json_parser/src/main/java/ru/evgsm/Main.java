package ru.evgsm;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main (String [] arg) {

        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id","firstName","lastName","country","age");

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> listEmployeeCsv = csv.parse();
            listEmployeeCsv.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
