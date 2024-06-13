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
 * Servlet implementation class ForgotPassword
 */
public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgotPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out= response.getWriter();
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String pass=request.getParameter("password");
		String confirmpass=request.getParameter("confirm-password");
		System.out.println("1");
		
		if(pass.equals(confirmpass)) {
			System.out.println("2");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
				Statement s= con.createStatement();
				System.out.println("3");
				int i=s.executeUpdate("update employee set password='"+pass+"' where name='"+name+"' and email='"+email+"'");
				System.out.println("4");
				if(i>0) {
					System.out.println("5");
					request.getRequestDispatcher("login.html").include(request, response);
					out.print("Dear "+name+", password reset successfully");
					System.out.println("6");
				}
				else {
					System.out.println("7");
					request.getRequestDispatcher("forgotpassword.html").include(request, response);
					out.print("Invalid user credentials");
				}
			}
			catch(Exception err) {
				System.out.println("Error");
			}
		}
		else {
			System.out.println("8");
			request.getRequestDispatcher("forgotpassword.html").include(request, response);
			out.print("Password didn't match");
		}
	}

}
