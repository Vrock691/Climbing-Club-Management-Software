<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Liste des sorties</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="navbar.jspf" %>

<div class="container">
    <h2>Sorties pour la catégorie : ${categorie.nom}</h2>

    <form action="/public/search" method="get" class="row g-3 mb-4">
        <div class="col-md-6">
            <input type="text" name="nom" class="form-control" placeholder="Rechercher par nom...">
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-secondary">Filtrer</button>
        </div>
    </form>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Nom</th>
            <th>Description</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pageSorties.content}" var="s">
            <tr>
                <td><fmt:formatDate value="${s.dateSortie}" pattern="dd/MM/yyyy" /></td>
                <td><strong>${s.nom}</strong></td>
                <td>${s.description}</td>
                <td><a href="/public/sortie/${s.id}" class="btn btn-info btn-sm">Détails</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <nav>
        <ul class="pagination">
            <c:forEach begin="0" end="${pageSorties.totalPages - 1}" var="i">
                <li class="page-item ${pageSorties.number == i ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}">${i + 1}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</div>
</body>
</html>