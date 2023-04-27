<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Role" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.author" var="locAuthor"/>
    <fmt:message bundle="${loc}" key="label.date.published" var="locDatePublished"/>
    <fmt:message bundle="${loc}" key="label.quantity.items" var="locQuantity"/>
    <fmt:message bundle="${loc}" key="label.button.update" var="locUpdate"/>
    <fmt:message bundle="${loc}" key="label.button.add" var="locAdd"/>
</fmt:bundle>
<html>
<head>
    <title>Book</title>
    <style><%@include file="/WEB-INF/jsp/style/book.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <div class="container-context">
            <jsp:include page="header.jsp"></jsp:include>
            <div class="container">
                <h1>${requestScope.book.title}</h1>
            </div>
            <div class="container">
                <h3>
                    ${locAuthor}:
                    <c:forEach var="author" items="${requestScope.book.authors}">
                        <br>${author.firstName}
                            ${author.lastName}
                    </c:forEach>
                </h3>
            </div>
            <div class="container">
                <h3>
                    ${locDatePublished}: ${requestScope.book.datePublished}
                </h3>
            </div>
            <div class="container">
                <h3>
                    ${locQuantity}: ${requestScope.book.amountOfLeft}
                </h3>
            </div>
            <div class="container">
                <c:choose>
                    <c:when test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                        <a class="btn btn-primary" href="/controller?command=update_book_page&id=${requestScope.book.id}" role="button">${locUpdate}</a>
                    </c:when>
                    <c:when test="${not empty sessionScope.account and (sessionScope.account.role eq Role.LIBRARIAN)}">
                        <a class="btn btn-primary disabled" href="/controller?command=create_book_order_page&id=${requestScope.book.id}" role="button" aria-disabled="true">${locAdd}</a>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-primary" href="/controller?command=create_book_order_page&id=${requestScope.book.id}" role="button">${locAdd}</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</body>
</html>
