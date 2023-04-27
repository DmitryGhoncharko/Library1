<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sdt" uri="jwd.epam.com" %>
<%@ page import="com.library.model.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.button.order" var="locOrder"/>
    <fmt:message bundle="${loc}" key="label.button.accounts" var="locAccounts"/>
    <fmt:message bundle="${loc}" key="label.button.check" var="locCheckOrders"/>
    <fmt:message bundle="${loc}" key="label.button.person" var="locPersonData"/>
</fmt:bundle>

<html>
<head>
    <title></title>
    <style><%@include file="/WEB-INF/jsp/style/main.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <div class="container-context">
            <jsp:include page="header.jsp"></jsp:include>
            <main class="container-body">
                <div class="container">
                    <div class="container-img">
                        <img src="https://mipt.ru/upload/medialibrary/907/111.jpg">
                        <span class="some-text">
                            <div class="btn-group-vertical">
                                <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.LIBRARIAN)}">
<%--                                    <a class="btn btn-primary" href="/controller?command=librarian_book_order_page" role="button"><fmt:message bundle="loc" key="label.button.check"/></a>--%>
<%--                                    <a class="btn btn-primary" href="/controller?command=librarian_book_order_page" role="button">${locAccounts}</a>--%>
                                    <a class="btn btn-primary" href="/controller?command=librarian_book_order_page" role="button">${locCheckOrders}</a>
                                </c:if>
                                <c:if test="${not empty sessionScope.account and (sessionScope.account.role eq Role.READER)}">
<%--                                    <a class="btn btn-primary" href="/controller?command=reader_book_order_page&id=${sessionScope.account.id}" role="button"><fmt:message bundle="${loc}" key="label.button.order"/></a>--%>
<%--                                    <a class="btn btn-primary" href="/controller?command=account_page&id=${sessionScope.account.id}" role="button"><fmt:message bundle="${loc}" key="label.button.person"/></a>--%>
                                    <a class="btn btn-primary" href="/controller?command=reader_book_order_page&id=${sessionScope.account.id}" role="button">${locOrder}</a>
                                    <a class="btn btn-primary" href="/controller?command=account_page&id=${sessionScope.account.id}" role="button">${locPersonData}</a>
                                </c:if>
                                <c:if test="${not empty sessionScope.account && sessionScope.account.role eq Role.ADMIN}">
<%--                                    <a class="btn btn-primary" href="/controller?command=accounts_page" role="button"><fmt:message bundle="loc" key="label.button.accounts"/></a>--%>
<%--                                    <a class="btn btn-primary" href="/controller?command=librarian_book_order_page" role="button">${locAccounts}</a>--%>
                                    <a class="btn btn-primary" href="/controller?command=accounts_page" role="button">${locAccounts}</a>
                                </c:if>
                            </div>
                        </span>
                    </div>
                </div>
            </main>
            <footer>
                <div class="container">
                    <i><sdt:presentDate /></i>
                </div>
            </footer>
        </div>
    </div>
</body>
</html>
