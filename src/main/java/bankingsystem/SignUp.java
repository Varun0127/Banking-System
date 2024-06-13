package bankingsystem;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public SignUp() {
        super();
    }

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		String name=request.getParameter("username");
		String email=request.getParameter("email");
		String mobile=request.getParameter("mobileno");
		String pass=request.getParameter("password");
		String usertype=request.getParameter("usertype");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
			PreparedStatement ps= con.prepareStatement("insert into employee values(?,?,?,?,?)");
			ps.setString(1,name);
			ps.setString(2,pass);
			ps.setString(3,email);
			ps.setString(4,mobile);
			ps.setString(5,usertype);
			int i=ps.executeUpdate();
			if(i>0) {
				request.getRequestDispatcher("login.html").include(request, response);
				out.println("<br><br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp; Hey ! "+name+" you are registered successfully.......</span>");
			}
		}
		catch(Exception err) {
			
		}
	}

}
