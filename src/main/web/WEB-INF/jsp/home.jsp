<%@ include file="header.jsp" %>
<%@ include file="navbar.jspf" %>

<main class="container my-5">
    <div class="text-center mb-5">
        <h1 class="display-4 fw-bold">Catalogue d'Escalade</h1>
        <p class="lead text-muted">Découvrez les sorties organisées par notre communauté de passionnés.</p>
        <hr class="w-25 mx-auto">
    </div>

    <div class="row g-4">
        <c:forEach items="${categories}" var="cat">
            <div class="col-md-4 col-sm-6">
                <div class="card h-100 shadow-sm border-0 bg-white">
                    <div class="card-body d-flex flex-column text-center">
                        <div class="mb-3 mt-2">
                            <span class="display-6 text-primary">⛰️</span>
                        </div>
                        <h4 class="card-title fw-bold">${cat.nom}</h4>
                        <p class="card-text text-muted flex-grow-1">
                            Explorez toutes les sorties enregistrées dans la catégorie <strong>${cat.nom}</strong>.
                        </p>
                        <a href="/public/categorie/${cat.id}" class="btn btn-outline-primary mt-3 stretched-link">
                            Voir les sorties
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty categories}">
        <div class="alert alert-light text-center border mt-5">
            <p class="mb-0">Aucune catégorie n'est disponible pour le moment.</p>
        </div>
    </c:if>
</main>

<%@ include file="footer.jsp" %>