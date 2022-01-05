<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Dashboard</title>
</head>
<body class="h-100">
  <div class="d-flex flex-column flex-5">
    <div class="d-flex flex-row">
      <header class="p-3 bg-primary text-white w-auto flex-1">
        <div class="container">
          <div class="d-grid align-items-center justify-content-center justify-content-lg-start">
            <div class="d-flex flex-column">
              <div class="d-grid w-100">
                
                <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">
              
                <a href="dashboard.jsp" class="rounded text-center my-2">Dashboard</a>
                <a href="gallery.jsp" class="rounded text-center my-2">Gallery</a>
                <a href="streaming.jsp" class="rounded text-center my-2">Streaming</a>
                <a href="contact.jsp" class="rounded text-center my-2">Contact us</a>
                <a href="about.jsp" class="rounded text-center my-2">About us</a>
                <a href="get_settings" class="rounder text-center my-2">Settings</a>
        
                <div class="text-end mt-lg-5">
                  <button type="button" onclick="location.href='logout'" class="btn btn-outline-light me-2">Log out</button>
                  <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>
    
      <div class="d-grid justify-content-center flex-5">
        <h1 class=""> Welcome to SecurIA.com </h1>

        <div class="d-flex align-items-center justify-content-center justify-content-lg-start">
          <div class="d-flex flex-row flex-5">
            <div class="d-grid flex-1">
              <h3>System statistics</h3>
              <div class="d-grid">
                <div class="d-grid align-items-center justify-content-center justify-content-lg-start">
                  <div class="d-flex flex-row">
                    <div class="d-grid md-lg-2">
                      <p>Number of users: </p>
                      <p>Number of cameras:</p>
                      <p>Number of alerts:</p>
                    </div>
                    <div class="d-grid">
                      <p>0</p>
                      <p>0</p>
                      <p>0</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="d-grid flex-1">
            <h3>Camera statistics</h3>
            <div class="d-grid">
              <div class="d-grid align-items-center justify-content-center">
                <div class="d-flex flex-row">
                  <div class="d-grid md-lg-2">
                    <p>Number of users: </p>
                    <p>Number of cameras:</p>
                    <p>Number of alerts:</p>
                  </div>
                  <div class="d-grid">
                    <p>0</p>
                    <p>0</p>
                    <p>0</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <footer class="bg-light pb-5 flex-1">
      <div class="container text-center">
        <p class="font-italic text-muted mb-0">&copy; Copyrights SecurIA.com All rights reserved.</p>
      </div>
    </footer>
  </div>
</body>
</html>