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
	
	Document document = new Document();
	Tekstelement leesDe = new Tekstelement("Lees de ");
	Hyperlink documentatieLink = new Hyperlink("Documentatie");
	
	Tekstelement leesDe2 = new Tekstelement("Lees de ");
	Tekstelement zieDe = new Tekstelement("Zie de ");
	Hyperlink documentatieLink2 = new Hyperlink("Documentatie");
	Hyperlink docs = new Hyperlink("Docs");

	@Test
	void testDocumentConstructor() {
		assertEquals(List.of(), document.getElementen());
	}
	
	@Test
	void testTekstelementConstructor() {
		assertNull(leesDe.getDocument());
		assertEquals("Lees de ", leesDe.getTekstinhoud());
		assertEquals(Set.of(), leesDe.getReferrers());
	}
	
	@Test
	void testHyperlinkConstructor() {
		assertNull(documentatieLink.getDocument());
		assertEquals("Documentatie", documentatieLink.getTekstinhoud());
		assertEquals(Set.of(), documentatieLink.getReferrers());
	}
	
	@Test
	void testSetDoelelement() {
		documentatieLink.setDoelelement(leesDe);
		assertSame(leesDe, documentatieLink.getDoelelement());
		assertEquals(Set.of(documentatieLink), leesDe.getReferrers());
	}
	
	@Test
	void testClearDoelelement() {
		documentatieLink.setDoelelement(leesDe);
		documentatieLink.clearDoelelement();
		assertNull(documentatieLink.getDoelelement());
		assertEquals(Set.of(), leesDe.getReferrers());
	}
	
	@Test
	void testAddElement() {
		document.addElement(leesDe);
		document.addElement(documentatieLink);
		assertEquals(List.of(leesDe, documentatieLink), document.getElementen());
		assertSame(document, leesDe.getDocument());
	}
	
	@Test
	void testVerwijderUitDocument() {
		document.addElement(leesDe);
		document.addElement(documentatieLink);
		documentatieLink.verwijderUitDocument();
		assertNull(documentatieLink.getDocument());
		assertEquals(List.of(leesDe), document.getElementen());
	}
	
	@Test
	void testContentEquals() {
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
		
		documentatieLink2.setDoelelement(zieDe);
		assertFalse(documentatieLink.contentEquals(documentatieLink2));
		documentatieLink.setDoelelement(leesDe2);
		assertFalse(documentatieLink.contentEquals(documentatieLink2));
		documentatieLink.clearDoelelement();
		documentatieLink.setDoelelement(zieDe);
		assertTrue(documentatieLink.contentEquals(documentatieLink2));
	}
	
	@Test
	void testGetTekstueleVoorstelling() {
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
	}
	
	@Test
	void testGetEvenElementenIterator() {
		document.addElement(leesDe);
		document.addElement(documentatieLink);
		document.addElement(zieDe);
		document.addElement(docs);
		document.addElement(documentatieLink2);
		
		ArrayList<Element> evenElementen = new ArrayList<>();
		for (Iterator<Element> i = document.getEvenElementenIterator(); i.hasNext(); )
			evenElementen.add(i.next());
		assertEquals(List.of(leesDe, zieDe, documentatieLink2), evenElementen);
	}
	
	@Test
	void testForEachHyperlink() {
		document.addElement(leesDe);
		document.addElement(documentatieLink);
		document.addElement(zieDe);
		document.addElement(docs);
		document.addElement(documentatieLink2);
		
		ArrayList<Hyperlink> hyperlinks = new ArrayList<>();
		document.forEachHyperlink(h -> hyperlinks.add(h));
		assertEquals(List.of(documentatieLink, docs, documentatieLink2), hyperlinks);
	}
	
	@Test
	void testGetDoelelementenStream() {
		document.addElement(leesDe);
		document.addElement(documentatieLink);
		document.addElement(zieDe);
		document.addElement(docs);
		document.addElement(documentatieLink2);
		
		docs.setDoelelement(zieDe);
		documentatieLink2.setDoelelement(docs);
		
		assertEquals(List.of(zieDe, docs), document.getDoelelementenStream().toList());
	}

}
