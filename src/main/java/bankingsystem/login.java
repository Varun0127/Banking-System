package bankingsystem;

import jakarta.servlet.ServletException;
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


public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    public login() {
        super();
      
    }
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		String name=request.getParameter("username");
		String pass=request.getParameter("password");
		String usertype=request.getParameter("usertype");
		HttpSession session= request.getSession();
		session.setAttribute("name", name);
		String sessionname=(String)session.getAttribute("name");
		boolean uflag = false; 
		boolean aflag = false;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
			PreparedStatement ps= con.prepareStatement("select name, password, usertype from employee");
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equals(name) && rs.getString(2).equals(pass) && rs.getString(3).equals(usertype)) {
					if(usertype.equalsIgnoreCase("Admin")) {
					aflag=true;
					break;
					}
					if(usertype.equalsIgnoreCase("User")) {
						uflag=true;
						break;
						}
				}
			}
			if(aflag==true)
				request.getRequestDispatcher("home-admin.html").include(request, response);
			else if(uflag==true) {
				request.getRequestDispatcher("home-login-head.html").include(request, response);
				out.println("<div style='background-color:white'><span style='color:#a20a3a; display: table; margin: 0 auto; font-size: 1.5vw;'>Hello "+sessionname.substring(0,1).toUpperCase()+""+sessionname.substring(1)+ "</span></div><br>");
				
				request.getRequestDispatcher("home-login-foot.html").include(request, response);
				}
			else {
				request.getRequestDispatcher("login.html").include(request, response);
				out.println("<br><span style='color:pink; margin-right: 5%;font-size: 1.5vw;'>&emsp;&emsp;&emsp;&emsp;&emsp;Incorrect Username or Password......</span>");
				request.getRequestDispatcher("home-login-foot.html").include(request, response);
			}
		}
		catch(Exception err) {
			
		}
	}

}
