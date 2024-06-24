package hypertekst;

import java.util.Arrays;

import logicalcollections.LogicalSet;

/**
 * @invar | getDoelelement() == null || getDoelelement().getReferrers().contains(this)
 */
public class Hyperlink extends Element {
	
	/**
	 * @invar | true // Phase 1 invariant; intentionally left blank
	 * @invar | doelelement == null || doelelement.referrers.contains(this)
	 * 
	 * @peerObject
	 */
	Element doelelement;
	
	/**
	 * @peerObject
	 */
	public Element getDoelelement() { return doelelement; }
	
	/**
	 * @pre | tekstinhoud != null
	 * @post | getTekstinhoud().equals(tekstinhoud)
	 * @post | getReferrers().isEmpty()
	 * @post | getDoelelement() == null
	 */
	public Hyperlink(String tekstinhoud) {
		super(tekstinhoud);
	}
	
	/**
	 * @pre | getDoelelement() == null
	 * @pre | doelelement != null
	 * @mutates_properties | getDoelelement(), doelelement.getReferrers()
	 * @post | getDoelelement() == doelelement
	 * @post | doelelement.getReferrers().equals(LogicalSet.plus(old(doelelement.getReferrers()), this))
	 */
	public void setDoelelement(Element doelelement) {
		this.doelelement = doelelement;
		doelelement.referrers.add(this);
	}
	
	/**
	 * @pre | getDoelelement() != null
	 * @mutates_properties | getDoelelement(), getDoelelement().getReferrers()
	 * @post | getDoelelement() == null
	 * @post | old(getDoelelement()).getReferrers().equals(LogicalSet.minus(old(getDoelelement().getReferrers()), this))
	 */
	public void clearDoelelement() {
		doelelement.referrers.remove(this);
		doelelement = null;
	}
	
	@Override
	public boolean contentEquals(Element other) {
		return super.contentEquals(other) && other instanceof Hyperlink h && doelelement == h.doelelement;
	}
	
	/**
	 * @pre | buffer != null
	 * @mutates | buffer
	 * @inspects | this
	 * @post | result == getTekstinhoud().length() + 2
	 * @post | result <= buffer.length || Arrays.equals(buffer, old(buffer.clone()))
	 * @post | buffer.length < result || Arrays.equals(buffer, result, buffer.length, old(buffer.clone()), result, buffer.length)
	 * @post | buffer.length < result || Arrays.equals(buffer, 0, result , ("_" + getTekstinhoud() + "_").toCharArray(), 0, result)
	 */
	@Override
	public int getTekstueleVoorstelling(char[] buffer) {
		int result = 2 + tekstinhoud.length();
		if (buffer.length >= result) {
			buffer[0] = '_';
			tekstinhoud.getChars(0, tekstinhoud.length(), buffer, 1);
			buffer[result - 1] = '_';
		}
		return result;
	}

}
