package it.aulab.progetto_finale.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.progetto_finale.models.CareerRequest;
import it.aulab.progetto_finale.models.Role;
import it.aulab.progetto_finale.models.User;
import it.aulab.progetto_finale.repositories.RoleRepository;
import it.aulab.progetto_finale.repositories.UserRepository;
import it.aulab.progetto_finale.services.CareerRequestService;
// import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/operations")


public class OperationController {
@Autowired
private RoleRepository roleRepository;


@Autowired
private UserRepository userRepository;

@Autowired
private CareerRequestService careerRequestService;

//! rotta request

@GetMapping("/career/request")
public String careerRequestCreate(Model viewModel) {
    viewModel.addAttribute("title", "Invia la tua richiesta");
viewModel.addAttribute("careerRequest", new CareerRequest());


List<Role> roles = roleRepository.findAll();

//! elimino la possibilità di scegliere il ruolo nella user select del form
roles.removeIf(e -> e.getName().equals("ROLE_USER"));
viewModel.addAttribute("roles", roles);

    return "career/requestForm";
}


//! rotta salvataggio richiesta

@PostMapping("/career/request/save")
public String careerRequestStore(@ModelAttribute("careerRequest") CareerRequest careerRequest, Principal principal, RedirectAttributes redirectAttributes){
    User user = userRepository.findByEmail(principal.getName());

    if(careerRequestService.isRoleAlreadyAssigned(user, careerRequest)){
        redirectAttributes.addFlashAttribute("errorMessage", "Sei già stato assegnato a questo ruolo");
        return "redirect:/";
    }

    careerRequestService.save(careerRequest, user);

    redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo");

    return "redirect:/";
}

}
