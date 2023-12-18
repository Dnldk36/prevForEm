package com.example.webapp3.Controller;

import com.example.webapp3.Models.User;
import com.example.webapp3.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    private String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    private String addUser(User user, Model model) {
        if (!userService.addUser(user)){
            model.addAttribute("userError", "user exist!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    private String activationAcc(Model model ,@PathVariable String code){

        boolean isActivate  = userService.activateUser(code);
        if (isActivate){
            model.addAttribute("message", "Activation successful");
        } else {
            model.addAttribute("message", "Activation code isn't found");
        }

        return "redirect:/login";
    }
}
