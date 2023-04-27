<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.signin" var="locSignIn"/>
    <fmt:message bundle="${loc}" key="label.login" var="locLogin"/>
    <fmt:message bundle="${loc}" key="label.valid.text" var="locValidText"/>
    <fmt:message bundle="${loc}" key="label.invalid.login.text" var="locInvalidLoginText"/>
    <fmt:message bundle="${loc}" key="label.password" var="locPassword"/>
    <fmt:message bundle="${loc}" key="label.invalid.password.text" var="locInvalidPasswordText"/>
    <fmt:message bundle="${loc}" key="label.not.member" var="locNotMember"/>
    <fmt:message bundle="${loc}" key="label.register" var="locRegister"/>
</fmt:bundle>
<html>
<head>
    <title>Log in</title>
    <style><%@include file="/WEB-INF/jsp/style/login.css" %></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-login">
        <div class="col-sm-4">
            <h1 class="header-login">${locSignIn}</h1>
            <form  class="needs-validation" name = "registration_form" method="post" action="/controller?command=login" novalidate>
                <div class="mb-4">
                    <label for="exampleInputLogin" class="form-label">${locLogin}</label>
                    <input type="text" name="login" pattern=".{3,30}" class="form-control" id="exampleInputLogin" required>
                    <div class="valid-feedback">
                        Looks good!
                    </div>
                    <div class="invalid-feedback">
                        Please enter login(3-30 symbols)
                    </div>
                </div>
                <div class="mb-3">
                    <label for="exampleInputPassword" class="form-label">${locPassword}</label>
                    <input type="password" name="password" pattern=".{3,100}" class="form-control" id="exampleInputPassword" required>
                    <div class="valid-feedback">
                        Looks good!
                    </div>
                    <div class="invalid-feedback">
                        Please enter password(3-100 symbols)
                    </div>
                </div>
<%--                <div class="form-outline mb-4">--%>
<%--                    <input type="text" name="login" id="form1" class="form-control"/>--%>
<%--                    <label class="form-label" for="form1">Login</label>--%>
<%--                </div>--%>
<%--                <div class="form-outline mb-4">--%>
<%--                    <input type="password" name="password" id="form2" class="form-control"/>--%>
<%--                    <label class="form-label" for="form2">${locPassword}</label>--%>
<%--                </div>--%>
                <c:if test="${not empty requestScope.errorLoginPassMessage}">
                    <b style="color: red">${requestScope.errorLoginPassMessage}</b>
                    <br>
                </c:if>
                <button type="submit" class="btn btn-primary">${locSignIn}</button>
            </form>
            <div class="text-center">
                <p>${locNotMember} <a href="/controller?command=registration_page">${locRegister}</a></p>
            </div>
        </div>
    </div>
</body>
<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })()
</script>
</html>