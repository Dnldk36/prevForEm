package com.example.webapp3.Controller;

import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    private String getUserProfile(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userAuth = userRepository.findByUsername(auth.getName());

        if (userAuth.getActivationCode() == null){
            model.addAttribute("isEmailActive", "Account activated");
        } else {
            model.addAttribute("isEmailActive", "Account isn't activated");
        }

        model.addAttribute("userInfo", userAuth);


        return "userProfile";
    }

}
