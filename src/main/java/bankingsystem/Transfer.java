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

/**
 * Servlet implementation class Transfer
 */
public class Transfer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Transfer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String receiveracc = request.getParameter("receiveraccno");
		long transferamount = Long.parseLong(request.getParameter("transferamount"));
		String receiver_name = "", senderacc = "";
		long samount = 0, newamount, ramount;
		PreparedStatement ps;
		ResultSet rs, rrs;
		Date date = Date.valueOf(LocalDate.now());
		HttpSession session = request.getSession(false);
		if (session == null) {
			request.getRequestDispatcher("header-before-login.html").include(request, response);

			out.println("<br><span style='color:pink; margin-left: 21%;font-size: 1.5vw;'> Timeout!</span>");
			out.println("<span style='color:pink; margin-left: 2%;font-size: 1.5vw;'>Please login first </span><br>");
			request.getRequestDispatcher("login-form.html").include(request, response);
		} else {
			String sessionname = (String) session.getAttribute("name");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
				Statement s = con.createStatement();
				rs = s.executeQuery("select accountno, balance from accountdata where name='" + sessionname + "'");
				if (rs.next()) {
					if (! senderacc.equals(receiveracc)) {
						senderacc = rs.getString(1);
						samount = Long.parseLong(rs.getString(2));
						rrs = s.executeQuery(
								"select name, balance from accountdata where accountno='" + receiveracc + "'");

						if (rrs.next()) {
							if (samount > transferamount && samount > 0) {
								newamount = samount - transferamount;
								receiver_name = rrs.getString(1);
								ramount = Long.parseLong(rrs.getString(2));

								s.executeUpdate("update accountdata set balance=" + newamount + " where accountno='"
										+ senderacc + "'");
//							**************updating Debit Table************
								ps = con.prepareStatement("insert into debit values(?,?,?)");
								ps.setString(1, sessionname);
								ps.setLong(2, transferamount);
								ps.setDate(3, date);
								ps.executeUpdate();

//							**************updating Credit Table************

								ps = con.prepareStatement("insert into credit values(?,?,?)");
								ps.setString(1, receiver_name);
								ps.setLong(2, transferamount);
								ps.setDate(3, date);
								ps.executeUpdate();

//							**************updating Receiver Account************					 

								newamount = ramount + transferamount;
								s.executeUpdate("update accountdata set balance=" + newamount + " where accountno='"
										+ receiveracc + "'");

//							**************updating Trancation Table************
								ps = con.prepareStatement("insert into transection values(?,?,?,?,?)");
								ps.setString(1, sessionname);
								ps.setString(2, senderacc);
								ps.setString(3, receiveracc);
								ps.setLong(4, transferamount);
								ps.setDate(5, date);
								ps.executeUpdate();
								request.getRequestDispatcher("transfer.html").include(request, response);
								out.print("<br><span style='color:pink;'>₹" + transferamount
										+ " Transferred successfully form " + sessionname + "(" + senderacc + ") to "
										+ receiver_name + "(" + receiveracc + ")\"</span>");

							} else {
								newamount = transferamount - samount;

								request.getRequestDispatcher("transfer.html").include(request, response);
								out.print(
										"<br><span style='color:pink; margin-right: 5%;font-size: 1vw;'>Inefficient Balance! needs "
												+ "₹" + newamount + " more to Transfer this amount</span>");
								request.getRequestDispatcher("footer.html").include(request, response);
							}

						} else {
							request.getRequestDispatcher("transfer.html").include(request, response);
							out.print(
									"<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>Invalid Receiver Account Number...</span>");
							request.getRequestDispatcher("footer.html").include(request, response);
						}
					} else {
						request.getRequestDispatcher("transfer.html").include(request, response);
						out.print(
								"<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>You can't send money to self on the same account...</span>");
						request.getRequestDispatcher("footer.html").include(request, response);
					}
				} else {
					request.getRequestDispatcher("transfer.html").include(request, response);
					out.print(
							"<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;Please Open an account first!</span>");
					request.getRequestDispatcher("footer.html").include(request, response);
				}
			} catch (Exception err) {
				request.getRequestDispatcher("transfer.html").include(request, response);
				out.print(
						"<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>Please Enter Valid Details</span>");
				request.getRequestDispatcher("footer.html").include(request, response);
			}
		}

	}

}
