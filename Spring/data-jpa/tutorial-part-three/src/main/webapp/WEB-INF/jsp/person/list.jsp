<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title><spring:message code="spring.data.jpa.example.title"/></title>
    <link rel="stylesheet" href="/static/css/styles.css" type="text/css"/>
</head>
<body>
<div>
    <a href="/"><spring:message code="person.list.link.label"/></a>
</div>
<div class="messages">
    <c:if test="${feedbackMessage != null}">
        <div class="messageblock"><c:out value="${feedbackMessage}"/></div>
    </c:if>
    <c:if test="${errorMessage != null}">
        <div class="errorblock"><c:out value="${errorMessage}"/></div>
    </c:if>
</div>
<h1><spring:message code="person.search.form.title"/></h1>
<form:form action="/person/search" commandName="searchCriteria" method="POST">
    <div>
        <form:label path="searchTerm"><spring:message code="person.search.searchterm.label"/></form:label>
        <form:input path="searchTerm" type="text"/>
    </div>
    <div>
        <form:label path="searchType"><spring:message code="person.search.searchtype.label"/></form:label>
        <form:select path="searchType">
            <form:option value="METHOD_NAME"><spring:message code="SearchType.METHOD_NAME"/></form:option>
            <form:option value="NAMED_QUERY"><spring:message code="SearchType.NAMED_QUERY"/></form:option>
            <form:option value="QUERY_ANNOTATION"><spring:message code="SearchType.QUERY_ANNOTATION"/></form:option>
        </form:select>
    </div>
    <div>
        <input type="submit" value="<spring:message code="person.search.form.submit.label"/>"/>
    </div>
</form:form>
<h1><spring:message code="person.list.page.title"/></h1>
<a href="/person/create"><spring:message code="person.create.link.label"/></a>
<c:if test="${not empty persons}">
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
</c:if>
<c:if test="${empty persons}">
    <p>
        <spring:message code="person.list.page.label.no.persons.found"/>
    </p>
</c:if>
</body>
</html>