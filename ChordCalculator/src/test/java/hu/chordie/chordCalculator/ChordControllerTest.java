package hu.chordie.chordCalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import hu.chordie.ChordCalculator.Controllers.ChordController;
import hu.chordie.ChordCalculator.DTOs.ChordComponentsDTO;
import hu.chordie.ChordCalculator.DTOs.ChordDTO;
import hu.chordie.ChordCalculator.Exceptions.BadExpressionException;
import hu.chordie.ChordCalculator.Model.Sound;
import hu.chordie.ChordCalculator.Model.Chord.BaseType;
import hu.chordie.ChordCalculator.Model.Chord.ChordType;
import hu.chordie.ChordCalculator.Repositories.InstrumentRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class ChordControllerTest {

	   @Mock
	   private InstrumentRepository instrumentRepository;
	   
	   @InjectMocks
	   private ChordController chordController;
	 
	   @Before
	   public void init() {
		   
	   }
	   
	   @Test
	   public void chordComponentTest() {
		   ChordComponentsDTO components = chordController.getChordComponents();
		   assertTrue(components.getBaseSounds().size()>0);
		   assertTrue(components.getBaseTypes().size()>0);
		   assertTrue(components.getChordTypes().size()>0);
	   }
	   
	   @Test
	   public void chordTextAnalyzeTest() {
		   String input = "Cm7>E<2";
		   try {
		   ChordDTO output = chordController.chordTextAnalyze(input);
		   assertEquals(output.getBaseSound().getName(), Sound.C.name());
		   assertEquals(output.getBaseType().getName(), BaseType.min.name());
		   assertEquals(output.getChordType().getName(), ChordType.n7.name());
		   assertEquals(output.getCapo(), new Integer(2));
		   assertEquals(output.getRootNote().getName(), Sound.E.name());
		   
		   } catch(BadExpressionException e ) {
			   fail();
		   }

	   }
	   @Test(expected=BadExpressionException.class)
	   public void chordTextAnalyzeTestWithException() throws BadExpressionException{
		   String input = "SDDSAASD";
		   chordController.chordTextAnalyze(input);
	   }
	
}
