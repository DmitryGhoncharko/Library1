<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.list.orders" var="locListOrders"/>
    <fmt:message bundle="${loc}" key="label.book" var="locBook"/>
    <fmt:message bundle="${loc}" key="label.author" var="locAuthor"/>
    <fmt:message bundle="${loc}" key="label.type.order" var="locTypeOrder"/>
    <fmt:message bundle="${loc}" key="label.date.created.order" var="locDateCreated"/>
    <fmt:message bundle="${loc}" key="label.date.issued.order" var="locDateIssued"/>
    <fmt:message bundle="${loc}" key="label.date.return.order" var="locDateReturn"/>
    <fmt:message bundle="${loc}" key="label.date.order.status" var="locOrderStatus"/>
    <fmt:message bundle="${loc}" key="label.list.empty" var="locListEmty"/>
</fmt:bundle>
<html>
<head>
    <title>Book order for reader</title>
    <style><%@include file="/WEB-INF/jsp/style/readerBookOrder.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <div class="container-context">
            <jsp:include page="header.jsp"></jsp:include>
            <c:choose>
                <c:when test="${not empty requestScope.bookOrders}">
                    <div class="container">
                        <h3>${locListOrders}</h3>
                    </div>
                    <c:forEach var="bookOrder" items="${bookOrders}">
                        <div class="container">
                                ${bookOrder.details.firstName} ${bookOrder.details.lastName}
                        </div>
                        <div class="container">
                            ${locBook}: &#34;${bookOrder.book.title}&#34;
                        </div>
                        <div class="container">
                            ${locAuthor}: <c:forEach var="author" items="${bookOrder.book.authors}">
                                    ${author.firstName}
                                <br>${author.lastName}
                            </c:forEach>
                        </div>
                        <div class="container">
                            ${locTypeOrder}: ${bookOrder.type.name()}
                        </div>
                        <div class="container">
                            ${locDateCreated}: ${bookOrder.dateCreate}
                        </div>
                        <div class="container">
                            ${locDateIssued}: ${bookOrder.dateIssue}
                        </div>
                        <div class="container">
                            ${locDateReturn}: ${bookOrder.dateReturn}
                        </div>
                        <div class="container">
                            ${locOrderStatus}: ${bookOrder.status.name()}
                        </div>
                        <div class="container">
                            <a class="btn btn-primary" href="/controller?command=delete_book_order&id=${bookOrder.id}" role="button">Delete</a>
                        </div>
                        <br>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="container-center">
                        <div class="alert alert-danger" role="alert">
                            <h3 class="alert-heading">${locListEmty}</h3>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
