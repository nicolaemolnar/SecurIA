<% Object email = session.getAttribute("email");
   String email_str = String.valueOf(email); %>



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
                    out.println("<li><a href=\"gallery?device=web\" class=\"nav-link px-2\">Gallery</a></li>");
                    out.println("<li><a href=\"streaming.jsp\" class=\"nav-link px-2\">Streaming</a></li>");
                }
                %>
                <li><a href="contact.jsp" class="nav-link px-2 text-secondary">Contact us</a></li>
                <li><a href="about.jsp" class="nav-link px-2">About us</a></li>
                <% if (email != null) { out.println("<li><a href=\"get_settings?email="+email+"&device=web\" class=\"nav-link px-2\">Settings</a></li>"); } %>
            </ul>
    
            <div class="text-end">
                <% 
               
                if(email != null) {
                    out.println(email_str.split("@")[0]); 
                    out.println("<button type=\"button\" class=\"btn btn-outline-light me-2\" onclick=\"location.href='logout'\">Log out</button>");
                } else {
                    out.println("<button type=\"button\" class=\"btn btn-outline-light me-2\" onclick=\"location.href='logout'\">Log In</button>");
                    out.println("<button type=\"button\" class=\"btn btn-outline-light btn-warning\" onclick=\"location.href='register.jsp'\">Sign-up</button>");
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
                                <% if(session.getAttribute("success") != null){
                                    out.println("<p style='color:green'> Message sent successfully!</p>");
                                }else if (session.getAttribute("error") != null){
                                    out.println("<p style='color:red'> Message not sent, something went wrong...</p>");
                                } 
                                session.removeAttribute("success");
                                session.removeAttribute("error");
                                %>
                            </div>
                        </div>
                        <div class="col-md-6" style="border: 1px solid; padding: 0;">
                            <iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d3033.2914241651893!2d-3.3509179!3d40.5130493!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd424be52f804d97%3A0x56581b9eb6489911!2sUAH%20-%20Escuela%20Polit%C3%A9cnica%2C%2028805%20Alcal%C3%A1%20de%20Henares%2C%20Madrid!5e0!3m2!1ses!2ses!4v1641900388077!5m2!1ses!2ses" style="border:0; width: 100%; height: 100%;" allowfullscreen="" loading="lazy"></iframe>
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