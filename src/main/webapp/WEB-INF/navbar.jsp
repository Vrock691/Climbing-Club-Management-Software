<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container">
        <a class="navbar-brand" href="<c:url value='/'/>">Club Escalade</a>

        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="<c:url value='/categories'/>">Catégories</a></li>
                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/outings/new'/>">Proposer une sortie</a></li>
                </sec:authorize>
            </ul>

            <div class="navbar-nav">
                <sec:authorize access="isAnonymous()">
                    <a class="btn btn-outline-light" href="<c:url value='/auth/login'/>">Connexion</a>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <span class="navbar-text me-3">Bonjour, <sec:authentication property="principal.username" /></span>
                    <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="btn btn-danger btn-sm">Déconnexion</button>
                    </form>
                </sec:authorize>
            </div>
        </div>
    </div>
</nav>