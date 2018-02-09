/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Victor Lin
 */
public class HTTPRequest {

    private String operation;
    private String url;
    private Map<String, String> params;

    public HTTPRequest(ArrayList<String> requestString) {
        params = new HashMap<String, String>();
        parseGet(requestString.get(0));
    }

    public String getURL() {
        return url;
    }

    public String getOp() {
        return operation;
    }

    public boolean hasParams() {
        return !(params.isEmpty());
    }

    public Map<String, String> getParams() {
        return params;
    }

    private void parseGet(String get) {

        String[] queryParts = get.split(" ");
        operation = queryParts[0];
        String[] url_params = queryParts[1].split("\\?");
        url = url_params[0];
        if (url_params.length > 1) {
            String paramString = url_params[1];  
            String[] keyValue = paramString.split("&");
            for (String value : keyValue) {
                String[] kv = value.split("=", -1);
                params.put(kv[0], kv[1]);
            }
        }
    }
}
