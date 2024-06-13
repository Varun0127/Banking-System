package bankingsystem;

import jakarta.servlet.ServletException;
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
import java.util.Arrays;
import java.util.Random;
import java.time.LocalDate;

/**
 * Servlet implementation class CreateAccount
 */
public class CreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAccount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		String name=request.getParameter("username");
		String adhar=request.getParameter("adharno");
		String email=request.getParameter("email");
		String mobile=request.getParameter("mobileno");
		String father=request.getParameter("fathername");
		String balance=request.getParameter("openbal");
		String gender=request.getParameter("gender");
		String acctype=request.getParameter("account");
		String update="";
		Date date = Date.valueOf(LocalDate.now());
		HttpSession session= request.getSession(false);
		if(session==null) {
			request.getRequestDispatcher("header-before-login.html").include(request, response);
			out.println("<br><span style='color:pink; margin-left: 21%;font-size: 1.5vw;'> Timeout!</span>");
			out.println("<span style='color:pink; margin-left: 2%;font-size: 1.5vw;'>Please login first </span><br>");
			request.getRequestDispatcher("login-form.html").include(request, response);
		}
		else {
			String sessionname=(String)session.getAttribute("name");
			Random random= new Random();
			String s="1234567890";
			char[] otp= new char[11];
			for(int i=0;i<11;i++) {
				otp[i]=s.charAt(random.nextInt(s.length()));
				}
			String strArray[]= new String[otp.length];
			for(int i=0;i<otp.length;i++) {
				strArray[i]=String.valueOf(otp[i]);
			}
			String s1=Arrays.toString(strArray);
			String res1="";
			for(String num:strArray) {
				res1+=num;
			}
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
				PreparedStatement ps= con.prepareStatement("insert into accountdata values(?,?,?,?,?,?,?,?,?,?)");
				ps.setString(1,name);
				ps.setString(2,res1);
				ps.setString(3,adhar);
				ps.setString(4,email);
				ps.setString(5,mobile);
				ps.setString(6,father);
				ps.setString(7,balance);
				ps.setString(8,gender);
				ps.setString(9,acctype);
				ps.setString(10,update);
				ps.executeUpdate();
				ps= con.prepareStatement("insert into credit values(?,?,?)");
				ps.setString(1,name);
				ps.setString(2,balance);
				ps.setDate(3,date);
				ps.executeUpdate();
				ps= con.prepareStatement("select * from accountdata where accountno='"+res1+"'");
				ResultSet rs=ps.executeQuery();
				request.getRequestDispatcher("newaccount.html").include(request, response);
				out.print("<br><span style='color:pink; font-size: 1.5vw;'>Congratulation! account open successfuly...</span><br>");
				while(rs.next()) {
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Name: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(1)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Account: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(2)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Adhar no.: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(3)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Email-id: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(4)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Mobile no.: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(5)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Father Name: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(6)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Balance: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(7)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Gender: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(8)+" </span>");
					out.println("<br> <span style='color:rgb(255, 196, 0); width: 200px;  display: inline-block; font-size: 1.5vw;'>Account Type: </span><span style='color:white; font-size: 1.5vw;'>"+ rs.getString(9)+" </span>");
				}
	
				
			}
			catch(Exception err) {
			
			}
		}
	}
}
