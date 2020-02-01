package com.start.controllers;

import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("")
@Controller
@Produces(TEXT_HTML)
public class IndexPageController {

    @Inject
    private Models models;

    @GET
    public String indexView() {
        models.put("message", "Hello, World!");
        return "index";
    }
}