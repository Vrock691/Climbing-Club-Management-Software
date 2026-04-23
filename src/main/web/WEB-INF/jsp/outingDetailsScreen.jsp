<%@ include file="header.jsp" %>
<%@ include file="navbar.jspf" %>

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow border-0">
                <div class="card-header bg-primary text-white py-3">
                    <h3 class="card-title mb-0">${sortie.nom}</h3>
                </div>
                <div class="card-body p-4">
                    <div class="mb-4">
                        <span class="badge bg-secondary mb-2">${sortie.categorie.nom}</span>
                        <p class="text-muted"><i class="bi bi-calendar"></i> Prévue le : <strong><fmt:formatDate value="${sortie.dateSortie}" pattern="dd MMMM yyyy" /></strong></p>
                    </div>

                    <h5>Description</h5>
                    <p class="lead">${sortie.description}</p>

                    <hr class="my-4">

                    <sec:authorize access="isAuthenticated()">
                        <div class="bg-light p-3 rounded mb-4">
                            <h5 class="text-primary">Informations réservées aux membres</h5>
                            <ul class="list-unstyled mb-0">
                                <li class="mb-2"><strong>🌐 Site Web :</strong> <a href="${sortie.siteWeb}" target="_blank">${sortie.siteWeb}</a></li>
                                <li><strong>👤 Organisateur :</strong> ${sortie.createur.prenom} ${sortie.createur.nom}</li>
                            </ul>
                        </div>

                        <c:if test="${pageContext.request.userPrincipal.name == sortie.createur.email}">
                            <div class="d-flex gap-2">
                                <a href="/member/sortie/edit/${sortie.id}" class="btn btn-warning">Modifier ma sortie</a>
                                <a href="/member/sortie/delete/${sortie.id}" class="btn btn-danger" onclick="return confirm('Supprimer définitivement ?')">Supprimer</a>
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

<%@ include file="footer.jsp" %>