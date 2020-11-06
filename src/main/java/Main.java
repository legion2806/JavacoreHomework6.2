import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);

        String obj = writeString(json);

    }

    protected static String writeString(String json){

        try (FileWriter file = new FileWriter("data.json"))
        { file.write(json.toString());
        file.flush();

        } catch (IOException e) {   e.printStackTrace();}
        return json;
    }

    protected static String listToJson(List<Employee> list) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listType = new TypeToken<List<Employee>>() {}.getType();

        String json = gson.toJson(list, listType);

        return json;

    }

    protected static List parseCSV(String[] columnMapping, String fileName) {

        System.out.println("gggg");

        List<Employee> staff = new ArrayList<>();

        System.out.println(3);

        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();

            strategy.setType(Employee.class);
            strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            staff = csv.parse();
            staff.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;

    }




}
