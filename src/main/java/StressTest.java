import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import sun.net.www.http.HttpClient;

public class StressTest implements Runnable {

    static Integer counter = 0; // all threads share this, too

    private String getURL() {


        synchronized (counter) {
            counter++;
        }

        return "http://localhost:12345/address?name=John&street=Street" + counter + "&state=PA&zip=16803";
        //return "http://localhost:12345";
    }

    private final String USER_AGENT = "Mozilla/5.0";

    private final StringBuilder nominalResponse; // shared between threads

    int success = 0;

    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        int succ = 0;
        int total = 0;

        StringBuilder goodResponse = new StringBuilder();

        // Let's do it 10 times
        for (int j = 0; j < 10; j++) {
            List<Pair<Thread, StressTest>> ts = new ArrayList<>();

            // Send 50 requests at the same time
            for (int i = 0; i < 50; i++) {
                StressTest http = new StressTest(goodResponse); // all threads share goodResponse

                Thread t = new Thread(http);
                ts.add(new Pair(t, http));
                t.start();

            }

            for (Pair p : ts) {
                ((Thread) p.getKey()).join();
                succ += ((StressTest) p.getValue()).success;
                total++;

            }
        }
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        System.err.flush();
        System.out.flush();

        System.out.println(String.format("finished.  %s ms.", elapsed));
        System.out.println(String.format("%s out of %s successful.", succ, total));

        // single-threaded server: 17 seconds
        // multi-threaded server:
    }

    public StressTest(StringBuilder sharedResponse) {
        nominalResponse = sharedResponse;
    }

    public void run() {

        String response = null;
        try {
            response = sendGet();
        } catch (Exception ex) {
            System.err.println("Request failed (exception) " + ex);
        }

        if (response == null) {
            System.err.println("Request returned null response.");
        } else {
            synchronized (nominalResponse) {

                if (nominalResponse.length() == 0) {
                    nominalResponse.append(response);
                }

                if (nominalResponse.toString().equals(response)) {
                    success = 1;
                } else {
                    System.err.println("Request returned inconsistent response.");
                    System.err.println("<RECEIVED>" + response + "</RECEIVED>");
                    System.err.println("<EXPECTED>" + nominalResponse.toString() + "</EXPECTED>");
                }

            }
        }

    }

    // HTTP GET request
    private String sendGet() throws Exception {

        URL obj = new URL(getURL());
        // This is a different way to make a HttpURLConnection
        // Some convenience provided by Java
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return (response.toString());

    }
}
