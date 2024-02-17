package joni.kz.SecondProjectWithSpringBoot.controllers;

import jakarta.validation.Valid;
import joni.kz.SecondProjectWithSpringBoot.models.Book;
import joni.kz.SecondProjectWithSpringBoot.models.Person;
import joni.kz.SecondProjectWithSpringBoot.services.BookService;
import joni.kz.SecondProjectWithSpringBoot.services.PeopleService;
import joni.kz.SecondProjectWithSpringBoot.util.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Neil Alishev
 */
@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BooksController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "-1") int page,
                        @RequestParam(required = false, defaultValue = "-1") int books_per_page,
                        @RequestParam(required = false, defaultValue = "false") boolean sort_by_year) {
        System.out.println(page);
        System.out.println(books_per_page);
        System.out.println(sort_by_year);
        model.addAttribute("books", bookService.index(page, books_per_page, sort_by_year));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.show(id));

        Optional<Person> bookOwner = bookService.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.index());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book Book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book Book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookService.save(Book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }
    @GetMapping("/search")
    public String search(Model model,
                         @ModelAttribute("obj") Title title){
        model.addAttribute("books", bookService.getByTitleStart(title));
        model.addAttribute("obj", new Title());
        return "books/search";
    }




}
