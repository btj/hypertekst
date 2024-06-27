package hypertekst;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import logicalcollections.LogicalList;

/**
 * @invar | getDocument() == null || getDocument().getElementen().contains(this)
 */
public abstract class Element {

	/**
	 * @invar | referrers != null
	 * @invar | tekstinhoud != null
	 * @invar | document == null || document.elementen.contains(this)
	 * @invar | referrers.stream().allMatch(h -> h != null && h.doelelement == this)
	 */
	final String tekstinhoud;
	/**
	 * @representationObject
	 * @peerObjects
	 */
	final Set<Hyperlink> referrers = new HashSet<>();
	/**
	 * @peerObject
	 */
	Document document;
	
	/**
	 * @peerObject
	 */
	public Document getDocument() { return document; }
	
	/**
	 * @post | result != null
	 * 
	 * @immutable
	 */
	public String getTekstinhoud() { return tekstinhoud; }
	
	/**
	 * @post | result != null
	 * @post | result.stream().allMatch(h -> h != null && h.getDoelelement() == this)
	 * 
	 * @creates | result
	 * @peerObjects
	 */
	public Set<Hyperlink> getReferrers() { return Set.copyOf(referrers); }
	
	Element(String tekstinhoud) {
		this.tekstinhoud = tekstinhoud;
	}
	
	/**
	 * @pre | getDocument() != null
	 * @mutates_properties | getDocument(), getDocument().getElementen()
	 * @post | getDocument() == null
	 * @post | old(getDocument()).getElementen().equals(LogicalList.minus(old(getDocument().getElementen()), this))
	 */
	public void verwijderUitDocument() {
		document.elementen.remove(this);
		document = null;
	}
	
	public boolean contentEquals(Element other) {
		return tekstinhoud.equals(other.tekstinhoud);
	}
	
	/*
	 * Gedragssubtypering betekent dat de specificatie van een overschrijvende methode strenger moet zijn
	 * dan de specificatie van de overschreven methode (of equivalent).
	 * Dit is het geval als de preconditie van de overschrijvende methode
	 * zwakker is (of equivalent), en de postconditie strenger (of equivalent).
	 */
	/**
	 * @pre | buffer != null
	 * @mutates | buffer
	 * @inspects | this
	 * @post | 0 <= result
	 * @post | result <= buffer.length || Arrays.equals(buffer, old(buffer.clone()))
	 * @post | buffer.length < result || Arrays.equals(buffer, result, buffer.length, old(buffer.clone()), result, buffer.length)
	 */
	public abstract int getTekstueleVoorstelling(char[] buffer);

}
