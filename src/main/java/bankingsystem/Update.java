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
import java.sql.Statement;

/**
 * Servlet implementation class Update 
 */
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Update() { 
        super();
        // TODO Auto-generated constructor stub
    } 

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out= response.getWriter();
		String field=request.getParameter("fieldname");
		String name=request.getParameter("username");
		String newvalue=request.getParameter("newvalue");
		HttpSession session=request.getSession(false);
		
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
				Statement s= con.createStatement();			
				int i=s.executeUpdate("update accountdata set "+field+"='"+newvalue+"' where name='"+name+"'");
				if(i>0) {
					
					request.getRequestDispatcher("accountdetails.html").include(request, response);
					out.println("<span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;Data updated successfully</span><br>");
					request.getRequestDispatcher("footer.html").include(request, response);
				}
				else {
					request.getRequestDispatcher("update.html").include(request, response);
					out.println("<font color=white>Invalid Username!</font>");
				}
				
			}
			catch(Exception err) {
				System.out.println("Error");
			}
		}
	}

}
