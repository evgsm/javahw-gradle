package ru.evgsm;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class JsonParser {
    public static void main (String [] arg) {
        String str = readString("data.json");
        List<Employee> employeeList = jsonToList(str);
        for (Employee e:employeeList) {
            System.out.println(e.toString());
        }

    }

    private static String readString(String jsonFileName) {

        StringBuilder str = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(jsonFileName))) {
            String s;
            while ((s=br.readLine()) != null) {
                str.append(s);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();

    }

    private static List<Employee> jsonToList(String jsonString) {
        List<Employee> employeeList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObj = (JSONObject) parser.parse(jsonString);
            JSONArray jsonArray = (JSONArray)jsonObj.get("employee");
            for (Object emp: jsonArray) {
                employeeList.add((Employee) emp);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return employeeList;
    }
}
