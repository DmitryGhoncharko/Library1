<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.name" var="locName"/>
    <fmt:message bundle="${loc}" key="label.main" var="locMain"/>
    <fmt:message bundle="${loc}" key="label.authors" var="locAuthors"/>
    <fmt:message bundle="${loc}" key="label.catalog" var="locCatalog"/>
    <fmt:message bundle="${loc}" key="label.signin" var="locSignIn"/>
    <fmt:message bundle="${loc}" key="label.logout" var="locLogout"/>
    <fmt:message bundle="${loc}" key="label.register" var="locRegister"/>
</fmt:bundle>

<html>
<head>
    <title>Header</title>
    <style><%@include file="/WEB-INF/jsp/style/header.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
<header class="container">
    <nav class="navbar navbar-expand navbar-light" style="background-color: #56abe7;">
        <div class="container">
            <div class="logo">${locName}</div>
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="/controller?command=main_page">${locMain}</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/controller?command=author_page">${locAuthors} </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/controller?command=catalog_page">${locCatalog} </a>
                </li>
            </ul>
        </div>
    </nav>
    <div class="header-rightside container">
        <div class="header-rightside-inner container">
            <div class="container-lang">
                <div class="lang-switcher">
                    <a href="/controller?command=change_lang&lang=en_US">en</a>
                    <a href="/controller?command=change_lang&lang=ru_RU">ru</a>
                </div>
            </div>
            <div class="sing-container">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <a class="btn btn-primary" href="/controller?command=logout" role="button">${locLogout}</a>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-primary" href="/controller?command=login_page" role="button">${locSignIn}</a>
                        <a class="btn btn-primary" href="/controller?command=registration_page" role="button">${locRegister}</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>
</body>
</html>
