package hypertekst.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import hypertekst.Document;
import hypertekst.Element;
import hypertekst.Hyperlink;
import hypertekst.Tekstelement;

class HypertekstTest {

	@Test
	void test() {
		Document document = new Document();
		assertEquals(List.of(), document.getElementen());
		
		Tekstelement leesDe = new Tekstelement("Lees de ");
		assertNull(leesDe.getDocument());
		assertEquals("Lees de ", leesDe.getTekstinhoud());
		assertEquals(Set.of(), leesDe.getReferrers());
		
		Hyperlink documentatieLink = new Hyperlink("Documentatie");
		assertNull(documentatieLink.getDocument());
		assertEquals("Documentatie", documentatieLink.getTekstinhoud());
		assertEquals(Set.of(), documentatieLink.getReferrers());
		
		documentatieLink.setDoelelement(leesDe);
		assertSame(leesDe, documentatieLink.getDoelelement());
		assertEquals(Set.of(documentatieLink), leesDe.getReferrers());
		
		document.addElement(leesDe);
		document.addElement(documentatieLink);
		assertEquals(List.of(leesDe, documentatieLink), document.getElementen());
		assertSame(document, leesDe.getDocument());
		
		documentatieLink.clearDoelelement();
		assertNull(documentatieLink.getDoelelement());
		assertEquals(Set.of(), leesDe.getReferrers());
		
		documentatieLink.verwijderUitDocument();
		assertNull(documentatieLink.getDocument());
		assertEquals(List.of(leesDe), document.getElementen());
		
		Tekstelement leesDe2 = new Tekstelement("Lees de ");
		Tekstelement zieDe = new Tekstelement("Zie de ");
		assertTrue(leesDe.contentEquals(leesDe2));
		assertFalse(leesDe.contentEquals(zieDe));
		assertFalse(leesDe.contentEquals(documentatieLink));
		
		Hyperlink documentatieLink2 = new Hyperlink("Documentatie");
		Hyperlink docs = new Hyperlink("Docs");
		assertTrue(documentatieLink.contentEquals(documentatieLink2));
		assertFalse(documentatieLink.contentEquals(docs));
		assertFalse(documentatieLink.contentEquals(leesDe));
		
		char[] groteBuffer = new char[10];
		assertEquals(7, zieDe.getTekstueleVoorstelling(groteBuffer));
		assertArrayEquals("Zie de \0\0\0".toCharArray(), groteBuffer);
		
		assertEquals(6, docs.getTekstueleVoorstelling(groteBuffer));
		assertArrayEquals("_Docs_ \0\0\0".toCharArray(), groteBuffer);
		
		char[] kleineBuffer = new char[5];
		char[] kleineBufferClone = kleineBuffer.clone();
		assertEquals(7, zieDe.getTekstueleVoorstelling(kleineBuffer));
		assertArrayEquals(kleineBufferClone, kleineBuffer);
		
		assertEquals(6, docs.getTekstueleVoorstelling(kleineBuffer));
		assertArrayEquals(kleineBufferClone, kleineBuffer);
		
		document.addElement(documentatieLink);
		document.addElement(zieDe);
		document.addElement(docs);
		document.addElement(documentatieLink2);
		
		ArrayList<Element> evenElementen = new ArrayList<>();
		for (Iterator<Element> i = document.getEvenElementenIterator(); i.hasNext(); )
			evenElementen.add(i.next());
		assertEquals(List.of(leesDe, zieDe, documentatieLink2), evenElementen);
		
		ArrayList<Hyperlink> hyperlinks = new ArrayList<>();
		document.forEachHyperlink(h -> hyperlinks.add(h));
		assertEquals(List.of(documentatieLink, docs, documentatieLink2), hyperlinks);
		
		docs.setDoelelement(zieDe);
		documentatieLink2.setDoelelement(docs);
		assertEquals(List.of(zieDe, docs), document.getDoelelementenStream().toList());
	}

}
