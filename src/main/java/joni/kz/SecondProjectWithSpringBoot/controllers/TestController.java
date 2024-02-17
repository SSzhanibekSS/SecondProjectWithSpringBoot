package joni.kz.SecondProjectWithSpringBoot.controllers;

import joni.kz.SecondProjectWithSpringBoot.models.Person;
import joni.kz.SecondProjectWithSpringBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class TestController {

    private final PeopleService peopleService;
    @Autowired
    public TestController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/test")
    public String testMethod(){
    return "hello";
    }
}
