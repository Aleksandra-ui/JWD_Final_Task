package test.com.epam.jwd.apotheca.dao.impl;

import org.junit.Assert;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.impl.DrugDAOImpl;
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
//
//	@Test
//	public void testSave() {
//
//		Drug drug = new Drug();
//		drug.setDose(2.5);
//		drug.setName("Aevit");
//		drug.setPrescription(false);
//		drug.setPrice(230);
//		drug.setQuantity(20);
//		
//		Drug drug2 = drugDAO.save(drug);
//		drug.setId(drug2.getId());
//		
//		drugDAO.delete(drug2.getId());
//		
//		Assert.assertEquals(drug, drug2);
//		
//	}
//
//	@Test
//	public void testFindAll() {
//		List<Drug> drugs = drugDAO.findAll();
//		assert  drugs != null && ! drugs.isEmpty();
//	}
//
//	@Test
//	public void testFindAllById() {
//		
//	}
//
//	@Test
//	public void testUpdate() {
//		
//	}
//
//	@Test
//	public void testDelete() {
//		
//		Drug drug = new Drug();
//		drug.setDose(3.0);
//		drug.setName("Sedavit");
//		drug.setPrescription(false);
//		drug.setPrice(140);
//		drug.setQuantity(30);
//		Drug drug2 = drugDAO.save(drug);
//		
//		drugDAO.delete(drug2.getId());
//		
//		Assert.assertNull(drugDAO.findById(drug2.getId()));
//		
//	}
//
//	@Test
//	public void testFindById() {
//		System.out.println(drugDAO.findById(1));;
//	}
//	
//	@Test
//	public void testFindByIds() {
//		List<Drug> drugs = drugDAO.findByIds(1, 6, 8, 14, 2, 9);
//		System.out.println("drugs size = " + drugs.size());
//		assert drugs.size() == 6;
//	}
//	
//	@Test
//	public void testFindDrug() {
//		
//		Drug drug = new Drug();
//		drug.setDose(3.0);
//		drug.setName("Sedavit");
//		drug.setPrescription(false);
//		drug.setPrice(140);
//		drug.setQuantity(30);
//		drugDAO.save(drug);
//		
//		Drug drug2 = drugDAO.findDrug("Sedavit", 3.0);
//		drug.setId(drug2.getId());
//		
//		drugDAO.delete(drug2.getId());
//		
//		Assert.assertEquals(drug, drug2);
//		
//	}
//	
//	@Test
//	public void testFindPrescripted() {
//		
//		Drug drug = new Drug();
//		drug.setDose(1.0);
//		drug.setName("u");
//		drug.setPrescription(true);
//		drug.setPrice(140);
//		drug.setQuantity(30);
//		Drug drug2 = drugDAO.save(drug);
//		
//		List<Drug> prescriptedDrugs = ((DrugDAOImpl)drugDAO).findPrescripted();
//		Assert.assertNotNull(prescriptedDrugs);
//		assert ! prescriptedDrugs.isEmpty();
//		
//		drugDAO.delete(drug2.getId());
//		
//	}
//	
//	@Test
//	public void testFindByRange() {
//		
//		List<Drug> drugs = ((DrugDAOImpl)drugDAO).findAll();
//		
//		List<Drug> drugs2 = drugDAO.findByRange(0, drugs.size() / 2);
//		
//		assert drugs.containsAll(drugs2);
//		
//	}
//	
//	@Test
//	public void testFindPrescriptedByRange() {
//		
//		List<Drug> drugs = ((DrugDAOImpl)drugDAO).findPrescripted();
//		
//		List<Drug> drugs2 = ((DrugDAOImpl)drugDAO).findPrescriptedByRange(0, drugs.size() / 2);
//		
//		assert drugs.containsAll(drugs2);
//		
//	}
	
	@Test
	public void testFindByName() {
		
		Drug drug = new Drug();
		drug.setDose(3.0);
		drug.setName("Sedavit");
		drug.setPrescription(false);
		drug.setPrice(140);
		drug.setQuantity(30);
		Drug drug1InDb = drugDAO.save(drug);

//		Drug drug2 = new Drug();
//		drug.setDose(1.5);
//		drug.setName("Sedavit");
//		drug.setPrescription(false);
//		drug.setPrice(100);
//		drug.setQuantity(30);
//		Drug drug2InDb = drugDAO.save(drug2);
//		System.out.println(drug2InDb);
		
		List<Drug> drugs = ((DrugDAOImpl)drugDAO).findByName("Sedavit");
		System.out.println(drugs);
		System.out.println(drug1InDb);
		assert (drugs.contains(drug1InDb) );
		
		drugDAO.delete(drug1InDb.getId());
		//drugDAO.delete(drug2InDb.getId());
		
	}

}
