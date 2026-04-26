<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<main class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Sorties : <span class="text-primary">${paginatedResponse.outings().totalElements()}</span></h2>
        <a href="<c:url value='/categories'/>" class="btn btn-outline-secondary btn-sm">Retour aux catégories</a>
    </div>

    <div class="mb-3">
        <button class="btn btn-outline-primary btn-sm" type="button"
                data-bs-toggle="collapse"
                data-bs-target="#searchFormCollapse"
                aria-expanded="true"
                aria-controls="searchFormCollapse">
            Afficher / masquer les filtres
        </button>
    </div>

    <div class="collapse hide" id="searchFormCollapse">
        <div class="card card-body mb-4">
            <form method="get">
                <div class="mb-3">
                    <label for="name" class="form-label">Nom</label>
                    <input id="name" name="name" type="text" class="form-control" placeholder="Nom de la sortie" value="${param.name}">
                </div>

                <c:if test="${pageContext.request.userPrincipal != null}">
                    <div class="mb-3">
                        <label for="ownerIds" class="form-label">Organisateurs</label>
                        <select id="ownerIds" name="ownerIds" class="form-select" multiple size="5">
                            <option value="">-- Sélectionner des organisateurs --</option>
                            <c:forEach items="${paginatedResponse.organizers()}" var="member">
                                <option value="${member.username}">${member.firstName} ${member.lastName}</option>
                            </c:forEach>
                        </select>
                        <div class="form-text">Maintenir Ctrl/Cmd pour sélectionner plusieurs organisateurs.</div>
                    </div>

                    <div class="mb-3">
                        <label for="ownerUsernameManual" class="form-label">Ou saisir un username</label>
                        <input
                                id="ownerUsernameManual"
                                name="ownerIds"
                                type="text"
                                class="form-control"
                                placeholder="ex: ${pageContext.request.userPrincipal.name}"
                                value="${param.ownerIds}">
                        <div class="form-text">Ce username sera ajouté au filtre des organisateurs.</div>
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="dateFrom" class="form-label">Date début</label>
                        <input id="dateFrom" name="dateFrom" type="date" class="form-control" value="${param.dateFrom}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="dateTo" class="form-label">Date fin</label>
                        <input id="dateTo" name="dateTo" type="date" class="form-control" value="${param.dateTo}">
                    </div>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">Rechercher</button>
                </div>
            </form>
        </div>
    </div>

    <c:if test="${paginatedResponse.error() != null}">
        <p class="alert alert-warning">${paginatedResponse.error()}</p>
    </c:if>

    <div class="table-responsive shadow-sm rounded">
        <table class="table table-hover bg-white align-middle">
            <thead class="table-light">
            <tr>
                <th>Date</th>
                <th>Nom de la sortie</th>
                <th>Description</th>
                <th class="text-center">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${paginatedResponse.outings().content()}" var="s">
                <tr>
                    <td class="text-nowrap"><fmt:formatDate value="${s.date}" pattern="dd/MM/yyyy" /></td>
                    <td><strong>${s.name}</strong></td>
                    <td class="text-truncate" style="max-width: 300px;">${s.description}</td>
                    <td class="text-center">
                        <a href="<c:url value='/outings/${s.id}'/>" class="btn btn-info btn-sm text-white">Voir détails</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <c:if test="${paginatedResponse.outings().totalPages() > 1}">
        <nav class="mt-5" aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <!-- Previous Button -->
                <li class="page-item <c:if test="${paginatedResponse.outings().isFirst()}">disabled</c:if>">
                    <a class="page-link" href="<c:url value='?page=${paginatedResponse.outings().pageNumber() - 1}&name=${param.name}&ownerIds=${param.ownerIds}&dateFrom=${param.dateFrom}&dateTo=${param.dateTo}' />" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <!-- Page Numbers -->
                <c:forEach begin="0" end="${paginatedResponse.outings().totalPages() - 1}" var="pageNum">
                    <li class="page-item <c:if test="${pageNum == paginatedResponse.outings().pageNumber()}">active</c:if>">
                        <a class="page-link" href="<c:url value='?page=${pageNum}&name=${param.name}&ownerIds=${param.ownerIds}&dateFrom=${param.dateFrom}&dateTo=${param.dateTo}' />">${pageNum + 1}</a>
                    </li>
                </c:forEach>

                <!-- Next Button -->
                <li class="page-item <c:if test="${paginatedResponse.outings().isLast()}">disabled</c:if>">
                    <a class="page-link" href="<c:url value='?page=${paginatedResponse.outings().pageNumber() + 1}&name=${param.name}&ownerIds=${param.ownerIds}&dateFrom=${param.dateFrom}&dateTo=${param.dateTo}' />" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>

        <!-- Pagination Info -->
        <div class="text-center text-muted mt-3">
            <small>Page ${paginatedResponse.outings().pageNumber() + 1} sur ${paginatedResponse.outings().totalPages()} | ${paginatedResponse.outings().totalElements()} résultats</small>
        </div>
    </c:if>
</main>

<%@ include file="../footer.jsp" %>