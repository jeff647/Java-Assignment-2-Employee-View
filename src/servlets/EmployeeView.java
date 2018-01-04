package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.ValidationHelper;
import utilities.DatabaseAccess;
import objects.Employee;

/**
 * Servlet implementation class EmployeeView
 */
@WebServlet("/employee_view")
public class EmployeeView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeView() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("/WEB-INF/jsp/employee/employeeView.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		boolean formIsValid = true;
		String department = request.getParameter("department");
		if (!ValidationHelper.isNotNullOrEmpty(department)) {
			// Set error message letting user know to select a option
			request.setAttribute("errorMessage8", "Please select a department");
			formIsValid = false;
		}
		int departmentId = 0;
		
	
	
		if (formIsValid == false) {
			request.getRequestDispatcher("/WEB-INF/jsp/employee/employeeView.jsp").forward(request, response);
			return;
		} else {
			// Connect to MySQL database
			try {
				Connection connect = DatabaseAccess.connectDataBase();
				// Change selected department name to its corresponding id.
				Statement statement = connect.createStatement();
				ResultSet idResult = statement.executeQuery("Select department_id from department where name = '" + department + "'");
				if(idResult.next()) {
					departmentId = idResult.getInt("department_id");
				}
				
				//Retrieve query
				String queryString = "Select * from Employee E INNER JOIN department D ON E.department_id = '" + departmentId + "'";
				
				ResultSet rs = statement.executeQuery(queryString);
				ArrayList<Employee> employeeList = new ArrayList<>();
				while(rs.next()) {
					
					Employee employee = new Employee(rs.getString("firstname"), rs.getString("lastname"),rs.getInt("employee_number"),rs.getDate("hire_date"),
							rs.getString("email"), rs.getString("job_position"));
					employeeList.add(employee);
				}
				
				request.setAttribute("employeeL", employeeList);
				
				connect.close();
				
				request.getRequestDispatcher("/WEB-INF/jsp/employee/employeeView.jsp").forward(request, response);
				return;	
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}
	}
}