<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow border-0">
                <div class="card-header bg-primary text-white py-3">
                    <h3 class="card-title mb-0">${outing.name()}</h3>
                </div>
                <div class="card-body p-4">
                    <div class="mb-4">
                        <span class="badge bg-secondary mb-2">${outing.category().name()}</span>
                        <p class="text-muted"><i class="bi bi-calendar"></i> Prévue le : <strong><fmt:formatDate value="${outing.date()}" pattern="dd MMMM yyyy" /></strong></p>
                    </div>

                    <h5>Description</h5>
                    <p class="lead">${outing.description()}</p>

                    <hr class="my-4">

                    <sec:authorize access="isAuthenticated()">
                        <div class="bg-light p-3 rounded mb-4">
                            <h5 class="text-primary">Informations réservées aux membres</h5>
                            <ul class="list-unstyled mb-0">
                                <li class="mb-2"><strong>🌐 Site Web :</strong> <a href="${outing.website()}" target="_blank">${outing.website()}</a></li>
                                <li><strong>👤 Organisateur :</strong> ${outing.member().firstName()} ${outing.member().lastName()} (${outing.member().username()})</li>
                            </ul>
                        </div>

                        <c:if test="${pageContext.request.userPrincipal.name == outing.member().username()}">
                            <div class="d-flex gap-2">
                                <a href="<c:url value='/outings/${outing.id()}/update'/>" class="btn btn-warning">Modifier ma sortie</a>
                                <form method="POST" action="<c:url value='/outings/${outing.id()}/delete'/>" style="display:inline;">
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                    <button type="submit" class="btn btn-danger" onclick="return confirm('Supprimer définitivement ?')">Supprimer</button>
                                </form>
                            </div>
                        </c:if>
                    </sec:authorize>

                    <sec:authorize access="isAnonymous()">
                        <div class="alert alert-info border-0">
                            <strong>Note :</strong> Connectez-vous pour accéder au site web de la sortie et contacter l'organisateur.
                        </div>
                    </sec:authorize>

                    <div class="mt-4">
                        <a href="javascript:history.back()" class="text-decoration-none">← Retour à la liste</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="../footer.jsp" %>