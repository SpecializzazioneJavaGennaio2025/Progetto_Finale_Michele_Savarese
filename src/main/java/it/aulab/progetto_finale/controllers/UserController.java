package it.aulab.progetto_finale.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import it.aulab.progetto_finale.services.UserService;

@Controller
public class UserController {

@Autowired

private UserService userService;

//!rotta di home

@GetMapping("/")
public String home() {
return "home";

}
}