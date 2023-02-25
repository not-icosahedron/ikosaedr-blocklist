import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FetchUrlsAndWriteToFile {

    public static void main(String[] args) {
        ArrayList<String> urlList = new ArrayList<>();
        String urlFile = "urls.txt";
        String outputFile = "ikosaedr-blocklist.txt";

        // Read the list of URLs from the file
        try (BufferedReader br = new BufferedReader(new FileReader(urlFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                urlList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fetch the content of each URL and append it to the output file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true))) {
            for (String url : urlList) {
                URLConnection conn = new URL(url).openConnection();
                conn.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    bw.write(inputLine);
                    bw.newLine();
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished writing content to " + outputFile);
    }
}