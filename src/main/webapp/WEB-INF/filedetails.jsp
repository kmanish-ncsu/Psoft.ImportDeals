<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<style>
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }

    td, th {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
    }

    tr:nth-child(even) {
        background-color: #dddddd;
    }
</style>
<h1>File Details</h1>
    <table>
        <tr>
            <td>File Name :<c:out value="${filedetails.sourceFile}" /></td>
        </tr>
        <tr>
            <td>Valid Rows : <c:out value="${filedetails.validRows}" /></td>
        </tr>
        <tr>
            <td>Invalid Rows : <c:out value="${filedetails.invalidRows}" /></td>
        </tr>
    </table>

<br><input type="button" value="HOME" onclick="location.href='/importdeals/all'">
</body>
</html>
