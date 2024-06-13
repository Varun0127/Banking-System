package bankingsystem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDate;

/**
 * Servlet implementation class Credit
 */
public class Credit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Credit() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
//		String accountno=request.getParameter("accno");
		String accountno = "";
		long balance = Long.parseLong(request.getParameter("creditamount"));
		Date date = Date.valueOf(LocalDate.now());
		HttpSession session = request.getSession(false);

		if (session == null) {
			request.getRequestDispatcher("header-before-login.html").include(request, response);

			out.println("<br><span style='color:pink; margin-left: 21%;font-size: 1.5vw;'> Timeout!</span>");
			out.println("<span style='color:pink; margin-left: 2%;font-size: 1.5vw;'>Please login first </span><br>");
			request.getRequestDispatcher("login-form.html").include(request, response);
		} else {
			String sessionname = (String) session.getAttribute("name");
			long amount = 0;
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("select accountno from accountdata where name='" + sessionname + "'");
				if (rs.next()) {
					accountno = rs.getString(1);
					rs = st.executeQuery("select balance from accountdata where accountno='" + accountno + "'");
					while (rs.next()) {
						amount = Long.parseLong(rs.getString(1));
					}
					long newamount = amount + balance;
					Statement s = con.createStatement();
					s.executeUpdate(
							"update accountdata set balance=" + newamount + " where accountno='" + accountno + "'");
//					**************updating Credit Table************
					PreparedStatement ps = con.prepareStatement("insert into credit values(?,?,?)");
					ps.setString(1, sessionname);
					ps.setLong(2, balance);
					ps.setDate(3, date);
					ps.executeUpdate();
					
					request.getRequestDispatcher("credit.html").include(request, response);
					out.print("<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;₹" + balance+" credited successfully.</span>");
					out.print("<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;New balance is ₹" + newamount+".</span>");
					request.getRequestDispatcher("footer.html").include(request, response);

				} else { 
//					request.getRequestDispatcher("header-after-login.html").include(request, response);
					request.getRequestDispatcher("credit.html").include(request, response);
					out.print(
							"<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;Please Open an account first!</span>");
					request.getRequestDispatcher("footer.html").include(request, response);
				}

			} catch (Exception err) {

			}
		}
	}

}
