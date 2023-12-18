package com.example.webapp3.Controller;

import com.example.webapp3.Models.Book;
import com.example.webapp3.Models.Reviews;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.BookRepository;
import com.example.webapp3.Repositories.UserRepository;
import com.example.webapp3.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@EnableWebMvc
@RequestMapping("/main")
public class MainController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserRepository userRepository;


    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.ImgPath}")
    private String uploadImgPath;


    @GetMapping
    private String viewAndAddBook(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userAuth = userRepository.findByUsername(auth.getName());

        model.addAttribute("userAuth",userAuth);
        model.addAttribute("bookList", bookRepository.findAll());
        model.addAttribute("noBalance", "No balance");

        return "Main";
    }


    @GetMapping("/addBook")
    private String viewAddBook(){
        return "addBook";
    }

    @RequestMapping(value = "/addBook", method = RequestMethod.POST)
    private String addBook(@RequestParam String bookName,
                           @RequestParam String bookTag,
                           @RequestParam(defaultValue = "No description yet") String bookDescription,
                           @RequestParam int bookPrice,
                           @RequestParam("bookFile") MultipartFile bookFile,
                           @RequestParam("bookImg") MultipartFile bookImg,
                           Model model) throws IOException {

        Book book = new Book(bookName, bookTag, bookDescription, bookPrice);

        if (bookFile != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFile = uuidFile + "." + bookFile.getOriginalFilename();
            bookFile.transferTo(new File(uploadPath + "/" + resultFile));
            book.setFileName(resultFile);
        }
        if (bookImg != null){
            File uploadDir = new File(uploadImgPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFile = uuidFile + "." + bookImg.getOriginalFilename();
            bookImg.transferTo(new File(uploadImgPath + "/" + resultFile));
            book.setImg(resultFile);
        }

        bookService.saveData(book);

        model.addAttribute("bookList", bookRepository.findAll());

        return "redirect:/main";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = "bookFilter")
    private String filter(@RequestParam String bookFilter,
                          Model model) {

        Iterable<Book> bookList;

        if (bookFilter != null && !bookFilter.isEmpty()) {
            bookList = bookService.search(bookFilter);
        } else {
            return "redirect:/main";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userAuth = userRepository.findByUsername(auth.getName());

        model.addAttribute("userAuth",userAuth);
        model.addAttribute("bookList", bookList);
        model.addAttribute("bookFilter", bookFilter);

        return "Main";
    }


    @PostMapping("/button")
    private String getButton(@RequestParam String buttonValue,
                             Model model){

        System.out.println("Нажата кнопка: " + buttonValue);
        model.addAttribute("buttonValue", buttonValue);

        return "redirect:/main";
    }

    @GetMapping("/{id}")
    private String getBookPage(@PathVariable Long id, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        Optional<Book> book = bookRepository.findById(id);

        List<Reviews> rev = book.get().getReviews();
        Collections.reverse(rev);

        model.addAttribute("title", book.get().getBookName());
        model.addAttribute("book", book.get());
        model.addAttribute("reviews", rev);
        model.addAttribute("user", user);
        model.addAttribute("isAccountActive", user.getIsActiveEmailAccount());

        String message = "To be able to write reviews, activate your account, " +
                "\nan email was sent to the email you provided during registration";
        model.addAttribute("message", message);


        return "bookPage";
    }
    @PostMapping("/{id}")
    private String reviews(@RequestParam String bookReviews, @PathVariable Long id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        Optional<Book> book = bookRepository.findById(id);

        List<Reviews> reviews = new ArrayList<>(book.get().getReviews());

        Reviews newReview = new Reviews(bookReviews);
        newReview.setUsername(user.getUsername());

        reviews.add(newReview);
        book.get().setReviews(reviews);

        bookRepository.save(book.get());


        bookRepository.save(book.get());
        return "redirect:/main/" + id;
    }

}
