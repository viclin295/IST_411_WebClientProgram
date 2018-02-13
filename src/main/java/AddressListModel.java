/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Victor Lin
 */
public class AddressListModel {

    ArrayList<AddressModel> database;
    AddressModel addressModel;
    static ObjectMapper mapper;

    static String jsonInString;
    String fileName = "AddressList.txt";
    String jsonFile = "AddressList.json";

    String name = "";

    public AddressListModel() {
        database = new ArrayList<AddressModel>();
    }

    public void add(AddressModel model) {
        database.add(model);
    }

    public String returnData() {
        for (int i = 0; i < database.size(); i++) {
            return "" + database.get(i).getName() + " "
                    + database.get(i).getStreet() + " "
                    + database.get(i).getState() + " "
                    + database.get(i).getZIP() + "\n";
        }
        return "list is empty";
    }

    public synchronized void writeToFile() {
        File log = new File("AddressList.txt");
        try {
            if (log.exists() == false) {
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            out.write(database.get(0).getName() + " "
                    + database.get(0).getStreet() + " "
                    + database.get(0).getState() + " "
                    + database.get(0).getZIP() + "\r\n");

            out.close();
        } catch (IOException e) {
            System.out.println("COULD NOT LOG!!");
        }

    }

    public synchronized ArrayList<String> readInFile() {
        String fileName = "AddressList.txt";
        String line = null;
        String word = null;
        ArrayList<String> listOfAddress = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                listOfAddress.add(line);
            }
            bufferedReader.close();
            return listOfAddress;
            

        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '"
                    + fileName + "'");

        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");

        }
        return null;
    }
    static public String serializeToJSON(AddressModel am) throws IOException{
        mapper = new ObjectMapper();
        jsonInString = mapper.writeValueAsString(am);
        System.out.println(jsonInString);
        return jsonInString;
    }

    static public AddressListModel deserializeFromJSON() throws IOException{
        File file = new File("AddressListModel.json");
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.readValue(file, AddressModel.class);
        AddressListModel addressListModel = mapper.readValue(jsonInString, AddressListModel.class);
        return addressListModel;
    }

    void saveToJSON(AddressModel am) throws IOException{
        File inputFile = new File("AddressListModel.json");
        try {
            if (inputFile.exists() == false) {
                System.out.println("We had to make a new file.");
                inputFile.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(inputFile, true));
            out.write(serializeToJSON(am));

            out.close();
        } catch (IOException e) {
        }
    }

    public String readFromJSON() throws IOException{
        return deserializeFromJSON().toString();
    }
}
