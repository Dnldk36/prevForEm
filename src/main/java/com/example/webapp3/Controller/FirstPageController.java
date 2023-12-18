package com.example.webapp3.Controller;

import com.example.webapp3.Models.Active;
import com.example.webapp3.Models.Book;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.BookRepository;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FirstPageController {

    @Value("${upload.path}")
    private String fileDirectory;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/")
    private String getInfoPage(){
        return "PresPage";
    }

    @GetMapping("/home")
    private String getHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userAuth = userRepository.findByUsername(auth.getName());
        if (userAuth.getActivationCode() == null){
            model.addAttribute("isEmailActive", "Account activated");
        } else {
            model.addAttribute("isEmailActive", "Account isn't activated");
        }
        model.addAttribute("userInfo", userAuth);
        return "home";
    }

    @GetMapping("/file/{bookFileName}")
    private ResponseEntity<Resource> linkDownloadHandler(@PathVariable String bookFileName)
            throws MalformedURLException {

        Path filePath = Paths.get(fileDirectory).resolve(bookFileName);
        Resource resource = new UrlResource(filePath.toUri());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Book infoBook = bookRepository.findByFileName(bookFileName);

        User user = userRepository.findByUsername(auth.getName());

        user.setBalance(user.getBalance() - infoBook.getPrice());

        List<Active> activity = new ArrayList<>(user.getActivity());

        StringBuilder activityText = new StringBuilder("buy the book \"");
        activityText.append(infoBook.getBookName()).append("\" for ")
                .append(infoBook.getPrice()).append("BalanceCoin on ")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm")));

        activity.add(new Active(activityText));
        user.setActivity(activity);

        userRepository.save(user);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @PostMapping("/file/{bookFileName}")
    private String linkDownloadHandlerPost(@PathVariable String bookFileName) {
        return "redirect:/main";
    }
    @ResponseBody
    @GetMapping("/style/script/{name}.js")
    public ResponseEntity<String> getScript(@PathVariable("name") String name) throws IOException {
        // получаем содержимое файла из папки ресурсов в виде потока
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/style/script/" + name + ".js");
        // преобразуем поток в строку
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = bf.readLine()) != null) {
            sb.append(line + "\n");
        }

        // создаем объект, в котором будем хранить HTTP заголовки
        final HttpHeaders httpHeaders = new HttpHeaders();
        // добавляем заголовок, который хранит тип содержимого
        httpHeaders.add("Content-Type", "application/javascript; charset=utf-8");
        // возвращаем HTTP ответ, в который передаем тело ответа, заголовки и статус 200 Ok
        return new ResponseEntity<String>(sb.toString(), httpHeaders, HttpStatus.OK);
    }
    @ResponseBody
    @GetMapping("/style/images/{name}.png")
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/style/images/" + name + ".png");
        byte[] imageBytes = StreamUtils.copyToByteArray(is);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(imageBytes, httpHeaders, HttpStatus.OK);
    }
    @ResponseBody
    @GetMapping("/style/{code}.css")
    public ResponseEntity<String> styles(@PathVariable("code") String code) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/style/" + code + ".css");
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = bf.readLine()) != null) {
            sb.append(line + "\n");
        }

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "text/css; charset=utf-8");
        return new ResponseEntity<String>(sb.toString(), httpHeaders, HttpStatus.OK);
    }
}
