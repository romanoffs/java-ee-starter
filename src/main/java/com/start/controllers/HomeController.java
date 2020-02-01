package com.start.controllers;


import lombok.val;

import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.*;

@Path("/home")
public class HomeController {

    @GET
    @Controller
    @Produces(TEXT_HTML)
    public String home() {
        return "home";
    }

    @POST
    @Produces(TEXT_PLAIN)
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response homeIN(MultivaluedMap<String, String> formParams) {
        val email = formParams.getFirst("email");
        return Response.ok(email).build();
    }
}
