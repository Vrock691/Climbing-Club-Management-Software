<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<main class="container my-5">
    <h1>Categories</h1>

    <div class="row mb-5">
        <div class="col-md-6 mx-auto">
            <form method="GET" action="<c:url value='/categories'/>" class="input-group">
                <input 
                    type="text" 
                    class="form-control" 
                    name="categoryName" 
                    placeholder="Rechercher une catégorie par nom..."
                    value="${categoryName}"
                >
                <button class="btn btn-primary" type="submit">
                    <i class="bi bi-search"></i> Rechercher
                </button>
                <c:if test="${not empty categoryName}">
                    <a href="<c:url value='/categories'/>" class="btn btn-secondary">
                        Réinitialiser
                    </a>
                </c:if>
            </form>
        </div>
    </div>

    <div class="row g-4">
        <c:forEach items="${paginatedResponse.content()}" var="cat">
            <div class="col-md-4 col-sm-6">
                <div class="card h-100 shadow-sm border-0 bg-white">
                    <div class="card-body d-flex flex-column text-center">
                        <div class="mb-3 mt-2">
                            <span class="display-6 text-primary">⛰️</span>
                        </div>
                        <h4 class="card-title fw-bold">${cat.name}</h4>
                        <p class="card-text text-muted flex-grow-1">
                            Explorez toutes les sorties enregistrées dans la catégorie <strong>${cat.name}</strong>.
                        </p>
                        <a href="<c:url value='/categories/${cat.id}'/>" class="btn btn-outline-primary mt-3 stretched-link">
                            Voir les sorties
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty paginatedResponse.content()}">
        <div class="alert alert-light text-center border mt-5">
            <p class="mb-0">Aucune catégorie trouvée.</p>
        </div>
    </c:if>

    <c:if test="${paginatedResponse.totalPages() > 1}">
        <nav class="mt-5" aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <li class="page-item <c:if test="${paginatedResponse.isFirst()}">disabled</c:if>">
                    <a class="page-link" href="<c:url value='/categories?page=${paginatedResponse.pageNumber() - 1}'/>" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <!-- Page Numbers -->
                <c:forEach begin="0" end="${paginatedResponse.totalPages() - 1}" var="pageNum">
                    <li class="page-item <c:if test="${pageNum == paginatedResponse.pageNumber()}">active</c:if>">
                        <a class="page-link" href="<c:url value='/categories?page=${pageNum}'/>">${pageNum + 1}</a>
                    </li>
                </c:forEach>

                <li class="page-item <c:if test="${paginatedResponse.isLast()}">disabled</c:if>">
                    <a class="page-link" href="<c:url value='/categories?page=${paginatedResponse.pageNumber() + 1}'/>" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>

        <div class="text-center text-muted mt-3">
            <small>Page ${paginatedResponse.pageNumber() + 1} sur ${paginatedResponse.totalPages()} | ${paginatedResponse.totalElements()} résultats</small>
        </div>
    </c:if>
</main>

<%@ include file="../footer.jsp" %>