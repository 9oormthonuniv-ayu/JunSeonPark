package com.junseon.book.controller;

import com.junseon.book.domain.entity.Book;
import com.junseon.book.domain.entity.User;
import com.junseon.book.repository.BookRepository;
import com.junseon.book.service.BookService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @GetMapping("/list")
    public String showBookList(Model model, HttpSession session){
        List<Book> bookList = bookService.findAllBooks();
        model.addAttribute("bookList", bookList);

        User user = (User) session.getAttribute("loginUser"); // 세션에서 사용자 정보 가져오기
        if (user != null) {
            model.addAttribute("user", user); // 뷰에서 사용할 수 있도록 모델에 담아
        }
        return "book/list";
    }
}
