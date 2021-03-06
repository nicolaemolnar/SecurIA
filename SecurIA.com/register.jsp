<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Sign up</title>

    <header class="p-3 bg-primary text-white">
      <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
          <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">
          
          <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
          </ul>
  
          <div class="text-end">
            <button type="button" onclick="location.href='logout'" class="btn btn-outline-light me-2">Login</button>
            <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
          </div>
        </div>
      </div>
    </header>
</head>
<body>
    <section class="vh-100 my-3">
        <div class="container h-100">
          <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-lg-12 col-xl-11">
              <div class="card text-black" style="border-radius: 25px;">
                <div class="card-body p-md-5">
                  <div class="row justify-content-center">
                    <div class="col-md-10 col-lg-6 col-xl-5 order-2 order-lg-1">
      
                      <p class="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">Sign up</p>
      
                      <div class="bg-light rounded-3">
                      <form action="register" method="post" class="mx-1 mx-md-4 my-3">
                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0 mt-3">
                            <label class="form-label" for="form3Example1c">Your Name</label>
                            <input type="text" id="form3Example1c" name="username" required=True class="form-control" />
                          </div>
                        </div>
      
                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-envelope fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0">
                            <label class="form-label" for="form3Example3c">Your Email</label>
                            <input type="email" id="form3Example3c" name="email" required=True class="form-control" />
                          </div>
                        </div>
      
                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-lock fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0">
                            <label class="form-label" for="form3Example4c">Password</label>
                            <input type="password" id="form3Example4c" name="password" required=True class="form-control" />
                          </div>
                        </div>
      
                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-key fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0">
                            <label class="form-label" for="form3Example4cd">Repeat your password</label>
                            <input type="password" id="form3Example4cd" name="password2" required=True class="form-control" />
                          </div>
                        </div>

                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0">
                            <label class="form-label" for="form3Example1c">First Name</label>
                            <input type="text" id="form3Example1c" name="first_name" required=True class="form-control" />
                          </div>
                        </div>

                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0">
                            <label class="form-label" for="form3Example1c">Surname</label>
                            <input type="text" id="form3Example1c" name="surname" required=True class="form-control" />
                          </div>
                        </div>
                        
                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0">
                            <label class="form-label" for="form3Example1c">Phone</label>
                            <input type="text" id="form3Example1c" name="phone_number" required=True class="form-control" />
                          </div>
                        </div>
                        
                        <div class="d-flex flex-row align-items-center mb-4">
                          <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                          <div class="form-outline flex-fill mb-0">
                            <label class="form-label" for="form3Example1c">Birth Date</label>
                            <input type="date" id="form3Example1c" name="birth_date"  class="form-control" />
                          </div>
                        </div>

                        <div class="form-check d-flex justify-content-center">
                          <input
                            class="form-check-input me-2"
                            type="checkbox"
                            value=""
                            id="form2Example3c"
                            required=True
                          />
                          <label class="form-check-label mb-3" for="form2Example3">
                            I agree all statements in <a href="termsOfService.txt">Terms of service</a>
                          </label>
                        </div>

                        <div class="form-check d-flex justify-content-center">
                          <% if (session.getAttribute("error") != null){
                            out.println("<p style='color:red;'>" + session.getAttribute("error") + "</p>");
                            session.removeAttribute("error");
                          } %>
                        </div>
                        <div class="d-flex justify-content-center mx-4 mb-3 mt-5">
                          <button type="submit" class="btn btn-outline-dark text-white btn-primary btn-lg">Register</button>
                        </div>
                      </form>
                    </div>
                    </div>
                    <div class="col-md-10 col-lg-6 col-xl-7 h-50 d-flex align-items-center order-1 order-lg-2">
                      <img src="logo.jpg" class="img-fluid" alt="Sample image">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      
</body>
</html>