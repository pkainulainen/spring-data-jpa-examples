<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<h1><spring:message code="person.list.page.title"/></h1>
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