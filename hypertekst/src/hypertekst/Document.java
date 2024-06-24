package hypertekst;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import logicalcollections.LogicalList;

public class Document {
	
	/**
	 * @invar | elementen != null
	 * @invar | elementen.stream().allMatch(e -> e != null && e.document == this)
	 * 
	 * @representationObject
	 * @peerObjects
	 */
	List<Element> elementen = new ArrayList<>();
	
	/**
	 * @peerObjects
	 * 
	 * @post | result != null
	 * @post | result.stream().allMatch(e -> e != null && e.getDocument() == this)
	 */
	public List<Element> getElementen() { return List.copyOf(elementen); }
	
	/**
	 * @post | getElementen().isEmpty()
	 */
	public Document() {}
	
	/**
	 * @pre | element != null
	 * @pre | element.getDocument() == null
	 * @mutates_properties | getElementen(), element.getDocument()
	 * @post | getElementen().equals(LogicalList.plus(old(getElementen()), element))
	 */
	public void addElement(Element element) {
		elementen.add(element);
		element.document = this;
	}
	
	public Iterator<Element> getEvenElementenIterator() {
		return new Iterator<>() {
			int index = 0;
			public boolean hasNext() {
				return index < elementen.size();
			}
			public Element next() {
				Element result = elementen.get(index);
				index += 2;
				return result;
			}
		};
	}
	
	public void forEachHyperlink(Consumer<? super Hyperlink> consumer) {
		for (Element element : elementen)
			if (element instanceof Hyperlink h)
				consumer.accept(h);
	}
	
	public Stream<Element> getDoelelementenStream() {
		return elementen.stream().flatMap(e -> e instanceof Hyperlink h ? Stream.of(h.getDoelelement()) : null).filter(e -> e != null);
	}

}
