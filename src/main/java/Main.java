import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";

        List<Employee> listCsv = parseCSV(columnMapping, fileName);

        String jsonCsv = listToJson(listCsv);

        String objCsv = writeString(jsonCsv);

        List<Employee> listXml = parseXML("data.xml");

        String jsonXml = listToJson(listXml);

        String objXml = writeString(jsonXml);

    }

    private static List<Employee> parseXML(String s) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("data.xml"));

        Node root = doc.getDocumentElement();
        System.out.println("Корневой элемент: " + root.getNodeName());

        List<Employee> staff = new ArrayList<>();

        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) nodeList;
            int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
            String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
            String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
            String counrty = element.getElementsByTagName("country").item(0).getTextContent();
            int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

            Employee employee = new Employee(id, firstName, lastName, counrty, age);
            staff = Arrays.asList(employee);
        }

        return staff;
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

        List<Employee> staff = new ArrayList<>();

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
