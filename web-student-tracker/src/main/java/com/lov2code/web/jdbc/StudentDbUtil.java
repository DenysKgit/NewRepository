package com.lov2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import javax.sql.DataSource;

public class StudentDbUtil {
	
	
	private DataSource dataSource;

	public StudentDbUtil(DataSource dataSource) {
	
		this.dataSource = dataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		
		List<Student> students = new ArrayList<>();
		
		try(Connection connection = dataSource.getConnection(); 
			Statement myStm = connection.createStatement();
			ResultSet myRs = myStm.executeQuery("SELECT * FROM student ORDER BY last_name")){
			
			while (myRs.next()) {
				Student student = new Student(myRs.getInt("id"), myRs.getString("first_name"), 
						myRs.getString("last_name"), myRs.getString("email"));
				students.add(student);
				
			}
		
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return students;		
	}

	public void addStudent(Student student) throws Exception {
		try(Connection connection = dataSource.getConnection(); 
			PreparedStatement myStm = connection.prepareStatement("insert into student "+ "(first_name, last_name, email) " + "values (?,?,?)")){
			myStm.setString(1, student.getFirstName());
			myStm.setString(2, student.getLastName());
			myStm.setString(3, student.getEmail());
			
			myStm.executeUpdate();
		}	
	}
	
	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		ResultSet myRs = null;
		int studentId;
		try(Connection connection = dataSource.getConnection(); 
			PreparedStatement myStm = connection.prepareStatement("select * from student where id=?");
			){
				studentId = Integer.parseInt(theStudentId);
				myStm.setInt(1, studentId);
				myRs = myStm.executeQuery();
				if(myRs.next()) {
					theStudent = new Student(studentId, myRs.getString("first_name"),
							myRs.getString("last_name"), myRs.getString("email"));
					
				}else {
					throw new Exception("Could not find student id: " + studentId);
				}
				
			return theStudent;
		}finally {
			if(myRs != null) myRs.close();
		}
			
	}

	public void updateStudent(Student student) throws Exception {
		try(Connection connection = dataSource.getConnection(); 
				PreparedStatement myStm = connection.prepareStatement("UPDATE student SET first_name=?, last_name=?, email=? WHERE id=? ")){
				myStm.setString(1, student.getFirstName());
				myStm.setString(2, student.getLastName());
				myStm.setString(3, student.getEmail());
				myStm.setInt(4, student.getId());
				
				myStm.executeUpdate();
			}	
		
		
		
	}

	public void deleteStudent(int id) throws Exception {
		try(Connection connection = dataSource.getConnection(); 
				PreparedStatement myStm = connection.prepareStatement("DELETE FROM student WHERE id=? ")){
				myStm.setInt(1, id);
				myStm.executeUpdate();
	}
}

	public List<Student> searchStudents(String theSearchName) throws Exception {
		List<Student> students = new ArrayList<>();
		
		
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        
        
        try(Connection myConn = dataSource.getConnection()) {
            if (theSearchName != null && theSearchName.trim().length() > 0) {
               
                String sql = "select * from student where lower(first_name) like ? or lower(last_name) like ?";
                
                myStmt = myConn.prepareStatement(sql);
                
                String theSearchNameLike = "%" + theSearchName.toLowerCase() + "%";
                myStmt.setString(1, theSearchNameLike);
                myStmt.setString(2, theSearchNameLike);
                
            } else {
                
                String sql = "select * from student order by last_name";
               
                myStmt = myConn.prepareStatement(sql);
            }
            
            
            myRs = myStmt.executeQuery();
            
            
            while (myRs.next()) {
                
               
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");
                
                
                Student tempStudent = new Student(id, firstName, lastName, email);
                
                
                students.add(tempStudent);            
            }
            
            return students;
        }
        finally {
            
            if(myStmt != null) {
            	myStmt.close();
            }else if(myRs != null) {
            	myRs.close();
            }
        }
		
	}
}
