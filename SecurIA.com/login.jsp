<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - We watch so you dont have to</title>
    <header class="p-3 bg-primary text-white">
      <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
          <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">
                    
          <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
            <li><a href="contact.jsp" class="nav-link px-2">Contact us</a></li>
            <li><a href="about.jsp" class="nav-link px-2">About us</a></li>
        </ul>
        <div class="text-end">
          <button type="button" onclick="location.href='register.jsp'" class="btn btn-outline-light btn-warning">Sign-up</button>
        </div>
        </div>
      </div>
    </header>
    <script>
      function notImplemented(){
        alert("This feature is not implemented yet. Please use the contact us page to ask a technician to help you.");
      }
    </script>
</head>
<body>
    <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
          <div class="col col-xl-10">
            <div class="card" style="border-radius: 1rem;">
              <div class="row g-0">
                <div class="col-md-6 col-lg-5 d-none d-md-block">
                  <img
                    src="https://mdbootstrap.com/img/Photos/new-templates/bootstrap-login-form/img1.jpg"
                    alt="login form"
                    class="img-fluid" style="border-radius: 1rem 0 0 1rem;"
                  />
                </div>
                <div class="col-md-6 col-lg-7 d-flex align-items-center">
                  <div class="card-body p-4 p-lg-5 text-black">
    
                    <form action="login" method="post">
    
                      <div class="d-flex align-items-center mb-3 pb-1">
                        <i class="fas fa-cubes fa-2x me-3" style="color: #ff6219;"></i>
                        <span class="h1 fw-bold mb-0">SecurIA Login</span>
                      </div>
    
                      <h5 class="fw-normal mb-3 pb-3" style="letter-spacing: 1px;">Sign into your account</h5>
    
                      <div class="form-outline mb-4">
                        <label class="form-label" for="form2Example17">Email address</label>
                        <input type="email" id="email" name="email" required class="form-control form-control-lg" required=True />
                      </div>
    
                      <div class="form-outline mb-4">
                        <label class="form-label" for="form2Example27">Password</label>
                        <input type="password" id="password" name="password" required class="form-control form-control-lg" required=True />
                      </div>

                      <div class="form-outline mb-4">
                        <% if (session.getAttribute("error") != null){
                            out.println("<p style='color:red;'>" + session.getAttribute("error") + "</p>");
                            session.removeAttribute("error");
                        } %>
                      </div>

                      <div class="pt-1 mb-4">
                        <button class="btn btn-primary btn-outline-dark text-white btn-lg btn-block" type="submit">Login</button>
                      </div>
    
                      <a class="small text-muted" onclick="notImplemented()">Forgot password?</a>
                      <p class="mb-5 pb-lg-2" style="color: #393f81;">Don't have an account? <a href="register.jsp" style="color: #393f81;">Register here</a></p>
                      <a href="/securia/termsOfService.txt" class="small text-muted">Terms of use.</a>
                      <a href="/securia/termsOfPrivacy.txt" class="small text-muted">Privacy policy</a>
                    </form>
    
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <footer class="bg-light pb-5">
        <div class="container text-center">
          <p class="font-italic text-muted mb-0">&copy; Copyrights SecurIA.com All rights reserved.</p>
        </div>
      </footer>
</body>
</html>