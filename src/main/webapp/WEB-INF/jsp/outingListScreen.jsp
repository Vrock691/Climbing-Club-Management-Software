<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<main class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Sorties : <span class="text-primary">${paginatedResponse.outings().totalElements()}</span></h2>
        <a href="<c:url value='/categories'/>" class="btn btn-outline-secondary btn-sm">Retour aux catégories</a>
    </div>

    <div class="card mb-4 border-0 shadow-sm">
        <div class="card-body">
            <form action="<c:url value='/search'/>" method="get" class="row g-2">
                <div class="col-md-9">
                    <input type="text" name="name" class="form-control" placeholder="Rechercher une sortie par nom...">
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-dark w-100">Rechercher</button>
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
                    <a class="page-link" href="<c:url value='?page=${paginatedResponse.outings().pageNumber() - 1}' />" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <!-- Page Numbers -->
                <c:forEach begin="0" end="${paginatedResponse.outings().totalPages() - 1}" var="pageNum">
                    <li class="page-item <c:if test="${pageNum == paginatedResponse.outings().pageNumber()}">active</c:if>">
                        <a class="page-link" href="<c:url value='?page=${pageNum}' />">${pageNum + 1}</a>
                    </li>
                </c:forEach>

                <!-- Next Button -->
                <li class="page-item <c:if test="${paginatedResponse.outings().isLast()}">disabled</c:if>">
                    <a class="page-link" href="<c:url value='?page=${paginatedResponse.outings().pageNumber() + 1}' />" aria-label="Next">
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