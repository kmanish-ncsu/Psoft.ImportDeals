<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>File Details</h1>
File Name : ${filedetails.sourceFile}
Valid Rows : ${filedetails.validRows}
Invalid Rows: ${filedetails.invalidRows}

<input type="button" value="HOME" onclick="location.href='/importdeals/all'">
</body>
</html>
