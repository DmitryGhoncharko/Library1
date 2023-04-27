<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Role" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.button.search" var="locSearch"/>
    <fmt:message bundle="${loc}" key="label.search.text" var="locSearchText"/>
    <fmt:message bundle="${loc}" key="label.button.create" var="locCreate"/>
    <fmt:message bundle="${loc}" key="label.title" var="locTitle"/>
    <fmt:message bundle="${loc}" key="label.author" var="locAuthor"/>
    <fmt:message bundle="${loc}" key="label.date.published" var="locDatePublished"/>
    <fmt:message bundle="${loc}" key="label.quantity" var="locQuantity"/>
    <fmt:message bundle="${loc}" key="label.action" var="locAction"/>
    <fmt:message bundle="${loc}" key="label.button.update" var="locUpdate"/>
    <fmt:message bundle="${loc}" key="label.button.delete" var="locDelete"/>
</fmt:bundle>
<html>
<head>
    <title>Catalog</title>
    <style><%@include file="/WEB-INF/jsp/style/catalog.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <div class="container-context">
            <jsp:include page="header.jsp"></jsp:include>
            <div class="container col-ms-6">
                <form class="col-sm-6" name="search" role="search" method="post" action="/controller?command=search_book">
                    <input name="title" class="form-control me-4" type="search" placeholder="${locSearchText}" aria-label="Search">
                    <button class="btn btn-primary" type="submit">${locSearch}</button>
                </form>
            </div>
            <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                <div class="d-grid gap-2">
                    <a class="btn btn-primary" href="/controller?command=create_book_page" role="button">${locCreate}</a>
                </div>
            </c:if>
            <div class="container">
                <table class="table table-striped table-hover">
                    <thead class="table-primary">
                        <tr>
                            <th>${locTitle}</th>
                            <th>${locAuthor}</th>
                            <th>${locDatePublished}</th>
                            <th>${locQuantity}</th>
                            <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                                <th>${locAction}</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="book" items="${requestScope.books}">
                            <tr>
                                <c:if test="${book.amountOfLeft > 0}">
                                    <td><a href="/controller?command=book_page&id=${book.id}">${book.title}</a></td>
                                    <td>
                                        <c:forEach var="author" items="${book.authors}">
                                            ${author.firstName} ${author.lastName}<br>
                                        </c:forEach>
                                    </td>
                                    <td>${book.datePublished}</td>
                                    <td>${book.amountOfLeft}</td>
                                    <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.ADMIN)}">
                                        <td>
                                            <a href="/controller?command=update_book_page&id=${book.id}">${locUpdate}</a>
                                            <a href="/controller?command=delete_book&id=${book.id}">${locDelete}</a>
                                        </td>
                                    </c:if>
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
