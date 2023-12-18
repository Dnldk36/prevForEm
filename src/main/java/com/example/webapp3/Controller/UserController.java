package com.example.webapp3.Controller;

import com.example.webapp3.Models.Role;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
public class UserController {



    @Autowired
    private UserRepository userRepository;


    @GetMapping
    private String getUserList(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userAuth = userRepository.findByUsername(auth.getName());

        model.addAttribute("isEmailActive", userAuth.getIsActiveEmailAccount());
        model.addAttribute("userList", userRepository.findByOrderByIdAsc());

        return "userList";
    }

    @GetMapping("/{user}")
    private String getUserEdit(@PathVariable User user, Model model) {

        model.addAttribute("user", user);
        model.addAttribute("roleList", Role.values());


        return "userEdit";
    }

    @PostMapping
    private String saveUserEdit(@RequestParam("userId") User user,
                                @RequestParam String userName,
                                @RequestParam int userBalance,
                                @RequestParam Map<String, String> form) {

        System.out.println("User: " + user);

        user.setUsername(userName);
        user.setBalance(userBalance);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);

        return "redirect:/user";
    }


    @GetMapping("/delete/{user}")
    private String getDeleteUser(@PathVariable User user) {
        System.out.println("Delete user: " + user);
        userRepository.delete(user);
        return "redirect:/user";
    }

    @DeleteMapping("/delete/{user}")
    private String deleteUser(@PathVariable User user) {
        System.out.println(user);
        userRepository.delete(user);
        return "redirect:/user";
    }
}
