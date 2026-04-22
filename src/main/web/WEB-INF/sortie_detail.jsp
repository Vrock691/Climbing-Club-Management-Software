<div class="card shadow">
    <div class="card-body">
        <h1 class="card-title">${sortie.nom}</h1>
        <p class="badge bg-info">${sortie.categorie.nom}</p>
        <hr>
        <p class="lead">${sortie.description}</p>

        <sec:authorize access="isAuthenticated()">
            <div class="alert alert-light border">
                <h5>Informations Membres :</h5>
                <p><strong>Site Web :</strong> <a href="${sortie.siteWeb}" target="_blank">${sortie.siteWeb}</a></p>
                <p><strong>Organisateur :</strong> ${sortie.createur.prenom} ${sortie.createur.nom} (${sortie.createur.email})</p>
            </div>

            <c:if test="${pageContext.request.userPrincipal.name == sortie.createur.email}">
                <div class="mt-3">
                    <a href="/member/sortie/edit/${sortie.id}" class="btn btn-warning">Modifier</a>
                    <a href="/member/sortie/delete/${sortie.id}" class="btn btn-danger"
                       onclick="return confirm('Supprimer cette sortie ?')">Supprimer</a>
                </div>
            </c:if>
        </sec:authorize>

        <sec:authorize access="isAnonymous()">
            <p class="text-muted italic">Connectez-vous pour voir le site web et le créateur.</p>
        </sec:authorize>
    </div>
</div>