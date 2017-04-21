import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

/*TODO:
* Clean code
* add args value to change increment
* add a loop and a sleep value !! CHOOSE API REQUEST FREQUENCY WISELY !!
*
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

    public static void main(String[] args) throws IOException, JSONException {
        int increment = 5000;
        String apiKey = "AIzaSyBU_oWEIULi3-n96vWKETYCMsldYDAlz2M";// random API key I found on the web
        String channel = "UC7tdoGx0eQfRJm9Qj6GCs0A";//nourish. channel

        JSONObject json = readJsonFromUrl("https://www.googleapis.com/youtube/v3/channels?part=statistics&id="+channel+"&key="+apiKey);
        //System.out.println(json.toString());
        String sub = new JSONObject((
                new JSONObject(json
                        .get("items")
                        .toString()
                        .replaceAll("^\\[|]$","")))
                .get("statistics")
                .toString())
                .get("subscriberCount")
                .toString();
        int actualSub = Integer.parseInt(sub);
        int goal = increment*((actualSub/increment)+1);
        writer(actualSub+"\\"+goal);
        System.out.println(actualSub+"\\"+goal);
    }
}