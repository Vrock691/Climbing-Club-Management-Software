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
                            <c:when test="${action == 'edit'}">Modifier la sortie</c:when>
                            <c:otherwise>Proposer une sortie</c:otherwise>
                        </c:choose>
                    </h2>

                    <div class="mb-3">
                        <label for="searchCategory" class="form-label">Rechercher une catégorie</label>
                        <form method="get" class="mb-2">
                            <div class="input-group">
                                <input type="text" id="searchCategory" name="searchCategory" class="form-control" placeholder="Tapez le nom d'une catégorie..." value="${param.searchCategory}" />
                                <button class="btn btn-outline-secondary" type="submit">Rechercher</button>
                            </div>
                        </form>
                    </div>

                    <c:set var="postUrl" value="${action == 'edit' ? '/outings/'.concat(outing.getId()).concat('/update') : '/outings/new'}" />
                    <form:form action="${postUrl}" method="post" modelAttribute="outing">

                        <div class="mb-3">
                            <label for="categoryId" class="form-label">Catégorie</label>
                            <form:select path="categoryId" id="categoryId" class="form-select" required="required">
                                <form:option value="" label="-- Sélectionner une catégorie --" />
                                <form:options items="${suggestedCategoryList}" itemValue="id" itemLabel="name" />
                            </form:select>
                            <form:errors path="categoryId" class="text-danger small" />
                        </div>

                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom</label>
                            <form:input path="name" id="nom" class="form-control" required="required" />
                            <form:errors path="name" class="text-danger small" />
                        </div>


                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <form:textarea path="description" id="description" class="form-control" rows="4" />
                            <form:errors path="description" class="text-danger small" />
                        </div>

                        <div class="mb-3">
                            <label for="date" class="form-label">Date de la sortie</label>
                            <form:input path="date" id="date" type="date" class="form-control" required="required" />
                            <form:errors path="date" class="text-danger small" />
                        </div>

                        <div class="mb-3">
                            <label for="website" class="form-label">Site Internet (Optionnel)</label>
                            <form:input path="website" id="website" class="form-control" placeholder="https://..." />
                        </div>

                        <div class="d-flex gap-2 mt-4">
                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</main>


<%@ include file="../footer.jsp" %>