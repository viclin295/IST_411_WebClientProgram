/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.net.InetAddress.getLocalHost;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Victor Lin
 */
public class HTTPController implements Runnable {

    private HTTPView httpView = new HTTPView();
    private Socket socket;

    public HTTPController(Socket socket) {

        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("\nClientHandler Started for "
                + this.socket);
        handleRequest(this.socket);
        System.out.println("ClientHandler Terminated for "
                + this.socket + "\n");
    }

    public void handleRequest(Socket socket) {
        HTTPRequest req = null;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));) {
            ArrayList<String> requestString = new ArrayList<String>();

            while (true) {
                String readLine = in.readLine();
                System.out.println(readLine);
                if (!readLine.isEmpty()) {
                    requestString.add(readLine);
                } else {
                    break;
                }
            }

            req = new HTTPRequest(requestString);

            if (req.getOp().equals("GET")) {

                if (req.getURL().equals("/list")) {
                    sendResponse(socket, 200, httpView.getAddressList());
            } else if (req.getURL().equals("/data")) {
                sendResponse(socket, 200, httpView.getHTTPData());
            } else if (req.getURL().equals("/json")) {
                    sendResponse(socket,200,httpView.getHTTPJSON());
            } else if (req.getURL().equals("/address")) {
                if (req.hasParams()) {
                    AddressModel addressModel = new AddressModel(req.getParams());
                    if (addressModel.isValid()) {
                        sendResponse(socket, 200, httpView.getThankYouForm(addressModel));//thank you html
                        System.out.println(">>Thank You form sent");
                        AddressListModel addressListModel = new AddressListModel();
                        addressListModel.add(addressModel);
                        addressListModel.writeToFile();
                        addressListModel.saveToJSON(addressModel);
                    } else {
                        sendResponse(socket, 200, httpView.getErrorForm(addressModel));//go back to form but with entries
                        System.out.println(">>The form has some errors");
                    }
                } else {
                    sendResponse(socket, 200, httpView.getHTTPForm()); //go back to form with error msg
                    System.out.println(">>HTTP form was sent");
                }
            } else {
                sendResponse(socket, 404, "error");
                req.getURL().equals("/index.html");
//                    sendResponse(socket, 200, httpView.getAddressList());
                System.out.println("404 error");
            }
        } else{
            sendResponse(socket, 405, "error");
            System.out.println("405 error");
        }

    } catch(IOException ie)

    {
        ie.printStackTrace();
        throw new RuntimeException();
    }

}

    //sends response 200 HTTP OK from textbook
    public void sendResponse(Socket socket, int statusCode, String responseString) {
        String statusLine;
        String serverHeader = "Server: WebServer\r\n";
        String contentTypeHeader = "Content-Type: text/html\r\n";

        try (DataOutputStream out
                     = new DataOutputStream(socket.getOutputStream());) {
            if (statusCode == 200) {
                statusLine = "HTTP/1.0 200 OK" + "\r\n";
                String contentLengthHeader = "Content-Length: "
                        + responseString.length() + "\r\n";

                out.writeBytes(statusLine);
                out.writeBytes(serverHeader);
                out.writeBytes(contentTypeHeader);
                out.writeBytes(contentLengthHeader);
                out.writeBytes("\r\n");
                out.writeBytes(responseString);
            } else if (statusCode == 405) {
                statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            } else {
                statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            }
            out.close();
            out.flush();
        } catch (IOException ex) {
            // Handle exception
            System.out.println("exception occured");
        }
    }
}
