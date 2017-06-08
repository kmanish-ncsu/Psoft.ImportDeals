<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Currency Count</title>
</head>
<body>
<style>
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
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
<h1>Currency Count</h1>
<c:forEach var="listItem" items="${currencycount}">
    <table>
        <tr>
        <td class="alt"><c:out value="${listItem.currency}" /></td>
        <td class="alt"><c:out value="${listItem.count}" /></td>
        </tr>
    </table>
</c:forEach>

<input type="button" value="HOME" onclick="location.href='/importdeals/all'">
</body>
</html>
