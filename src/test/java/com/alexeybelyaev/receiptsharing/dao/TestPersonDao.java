package com.alexeybelyaev.receiptsharing.dao;

//@SpringBootTest
public class TestPersonDao {
//
//    @Autowired
//    private @Qualifier("postgres") PersonDao personDao;
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testAddPerson(){
//        UUID uid = UUID.randomUUID();
//        Person person = new Person(uid,"SomeName","SomeEmail@mail.com","89345950123");
//        personDao.insertPerson(uid, person);
//
//        List<Person> personList = personDao.selectAllPersons();
//        boolean result = personList.contains(person);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testInvalidEmail(){
//        Person person = new Person(UUID.randomUUID(),"SomeName","InvalidEmail","89345950123");
//        Assert.assertEquals(personDao.insertPerson(person),-1);
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testGetPersonById(){
//        // insert new person
//        UUID uid = UUID.randomUUID();
//        Person person = new Person(uid,"SomeName","SomeEmail@mail.com","89345950123");
//        personDao.insertPerson(uid, person);
//
//        // fetch person
//        Optional<Person> gottenPerson = personDao.getPersonById(person.getUuid());
//        // assert it found
//        Assert.assertFalse("Person have not been found",gottenPerson.isEmpty());
//        // assert it equals
//        Assert.assertEquals(gottenPerson.get().getUuid(), person.getUuid());
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testUpdatePerson(){
//        // insert new person
//        UUID uid = UUID.randomUUID();
//        Person person =
//                new Person(uid,"SomeName","SomeEmail@mail.com","89345950123");
//        personDao.insertPerson(uid, person);
//
//        Person personToUpdate =
//                new Person(uid, "ChangedName", "Changed@email.ru", "89345950155");
//
//        personDao.updatePerson(uid, personToUpdate);
//
//        // fetch person
//        Optional<Person> optionalPerson = personDao.getPersonById(uid);
//        // assert it found
//       // Assert.assertFalse("Person have not been found",gottenPerson.isEmpty());
//        // assert it equals
//        Person changedPerson = optionalPerson.get();
//        Assert.assertEquals(changedPerson.getName(), personToUpdate.getName());
//        Assert.assertEquals(changedPerson.getEmail(), personToUpdate.getEmail());
//        Assert.assertEquals(changedPerson.getPhoneNumber(), personToUpdate.getPhoneNumber());
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testUpdatePersonInvalidUid(){
//        // insert new person
//        UUID uid = UUID.randomUUID();
//        Person person =
//                new Person(uid,"SomeName","SomeEmail@mail.com","89345950123");
//        person.setPhoneNumber("111111");
//        personDao.insertPerson(uid, person);
//
//        Person personToUpdate =
//                new Person(uid, "ChangedName", "Changed@email.ru","89345950123");
//        personToUpdate.setPhoneNumber("222222");
//
//        // pass new UUID as result should be 0
//        int result = personDao.updatePerson(UUID.randomUUID(), personToUpdate);
//
//        Assert.assertEquals(result,0);
//
//        // fetch person
//        Optional<Person> optionalPerson = personDao.getPersonById(uid);
//        // assert it found
//        // Assert.assertFalse("Person have not been found",gottenPerson.isEmpty());
//        // assert it equals
//        Person changedPerson = optionalPerson.get();
//        Assert.assertNotEquals(changedPerson.getName(), personToUpdate.getName());
//        Assert.assertNotEquals(changedPerson.getEmail(), personToUpdate.getEmail());
//        Assert.assertNotEquals(changedPerson.getPhoneNumber(), personToUpdate.getPhoneNumber());
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testDeletePerson(){
//        // insert new person
//        UUID uid = UUID.randomUUID();
//        Person person = new Person(uid,"SomeName","SomeEmail@mail.com", "89345950123");
//        personDao.insertPerson(uid, person);
//
//        int result = personDao.deletePerson(uid);
//
//        Assert.assertEquals(1,result);
//        // fetch person
//        Optional<Person> gottenPerson = personDao.getPersonById(person.getUuid());
//        // assert it found
//        Assert.assertTrue(gottenPerson.isEmpty());
//    }
//
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testDeletePersonInvalidUid(){
//        // insert new person
//        UUID uid = UUID.randomUUID();
//        Person person = new Person(uid,"SomeName","SomeEmail@mail.com", "89345950123");
//        personDao.insertPerson(uid, person);
//
//        int result = personDao.deletePerson(UUID.randomUUID());
//
//        Assert.assertEquals(0,result);
//    }
//
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testAddContact(UUID user_uid, UUID person_uid){
//
//        //1. create 2 users
//
//
//
//
//        //2. create 3 persons and associate them to users
//
//        //3. get contacts and assert
//        UUID uid = UUID.randomUUID();
//        Person person =
//                new Person(uid,"SomeName","SomeEmail@mail.com","89345950123");
//        personDao.insertPerson(uid, person);
//
//        Person personToUpdate =
//                new Person(uid, "ChangedName", "Changed@email.ru", "89345950155");
//
//        personDao.updatePerson(uid, personToUpdate);
//
//        // fetch person
//        Optional<Person> optionalPerson = personDao.getPersonById(uid);
//        // assert it found
//        // Assert.assertFalse("Person have not been found",gottenPerson.isEmpty());
//        // assert it equals
//        Person changedPerson = optionalPerson.get();
//        Assert.assertEquals(changedPerson.getName(), personToUpdate.getName());
//        Assert.assertEquals(changedPerson.getEmail(), personToUpdate.getEmail());
//        Assert.assertEquals(changedPerson.getPhoneNumber(), personToUpdate.getPhoneNumber());
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testGetAllContactsByUserID(UUID user_uuid) {
//
//    }

}
