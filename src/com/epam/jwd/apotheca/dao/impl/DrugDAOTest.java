package com.epam.jwd.apotheca.dao.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class DrugDAOTest {
	
	private static DrugDAO drugDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		drugDAO = new DrugDAOImpl();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAll() {
		List<Drug> drugs = drugDAO.findAll();
		assert  drugs != null && ! drugs.isEmpty();
	}

	@Test
	public void testFindAllById() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

}
