import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/ManageFlight")
public class ManageFlight extends HttpServlet {

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
                String from = request.getParameter("from").toUpperCase();
                String to = request.getParameter("to").toUpperCase();
                String route = from + "-" + to;

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO flight905 VALUES (?, ?, ?, ?, ?)");

                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                ps.setString(2, request.getParameter("number"));
                ps.setString(3, route);
                ps.setString(4, request.getParameter("airline"));
                ps.setString(5, request.getParameter("country"));

                ps.executeUpdate();
            }

            if ("delete".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM flight905 WHERE flight_id=?");

                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                ps.executeUpdate();
            }
            if ("update".equals(action)) {
                String from = request.getParameter("from").toUpperCase();
                String to = request.getParameter("to").toUpperCase();
                String route = from + "-" + to;

                PreparedStatement ps = con.prepareStatement(
                    "UPDATE flight905 SET flight_number=?, route_code=?, airline_name=?, destination_country=? WHERE flight_id=?");

                ps.setString(1, request.getParameter("number"));
                ps.setString(2, route);
                ps.setString(3, request.getParameter("airline"));
                ps.setString(4, request.getParameter("country"));
                ps.setInt(5, Integer.parseInt(request.getParameter("id")));

                ps.executeUpdate();
            }
            out.println("<html><head>");
            out.println("<title>Manage Flights</title>");
            out.println("<link href='https://fonts.googleapis.com/css2?family=Dosis:wght@200..800&display=swap' rel='stylesheet'>");
            out.println("<link rel='stylesheet' href='style/manageFlight.css'>");
            out.println("</head><body>");

            out.println("<h2>Manage Flights</h2>");
            out.println("<div class='form-box'>");
            out.println("<h3>Add Flight</h3>");
            out.println("<form method='post'>");
            out.println("<input type='hidden' name='action' value='add'>");
            out.println("<input name='id' placeholder='Flight ID' required>");
            out.println("<input name='number' placeholder='Flight Number (AI202)' required>");
            out.println("<input name='from' placeholder='From (DEL)' required>");
            out.println("<input name='to' placeholder='To (DXB)' required>");
            out.println("<input name='airline' placeholder='Airline' required>");
            out.println("<input name='country' placeholder='Country' required>");
            out.println("<button type='submit'>Add</button>");
            out.println("</form>");
            out.println("</div>");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM flight905");
            ResultSet rs = ps.executeQuery();

            out.println("<table>");
            out.println("<tr><th>ID</th><th>No</th><th>From</th><th>To</th><th>Airline</th><th>Country</th><th>Actions</th></tr>");

            while (rs.next()) {

                int id = rs.getInt("flight_id");
                String route = rs.getString("route_code");
                String[] parts = route.split("-");

                String from = parts[0];
                String to = parts[1];

                out.println("<tr>");
                out.println("<form method='post'>");

                out.println("<td>" + id + "<input type='hidden' name='id' value='" + id + "'></td>");
                out.println("<td><input name='number' value='" + rs.getString("flight_number") + "'></td>");
                out.println("<td><input name='from' value='" + from + "'></td>");
                out.println("<td><input name='to' value='" + to + "'></td>");
                out.println("<td><input name='airline' value='" + rs.getString("airline_name") + "'></td>");
                out.println("<td><input name='country' value='" + rs.getString("destination_country") + "'></td>");

                out.println("<td>");
                out.println("<button name='action' value='update'>Update</button>");
                out.println("</form>");
                out.println("<form method='post' style='display:inline;'>");
                out.println("<input type='hidden' name='id' value='" + id + "'>");
                out.println("<button name='action' value='delete'>Delete</button>");
                out.println("</form>");

                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red'>" + e.getMessage() + "</p>");
        }
    }
}
