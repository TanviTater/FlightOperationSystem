import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/AdminLogin")
public class AdminLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String error = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/flight_ops?useSSL=false&serverTimezone=UTC",
                "root",
                "Tanvi@2003"
            );
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM admin905 WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password_hash");
                String role = rs.getString("role");
                int adminId = rs.getInt("admin_id");
                if (password.equals(dbPassword)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("admin_id", adminId);
                    session.setAttribute("username", username);
                    session.setAttribute("role", role);
                    RequestDispatcher rd = request.getRequestDispatcher("/Admin");
                    rd.forward(request, response);
                    return;
                } else {
                    error = "Invalid Password";
                }
            } else {
                error = "User not found";
            }
            con.close();
        } catch (Exception e) {
            error = "Something went wrong";
        }
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Admin Login</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Dosis:wght@200..800&display=swap' rel='stylesheet'>");
        out.println("<link rel='stylesheet' href='style/adminLogin.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='login-box'>");
        out.println("<h2>Admin Login</h2>");
        if (error != null) {
            out.println("<p class='error'>" + error + "</p>");
        }
        out.println("<form method='post' action='AdminLogin'>");
        out.println("<input type='text' name='username' placeholder='Enter Username' required>");
        out.println("<input type='password' name='password' placeholder='Enter Password' required>");
        out.println("<input type='submit' value='Login'>");
        out.println("</form>");
        out.println("<form method='get' action='index.html'>");
        out.println("<input type='submit' value='Back'>");
        out.println("</form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}