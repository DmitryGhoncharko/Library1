<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.id" var="locId"/>
    <fmt:message bundle="${loc}" key="label.login" var="locLogin"/>
    <fmt:message bundle="${loc}" key="label.first.name" var="locFirstName"/>
    <fmt:message bundle="${loc}" key="label.last.name" var="locLastName"/>
    <fmt:message bundle="${loc}" key="label.role" var="locRole"/>
    <fmt:message bundle="${loc}" key="label.action" var="locAction"/>
    <fmt:message bundle="${loc}" key="label.button.change.role" var="locChangeRole"/>
    <fmt:message bundle="${loc}" key="label.button.delete" var="locDelete"/>
</fmt:bundle>
<html>
<head>
    <title>Accounts</title>
    <style><%@include file="/WEB-INF/jsp/style/accounts.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <div class="container-context">
            <jsp:include page="header.jsp"></jsp:include>
            <div class="container">
                <table class="table table-striped table-hover">
                    <thead class="table-primary">
                    <tr>
                        <th>${locId}</th>
                        <th>${locLogin}</th>
                        <th>${locRole}</th>
                        <th>${locFirstName}</th>
                        <th>${locLastName}</th>
                        <th>${locAction}</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="account" items="${requestScope.accounts}">
                        <tr>
                            <td>${account.id}</td>
                            <td>${account.login}</td>
                            <td>${account.role}</td>
                            <td>${account.details.firstName}</td>
                            <td>${account.details.lastName}</td>
                            <td>
                                <a href="/controller?command=account_page&id=${account.id}">${locChangeRole}</a>
                                <a href="/controller?command=delete_account&id=${account.id}">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>