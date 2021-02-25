package com.alexeybelyaev.receiptsharing.api;

import com.alexeybelyaev.receiptsharing.model.Person;
import com.alexeybelyaev.receiptsharing.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("management/api/v1/person")
@RestController
public class PersonManagementController {

    private final PersonService personService;

    @Autowired
    public PersonManagementController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMIN_TRAINEE')")
    public List<Person> get(){
        return personService.getAllPersons();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('person:write')")
    public void addPerson(@Valid @NonNull @RequestBody Person person){
        personService.addPerson(person);
    }
}
