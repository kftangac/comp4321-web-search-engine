<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
</head>
<body>
<%@ page import="Manage_Rocksdb.Manage_Rocksdb.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.rocksdb.*" %>
<%@ page import="javax.xml.stream.FactoryConfigurationError" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
      integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>

<%
    String lastSearchResult = "";
    if (request.getParameter("txtname") != null && !request.getParameter("txtname").equals("")) {
        lastSearchResult = request.getParameter("txtname");
    }
%>
<main class="container">
    <br><br>
    <h1>COMP4321 Group 11</h1>
    <form method="post">
        <br>
        <div class="row">
            <div class="col-5">
                <input class="form-control" type="search" placeholder="Please enter a list of words, separated by a space"
                       aria-label="Search" name="txtname" value="<%=lastSearchResult%>">
            </div>
            <div class="col-4">
                <button class="btn btn-outline-success" type="submit">Search</button>
            </div>
        </div>
    </form>
    <%
        String s = request.getParameter("txtname");
    %>

    <%
        String hi = "no error";
        List<String> retrieval_result = null;
        List<String> parentResult = null;
        List<String> childResult = null;

        ArrayList<String> scores = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> url = new ArrayList<>();
        ArrayList<String> last_modification_date = new ArrayList<>();
        ArrayList<String> size = new ArrayList<>();
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> parentLinks = new ArrayList<>();
        ArrayList<String> childLinks = new ArrayList<>();

        if (s != null && !s.equals("")) {
            RetrievalFunction rf = null;
            try {
                rf = new RetrievalFunction(s);
            } catch (Exception ex) {
                hi = ex.toString();
            }
            try {
                retrieval_result = rf.get_retrieval_result();
            } catch (Exception ex) {
                hi = ex.toString();
            }

            for (int i = 0; retrieval_result != null && i < retrieval_result.size(); i++) {
                List<String> record = Arrays.asList(retrieval_result.get(i).split(","));
                scores.add(String.format("%.2f", Double.parseDouble(record.get(0))));
                temp.add(record.get(1));
                title.add(record.get(2));
                url.add(record.get(3));
                last_modification_date.add(record.get(4));
                size.add(record.get(5));

                String reformatted = record.get(6).replace("\n", "<br>");
                keywords.add(reformatted);

            }

            parentResult = rf.get_p_links();
            for (int i = 0; parentResult != null && i < parentResult.size(); i++) {
                String reformatted = parentResult.get(i).replace(",", "<br>");
                parentLinks.add(reformatted);
            }
            childResult = rf.get_c_links();
            for (int i = 0; childResult != null && i < childResult.size(); i++) {
                String reformatted = childResult.get(i).replace(",", "<br>");
                childLinks.add(reformatted);
            }

        }
    %>
    <%--    <%=childResult%>--%>
    <%
        boolean sessionExist = false;
        if (session.isNew()) {
            session.setMaxInactiveInterval(1800);
        } else {
            sessionExist = true;
            if (request.getParameter("txtname") != null && !request.getParameter("txtname").equals("")) {
                List<String> search_history = new ArrayList<>();
                List<String> search_date = new ArrayList<>();
                if (session.getAttribute("name") != null) {
                    search_history = (ArrayList<String>) session.getAttribute("name");
                    search_history.add(request.getParameter("txtname"));

                    search_date = (ArrayList<String>) session.getAttribute("search_date");
                    search_date.add((new java.util.Date()).toString());
                } else {
                    search_history.add(request.getParameter("txtname"));
                    search_date.add((new java.util.Date()).toString());
                }
                session.setAttribute("name", search_history);
                session.setAttribute("search_date", search_date);
            }

            if (session.getAttribute("name") == null) {
                session.invalidate();
            }
        }
    %>
    <%--    <%=childLinks%>--%>

    <%--    <%=hi%>--%>
    <%--    <%=s%>--%>
    <div class="row">
        <div class="col-6">
            <div class="my-3 p-3 bg-body rounded shadow-sm">
                <h6 class="border-bottom pb-2 mb-0">Search Results</h6>
                <% if (retrieval_result != null && retrieval_result.size() > 0) { %>
                <% for (int i = 0; i < retrieval_result.size(); i++) { %>
                <div class="d-flex pt-3">
                    <div class="badge bg-success"><%=scores.get(i) %>
                    </div>
                    <div class="p-3 mb-0 small lh-sm border-bottom w-100">
                        <div class="d-flex justify-content-between">
                            <a href="<%=url.get(i)%>"><h6><strong><%=title.get(i) %>
                            </strong></h6></a>
                        </div>
                        <span class="d-block"><a href="<%=url.get(i)%>"><%=url.get(i) %></a></span>
                        <span class="d-block">Last modification date:<%=last_modification_date.get(i)%>, Size: <%=size.get(i) %></span>
                        <br>
                        <div class="accordion" id="accordionExample">
                            <div class="accordion-item">
                                <h2 class="accordion-header" id="<%="heading"+i+"_1"%>">
                                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#<%="collapse"+i+"_1"%>" aria-expanded="false"
                                            aria-controls="<%="collapse"+i+"_1"%>">
                                        Show keyword frequency
                                    </button>
                                </h2>
                                <div id="<%="collapse"+i+"_1"%>" class="accordion-collapse collapse"
                                     aria-labelledby="<%="heading"+i+"_1"%>" data-bs-parent="#accordionExample">
                                    <div class="accordion-body">
                                        <span class="d-block"><%=keywords.get(i) %></span>
                                    </div>
                                </div>
                            </div>
                            <div class="accordion-item">
                                <h2 class="accordion-header" id="<%="heading"+i+"_2"%>">
                                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#<%="collapse"+i+"_2"%>" aria-expanded="false"
                                            aria-controls="<%="collapse"+i+"_2"%>">
                                        Show parent link
                                    </button>
                                </h2>
                                <div id="<%="collapse"+i+"_2"%>" class="accordion-collapse collapse"
                                     aria-labelledby="<%="heading"+i+"_2"%>" data-bs-parent="#accordionExample">
                                    <div class="accordion-body">
                                        <span class="d-block"><%=parentLinks.get(i) %></span>
                                    </div>
                                </div>
                            </div>
                            <div class="accordion-item">
                                <h2 class="accordion-header" id="<%="heading"+i+"_3"%>">
                                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#<%="collapse"+i+"_3"%>" aria-expanded="false"
                                            aria-controls="<%="collapse"+i+"_3"%>">
                                        Show child link
                                    </button>
                                </h2>
                                <div id="<%="collapse"+i+"_3"%>" class="accordion-collapse collapse"
                                     aria-labelledby="<%="heading"+i+"_3"%>" data-bs-parent="#accordionExample">
                                    <div class="accordion-body">
                                        <span class="d-block"><%=childLinks.get(i) %></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <% }%>
                <% } else {%>
                <div class="p-3 mb-0 small lh-sm border-bottom w-100">
                    No Search Results is found.
                </div>
                <% } %>
            </div>
        </div>

        <div class="col-6">
            <div class="my-3 p-3 bg-body rounded shadow-sm">
                <h6 class="border-bottom pb-2 mb-0">Search History</h6>

                <div class="p-3 small border-bottom w-100">
                    <%
                        List<String> search_history = new ArrayList<>();
                        List<String> search_date = new ArrayList<>();
                        if (sessionExist && s != null && !s.equals("") && session.getAttribute("name") != null && session.getAttribute("search_date") != null)
                            search_history = (ArrayList<String>) session.getAttribute("name");
                        search_date = (ArrayList<String>) session.getAttribute("search_date");
                        for (int i = search_history.size() - 1; i >= 0; i--) {
                    %>
                    <div class="row">
                        <div class="col-6">
                            <form method="post" action="#">
                                <input hidden name="txtname" id="<%="search_history"+i%>">
                                <button class="btn btn-outline-success" type="submit">
                                    <%=search_history.get(i)%>
                                </button>
                            </form>
                        </div>
                        <div class="col-6">
                            Visited on
                            <%=search_date.get(i)%>
                        </div>
                    </div>
                    <%
                        }
                        if (search_history.size() == 0) {
                    %>
                    No Search History is found.
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous"></script>

<script type="text/javascript">
    <%
        for (int i = search_history.size() - 1; i >= 0; i--) {
    %>
    document.getElementById('<%="search_history"+i%>').value = '<%=search_history.get(i)%>';
    <%
        }
    %>

</script>


</body>
</html>
	