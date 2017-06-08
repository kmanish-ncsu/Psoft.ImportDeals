<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<h1>Unprocessed files</h1>
<c:choose>
    <c:when test="${unprocessedSourceFiles != null}">
        <form method="POST" action="${pageContext.request.contextPath}/importdeals/process">
            <table>
                <tr>
                    <th>File</th>
                </tr>
                <c:forEach var="listItem" items="${unprocessedSourceFiles}">
                    <tr class="spec">
                        <td class="alt"><c:out value="${listItem}" /></td>
                        <td><button id="fileToProcess" type="submit" class="fileToProcess" name="fileToProcess" value="${listItem}">Process</button></td>
                    </tr>
                </c:forEach>

            </table>
        </form>
    </c:when>
    <c:when test="${list == null}">
        <h1>No Files to process.</h1>
    </c:when>
</c:choose>

<h1>Processed files</h1>
<c:choose>
    <c:when test="${processedSourceFiles != null}">
        <form method="GET" action="${pageContext.request.contextPath}/importdeals/filedetails">
            <table>
                <tr>
                    <th>File</th>
                </tr>
                <c:forEach var="listItem" items="${processedSourceFiles}">
                    <tr class="spec">
                        <td class="alt"><c:out value="${listItem}" /></td>
                        <td><button type="submit" name="file" value="${listItem}">Detail</button></td>

                    </tr>
                </c:forEach>

            </table>
        </form>
    </c:when>
    <c:when test="${list == null}">
        <h1>No Files to display.</h1>
    </c:when>
</c:choose>

<c:choose><c:when test="${unprocessedSourceFiles != null}">
    ${fileToProcessMsg}
</c:when></c:choose>

<br>
<input type="button" value="Currency Count" onclick="location.href='/importdeals/currencycount'">

</body>
</html>