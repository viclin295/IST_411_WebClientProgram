/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Victor Lin
 */
public class HTTPSimpleForm {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Waiting for connection...");
        while (true) {
            Socket socket = serverSocket.accept();
                System.out.println("Connection made");  
                HTTPController controller = new HTTPController(socket);
                Thread th = new Thread(controller);
                th.start();
//            controller.run();
        }
    }
}
