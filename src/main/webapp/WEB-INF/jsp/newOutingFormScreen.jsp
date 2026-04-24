<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="h4 mb-4">
                        <c:choose>
                            <c:when test="${not empty outingId}">Modifier la sortie</c:when>
                            <c:otherwise>Proposer une sortie</c:otherwise>
                        </c:choose>
                    </h2>

                    <%-- On définit l'URL dynamiquement selon qu'on a un ID ou non --%>
                    <c:set var="postUrl" value="${not empty outingId ? '/outings/'.concat(outingId).concat('/update') : '/outings/new'}" />

                    <form:form action="${postUrl}" method="post" modelAttribute="sortie">
                        <%-- Le token CSRF est géré automatiquement par form:form si configuré,
                             sinon Spring Boot l'ajoute tout seul --%>

                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom</label>
                            <form:input path="nom" id="nom" class="form-control" required="required" />
                            <form:errors path="nom" class="text-danger small" />
                        </div>

                        <div class="mb-3">
                            <label for="idCategorie" class="form-label">Catégorie</label>
                            <form:select path="idCategorie" id="idCategorie" class="form-select" required="required">
                                <form:option value="" label="-- Sélectionner une catégorie --" />
                                <form:options items="${categories}" itemValue="id" itemLabel="nom" />
                            </form:select>
                            <form:errors path="idCategorie" class="text-danger small" />
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <form:textarea path="description" id="description" class="form-control" rows="4" />
                            <form:errors path="description" class="text-danger small" />
                        </div>

                        <div class="mb-3">
                            <label for="dateSortie" class="form-label">Date de la sortie</label>
                            <form:input path="dateSortie" id="dateSortie" type="date" class="form-control" required="required" />
                            <form:errors path="dateSortie" class="text-danger small" />
                        </div>

                        <div class="mb-3">
                            <label for="siteWeb" class="form-label">Site Internet (Optionnel)</label>
                            <form:input path="siteWeb" id="siteWeb" class="form-control" placeholder="https://..." />
                        </div>

                        <div class="d-flex gap-2 mt-4">
                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                            <a href="<c:url value='/categories'/>" class="btn btn-outline-secondary">Annuler</a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="../footer.jsp" %>