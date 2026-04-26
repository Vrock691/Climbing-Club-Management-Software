<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="h4 mb-3">Recuperation du mot de passe</h2>
                    <p class="text-muted">Saisissez votre email pour recevoir un mot de passe temporaire.</p>

                    <c:if test="${not empty message}">
                        <div class="alert alert-success">${message}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <form action="<c:url value='/auth/forgot-password'/>" method="post">
                        <div class="mb-3">
                            <label for="email" class="form-label">Adresse email</label>
                            <input id="email" name="email" type="email" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Envoyer</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="../footer.jsp" %>
