package com.example.webapp3.Controller;

import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.UserRepository;
import com.example.webapp3.Service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
@RequestMapping("/accessRestoration")
public class AccessRestorationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSender mailSender;

    @GetMapping
    private String getAccessRestoration() {
        return "accessRestoration";
    }

    @PostMapping
    private String changePassword(@RequestParam String UserName,
                                  @RequestParam String UserEmail,
                                  Model model) {

        User user = userRepository.findByUsername(UserName);

        if (user != null) {
            if (user.getEmail() != null && user.getEmail().equals(UserEmail) && user.getIsActiveEmailAccount()) {
                model.addAttribute("showCode", true);
                model.addAttribute("UserNameForCode", user.getUsername());
                model.addAttribute("NewPassword", false);

                Random random = new Random();
                int number = 100_000 + random.nextInt(900_000);

                user.setActivationCode(String.valueOf(number));
                userRepository.save(user);


                String message = String.format(
                        "Dear %s, \n" +
                                "Please enter this code to recover your password: \n" +
                                "%s \n\n" +
                                "If you have not requested a password reset for your ProgrammerLibrary account, you can safely ignore this email.\n\n" +
                                "Yours sincerely,\n" +
                                "ProgrammerLibrary team\n" +
                                "http://localhost:8081/\n" +
                                "Pursuit of Knowledge",
                        user.getUsername(),
                        user.getActivationCode()
                );
                mailSender.sendMail(user.getEmail(), "Recover password", message);


                System.out.println("code: " + number);
                System.out.println("success");

                return "accessRestoration";
            } else {
                model.addAttribute("EmailMessage", "email not correct!");
                return "accessRestoration";
            }
        } else {
            model.addAttribute("UserMessage", "user not found!");
            return "accessRestoration";
        }
    }

    @PostMapping("/code")
    private String enterCode(@RequestParam String UserCode,
                             @RequestParam String UserNameForCode,
                             Model model) {

        User user = userRepository.findByUsername(UserNameForCode);
        System.out.println("user/code: " + user);

        if (UserCode.equals(user.getActivationCode())) {
            model.addAttribute("NewPassword", true);
            model.addAttribute("UserNameForNewPassword", user.getUsername());
            System.out.println("new password next...");
            return "accessRestoration";
        } else {
            model.addAttribute("CodeMessenger", "wrong code");
            return "accessRestoration";
        }
    }

    @PostMapping("/newPassword")
    private String changePassword(@RequestParam String UserNameForCode,
                                  @RequestParam String UserPassword,
                                  @RequestParam String UserPasswordConfirm,
                                  Model model) {

        User user = userRepository.findByUsername(UserNameForCode);

        System.out.println("UserName: " + UserNameForCode);
        System.out.println("user: " + user);

        if (user == null) {
            model.addAttribute("UserMessageNull", "user is null");
            System.out.println("user is null");
            return "accessRestoration";
        }

        if (user.getPassword().equals(UserPassword)) {
            model.addAttribute("SamePasswordMessage", "password matches your old-password");
            System.out.println("password matches your old-password");
            return "accessRestoration";
        }
        String pass = new BCryptPasswordEncoder(12).encode(UserPassword);
        if (UserPassword.equals(UserPasswordConfirm)) {
            if (!pass.equals(user.getPassword())) {
                user.setPassword(new BCryptPasswordEncoder(12).encode(UserPassword));
                user.setActivationCode(null);
                userRepository.save(user);
                System.out.println("password success changed");
                return "redirect:/login";
            } else {
                model.addAttribute("PasswordMessage", "passwords are same");
                model.addAttribute("NewPassword", true);
                return "accessRestoration";
            }
        } else {
            model.addAttribute("PasswordMessage", "passwords are different");
            System.out.println("passwords are different");
            return "accessRestoration";
        }
    }
}
























