package com.alexeybelyaev.receiptsharing.api;

import com.alexeybelyaev.receiptsharing.model.Person;
import com.alexeybelyaev.receiptsharing.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("api/v1/person")
@RestController
public class PersonController {
   
    private final PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    private void addPerson(@Valid @NonNull @RequestBody Person person){
        personService.addPerson(person);
    }

    @GetMapping
    private List<Person> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("contacts/{id}")
    private List<Person> getAllContactsByUserID(@PathVariable("user_id") UUID user_uuid){
        return personService.getAllContactsByUserID(user_uuid);
    }

    @GetMapping("{id}")
    private Person getPersonById(@PathVariable("id") UUID uuid){
        return personService.getPersonById(uuid).orElse(null);
    }

    @DeleteMapping("{id}")
    private void deletePersonById(@PathVariable("id") UUID uuid){
        personService.deletePersonById(uuid);
    }

    @PutMapping("{id}")
    private void updatePersonById(@PathVariable("id") UUID uuid, @Valid @NonNull @RequestBody Person person ){
        personService.updatePersonById(uuid,person);
    }

}
