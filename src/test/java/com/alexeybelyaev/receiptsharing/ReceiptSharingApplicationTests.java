package com.alexeybelyaev.receiptsharing;



import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alexeybelyaev.receiptsharing.dao.PersonDao;
import com.alexeybelyaev.receiptsharing.dao.PersonDaoImpl;
import com.alexeybelyaev.receiptsharing.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class ReceiptSharingApplicationTests {

	@Test
	void contextLoads() {
	}



}
