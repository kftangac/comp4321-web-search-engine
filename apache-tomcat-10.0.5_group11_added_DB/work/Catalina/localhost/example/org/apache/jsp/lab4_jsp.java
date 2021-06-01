/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/10.0.5
 * Generated at: 2021-05-02 14:42:05 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;
import Manage_Rocksdb.Manage_Rocksdb.*;
import java.util.*;
import org.rocksdb.*;

public final class lab4_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final jakarta.servlet.jsp.JspFactory _jspxFactory =
          jakarta.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("org.rocksdb");
    _jspx_imports_packages.add("java.util");
    _jspx_imports_packages.add("jakarta.servlet");
    _jspx_imports_packages.add("Manage_Rocksdb.Manage_Rocksdb");
    _jspx_imports_packages.add("jakarta.servlet.http");
    _jspx_imports_packages.add("jakarta.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile jakarta.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public jakarta.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final jakarta.servlet.http.HttpServletRequest request, final jakarta.servlet.http.HttpServletResponse response)
      throws java.io.IOException, jakarta.servlet.ServletException {

    if (!jakarta.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final jakarta.servlet.jsp.PageContext pageContext;
    jakarta.servlet.http.HttpSession session = null;
    final jakarta.servlet.ServletContext application;
    final jakarta.servlet.ServletConfig config;
    jakarta.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    jakarta.servlet.jsp.JspWriter _jspx_out = null;
    jakarta.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<html>\n");
      out.write("<head>\n");
      out.write("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css\" rel=\"stylesheet\"\n");
      out.write("      integrity=\"sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6\" crossorigin=\"anonymous\">\n");
      out.write("<link rel=\"stylesheet\" href=\"https://pro.fontawesome.com/releases/v5.10.0/css/all.css\"\n");
      out.write("      integrity=\"sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p\" crossorigin=\"anonymous\"/>\n");
      out.write("\n");
      out.write("<main class=\"container\">\n");
      out.write("    <h1>COMP4321 Group 11</h1>\n");
      out.write("    <form method=\"post\" action=\"#\">\n");
      out.write("        <br>\n");
      out.write("        <div class=\"row\">\n");
      out.write("            <div class=\"col-5\">\n");
      out.write("                <input class=\"form-control\" type=\"search\" placeholder=\"Please enter a list of words, separated by space\"\n");
      out.write("                       aria-label=\"Search\" name=\"txtname\">\n");
      out.write("            </div>\n");
      out.write("            <div class=\"col-4\">\n");
      out.write("                <button class=\"btn btn-outline-success\" type=\"submit\">Search</button>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </form>\n");
      out.write("    ");

        String s = request.getParameter("txtname");
    
      out.write("\n");
      out.write("\n");
      out.write("    ");

        String hi = "no error";
        List<String> retrieval_result = null;
        ArrayList<String> scores = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> url = new ArrayList<>();
        ArrayList<String> last_modification_date = new ArrayList<>();
        ArrayList<String> size = new ArrayList<>();
        ArrayList<String> keywords = new ArrayList<>();

        if (s != null) {
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
                scores.add(record.get(0));
                temp.add(record.get(1));
                title.add(record.get(2));
                url.add(record.get(3));
                last_modification_date.add(record.get(4));
                size.add(record.get(5));
                keywords.add(record.get(6));
            }
        }
//        retrieval_result = new ArrayList<>();
//        retrieval_result.add("dawdawawd");
//        retrieval_result.add("dwawada");
    
      out.write("\n");
      out.write("    ");


        session.isNew();
        session.setMaxInactiveInterval(18000);
        if (request.getParameter("txtname") != null) {

//            out.println("Hi! " + session.getAttribute("name"));

            List<String> search_history = new ArrayList<>();
            if (session.getAttribute("name") != null) {
                search_history = (ArrayList<String>) session.getAttribute("name");
                search_history.add(request.getParameter("txtname"));
            } else {
                search_history.add(request.getParameter("txtname"));
            }

            session.setAttribute("name", search_history);
        }

        if (session.getAttribute("name") == null) {
            session.invalidate();
//            out.println("You have not entered your name! Please refresh");
        } else {
//            out.println("bye! " + session.getAttribute("name"));
            List<String> search_history = new ArrayList<>();
            search_history = (ArrayList<String>) session.getAttribute("name");
            for (int i = 0; i < search_history.size(); i++) {
//                out.println(search_history.get(i));
            }
        }
    
      out.write("\n");
      out.write("\n");
      out.write("    ");
      out.print(hi);
      out.write("\n");
      out.write("    ");
      out.print(s);
      out.write("\n");
      out.write("    <div class=\"row\">\n");
      out.write("        <div class=\"col-6\">\n");
      out.write("            <div class=\"my-3 p-3 bg-body rounded shadow-sm\">\n");
      out.write("                ");
 if (/*hi.equals("no error") && */retrieval_result != null) { 
      out.write("\n");
      out.write("                <h6 class=\"border-bottom pb-2 mb-0\">Search Results</h6>\n");
      out.write("                ");
 for (int i = 0; i < retrieval_result.size(); i++) { 
      out.write("\n");
      out.write("                <div class=\"d-flex pt-3\">\n");
      out.write("                    <div class=\"badge bg-danger\">");
      out.print("1.23" );
      out.write("\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"p-3 mb-0 small lh-sm border-bottom w-100\">\n");
      out.write("                        <div class=\"d-flex justify-content-between\">\n");
      out.write("                            <strong>");
      out.print(scores.get(i) );
      out.write("\n");
      out.write("                            </strong>\n");
      out.write("                        </div>\n");
      out.write("                        <span class=\"d-block\">");
      out.print(title.get(i) );
      out.write("</span>\n");
      out.write("                        <span class=\"d-block\">");
      out.print(url.get(i) );
      out.write("</span>\n");
      out.write("                        <span class=\"d-block\">");
      out.print(last_modification_date.get(i)+","+size.get(i) );
      out.write("</span>\n");
      out.write("\n");
      out.write("                        <div class=\"accordion\" id=\"accordionExample\">\n");
      out.write("                            <div class=\"accordion-item\">\n");
      out.write("                                <h2 class=\"accordion-header\" id=\"");
      out.print("heading"+i+"_1");
      out.write("\">\n");
      out.write("                                    <button class=\"accordion-button collapsed\" type=\"button\" data-bs-toggle=\"collapse\"\n");
      out.write("                                            data-bs-target=\"#");
      out.print("collapse"+i+"_1");
      out.write("\" aria-expanded=\"false\"\n");
      out.write("                                            aria-controls=\"");
      out.print("collapse"+i+"_1");
      out.write("\">\n");
      out.write("                                        Show keyword frequency\n");
      out.write("                                    </button>\n");
      out.write("                                </h2>\n");
      out.write("                                <div id=\"");
      out.print("collapse"+i+"_1");
      out.write("\" class=\"accordion-collapse collapse\"\n");
      out.write("                                     aria-labelledby=\"");
      out.print("heading"+i+"_1");
      out.write("\" data-bs-parent=\"#accordionExample\">\n");
      out.write("                                    <div class=\"accordion-body\">\n");
      out.write("                                        <span class=\"d-block\">");
      out.print(keywords.get(i) );
      out.write("</span>\n");
      out.write("                                    </div>\n");
      out.write("                                </div>\n");
      out.write("                            </div>\n");
      out.write("                            <div class=\"accordion-item\">\n");
      out.write("                                <h2 class=\"accordion-header\" id=\"");
      out.print("heading"+i+"_2");
      out.write("\">\n");
      out.write("                                    <button class=\"accordion-button collapsed\" type=\"button\" data-bs-toggle=\"collapse\"\n");
      out.write("                                            data-bs-target=\"#");
      out.print("collapse"+i+"_2");
      out.write("\" aria-expanded=\"false\"\n");
      out.write("                                            aria-controls=\"");
      out.print("collapse"+i+"_2");
      out.write("\">\n");
      out.write("                                        Show parent link\n");
      out.write("                                    </button>\n");
      out.write("                                </h2>\n");
      out.write("                                <div id=\"");
      out.print("collapse"+i+"_2");
      out.write("\" class=\"accordion-collapse collapse\"\n");
      out.write("                                     aria-labelledby=\"");
      out.print("heading"+i+"_2");
      out.write("\" data-bs-parent=\"#accordionExample\">\n");
      out.write("                                    <div class=\"accordion-body\">\n");
      out.write("                                        <span class=\"d-block\">");
      out.print("result.get(5)" );
      out.write("</span>\n");
      out.write("                                        <span class=\"d-block\">");
      out.print("result.get(6)" );
      out.write("</span>\n");
      out.write("                                    </div>\n");
      out.write("                                </div>\n");
      out.write("                            </div>\n");
      out.write("                            <div class=\"accordion-item\">\n");
      out.write("                                <h2 class=\"accordion-header\" id=\"");
      out.print("heading"+i+"_3");
      out.write("\">\n");
      out.write("                                    <button class=\"accordion-button collapsed\" type=\"button\" data-bs-toggle=\"collapse\"\n");
      out.write("                                            data-bs-target=\"#");
      out.print("collapse"+i+"_3");
      out.write("\" aria-expanded=\"false\"\n");
      out.write("                                            aria-controls=\"");
      out.print("collapse"+i+"_3");
      out.write("\">\n");
      out.write("                                        Show child link\n");
      out.write("                                    </button>\n");
      out.write("                                </h2>\n");
      out.write("                                <div id=\"");
      out.print("collapse"+i+"_3");
      out.write("\" class=\"accordion-collapse collapse\"\n");
      out.write("                                     aria-labelledby=\"");
      out.print("heading"+i+"_3");
      out.write("\" data-bs-parent=\"#accordionExample\">\n");
      out.write("                                    <div class=\"accordion-body\">\n");
      out.write("                                        <span class=\"d-block\">");
      out.print("result.get(7)" );
      out.write("</span>\n");
      out.write("                                        <span class=\"d-block\">");
      out.print("result.get(8)" );
      out.write("</span>\n");
      out.write("                                    </div>\n");
      out.write("                                </div>\n");
      out.write("                            </div>\n");
      out.write("                        </div>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("                ");
 }
      out.write("\n");
      out.write("                ");
 } 
      out.write("\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <div class=\"col-6\">\n");
      out.write("            <div class=\"my-3 p-3 bg-body rounded shadow-sm\">\n");
      out.write("                <h6 class=\"border-bottom pb-2 mb-0\">Search History</h6>\n");
      out.write("\n");
      out.write("                <div class=\"p-3 small border-bottom w-100\">\n");
      out.write("                    ");

                        List<String> search_history = new ArrayList<>();
                        search_history = (ArrayList<String>) session.getAttribute("name");
                        for (int i = search_history.size() - 1; i >= 0; i--) {
                    
      out.write("\n");
      out.write("                    <form method=\"post\" action=\"#\">\n");
      out.write("                        <input hidden name=\"txtname\" id=\"");
      out.print("search_history"+i);
      out.write("\">\n");
      out.write("                        <button class=\"btn btn-outline-success\" type=\"submit\">\n");
      out.write("                            ");
      out.print(search_history.get(i));
      out.write("\n");
      out.write("                        </button>\n");
      out.write("                    </form>\n");
      out.write("                    ");

                        }
                    
      out.write("\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("</main>\n");
      out.write("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js\"\n");
      out.write("        integrity=\"sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf\"\n");
      out.write("        crossorigin=\"anonymous\"></script>\n");
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("    ");

        for (int i = search_history.size() - 1; i >= 0; i--) {
    
      out.write("\n");
      out.write("    document.getElementById('");
      out.print("search_history"+i);
      out.write("').value = '");
      out.print(search_history.get(i));
      out.write("';\n");
      out.write("    ");

        }
    
      out.write("\n");
      out.write("\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
      out.write("\t");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof jakarta.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
