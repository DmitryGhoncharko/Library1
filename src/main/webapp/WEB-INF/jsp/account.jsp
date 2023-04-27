<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Role" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.personal.data" var="locPersonData"/>
    <fmt:message bundle="${loc}" key="label.personal.number" var="locPersonalNumber"/>
    <fmt:message bundle="${loc}" key="label.login" var="locLogin"/>
    <fmt:message bundle="${loc}" key="label.first.name" var="locFirstName"/>
    <fmt:message bundle="${loc}" key="label.last.name" var="locLastName"/>
    <fmt:message bundle="${loc}" key="label.role" var="locRole"/>
    <fmt:message bundle="${loc}" key="label.select.role" var="locSelect"/>
    <fmt:message bundle="${loc}" key="label.admin" var="locAdmin"/>
    <fmt:message bundle="${loc}" key="label.librarian" var="locLibrarian"/>
    <fmt:message bundle="${loc}" key="label.reader" var="locReader"/>
    <fmt:message bundle="${loc}" key="label.guest" var="locGuest"/>
    <fmt:message bundle="${loc}" key="label.button.change.role" var="locChangeRole"/>
</fmt:bundle>
<html>
<head>
    <title>Account</title>
    <style><%@include file="/WEB-INF/jsp/style/account.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <div class="container-context">
            <jsp:include page="header.jsp"></jsp:include>
            <div class="container">
                <h2>${locPersonData}</h2>
            </div>
            <div class="container">
                <h3>${locPersonalNumber}: ${requestScope.account.id}</h3>
            </div>
            <div class="container">
                <h3>${locLogin}: ${requestScope.account.login}</h3>
            </div>
            <div class="container">
                <h3>${locFirstName}: ${requestScope.account.details.firstName}</h3>
            </div>
            <div class="container">
               <h3>${locLastName}: ${requestScope.account.details.lastName}</h3>
            </div>
            <div class="container">
                <h3>${locRole}: ${requestScope.account.role.name()}</h3>
            </div>
            <div class="container">
                <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                    <form name="change_role_form" method="post" action="/controller?command=change_account_role">
                        <input type="hidden" name="id" value="${requestScope.account.id}">
                        <div class="container">
                            <div class="mb-3">
                                <select name="role" class="form-select mb-3" aria-label=".form-select-lg example">
                                    <option selected>${locSelect}</option>
                                    <option value="ADMIN">${locAdmin}</option>
                                    <option value="LIBRARIAN">${locLibrarian}</option>
                                    <option value="READER">${locReader}</option>
                                    <option value="GUEST">${locGuest}</option>
                                </select>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">${locChangeRole}</button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</body>
</html>
