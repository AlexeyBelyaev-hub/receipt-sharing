SELECT person.person_id, person.name, person.email, person.phone_number, person.app_user_id
FROM contacts
INNER JOIN person ON person.person_id=contacts.person_id WHERE contacts.app_user_id = appUserId



INSERT INTO app_user (app_user_id, nick_name, email)
VALUES (uuid_generate_v4(),'Alexey', 'bel.alexey@mail.ru');

INSERT INTO person (person_id, name, email, phone_number, app_user_id)
VALUES (uuid_generate_v4(),'Alexey',
        (SELECT email FROM app_user WHERE nick_name='Alexey'),
         '89031037530',
         (SELECT app_user_id FROM app_user WHERE nick_name='Alexey') );

INSERT INTO app_user (app_user_id, nick_name, email)
VALUES (uuid_generate_v4(),'Vasiliy', 'vas.vas@mail.ru');

INSERT INTO person (person_id, name, email, phone_number, app_user_id)
VALUES (uuid_generate_v4(),'Vasiliy',
        (SELECT email FROM app_user WHERE nick_name='Vasiliy'),
         '89857650900',
         (SELECT app_user_id FROM app_user WHERE nick_name='Vasiliy') );