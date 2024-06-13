package bankingsystem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class ChangePassword
 */
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangePassword() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String oldpass = request.getParameter("oldpass");
		String newpass = request.getParameter("newpass");
		String confirmpass = request.getParameter("confirmpass");
		HttpSession session = request.getSession(false);
		if (session == null) {
			request.getRequestDispatcher("header-before-login.html").include(request, response);

			out.println("<br><span style='color:pink; margin-left: 21%;font-size: 1.5vw;'> Timeout!</span>");
			out.println("<span style='color:pink; margin-left: 2%;font-size: 1.5vw;'>Please login first </span><br>");
			request.getRequestDispatcher("login-form.html").include(request, response);
		} else {
			String name = (String) session.getAttribute("name");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
				Statement s = con.createStatement();
				int i = s.executeUpdate("update employee set password='" + newpass + "' where name='" + name
						+ "' and password='" + oldpass + "'");
				if (i > 0) {
					session.invalidate();
					request.getRequestDispatcher("login.html").include(request, response);
					out.print("<span style='color:pink; margin-right: 5%;'><br>Dear " + name
							+ ", password updated successfully please login again...</span>");
					request.getRequestDispatcher("footer.html").include(request, response);
				}
				else {
					request.getRequestDispatcher("changepassword.html").include(request, response);
					out.print("<span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;Current password does'nt match..</span>");
					request.getRequestDispatcher("footer.html").include(request, response);
					
				}
				
			} catch (Exception err) {
				System.out.println("Error");
			}
		}
	}

}
