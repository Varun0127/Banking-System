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
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class TransectionHistoryAdmin
 */
public class TransectionHistoryAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransectionHistoryAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		HttpSession session= request.getSession(false);
				
		if (session == null) {
			request.getRequestDispatcher("header-before-login.html").include(request, response);

			out.println("<br><span style='color:pink; margin-left: 21%;font-size: 1.5vw;'> Timeout!</span>");
			out.println("<span style='color:pink; margin-left: 2%;font-size: 1.5vw;'>Please login first </span><br>");
			request.getRequestDispatcher("login-form.html").include(request, response);
		}
		else {
			
		
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
				Statement st= con.createStatement();
				
				ResultSet rs=st.executeQuery("select * from transection");
				
				request.getRequestDispatcher("transectionhistoryadmin.html").include(request, response);
				out.println("<table style='width: 100%; text-align: center; border: 2px solid rgb(255, 196, 0);'>");
				out.println("<tr style='background-color: rgb(255, 196, 0); color: white; height: 30px; padding-left: 3%;'><th>Name</th><th>Sender a/c</th><th>Receiver a/c</th><th>Amount</th><th>Date</th></tr>");
				
				while(rs.next()) {	
					out.println("<tr style='color: rgb(255, 196, 0); border: 2px solid rgb(255, 196, 0);'><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td></tr>");
				}
				out.println("</table>");
				request.getRequestDispatcher("footer.html").include(request, response);
			}
			catch(Exception err) {
				
			}
		}
	}

}
