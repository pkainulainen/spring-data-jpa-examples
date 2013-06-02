<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="spring.data.jpa.example.title"/></title>
    <link rel="stylesheet" href="/static/css/styles.css" type="text/css"/>
</head>
<body>
<div class="messages">
    <c:if test="${feedbackMessage != null}">
        <div class="messageblock"><c:out value="${feedbackMessage}"/></div>
    </c:if>
    <c:if test="${errorMessage != null}">
        <div class="errorblock"><c:out value="${errorMessage}"/></div>
    </c:if>
</div>
<h1><spring:message code="person.list.page.title"/></h1>
<a href="/person/create"><spring:message code="person.create.link.label"/></a>
<table>
    <thead>
    <tr>
        <td><spring:message code="person.label.lastName"/></td>
        <td><spring:message code="person.label.firstName"/></td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${persons}" var="person">
        <tr>
            <td><c:out value="${person.lastName}"/></td>
            <td><c:out value="${person.firstName}"/></td>
            <td><a href="/person/edit/<c:out value="${person.id}"/>"><spring:message code="person.edit.link.label"/></a></td>
            <td><a href="/person/delete/<c:out value="${person.id}"/>"><spring:message code="person.delete.link.label"/></a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>