package com.example.webapp3.Controller;

import com.example.webapp3.Models.Test;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.TestRepository;
import com.example.webapp3.Repositories.UserRepository;
import com.example.webapp3.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class UserTestController {


    @Autowired
    private TestRepository testRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private static Test testById;
    private static int score = 0;
    private static String message;
    private boolean isFirstTime;

    @GetMapping
    private String getTests(Model model) {
        model.addAttribute("testList", testRepository.findAll());
        return "Testing";
    }

    @GetMapping("/addTest")
    private String getAddTest() {
        return "addTest";
    }

    @PostMapping("/addTest")
    private String redirectTest() {
        return "redirect:/test";
    }

    @GetMapping("/{id}")
    private String getTest(@PathVariable Long id,
                           Model model) {

        model.addAttribute("getTest", testRepository.findById(id).get());
        testById = testRepository.findById(id).get();
        return "testPage";
    }

    @PostMapping("/sendResult")
    public String getUserAnswer(@RequestParam Map<String, String> allParams) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        System.out.println(allParams);

        List<String> questionList = new ArrayList<>();
        List<String> correctAnswersList = new ArrayList<>();
        List<String> userAnswersList = new ArrayList<>();


        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("questions"))
                questionList.add(entry.getValue());
            else if (entry.getKey().startsWith("correctAnswers"))
                correctAnswersList.add(entry.getValue());
            else if (entry.getKey().startsWith("userAnswers"))
                userAnswersList.add(entry.getValue());
        }

        score = 0;

        for (String correctAnswer : correctAnswersList) {
            for (String userAnswer : userAnswersList) {
                if (userAnswer.equals(correctAnswer)) {
                    score++;
                }
            }
        }

        System.out.println("userScore: " + score);


        if (testById.getId() - 1 == user.getTestConfirm()) {
            System.out.println("testId " + (testById.getId() - 1) + " userConfirm " + user.getTestConfirm());
            message = "You've completed the test, you're getting " + score + " LC";
            isFirstTime = true;
            user.setBalance(user.getBalance() + score);
            user.setTestConfirm(user.getTestConfirm() + 1);
            userRepository.save(user);
            System.out.println("Data completely save!");

            String text = String.format(
                    "passed the test '%s' by getting %s LC ",
                    testById.getTestTitle(),
                    score
            );
            if (userService.addUserActivity(text)) {
                System.out.println("Activity added successfully");
            } else {
                System.out.println("Activity not added");
            }

        } else {
            message = "You've completed the test, your score: "
                    + score
                    + "\n\n But you won't get points for one of these reasons: "
                    + "\n 1. You have already taken this test"
                    + "\n 2. You have not completed previous tests";
            isFirstTime = false;
        }


        testById = null;
        return "redirect:/test/confirm";
    }

    @GetMapping("/confirm")
    public String confirmTest(Model model) {
        model.addAttribute("isFirstTime", isFirstTime);
        model.addAttribute("message", message);
        model.addAttribute("score", score);
        return "testSendSubmit";
    }

}
