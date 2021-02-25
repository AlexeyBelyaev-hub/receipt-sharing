package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.model.Person;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

    private static List<Person> DB = new ArrayList<>();
    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id,person.getName(),person.getEmail(),person.getPhoneNumber()));
        return 1;
    }

    @Override
    public List<Person> selectAllPersons() {
        return List.copyOf(DB);
    }

    @Override
    public Optional<Person> getPersonById(UUID uid) {
        //try to find person
        return DB.stream().filter(p->p.getUuid().equals(uid)).findFirst();

    }

    @Override
    public int deletePerson(UUID uid) {
        Optional<Person> personToDelete = getPersonById(uid);
        if (personToDelete.isEmpty()){
            return 0;
        }else {
            DB.remove(personToDelete.get());
            return 1;
        }
    }

    @Override
    public int addContact(UUID user_uid, UUID person_uid) {
        return 0;
    }

    @Override
    public List<Person> getAllContactsByUserID(UUID user_uuid) {
        return null;
    }

    @Override
    public int updatePerson(UUID uid, Person person) {
        return getPersonById(uid)
                .map(p -> {
                p.setName(person.getName());
                p.setEmail(person.getEmail());
                return 1;
        }).orElse(0);
    }
}
