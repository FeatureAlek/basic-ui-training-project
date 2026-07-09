package lv.bootcamp.shelter.controller;

import lv.bootcamp.shelter.form.AnimalForm;
import lv.bootcamp.shelter.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AnimalPageController {

    private final AnimalService animalService;

    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication auth) {
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/animals")
    public String listAnimals(Model model) {
        model.addAttribute("animals", animalService.findAll());
        model.addAttribute("form", new AnimalForm(null, null, null, null, null, null));
        return "animals";
    }

    @GetMapping("/animals/new")
    public String newAnimalForm(Model model) {
        model.addAttribute("form",
                new AnimalForm(null, null, null, null, null, null));
        return "animals-new";
    }

    @PostMapping("/animals")
    public String createAnimal(@ModelAttribute AnimalForm form) {
        animalService.createFromForm(form);
        return "redirect:/animals";
    }
}