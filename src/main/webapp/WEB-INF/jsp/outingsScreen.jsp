<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<main class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Sorties : <span class="text-primary">${categorie.nom}</span></h2>
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
            <c:forEach items="${pageSorties.content}" var="s">
                <tr>
                    <td class="text-nowrap"><fmt:formatDate value="${s.dateSortie}" pattern="dd/MM/yyyy" /></td>
                    <td><strong>${s.nom}</strong></td>
                    <td class="text-truncate" style="max-width: 300px;">${s.description}</td>
                    <td class="text-center">
                        <a href="<c:url value='/outings/${s.id}'/>" class="btn btn-info btn-sm text-white">Voir détails</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <nav class="mt-4">
        <ul class="pagination justify-content-center">
            <c:forEach begin="0" end="${pageSorties.totalPages - 1}" var="i">
                <li class="page-item ${pageSorties.number == i ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}">${i + 1}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</main>

<%@ include file="../footer.jsp" %>