package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BookController {

    @Value("${spring.profiles.active}")
    private String isDev;

    @GetMapping("/**")
    public String getBookList(Model model) {
        model.addAttribute("isDevMode", "dev".equals(isDev));
        return "bookList";
    }

}
