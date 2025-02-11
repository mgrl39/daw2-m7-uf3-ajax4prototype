package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class Generador
 */
@WebServlet("/Generador")
public class Generador extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String URL = "jdbc:mysql://172.16.0.25:3306/dbpalabras?user=mp7&password=secreto";

	private static final String SQL = "SELECT palabra FROM palabras WHERE palabra LIKE ? ORDER BY palabra";

	public Generador() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try (PrintWriter out = response.getWriter()) {
			String dato = request.getParameter("texto");

			try (Connection conn = DriverManager.getConnection(URL);
					PreparedStatement pstmt = conn.prepareStatement(SQL)) {
				pstmt.setString(1, dato + "%");
				ResultSet rs = pstmt.executeQuery();

				StringBuilder result = new StringBuilder("<ul>");
				while (rs.next()) {
					result.append("<li>");
					result.append(rs.getString("palabra"));
					result.append("</li>");
				}

				result.append("</ul>");
				out.println(result.toString());

				rs.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
