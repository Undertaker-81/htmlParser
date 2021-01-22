import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Dmitriy Panfilov
 * 21.01.2021
 */
public class HtmlParser {

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
                Map<String, String> result = parseString(getTextFromUrl(args[0], i, timeout));
 //               System.out.println(parseString(getTextFromUrl(args[0], i, timeout)));
                System.out.print(result.get("address") + "\t");
                System.out.print(result.get("city") + "\t");
                System.out.print(result.get("email") + "\t");
                System.out.print(result.get("firstName") + "\t");
                System.out.print(result.get("lastName") + "\t");
                System.out.print(result.get("phone") + "\n");
            }


        }
    }
     //https://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
    private static String getTextFromUrl(String urlString, int param, int timeout){
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString  + param);
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

    private static Map<String, String> parseString(String value){
        int t1 = value.lastIndexOf("{");
        int t2 = value.lastIndexOf("}");
        String[] params = value.substring(t1+1, t2).split(",");
        Map<String, String> mapParams = new HashMap<>();
        for(int i = 0; i < params.length; i++){
          String[] strings =  params[i].split(":");
          mapParams.put(strings[0].substring(1,strings[0].length()-1), strings[1].substring(1,strings[1].length()-1));
        }
        return mapParams;
    }
}
