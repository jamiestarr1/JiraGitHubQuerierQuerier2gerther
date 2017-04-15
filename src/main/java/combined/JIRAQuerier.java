package combined;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 *
 *JIRAQuerier.idQuery()
 *accepts a jira Issue Key from the HTML JSP page displayed by the Apache Tomcat Server
 * Uses it to build a URL, which will be passed to the Util.queryAPI() method.
 * the returned JSON object will then be parsed for the description, concatinated with HTML
 * and then displayed to the user.
 *
 *
 */
public class JIRAQuerier {

    private static String JIRA_BASE_URL = "https://issues.apache.org/jira/rest/api/2/issue/";


    public static String idQuery(String jiraIssueKey) throws IOException {

        // Query the appropriate API
        URL completeJiraURL = new URL(JIRA_BASE_URL + jiraIssueKey);
        // Read all bytes from the URL, if it exists. If it does not, the code below will throw an IOException
        StringBuilder htmlStringBuilder = new StringBuilder();

        htmlStringBuilder.append("<div>");

        //use Util.queryAPI() method to retrieve JSON from Jira API.
        //then parse the returned JSON, and built html with issue key and description for displaying to the user.
        JSONObject json = Util.queryAPI(completeJiraURL);
        JSONObject root = json.getJSONObject("root");
        JSONObject fields = root.getJSONObject("fields");
        htmlStringBuilder.append("<h3>Description of " + jiraIssueKey + "</h3>");
        htmlStringBuilder.append("<body>" + fields.getString("description") +"</body>");

        return htmlStringBuilder.toString();
    }

}



