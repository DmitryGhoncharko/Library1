<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Role" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.id" var="locId"/>
    <fmt:message bundle="${loc}" key="label.first.name" var="locFirstName"/>
    <fmt:message bundle="${loc}" key="label.last.name" var="locLastName"/>
    <fmt:message bundle="${loc}" key="label.action" var="locAction"/>
    <fmt:message bundle="${loc}" key="label.button.update" var="locUpdate"/>
    <fmt:message bundle="${loc}" key="label.button.delete" var="locDelete"/>
</fmt:bundle>
<html>
<head>
    <title>Authors</title>
    <style><%@include file="/WEB-INF/jsp/style/authors.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <div class="container-context">
            <jsp:include page="header.jsp"></jsp:include>
            <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                <div class="d-grid gap-2">
                    <a class="btn btn-primary" href="/controller?command=create_author_page" role="button">create author</a>
                </div>
            </c:if>
            <div class="container">
                <table class="table table-striped table-hover">
                    <thead class="table-primary">
                        <tr>
                            <th>${locId}</th>
                            <th>${locFirstName}</th>
                            <th>${locLastName}</th>
                            <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                                <th>${locAction}</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="author" items="${requestScope.authors}">
                            <tr>
                                <td>${author.id}</td>
                                <td>${author.firstName}</td>
                                <td>${author.lastName}</td>
                                <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                                    <td>
                                        <a href="/controller?command=update_author_page&id=${author.id}">${locUpdate}</a>
                                        <a href="/controller?command=delete_author&id=${author.id}">${locDelete}</a>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>