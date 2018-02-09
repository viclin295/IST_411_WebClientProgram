/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Victor Lin
 */
public class AddressModel extends HTTPModel {

    String name = "";
    String street = "";
    String state = "";
    String zip = "";
    String country = "";
    String jsonInString;
    public AddressModel(){
        
    }

    public AddressModel(String nameIn, String streetIn, String stateIn, String zipIn) {
        name = nameIn;
        street = streetIn;
        state = stateIn;
        zip = zipIn;
        country = "US";
    }

    public AddressModel(Map<String, String> params) {
        this(params.get("name"), params.get("street"), params.get("state"), params.get("zip"));
    }

    public Boolean isValid() {
        if (name.length() > 0 && zip.length() == 5) {
            
            if (street.length() > 1 && state.length() == 2) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getZIP() {
        return zip;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String serializeToJSON(AddressModel am) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("AddressModel.json"), am);
        jsonInString = mapper.writeValueAsString(am);
        return jsonInString;
    }

    public AddressModel deserializeFromJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(new File("AddressModel.json"), AddressModel.class);
        AddressModel am = mapper.readValue(jsonInString, AddressModel.class);
        return am;
    }
}
