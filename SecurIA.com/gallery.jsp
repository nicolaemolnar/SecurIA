<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Gallery</title>
    <header class="p-3 bg-primary text-white">
        <div class="container">
          <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            
            <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="dashboard.jsp" class="nav-link px-2">Dashboard</a></li>
                <li><a href="gallery.jsp" class="nav-link px-2 text-secondary">Gallery</a></li>
                <li><a href="streaming.jsp" class="nav-link px-2">Streaming</a></li>
                <li><a href="contact.jsp" class="nav-link px-2">Contact us</a></li>
                <li><a href="about.jsp" class="nav-link px-2">About us</a></li>
                <li><a href="get_settings" class="nav-link px-2">Settings</a></li>
            </ul>
    
            <div class="text-end">
              <%= String.valueOf(session.getAttribute("email")).split("@")[0] %>
              <button type="button" onclick="location.href='logout'" class="btn btn-outline-light me-2">Log out</button>
                            <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
            </div>
          </div>
        </div>
      </header>
      
</head>
<body>

  <div class="align-content-center rounded-top d-grid mb-lg-3" style="border: 1px solid;">
    <div class="d-flex justify-content-center md-5 mb-3 pb-1">
        <span class="h3 fw-bold mb-0 mt-2">WebCam Image Gallery</span>
    </div>
  </div>

  <div class="d-grid">
    <div class="d-flex justify-content-center">
      <div class="d-grid">
        <img src="Avatar.jpg" class="border-card justify-content-center" style="width: 450px;height: 450px;">
      </div>
      <div class="grid p-md-4">
        <div>
          <h4>Image properties</h4>
          <hr class="line-blue" width="100%" style="height: 3px;">
          <p><b>Label:</b> Undetected</p>
          <p><b>Timestamp:</b> 18-11-2021 10:51:22</p>
        </div>
        <div class="d-grid mt-xl-5 align-items-end" >
          <button class="btn btn-outline-light btn-primary" style="height: fit-content;">Download</button>
          <button class="btn btn-outline-light btn-primary" style="height: fit-content;">Delete</button>
        </div>
      </div>
    </div>
  </div>

  <div class="mx-lg-4 my-lg-4">
    <div class="d-flex">
      <h3 class="flex-5">Captured Images</h3>
      <div class="d-flex">
        <div class="d-grid me-lg-4">
          <label for="filter">Image filter</label>
          <input type="date" id="filter">
        </div>
        <div class="d-grid align-items-end ">
          <button class="btn btn-outline-light btn-primary" style="height: fit-content;">Filter</button>
        </div>
      </div>
    </div>
    <div class="d-grid my-lg-4 justify-content-center">
      <%
        int rows = 20;
        int cols = 6;

        for (int i = 0; i < rows; i++) {
          out.println("<div class=\"d-flex\">");
          for (int j = 0; j < cols; j++) {
              out.println("<input type=\"image\" src=\"Avatar.jpg\" class=\"border-card\" onclick=\"\" style=\"width: 200px;height: 200px;\">");
          }
          out.println("</div>");
        }
      %>

      <!-- 
      <div class="d-flex"> Fila 
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;"> Columna 0 
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
      </div>
      -->
    </div>
  </div>

  <footer class="bg-light pb-5">
    <div class="container text-center">
      <p class="font-italic text-muted mb-0">&copy; Copyrights SecurIA.com All rights reserved.</p>
    </div>
  </footer>
</body>
</html>