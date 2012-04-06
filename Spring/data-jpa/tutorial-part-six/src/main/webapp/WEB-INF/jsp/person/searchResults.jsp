<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="spring.data.jpa.example.title"/></title>
    <link rel="stylesheet" href="/static/css/styles.css" type="text/css"/>
</head>
<body>
<jsp:include page="navigation.jsp"/>
<jsp:include page="searchForm.jsp"/>
<h1><spring:message code="person.search.result.page.title"/>: <c:out value="${searchCriteria.searchTerm}"/></h1>
<jsp:include page="personList.jsp"/>
</body>
</html>