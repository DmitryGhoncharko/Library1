<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.error.text" var="locErrorText"/>
</fmt:bundle>
<html>
<head>
    <title>Error</title>
    <style><%@include file="/WEB-INF/jsp/style/error.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-main">
        <jsp:include page="header.jsp"></jsp:include>
        <div class="container-center">
            <div class="alert alert-danger" role="alert">
                <h3 class="alert-heading">${locErrorText}</h3>
                <p class="mb-0">${errorPassMassage}</p>
            </div>
        </div>
    </div>
</body>
</html>


