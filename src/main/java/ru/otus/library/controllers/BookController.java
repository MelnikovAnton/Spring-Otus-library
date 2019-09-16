package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class BookController {

    @Value("${spring.profiles.active}")
    private String isDev;

    @GetMapping("/*")
    public String getBookList(Model model/*, @AuthenticationPrincipal User user*/) {
        model.addAttribute("isDevMode", "dev".equals(isDev));
        return "bookList";
    }

}
