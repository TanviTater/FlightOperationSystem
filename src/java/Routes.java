import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Routes")
public class Routes extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(response);
    }
    private void processRequest(HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Flight Routes</title>");

        out.println("<link href='https://fonts.googleapis.com/css2?family=Dosis:wght@200..800&display=swap' rel='stylesheet'>");
        out.println("<link rel='stylesheet' type='text/css' href='style/routes.css'>");

        out.println("</head><body>");
        out.println("<h2>Flight Routes</h2>");

        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Flight No</th>");
        out.println("<th>Route</th>");
        out.println("<th>Airline</th>");
        out.println("<th>Country</th>");
        out.println("</tr>");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/flight_ops?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "Tanvi@2003"
            );
            PreparedStatement ps = con.prepareStatement("SELECT * FROM flight905");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("flight_id") + "</td>");
                out.println("<td>" + rs.getString("flight_number") + "</td>");
                out.println("<td>" + rs.getString("route_code") + "</td>");
                out.println("<td>" + rs.getString("airline_name") + "</td>");
                out.println("<td>" + rs.getString("destination_country") + "</td>");
                out.println("</tr>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }
}