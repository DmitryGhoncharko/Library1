<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.library.model.OrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.id" var="locId"/>
    <fmt:message bundle="${loc}" key="label.first.last.name" var="locFirstLastName"/>
    <fmt:message bundle="${loc}" key="label.book.title" var="locBookTitle"/>
    <fmt:message bundle="${loc}" key="label.date.created.order" var="locDateCreated"/>
    <fmt:message bundle="${loc}" key="label.date.issued.order" var="locDateIssued"/>
    <fmt:message bundle="${loc}" key="label.date.return.order" var="locDateReturn"/>
    <fmt:message bundle="${loc}" key="label.date.order.status" var="locOrderStatus"/>
    <fmt:message bundle="${loc}" key="label.action" var="locAction"/>
    <fmt:message bundle="${loc}" key="label.button.issue" var="locIssue"/>
    <fmt:message bundle="${loc}" key="label.button.delete" var="locDelete"/>
    <fmt:message bundle="${loc}" key="label.button.end" var="locEnd"/>
</fmt:bundle>
<html>
<head>
    <title>Book orders for librarian</title>
    <style><%@include file="/WEB-INF/jsp/style/librarianBookOrder.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <table>
        <div class="container-main">
            <div class="container-context">
                <jsp:include page="header.jsp"></jsp:include>
                <div class="container">
                    <table class="table table-striped table-hover">
                        <thead class="table-primary">
                        <tr>
                            <th>${locId}</th>
                            <th>${locFirstLastName}</th>
                            <th>${locBookTitle}</th>
                            <th>${locDateCreated}</th>
                            <th>${locDateIssued}</th>
                            <th>${locDateReturn}</th>
                            <th>${locOrderStatus}</th>
                            <th>${locAction}</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="bookOrder" items="${requestScope.bookOrders}">
                            <tr>
                                <td>${bookOrder.id}</td>
                                <td> <a href="/controller?command=reader_book_order_page&id=${bookOrder.details.id}">
                                        ${bookOrder.details.firstName} ${bookOrder.details.lastName}
                                    </a>
                                </td>
                                <td>${bookOrder.book.title}</td>
                                <td>${bookOrder.dateCreate}</td>
                                <td>${bookOrder.dateIssue}</td>
                                <td>${bookOrder.dateReturn}</td>
                                <td>${bookOrder.status}</td>
                                <td>
                                    <c:if test="${bookOrder.status eq OrderStatus.CLAIMED}">
                                        <a href="/controller?command=issue_book&id=${bookOrder.id}">${locIssue}</a>
                                        <a href="/controller?command=delete_book_order&id=${bookOrder.id}">${locDelete}</a>
                                    </c:if>
                                    <c:if test="${not empty bookOrder.status and (bookOrder.status eq OrderStatus.ISSUED)}">
                                        <a href="/controller?command=end_book_order&id=${bookOrder.id}">${locEnd}</a>
                                    </c:if>
                                    <c:if test="${not empty bookOrder.status and (bookOrder.status eq OrderStatus.ENDED)}">
                                        <a href="/controller?command=delete_book_order&id=${bookOrder.id}">${locDelete}</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </table>
</body>
</html>

