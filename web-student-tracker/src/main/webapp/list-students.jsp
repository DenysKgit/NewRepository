<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Tracker App</title>
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
	<div id = "wrapper">
		<div id = "header">
			<h2>FooBar University</h2>
		</div>
	</div>
	
	<div id="container">
		<div id="content">
		<input type="button" value="Add Student" 
		onclick="window.location.href='add-student-form.jsp; return fasle'"
		class="add-student-button"/>
		
		<form action="StudentControllerServlet" method="GET">
        
                <input type="hidden" name="command" value="SEARCH" />
            
                Search student: <input type="text" name="theSearchName" />
                
                <input type="submit" value="Search" class="add-student-button" />
            
            </form>
			
			<table>
				<tr> 
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				<c:forEach var="student" items="${STUDENTS_LIST}">
					<c:url var="tempLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"></c:param>
						<c:param name="studentId" value="${student.id}"></c:param>	
					</c:url>
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"></c:param>
						<c:param name="studentId" value="${student.id}"></c:param>	
					</c:url>
				<tr>
						<td>${student.firstName}</td>
						<td>${student.lastName}</td>
						<td>${student.email}</td>
						<td>
						<a href="${tempLink}">Update</a>
						|
						<a href="${deleteLink}" 
						onclick="if (!(confirm('Are you sure you want to delete this student?')))return false">Delete</a>
						</td>
				</tr>	
				</c:forEach>
			</table>
		</div>	
	</div>
</body>
</html>