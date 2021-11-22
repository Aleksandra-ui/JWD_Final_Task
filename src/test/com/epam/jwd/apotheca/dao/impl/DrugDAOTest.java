package com.epam.jwd.apotheca.dao.impl;

import org.junit.Assert;


import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.pool.ConnectionPool;


public class DrugDAOTest {
	
	private static DrugDAO drugDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		drugDAO = DrugDAOImpl.getInstance();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}

	@Test
	public void testSave() {

		Drug drug = createDrug();
		Drug drugInDB = drugDAO.save(drug);
		drug.setId(drugInDB.getId());
		
		drugDAO.delete(drug.getId());
		
		Assert.assertEquals(drug, drugInDB);
		
	}

	@Test
	public void testFindAll() {
		List<Drug> drugs = drugDAO.findAll();
		assert  drugs != null && ! drugs.isEmpty();
	}

	@Test
	public void testDelete() {
		
		Drug drugInDB = drugDAO.save(createDrug());
		
		drugDAO.delete(drugInDB.getId());
		
		Assert.assertNull(drugDAO.findById(drugInDB.getId()));
		
	}

	@Test
	public void testFindById() {
		System.out.println(drugDAO.findById(1));
	}
	
	@Test
	public void testFindByIds() {
		List<Drug> drugs = drugDAO.findByIds(1, 6, 8, 14, 2, 9);
		assert drugs.size() == 6;
	}
	
	@Test
	public void testFindDrug() {
		
		Drug drug = createDrug();
		drugDAO.save(drug);
		
		Drug drugInDB = drugDAO.findDrug("Sedavit", 3.0);
		drug.setId(drugInDB.getId());
		
		drugDAO.delete(drug.getId());
		
		Assert.assertEquals(drug, drugInDB);
		
	}
	
	@Test
	public void testFindPrescripted() {
		
		Drug drug = drugDAO.save(createDrug());
		System.out.println("drug from DB: " + drug);
		
		List<Drug> prescriptedDrugs = ((DrugDAOImpl)drugDAO).findPrescripted();
		
		System.out.println( drugDAO.delete(drug.getId()));;
		
		Assert.assertNotNull(prescriptedDrugs);
		assert ! prescriptedDrugs.isEmpty();
		
	}
	
	@Test
	public void testFindByRange() {
		
		List<Drug> drugs = ((DrugDAOImpl)drugDAO).findAll();
		
		List<Drug> drugs2 = drugDAO.findByRange(0, drugs.size() / 2, null, 0);
		
		assert drugs.containsAll(drugs2);
		
	}
	
	@Test
	public void testFindPrescriptedByRange() {
		
		List<Drug> drugs = ((DrugDAOImpl)drugDAO).findPrescripted();
		
		List<Drug> drugs2 = ((DrugDAOImpl)drugDAO).findPrescriptedByRange(0, drugs.size() / 2);
		
		assert drugs.containsAll(drugs2);
		
	}
	
	@Test
	public void testFindByName() {
		
		Drug drug = drugDAO.save(createDrug());
		
		List<Drug> drugs = ((DrugDAOImpl)drugDAO).findByName("Sedavit");
		
		drugDAO.delete(drug.getId());
		
		assert (drugs.contains(drug) );

	}
	
	@Test
	public void testGetTotalCount() {
		
		int first = drugDAO.getTotalCount();
		
		Drug drug = drugDAO.save(createDrug());
		
		int second = drugDAO.getTotalCount();
		
		drugDAO.delete(drug.getId());
		
		assert first == second - 1;
		
	}
	
	@Test
	public void testGetPrescriptedCount() {
		
		int first = drugDAO.getTotalCount();
		
		Drug drug = drugDAO.save(createDrug());
		
		int second = drugDAO.getTotalCount();
		
		drugDAO.delete(drug.getId());
		
		assert first == second - 1;
		
	}
	
	public Drug createDrug() {
		
		Drug drug = new Drug();
		drug.setDose(3.0);
		drug.setName("Sedavit");
		drug.setPrescription(true);
		drug.setPrice(140);
		drug.setQuantity(30);
		return drug;
		
	}

}
