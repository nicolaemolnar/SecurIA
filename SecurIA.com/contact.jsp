<%  Object email = session.getAttribute("email"); %>
<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Contact us</title>

    <header class="p-3 bg-primary text-white">
        <div class="container">
          <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            
            <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <% if (email != null) { 
                    out.println("<li><a href=\"dashboard.jsp\" class=\"nav-link px-2\">Dashboard</a></li>");
                    out.println("<li><a href=\"gallery.jsp\" class=\"nav-link px-2\">Gallery</a></li>");
                    out.println("<li><a href=\"streaming.jsp\" class=\"nav-link px-2\">Streaming</a></li>");
                }
                %>
                <li><a href="contact.jsp" class="nav-link px-2 text-secondary">Contact us</a></li>
                <li><a href="about.jsp" class="nav-link px-2">About us</a></li>
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
    <div class="container py-2 mt-5 border-card rounded-3" style="border: 1px solid;">
        <div class="content">
            <div class="container">
                <div class="row align-items-stretch no-gutters contact-wrap">
                    <div class="col-md-6">
                        <div class="form h-100">
                            <span class="h3">Contact us</span>
                            <div class="mt-3 bg-light rounded-3">
                            <form class="mb-3 mx-3" action="request_contact" method="post" id="contactForm" name="contactForm">
                                <div class="row">
                                    <div class="col-md-6 form-group mb-3">
                                        <label for="" class="col-form-label">Name *</label>
                                        <input type="text" class="form-control" name="name" required=True id="name" placeholder="Your name">
                                    </div>
                                    <div class="col-md-6 form-group mb-3">
                                        <label for="" class="col-form-label">Email *</label>
                                        <input type="text" class="form-control" name="email" required=True did="email" placeholder="Your email">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 form-group mb-5">
                                        <label for="" class="col-form-label">Phone</label>
                                        <input type="text" class="form-control" name="phone" id="phone" placeholder="Phone #">
                                    </div>
                                    <div class="col-md-6 form-group mb-5">
                                        <label for="" class="col-form-label">Company</label>
                                        <input type="text" class="form-control" name="company" id="company" placeholder="Company  name">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 form-group mb-4">
                                    <label for="message" class="col-form-label">Message *</label>
                                    <textarea class="form-control" name="message" required=True id="message" cols="30" rows="4" placeholder="Write your message"></textarea>
                                    </div>
                                </div>
                            </div>
                                <div class="row">
                                    <div class="col-md-12 mb-3 form-group">
                                    <input type="submit" value="Send Message" class="btn text-white w-100 btn-outline-dark btn-primary rounded-0 py-2 px-4">
                                    <span class="submitting"></span>
                                    </div>
                                </div>
                            </form>
                            <div id="form-message-warning mt-4"></div>
                                <div id="form-message-success" hidden=True>
                                    Your message was sent, thank you!
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6" style="border: 1px solid;">
                            <a href="https://www.google.es/maps/place/UAH+-+Escuela+Polit%C3%A9cnica,+28805+Alcal%C3%A1+de+Henares,+Madrid/@40.5130493,-3.3509179,17.03z/data=!3m1!5s0xd424be52f764da1:0x555c9fe9196e69fc!4m5!3m4!1s0xd424be52f804d97:0x56581b9eb6489911!8m2!3d40.5131582!4d-3.3505356" target="_blank">
                                <div class="contact-info h-100" style="background-image:url(ubiUAH.jpg)"></div>
                            </a>    
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