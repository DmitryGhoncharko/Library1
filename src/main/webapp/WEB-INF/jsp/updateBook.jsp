<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="lang.main" var="loc" />

<fmt:bundle basename="${loc}">
    <fmt:message bundle="${loc}" key="label.update.book" var="locUpdateBook"/>
    <fmt:message bundle="${loc}" key="label.title" var="locTitle"/>
    <fmt:message bundle="${loc}" key="label.valid.text" var="locValidText"/>
    <fmt:message bundle="${loc}" key="label.invalid.title.text" var="locInvalidTitleText"/>
    <fmt:message bundle="${loc}" key="label.date.published" var="locDatePublished"/>
    <fmt:message bundle="${loc}" key="label.invalid.date.published.text" var="locInvalidDateText"/>
    <fmt:message bundle="${loc}" key="label.quantity.items" var="locQuantity"/>
    <fmt:message bundle="${loc}" key="label.invalid.quantity.text" var="locInvalidQuantityText"/>
    <fmt:message bundle="${loc}" key="label.first.name" var="locFirstName"/>
    <fmt:message bundle="${loc}" key="label.invalid.first.name.text" var="locInvalidFirstNameText"/>
    <fmt:message bundle="${loc}" key="label.last.name" var="locLastName"/>
    <fmt:message bundle="${loc}" key="label.invalid.last.name.text" var="locInvalidLastNameText"/>
    <fmt:message bundle="${loc}" key="label.button.update" var="locUpdate"/>
</fmt:bundle>
<html>
<head>
    <title>Update book</title>
    <style><%@include file="/WEB-INF/jsp/style/updateBook.css"%></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.3.0/lodash.js"></script>
</head>
<body>
    <div class="container-context">
        <jsp:include page="header.jsp"></jsp:include>
        <div class="container-login">
            <div class="col-sm-4">
                <h1 class="header-update">${locUpdateBook}</h1>
                <h2 class="header-form">Title:${book.title} Date:${book.datePublished} Quantity:${book.amountOfLeft}</h2>
                <form class="needs-validation" name = "update_book_form" method="post"
                      action="/controller?command=update_book" novalidate>
                    <input type="hidden" name="id" value="${book.id}">
                    <div class="mb-3">
                        <label for="exampleInputTitle" class="form-label">${locTitle}</label>
                        <input type="text" pattern=".{0,100}" name="title" class="form-control"
                               id="exampleInputTitle" required>
                        <div class="valid-feedback">
                            ${locValidText}
                        </div>
                        <div class="invalid-feedback">
                            ${locInvalidTitleText}
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="exampleInputDate" class="form-label">${locDatePublished}</label>
                        <input type="date" pattern="^\d{4}[-]?((((0[13578])|(1[02]))[-]?(([0-2][0-9])|(3[01])))|(((0[469])|(11))[-]?(([0-2][0-9])|(30)))|(02[-]?[0-2][0-9]))$"
                               name="date_published" class="form-control" id="exampleInputDate" required>
                        <div class="valid-feedback">
                            ${locValidText}
                        </div>
                        <div class="invalid-feedback">
                            ${locInvalidDateText}
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="exampleInputQuantity" class="form-label">${locQuantity}</label>
                        <input type="number" pattern="^\d{1,4}" name="amount_of_left" class="form-control"
                               id="exampleInputQuantity" required>
                        <div class="valid-feedback">
                            ${locValidText}
                        </div>
                        <div class="invalid-feedback">
                            ${locInvalidQuantityText}
                        </div>
                    </div>
                    <c:if test="${not empty requestScope.errorUpdateBookPassMessage}">
                        <b style="color: red">${requestScope.errorUpdateBookPassMessage}</b>
                        <br>
                    </c:if>
                    <button type="submit" class="btn btn-primary">${locUpdate}</button>
                </form>
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
