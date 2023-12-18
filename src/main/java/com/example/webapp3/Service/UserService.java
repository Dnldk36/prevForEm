package com.example.webapp3.Service;

import com.example.webapp3.Models.Active;
import com.example.webapp3.Models.Role;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        User userEmailFromDb = userRepository.findByEmail(user.getEmail());

        if (userEmailFromDb != null) {
            return false;
        }
        if (userFromDb != null) {
            return false;
        }
        if (user.getPassword().length() <= 2) {
            return false;
        }

        user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));

        user.setEmail(user.getEmail());
        user.setActivationCode(UUID.randomUUID().toString());

        user.setBalance(10);

        user.setActive(true);
        user.setTestConfirm(0);

        user.setRoles(Collections.singleton(Role.USER));

        StringBuilder activityText = new StringBuilder("user registration");
        activityText.append(" on ")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm")));


        user.setActivity(Arrays.asList(new Active(activityText)));
        userRepository.save(user);

        //Email Sender
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to Programmer Library.\nPlease, " +
                            "visit next link to activation: http://localhost:8081/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.sendMail(user.getEmail(), "Activation Code", message);
        }

        return true;
    }

    public boolean addUserActivity(String text){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        List<Active> activity = new ArrayList<>(user.getActivity());

        StringBuilder SBtext = new StringBuilder(text);
        activity.add(new Active(SBtext));
        user.setActivity(activity);

        userRepository.save(user);
        return true;
    }

    public boolean activateUser(String code) {

        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActiveEmailAccount(true);

        userRepository.save(user);

        return true;
    }
}
