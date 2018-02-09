/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.ArrayList;

/**
 * @author Victor Lin
 */
public class HTTPView {

    public String getHTTPForm() {

        StringBuilder responseBuffer = new StringBuilder();
        responseBuffer
                .append("<html><body>")
                .append("<form submit='/submit'>")
                .append("Please fill out this form with appropriate fields <br>")
                .append("Name: <input type='text' name='name'><br>")
                .append("Street: <input type='text' name='street'><br>")
                .append("State: <input type='text' name='state'><br>")
                .append("Zip: <input type='text' name='zip'><br>")
                .append("<input type='submit' value='Submit'<br>")
                .append("</form>")
                .append("</body></html>");

        return responseBuffer.toString();
    }

    public String getThankYouForm(AddressModel addressModel) {

        StringBuilder responseBuffer = new StringBuilder();
        responseBuffer
                .append("<html><body>")
                .append("THANK YOU, " + addressModel.name + "<br>")
                .append("</body></html>");

        return responseBuffer.toString();
    }

    public String getErrorForm(AddressModel addressModel) {
        StringBuilder responseBuffer = new StringBuilder();
        responseBuffer
                .append("<html><body>")
                .append("<form submit='/submit'>")
                .append("There is an mistake in the form <br>")
                .append("Name: <input type='text' name='name' value=" + addressModel.name + ">" + "<br>")
                .append("Street: <input type='text' name='street' value=" + addressModel.street + ">" + "<br>")
                .append("State: <input type='text' name='state' value=" + addressModel.state + ">" + "<br>")
                .append("Zip: <input type='text' name='zip' value=" + addressModel.zip + ">" + "<br>")
                .append("<input type='submit' value='Submit'<br>")
                .append("</form>")
                .append("</body></html>");

        return responseBuffer.toString();
    }

    public String getAddressList() {
        StringBuilder responseBuffer = new StringBuilder();
        AddressListModel addressListModel = new AddressListModel();
        ArrayList<String> addresses = new ArrayList<String>();
        for (int k = 0; k < addressListModel.readInFile().size(); k++) {
            addresses.add(addressListModel.readInFile().get(k));
        }
        responseBuffer
                //you can append the append
                .append("<html><body>")
                .append("LIST OF ADDRESSES <br>")
                .append("<table style='width:100%' border='1'>")
                .append("<tr><th>Name</th><th>Street</th><th>State</th><th>Zip</th></tr>");

        for (int i = 0; i < addresses.size(); i++) {

            responseBuffer.append("<tr>");

            String[] wordSplit = addresses.get(i).split(" ");

            for (int k = 0; k < wordSplit.length; k++) {

                responseBuffer.append("<td align='center'>"
                        + wordSplit[k].replaceAll("\\+", "   "));
            }
            responseBuffer.append("</td>");
        }
        responseBuffer.append("</tr></table></body></html>");

        return responseBuffer.toString();
    }

    public String getHTTPData() throws IOException {
        StringBuilder responseBuffer = new StringBuilder();
        File fName = new File("AddressListModel.json");
        String line;
        responseBuffer.append("<html><body>");
        try {
            FileReader fr = new FileReader(fName);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                responseBuffer.append(line);
                System.out.println(line);
            }
            br.close();

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        responseBuffer.append("</body></html>");
        return responseBuffer.toString();
    }

    public String getHTTPJSON() throws IOException {
        StringBuilder responseBuffer = new StringBuilder();
        AddressModel am = new AddressModel();
        AddressListModel alm = new AddressListModel();
        alm.add(am);

        responseBuffer
                .append("<html><body>").append("")
                .append(alm.deserializeFromJSON())
                .append("<br>")
                .append("</body></html>");

        return responseBuffer.toString();
    }

}
