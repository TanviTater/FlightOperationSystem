import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/Controller")
public class Controller extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/flight_ops?useSSL=false&serverTimezone=UTC",
                "root",
                "your_password"
            );
            if ("add".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO admin905 (username, password_hash, email, role) VALUES (?, ?, ?, ?)");

                ps.setString(1, request.getParameter("username"));
                ps.setString(2, request.getParameter("password"));
                ps.setString(3, request.getParameter("email"));
                ps.setString(4, request.getParameter("role"));

                ps.executeUpdate();
            }
            if ("delete".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM admin905 WHERE admin_id=?");

                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                ps.executeUpdate();
            }
            out.println("<html><head>");
            out.println("<title>Controller Panel</title>");
            out.println("<link href='https://fonts.googleapis.com/css2?family=Dosis:wght@200..800&display=swap' rel='stylesheet'>");
            out.println("<link rel='stylesheet' href='style/manageFlight.css'>");
            out.println("</head><body>");

            out.println("<h2>Controller Panel - Manage Admins</h2>");
            out.println("<div class='form-box'>");
            out.println("<h3>Add Admin</h3>");
            out.println("<form method='post' action='Controller'>");
            out.println("<input type='hidden' name='action' value='add'>");
            out.println("<input name='username' placeholder='Username' required><br>");
            out.println("<input name='password' placeholder='Password' required><br>");
            out.println("<input name='email' placeholder='Email' required><br>");
            out.println("<input name='role' placeholder='Role (Controller/Manager/Dispatcher)' required><br>");
            out.println("<button type='submit'>Add Admin</button>");
            out.println("</form>");
            out.println("</div>");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM admin905");
            ResultSet rs = ps.executeQuery();

            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>ID</th><th>Username</th><th>Email</th><th>Role</th><th>Action</th></tr>");

            while (rs.next()) {

                int id = rs.getInt("admin_id");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("role") + "</td>");

                out.println("<td>");
                out.println("<form method='post' action='Controller'>");
                out.println("<input type='hidden' name='id' value='" + id + "'>");
                out.println("<button type='submit' name='action' value='delete'>Delete</button>");
                out.println("</form>");
                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red'>Error: " + e.getMessage() + "</p>");
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }
}
