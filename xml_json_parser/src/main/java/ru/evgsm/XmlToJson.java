package ru.evgsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XmlToJson {
    public static void main (String [] arg) {
        List<Employee> employeeList = parseXml("data.xml");
        for (int i=0; i<employeeList.size(); i++){
            System.out.println(employeeList.get(i).toString());
        }

        try (FileWriter fileWriter = new FileWriter("data.json")){
            listToJson(employeeList,fileWriter);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void read(Node argNode){
        NodeList nodeList = argNode.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Текущий узел: " + node.getNodeName());
                Element element = (Element) node;
                NamedNodeMap map = element.getAttributes();
                for (int j=0; j<map.getLength();j++){
                    String attrName = map.item(j).getNodeName();
                    String attrValue = map.item(j).getNodeValue();
                    System.out.println("\tАтрибут: " + attrName + " = " + attrValue);
                }
                read(node);
                //System.out.println("Элемент age:" + element.getElementsByTagName("age").item(0).getTextContent());
            }
        }
    }

    private static List<Employee> parseXml(String xmlFileName) {

        List<Employee> employeeList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFileName));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            // Раскрываем рутовое дерево
            for (int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                // Погружаемся вглубь ветви employee, если находим нод с таким именем
                if ((node.getNodeType() == Node.ELEMENT_NODE) && (node.getNodeName().equals("employee"))) {
                    // Готовим параметры для заполнения по ходу чтения
                    long id = 0;
                    StringBuilder firstName = new StringBuilder("");
                    StringBuilder lastName = new StringBuilder("");
                    StringBuilder country = new StringBuilder("");
                    int age = 0;
                    // Ищем дочерние ноды ветви employee
                    NodeList ndLst = node.getChildNodes();
                    // Перебираем дочерние ноды
                    for (int j=0; j<ndLst.getLength(); j++) {
                        Node nd = ndLst.item(j);
                        // Ищем именно ноды, а не всякий мусор
                        if (nd.getNodeType() == Node.ELEMENT_NODE) {
                            // Если находим соответствующие ноды, раскрываем текст внутри
                            switch (nd.getNodeName()) {
                                case ("id"):
                                    id = Integer.parseInt(nd.getTextContent());
                                    break;
                                case("firstName"):
                                    firstName.append(nd.getTextContent());
                                    break;
                                case("lastName"):
                                    lastName.append(nd.getTextContent());
                                    break;
                                case("country"):
                                    country.append(nd.getTextContent());
                                    break;
                                case ("age"):
                                    age = Integer.parseInt(nd.getTextContent());
                                    break;
                                default:
                            }
                        }
                    }
                    employeeList.add(new Employee(id,firstName.toString(),lastName.toString(),country.toString(),age));
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    private static void listToJson(List<Employee> employeeList, FileWriter fileWriter) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        try {
            for (Employee emp: employeeList) {
                fileWriter.write(gson.toJson(emp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
