package joni.kz.SecondProjectWithSpringBoot.services;

import joni.kz.SecondProjectWithSpringBoot.models.Book;
import joni.kz.SecondProjectWithSpringBoot.models.Person;
import joni.kz.SecondProjectWithSpringBoot.repositories.BookRepository;
import joni.kz.SecondProjectWithSpringBoot.util.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> index(int page, int books_per_page, boolean sort_by_year){

        if(page == -1 && books_per_page == -1){
            if (sort_by_year){
                System.out.println("first");
                return bookRepository.findAll(Sort.by("year"));}
            }

        else if(page != -1 && books_per_page != -1) {
            System.out.println("second");
            if (sort_by_year) {
                System.out.println("third");
                return bookRepository.findAll(PageRequest.of(page, books_per_page, Sort.by("year"))).getContent();
            }else{
                System.out.println("fourth");
                return bookRepository.findAll(PageRequest.of(page, books_per_page)).getContent();
        }}
        System.out.println("fifth");
        return bookRepository.findAll();
    }
    public Book show(int id){
        Optional<Book> optional =  bookRepository.findById(id);
        return optional.orElse(null);
    }
    public List<Book> getByTitleStart(Title title) {
        if (title == null) {
            return null;
        }
        String titleStart = title.getTitle();
        if (titleStart == null || titleStart.isEmpty()) {
            return null;
        }

        return bookRepository.findByTitleStartingWith(titleStart);
    }

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }
    @Transactional
    public void update(int id, Book book){
        book.setId(id);
        bookRepository.save(book);
    }
    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }
    public Optional<Person> getBookOwner(int id){
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null) return null;

        else return Optional.ofNullable(book.getOwner());
    }
    @Transactional
    public void release(int id){
        Book book = bookRepository.findById(id).orElse(null);
        book.setOwner(null);
    }
    @Transactional
    public void assign(int id, Person person){
        Book book = bookRepository.findById(id).orElse(null);
        book.setReserveDate(new Date());
        book.setOwner(person);

    }
}
