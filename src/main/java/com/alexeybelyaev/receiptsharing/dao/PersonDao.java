package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao {

    // COMMENT THIS !!!!!!!!!
    int insertPerson(UUID id, Person person);

    default int insertPerson(Person person){
        UUID id = UUID.randomUUID();
        return insertPerson(id,person);
    }

    List<Person> selectAllPersons();

    Optional<Person> getPersonById(UUID uid);

    int updatePerson(UUID uid,Person person);

    int deletePerson(UUID uid);

    int addContact(UUID user_uid, UUID person_uid);

    List<Person> getAllContactsByUserID(UUID user_uuid);

}
