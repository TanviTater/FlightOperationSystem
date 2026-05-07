import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Admin")
public class Admin extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("controller".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null &&
                "Controller".equalsIgnoreCase((String) session.getAttribute("role"))) {
                RequestDispatcher rd = request.getRequestDispatcher("/Controller");
                rd.forward(request, response);
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body style='text-align:center;font-family:Arial;'>");
                out.println("<h2 style='color:red;'>Access Denied ❌</h2>");
                out.println("<p>You are not authorized to access Controller Panel</p>");
                out.println("<form method='post' action='Admin'>");
                out.println("<input type='submit' value='⬅ Back'>");
                out.println("</form>");
                out.println("</body></html>");
            }
            return;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : "";
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Admin Panel</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Dosis:wght@200..800&display=swap' rel='stylesheet'>");
        out.println("<link rel='stylesheet' href='style/admin.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='mainDiv'>");
        out.println("<div class='content'>");
        out.println("<h1>Admin Control Panel</h1>");
        out.println("<div class='btn-group'>");
        out.println("<form action='ManageFlight' method='post'>");
        out.println("<input type='submit' value='Manage Flight'>");
        out.println("</form>");
        if ("Controller".equalsIgnoreCase(role)) {
            out.println("<form method='post' action='Admin'>");
            out.println("<input type='hidden' name='action' value='controller'>");
            out.println("<input type='submit' value='Controller'>");
            out.println("</form>");
        }
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");

        out.println("<footer>© 2026 Flight System | Designed by Tanvi</footer>");

        out.println("</body>");
        out.println("</html>");
    }
}