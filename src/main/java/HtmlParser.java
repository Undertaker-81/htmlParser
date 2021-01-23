import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Dmitriy Panfilov
 * 21.01.2021
 */
public class HtmlParser {

    public static void main(String[] args) throws IOException, InterruptedException {
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
            String url = args[0];
            if (!url.contains("=")){
                url = url + "=";
            }
            System.out.println("address" + "," + "city" + "," + "email" + "," + "firstName" + "," + "lastName" + "," + "phone" + ",");
            for (int i = start; i < end +1; i++){
                Map<String, String> result = parseString(getTextFromUrl(url, i).replace("{",""));
 //               System.out.println(parseString(getTextFromUrl(args[0], i, timeout)));
                System.out.print(result.get("address") + ",");
                System.out.print(result.get("city") + ",");
                System.out.print(result.get("email") + ",");
                System.out.print(result.get("firstName") + ",");
                System.out.print(result.get("lastName") + ",");
                System.out.print(result.get("phone") + ",\n");
                Thread.sleep(timeout);
            }
        }else {
            System.out.println("usage: java -jar jarfile URL star end [timeout]");
        }

    }
     //https://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
    private static String getTextFromUrl(String urlString, int param){
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString  + param);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(500);
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

    private static Map<String, String> parseString(String value){

        Pattern pattern = Pattern.compile("\"(?<field>.+?)\":\"(?<value>.*?)\"");
        Matcher matcher = pattern.matcher(value);
        Map<String, String> mapParams = new HashMap<>();
        while(matcher.find()) {
         mapParams.put(matcher.group("field"),matcher.group("value") );

        }
        return mapParams;

    }
}
