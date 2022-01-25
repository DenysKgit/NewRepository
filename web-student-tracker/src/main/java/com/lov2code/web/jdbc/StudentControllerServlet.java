package com.lov2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init(); 
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String theCommend = request.getParameter("command");
			
			if(theCommend == null) {
				
				theCommend = "LIST";
			}
			
			switch (theCommend) {
			case "LIST":
				listStudents(request, response);
				break;
			case "ADD":
				addStudent(request, response);
				break;
			case "LOAD":
				loadStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request, response);
				break;
			case "SEARCH":
				searchStudent(request, response);
				break;
			default:
				listStudents(request, response);
				
			}	
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}

	private void searchStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        String theSearchName = request.getParameter("theSearchName");
        
        
        List<Student> students = studentDbUtil.searchStudents(theSearchName);
        
        
        request.setAttribute("STUDENTS_LIST", students);
                
    
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
		
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		studentDbUtil.deleteStudent(id);
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		studentDbUtil.updateStudent(new Student(id, firstName, lastName, email));
		listStudents(request, response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theStudentId = request.getParameter("studentId");
		
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		
		request.setAttribute("THE_STUDENT", theStudent);
		
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student student = new Student(firstName, lastName, email);
		studentDbUtil.addStudent(student);
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Student> students = studentDbUtil.getStudents();
		
		request.setAttribute("STUDENTS_LIST", students);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/list-students.jsp");
		requestDispatcher.forward(request, response);
	}

}







