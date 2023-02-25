import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

        // Delete the previous output file (if it exists)
        File file = new File(outputFile);
        if (file.exists()) {
            file.delete();
        }

        // Fetch the content of each URL and append it to the output file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            int numUrls = urlList.size();
            for (int i = 0; i < numUrls; i++) {
                String url = urlList.get(i);
                System.out.println("Downloading URL " + (i + 1) + " of " + numUrls + ": " + url);
                URLConnection conn = new URL(url).openConnection();
                conn.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    bw.write(inputLine);
                    bw.newLine();
                }
                in.close();
                printProgressBar((i + 1) / (double) numUrls);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinished writing content to " + outputFile);
    }

    private static void printProgressBar(double progress) {
        int width = 50;
        System.out.print("\r[");
        int pos = (int) (width * progress);
        for (int i = 0; i < width; i++) {
            if (i < pos) {
                System.out.print("=");
            } else if (i == pos) {
                System.out.print(">");
            } else {
                System.out.print(" ");
            }
        }
        System.out.printf("] %.1f%%", progress * 100);
    }
}