package com.alexeybelyaev.receiptsharing.service;

import com.alexeybelyaev.receiptsharing.dao.PersonDao;
import com.alexeybelyaev.receiptsharing.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PersonService {

    private final PersonDao personDao;

    @Autowired
    public PersonService(@Qualifier("postgres") PersonDao personDao) {
        this.personDao = personDao;
    }

    public int addPerson(Person person){
        return personDao.insertPerson(person);
    }

    public List<Person> getAllPersons(){
        return personDao.selectAllPersons();
    }

    public Optional<Person> getPersonById(UUID uid){
        return personDao.getPersonById(uid);
    }

    public int deletePersonById(UUID uid){
        log.info("Delete person...");
        return personDao.deletePerson(uid);
    }

    public int updatePersonById(UUID uid, Person person){
        log.info("Update person...");
        return   personDao.updatePerson(uid, person);

    }

    public List<Person> getAllContactsByUserID(UUID user_uuid) {

        return personDao.getAllContactsByUserID(user_uuid);
    }

}
