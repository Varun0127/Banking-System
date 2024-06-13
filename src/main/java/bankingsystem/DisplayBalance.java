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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class DisplayBalance
 */
public class DisplayBalance extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayBalance() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		String accountno;
		HttpSession session= request.getSession(false);
		if(session==null) {
			request.getRequestDispatcher("header-before-login.html").include(request, response);
			
			out.println("<br><span style='color:pink; margin-left: 21%;font-size: 1.5vw;'> Timeout!</span>");
			out.println("<span style='color:pink; margin-left: 2%;font-size: 1.5vw;'>Please login first </span><br>");
			request.getRequestDispatcher("login-form.html").include(request, response);
		}
		else {
			String sessionname=(String)session.getAttribute("name");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
				Statement st= con.createStatement();
				ResultSet rs=st.executeQuery("select accountno from accountdata where name='"+sessionname+"'");
				if(rs.next()) {
					accountno=rs.getString(1);
					rs=st.executeQuery("select balance from accountdata where accountno="+accountno);
					request.getRequestDispatcher("header-after-login.html").include(request, response);
					request.getRequestDispatcher("checkbalanceform.html").include(request, response);
					while(rs.next()) {
						out.println("<br><span style='color:rgb(255, 196, 0); margin-left: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;Account no. &emsp;&emsp; &emsp; Balance</span><br>");
						out.println("<br><span style='color:white; margin-left: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;"+accountno+ "&emsp; &emsp; &nbsp; ₹"+rs.getString(1)+ "</span><br>");
			
				}
				request.getRequestDispatcher("footer.html").include(request, response);
				}
				else {
					request.getRequestDispatcher("header-after-login.html").include(request, response);
					request.getRequestDispatcher("checkbalanceform.html").include(request, response);
					out.print("<br><span style='color:pink; font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;Please Open an account first!</span>");
					request.getRequestDispatcher("footer.html").include(request, response);
					
				}
				
			}
			catch(Exception err) {
			
			}
		}
	}

}
