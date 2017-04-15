package combined;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *GitHubQuerier.eventsAsHTML()
 *accepts a github user from the HTML JSP page displayed by the Apache Tomcat Server
 * Uses it to build a URL, which will be passed to the Util.queryAPI() method.
 * the returned JSON object will then be parsed for the Push events only, and the comments contained
 * within those push events, concatenated within the HTML page, stored within a collapsible table,
 * and then displayed to the user.
 */
public class GitHubQuerier {

    //what about pages of events? there are more pages,and would explain why you dont count to 10 sometimes on vincents page

    private static final String GITHUB_BASE_URL = "https://api.github.com/users/";

    public static String eventsAsHTML(String user) throws IOException, ParseException {
        List<JSONObject> response = getEvents(user);
        StringBuilder htmlStringBuilder = new StringBuilder();
        htmlStringBuilder.append("<div>");

        int pushCount = 1;
        String pushType = "PushEvent";

        for (int i = 0; i < response.size(); i++) {
            JSONObject event = response.get(i);

          if (pushType.equalsIgnoreCase(event.getString("type"))){

                // Get event type
                String eventType = event.getString("type");

                // Get created_at date, then format the date for later display
                String creationDate = event.getString("created_at");
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
                Date date = inFormat.parse(creationDate);
                String formattedDate = outFormat.format(date);

                // Add type of event as header
                htmlStringBuilder.append("<h3 class=\"type\">");
                htmlStringBuilder.append(eventType);
                htmlStringBuilder.append("</h3>");

                // Add formatted date to HTML page using string builder
                htmlStringBuilder.append(" on ");
                htmlStringBuilder.append(formattedDate);
                htmlStringBuilder.append("<br />");
                //a vertically formatted list of all its commits with their SHA ( abbrev to first 8 characters)
                // and their commit message in a collapsible box.
                //Is inside conditional, as a Push may or may not actually contain commits
                JSONObject payload = event.getJSONObject("payload");
                if(payload.has("commits")){

                    JSONArray commitArray;
                    commitArray = payload.getJSONArray("commits");
                    htmlStringBuilder.append("<a data-toggle=\"collapse\" href=\"#event-" + i + "\">Commits and their \"Messages\"</a>");
                    htmlStringBuilder.append("<div id=event-" + i + " class=\"collapse\" style=\"height: auto;\"> <pre>");

                    //uses a counter so that commits can be ordered and labeled with a number
                    for (int j = 0; j < commitArray.length(); j++) {
                        JSONObject jsonobject = commitArray.getJSONObject(j);
                        String sha = jsonobject.getString("sha");
                        String message = jsonobject.getString("message");
                        int counter = j+1;
                        htmlStringBuilder.append("<p>" + counter +") ");
                        htmlStringBuilder.append(sha.substring(0,8));
                        htmlStringBuilder.append(" \"" + message +"\"");
                        htmlStringBuilder.append("</p>");
                    }
                    htmlStringBuilder.append("</pre> </div>");
                }
          }
        }
        htmlStringBuilder.append("</div>");
        return htmlStringBuilder.toString();
    }


    private static List<JSONObject> getEvents(String githubUserName) throws IOException {
        String otherArgumentsURL = "&page=";
        int pageNumber = 1;
        boolean JSONArrayEmpty = false;
        List<JSONObject> eventList = new ArrayList<JSONObject>();
        while (JSONArrayEmpty == false) {
            String completeGitHubURL = GITHUB_BASE_URL + githubUserName + "/events?" + otherArgumentsURL + pageNumber;
            JSONObject githubJSON = Util.queryAPI(new URL(completeGitHubURL));
            JSONArray GitHubEventsJsonArray = githubJSON.getJSONArray("root");
            if (GitHubEventsJsonArray.length() == 0) {
                JSONArrayEmpty = true;
            } else {
                for (int i = 0; i < GitHubEventsJsonArray.length(); i++) {
                    eventList.add(GitHubEventsJsonArray.getJSONObject(i));
                }
                pageNumber++;
            }
        }
        return eventList;

    }
}