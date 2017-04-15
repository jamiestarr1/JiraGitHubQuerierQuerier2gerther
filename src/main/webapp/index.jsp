<%@ page import="combined.GitHubQuerier" %>
<%@ page import="combined.JIRAQuerier" %><%--
Notes: fill in later

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel='stylesheet' href='webjars/bootstrap/3.2.0/css/bootstrap.min.css'>
    <link rel='stylesheet' href='main.css'>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <title>GitHub Querier</title>
  </head>
  <body>
  <div id="core">
    <h2>Push Events and their commit details</h2>
    <form action="index.jsp" method="GET">
      <div class="form-group">
        <label for="user"><h3>GitHub Username</h3></label>
        <input type="user" name="user_name" class="form-control" id="user"
               value="<%= request.getParameter("user_name") == null ? "" : request.getParameter("user_name")%>">
      </div>
      <%String user=request.getParameter("user_name"); %>
      <button type="submit" class="btn btn-default">
        <c:choose>
          <c:when test="${empty user}">
            Submit
          </c:when>
          <c:otherwise>
            Refresh
          </c:otherwise>
        </c:choose>
      </button>    </form>
    <div id="activity1">
      <%if (user != null && !user.isEmpty()){%>
          <%=GitHubQuerier.eventsAsHTML(user)%>
        <% } else { %>
            Please Enter the username you wish to query.
        <% }%>
    </div>
  </div>


  </body>
  <body>
  <div id="core2">
    <h2>Jira Issue info gatherer</h2>
    <form action="index.jsp" method="GET">
      <div class="form-group">
        <label for="user"><h3>Jira Issue Key</h3></label>
        <input type="ID" name="Jira ID" class="form-control" id="ID"
               value="<%= request.getParameter("Jira ID") == null ? "" : request.getParameter("Jira ID")%>">
      </div>
      <%String ID=request.getParameter("Jira ID"); %>
      <button type="submit" class="btn btn-default">
        <c:choose>
          <c:when test="${empty ID}">
            Submit
          </c:when>
          <c:otherwise>
            Refresh
          </c:otherwise>
        </c:choose>
      </button>    </form>
    <div id="activity2">
      <%if (ID != null && !ID.isEmpty()){%>
      <%=JIRAQuerier.idQuery(ID)%>
      <% } else { %>
      Please Enter the JiraID you wish to query.
      <% }%>
    </div>
  </div>

  <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.min.js"></script>
  <script type="text/javascript" src="webjars/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  </body>

</html>
