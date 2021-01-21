import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 * @author Dmitriy Panfilov
 * 21.01.2021
 */
public class HtmlParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        if (args.length != 0){
            int timeout = 500;
            if (args.length == 4){
                try {
                    timeout = Integer.parseInt(args[3]);

                }catch (NumberFormatException exception){
                    System.out.println("timeout  не целое число");
                }
            }
            int start = 0;
            int end = 0;
            try {
                 start = Integer.parseInt(args[1]);
                 end = Integer.parseInt(args[2]);
            }catch (NumberFormatException exception){
                System.out.println("Start или end не целое число");
            }
            System.out.println("address" + "\t" + "city" + "\t" + "email" + "\t" + "firstName" + "\t" + "lastName" + "\t" + "phone" + "\t");
            for (int i = start; i < end +1; i++){
                JsonNode root = objectMapper.readTree(parseToJsonString(getTextFromUrl(args[0], i, timeout)));
                System.out.print(root.findValue("address").asText() + "\t");
                System.out.print(root.findValue("city").asText() + "\t");
                System.out.print(root.findValue("email").asText() + "\t");
                System.out.print(root.findValue("firstName").asText() + "\t");
                System.out.print(root.findValue("lastName").asText() + "\t");
                System.out.print(root.findValue("phone").asText() + "\n");
            }


        }
    }
     //https://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
    private static String getTextFromUrl(String urlString, int param, int timeout){
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString + "=" + param);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();
        } catch (IOException exception){
            System.out.println("неправильный URL");
        }
        return response.toString();
    }

    private static String parseToJsonString(String value){
        int t = value.indexOf("{");
        int t1 = value.lastIndexOf("}");
        String st = value.substring(t, t1 + 1);
        int t3 = st.lastIndexOf("{");
        return st.substring(t3);
    }
}
