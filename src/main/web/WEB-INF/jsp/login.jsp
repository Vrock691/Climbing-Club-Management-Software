<%@ include file="header.jsp" %>
<%@ include file="navbar.jspf" %>

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-lg border-0 px-3 py-4">
                <div class="card-body">
                    <h2 class="text-center fw-bold mb-4">Espace Membre</h2>


                    <c:if test="${param.error != null}">
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <div class="small">Identifiants incorrects. Veuillez réessayer.</div>
                        </div>
                    </c:if>

                    <c:if test="${param.logout != null}">
                        <div class="alert alert-success d-flex align-items-center" role="alert">
                            <div class="small">Vous avez été déconnecté avec succès.</div>
                        </div>
                    </c:if>


                    <form action="/login" method="post" class="mt-4">

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="form-floating mb-3">
                            <input type="email" name="username" class="form-control" id="floatingInput" placeholder="nom@exemple.com" required>
                            <label for="floatingInput">Adresse Email</label>
                        </div>

                        <div class="form-floating mb-4">
                            <input type="password" name="password" class="form-control" id="floatingPassword" placeholder="Mot de passe" required>
                            <label for="floatingPassword">Mot de passe</label>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg shadow-sm">Se connecter</button>
                        </div>
                    </form>

                    <hr class="my-4">

                    <div class="text-center">
                        <p class="mb-1">
                            <a href="/forgot-password" class="text-decoration-none text-secondary small">Mot de passe oublié ?</a>
                        </p>
                        <p class="mb-0">
                            <span class="small text-muted">Créer un compte.</span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="footer.jsp" %>