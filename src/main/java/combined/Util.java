package combined;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * queryAPI() method
 * This utility accepts a URL reference, and using a BufferedReader, will take the content of the returned JSON,
 * and concatinate it within {root:  and close curly brace. This allows for the root key to be used first when
 * attempting to work with the JSON data.
 *
 *returns the concatinated JSON data which then can be parsed using key values in the cass that called queryAPI()
 *
 *
 */
public class Util {

    public static JSONObject queryAPI(URL url) throws IOException {

        // Read all bytes from the URL, if it exists. If it does not, the code below will throw an IOException,
        // which will be caught and interpreted as a non JIRA-link in above method (spurious matches for the regex sometimes occur)
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder stringBuilder = new StringBuilder();
        int characterPosition;
        while ((characterPosition = bufferedReader.read()) != -1) {
            stringBuilder.append((char) characterPosition);
        }
        bufferedReader.close();
        String contentJSON = stringBuilder.toString();

        // Extract issue-type from the JSON object return.
        String jsonText = "{root:" + contentJSON + "}";
        return new JSONObject(jsonText);
    }
}
