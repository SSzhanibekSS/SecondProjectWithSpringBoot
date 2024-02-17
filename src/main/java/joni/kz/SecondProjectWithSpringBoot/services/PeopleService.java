package joni.kz.SecondProjectWithSpringBoot.services;

import joni.kz.SecondProjectWithSpringBoot.models.Book;
import joni.kz.SecondProjectWithSpringBoot.models.Person;
import joni.kz.SecondProjectWithSpringBoot.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PersonRepository personRepository;
    @Autowired
    public PeopleService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> index(){

        return personRepository.findAll();
    }
    public Person show(int id){
        return personRepository.findById(id).orElse(null);
    }
    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person){
        person.setId(id);
        personRepository.save(person);
    }
    @Transactional
    public void delete(int id){
        personRepository.deleteById(id);
    }

    @Transactional
    public Optional<Person> getPersonByFullName(String fullName){
        return personRepository.findByFullName(fullName);
    }
    public List<Book> getBooksByPersonId(int id){

        Person person = personRepository.findById(id).orElse(null);

        Hibernate.initialize(person.getBooks());
        List<Book> books = person.getBooks();
        Date date = new Date();
        long currentTime = date.getTime();
        if(books != null) {
            for (Book book : books) {
                long differenceInMillis = currentTime - book.getReserveDate().getTime();
                long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
                if(differenceInDays >= 10){
                    book.setStatus("red");
                }
                else book.setStatus("green");
            }
        }

        return books;
    }


}
