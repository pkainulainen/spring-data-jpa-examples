<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form action="/person/search" commandName="searchCriteria" method="POST">
    <fieldset>
        <legend><spring:message code="person.search.form.title"/></legend>
        <div>
            <form:label path="searchTerm"><spring:message code="person.search.searchterm.label"/></form:label>
            <form:input path="searchTerm" type="text"/>
        </div>
        <div>
            <input type="submit" value="<spring:message code="person.search.form.submit.label"/>"/>
        </div>
    </fieldset>
</form:form>