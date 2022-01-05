<% Object email = session.getAttribute("email"); %>
<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - About us</title>
    <header class="p-3 bg-primary text-white">
        <div class="container">
          <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            
            <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
              <% if (email != null) { 
                  out.println("<li><a href=\"dashboard.jsp\" class=\"nav-link px-2\">Dashboard</a></li>");
                  out.println("<li><a href=\"gallery.jsp\" class=\"nav-link px-2\">Gallery</a></li>");
                  out.println("<li><a href=\"streamin.jsp\" class=\"nav-link px-2\">Streaming</a></li>");
              }
              %>
              <li><a href="contact.jsp" class="nav-link px-2">Contact us</a></li>
              <li><a href="about.jsp" class="nav-link px-2 text-secondary">About us</a></li>
              <% if (email != null) { out.println("<li><a href=\"get_settings\" class=\"nav-link px-2\">Settings</a></li>"); } %>
          </ul>
  
          <div class="text-end">
              <% 
             
              if(email != null) {
                out.println(String.valueOf(session.getAttribute("email")).split("@")[0]);
                out.println("<button type=\"button\" class=\"btn btn-outline-light me-2\" onclick=\"location.href='logout'\">Log out</button>");
              } else {
                out.println("<button type=\"button\" class=\"btn btn-outline-light me-2\" onclick=\"location.href='index.html'\">Log In</button>");
                out.println("<button type=\"button\" class=\"btn btn-outline-light btn-warning\" onclick=\"location.href='register.html'\">Sign-up</button>");
              }
              %>
              
            <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
            </div>
          </div>
        </div>
      </header>
</head>
<body>
  <div class="bg-light">
    <div class="container py-5">
      <div class="row h-100 align-items-center py-5">
        <div class="col-lg-6">
          <h1 class="display-4">About us</h1>
          <p class="lead text-muted mb-0">SecurIA is a project lead by students to achieve the objective
            of a safe environment for living.</p>
        </div>
        <div class="col-lg-6 d-none d-lg-block"><img src="https://bootstrapious.com/i/snippets/sn-about/illus.png" alt="" class="img-fluid"></div>
      </div>
    </div>
  </div>
  
  <div class="bg-white py-5">
    <div class="container py-5">
      <div class="row align-items-center mb-5">
        <div class="col-lg-6 order-2 order-lg-1"><i class="fa fa-bar-chart fa-2x mb-3 text-primary"></i>
          <h2 class="font-weight-light">Why SecurIA?</h2>
          <p class="font-italic text-muted mb-4">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><a href="#" class="btn btn-light px-5 rounded-pill shadow-sm">Learn More</a>
        </div>
        <div class="col-lg-5 px-5 mx-auto order-1 order-lg-2"><img src="https://bootstrapious.com/i/snippets/sn-about/img-1.jpg" alt="" class="img-fluid mb-4 mb-lg-0"></div>
      </div>
      <div class="row align-items-center">
        <div class="col-lg-5 px-5 mx-auto"><img src="https://bootstrapious.com/i/snippets/sn-about/img-2.jpg" alt="" class="img-fluid mb-4 mb-lg-0"></div>
        <div class="col-lg-6"><i class="fa fa-leaf fa-2x mb-3 text-primary"></i>
          <h2 class="font-weight-light">Assurance of security</h2>
          <p class="font-italic text-muted mb-4">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><a href="#" class="btn btn-light px-5 rounded-pill shadow-sm">Learn More</a>
        </div>
      </div>
    </div>
  </div>
  
  <div class="bg-light py-5">
    <div class="container py-5">
      <div class="row mb-4">
        <div class="col-lg-5">
          <h2 class="display-4 font-weight-light">Our team</h2>
          <p class="font-italic text-muted">The team that created, developed and brought SecurIA to life.</p>
        </div>
      </div>
  
      <div class="row text-center">
        <!-- Team item-->
        <div class="col-xl-3 col-sm-6 mb-5 w-20 h-100">
          <div class="bg-white rounded shadow-sm py-5 px-4"><img src="https://bootstrapious.com/i/snippets/sn-about/avatar-1.png" alt="" width="100" class="img-fluid rounded-circle mb-3 img-thumbnail-about shadow-sm">
            <h4 class="mb-0">Eduardo Garzo Jimenez</h4><span class="small text-uppercase text-muted">CEO - Founder</span>
            <ul class="social mb-0 list-inline mt-3">
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-facebook-f"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-twitter"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-instagram"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-linkedin"></i></a></li>
            </ul>
          </div>
        </div>
        <!-- End-->
  
        <!-- Team item-->
        <div class="col-xl-3 col-sm-6 mb-5 w-20 h-100">
          <div class="bg-white rounded shadow-sm py-5 px-4"><img src="https://bootstrapious.com/i/snippets/sn-about/avatar-3.png" alt="" width="100" class="img-fluid rounded-circle mb-3 img-thumbnail-about shadow-sm">
            <h4 class="mb-0">Alberto Estevez Casarrubios</h4><span class="small text-uppercase text-muted">CEO - Founder</span>
            <ul class="social mb-0 list-inline mt-3">
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-facebook-f"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-twitter"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-instagram"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-linkedin"></i></a></li>
            </ul>
          </div>
        </div>
        <!-- End-->
  
        <!-- Team item-->
        <div class="col-xl-3 col-sm-6 mb-5 w-20 h-100">
          <div class="bg-white rounded shadow-sm py-5 px-4"><img src="https://bootstrapious.com/i/snippets/sn-about/avatar-2.png" alt="" width="100" class="img-fluid rounded-circle mb-3 img-thumbnail-about shadow-sm">
            <h4 class="mb-0">Alvaro Barragan Codesal</h4><span class="small text-uppercase text-muted">CEO - Founder</span>
            <ul class="social mb-0 list-inline mt-3">
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-facebook-f"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-twitter"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-instagram"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-linkedin"></i></a></li>
            </ul>
          </div>
        </div>
        <!-- End-->
  
        <!-- Team item-->
        <div class="col-xl-3 col-sm-6 mb-5 w-20 h-100">
          <div class="bg-white rounded shadow-sm py-5 px-4"><img src="/securia/Images/Alex_cut.jpg" alt="" width="100" class="img-fluid rounded-circle mb-3 img-thumbnail-about shadow-sm">
            <h4 class="mb-0">Nicolae Alexandru Molnar</h4><span class="small text-uppercase text-muted">CEO - Founder</span>
            <ul class="social mb-0 list-inline mt-3">
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-facebook-f"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-twitter"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-instagram"></i></a></li>
              <li class="list-inline-item"><a href="#" class="social-link"><i class="fa fa-linkedin"></i></a></li>
            </ul>
          </div>
        </div>
        <!-- End-->
  
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