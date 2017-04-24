import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

/*TODO:
* Clean code
* add args value to change increment or conf file
* add a loop and a sleep value (thread pause/wait ?)
* !! CHOOSE API REQUEST FREQUENCY WISELY !!
**/


public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static void writer(String s) throws IOException {
        File subFile = new File("sub.log");
        FileWriter subWriter = new FileWriter(subFile, false);// false to overwrite.
        subWriter.write(s);
        subWriter.close();
    }

    public static String getSubNumber(String apiKey, String channel) throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://www.googleapis.com/youtube/v3/channels?part=statistics&id="+channel+"&key="+apiKey);
        return new JSONObject((
                new JSONObject(json
                        .get("items")
                        .toString()
                        .replaceAll("^\\[|]$","")))
                .get("statistics")
                .toString())
                .get("subscriberCount")
                .toString();
    }

    public static void main(String[] args) {
        int increment = 5000;
        String apiKey = "PUT_YOUR_API_KEY_HERE";// random API key I found on the web
        String channel = "UC7tdoGx0eQfRJm9Qj6GCs0A";//nourish. channel
        
        //System.out.println(json.toString());
        String sub = null;
        try {
            sub = getSubNumber(apiKey,channel);
        } catch (IOException e) {
            System.out.println("I/O Error\n");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("JSON Error\n");
            e.printStackTrace();
        }
        int actualSub = Integer.parseInt(sub);
        int goal = increment*((actualSub/increment)+1);
        try {
            writer(actualSub+"\\"+goal);
        } catch (IOException e) {
            System.out.println("I/O Error : writer error\n");
            e.printStackTrace();
        }
        System.out.println(actualSub+"\\"+goal);
    }
}