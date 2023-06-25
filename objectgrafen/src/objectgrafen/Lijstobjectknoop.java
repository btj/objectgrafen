package objectgrafen;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import logicalcollections.LogicalList;
import logicalcollections.LogicalSet;

/**
 * @invar | getElementen() != null
 * @invar | getElementen().stream().allMatch(e -> e != null && e.getVerwijzers().contains(this))
 */
public class Lijstobjectknoop extends Objectknoop {
	
	/**
	 * @invar | elementen != null
	 * @invar | elementen.stream().allMatch(o -> o != null && o.verwijzers.contains(this))
	 * 
	 * @representationObject
	 * @peerObjects
	 */
	List<Objectknoop> elementen = new ArrayList<>();
	
	/**
	 * @creates | result
	 * @peerObjects
	 */
	public List<Objectknoop> getElementen() { return List.copyOf(elementen); }
	
	/**
	 * @post | getElementen().isEmpty()
	 * @post | getVerwijzers().isEmpty()
	 */
	public Lijstobjectknoop() {}
	
	/**
	 * @pre | 0 <= index
	 * @pre | index <= getElementen().size()
	 * @pre | element != null
	 * @mutates_properties | getElementen(), element.getVerwijzers()
	 * @post | getElementen().equals(LogicalList.plusAt(old(getElementen()), index, element))
	 * @post | element.getVerwijzers().equals(LogicalSet.plus(old(element.getVerwijzers()), this))
	 */
	public void voegIn(int index, Objectknoop element) {
		elementen.add(index, element);
		element.verwijzers.add(this);
	}
	
	/**
	 * @pre | 0 <= index
	 * @pre | index < getElementen().size()
	 * @mutates_properties | getElementen(), getElementen().get(index).getVerwijzers()
	 * @post | getElementen().equals(LogicalList.minusAt(old(getElementen()), index))
	 * @post | old(getElementen()).get(index).getVerwijzers().equals(
	 *       |     getElementen().contains(old(getElementen()).get(index)) ?
	 *       |         old(getElementen().get(index).getVerwijzers())
	 *       |     :
	 *       |         LogicalSet.minus(old(getElementen().get(index).getVerwijzers()), this)
	 *       | )
	 * // Volstaat ook: old(getElementen().get(index).getVerwijzers()).containsAll(old(getElementen()).get(index).getVerwijzers())
	 */
	public void verwijder(int index) {
		Objectknoop element = elementen.remove(index);
		if (!elementen.contains(element))
			element.verwijzers.remove(this);
	}
	
	/**
	 * @pre | builder != null
	 * @inspects | this
	 * @mutates | builder
	 * @post | builder.toString().equals(
	 *       |     old(builder.toString()) + "[" +
	 *       |     getElementen().stream().map(e -> {
	 *       |         StringBuilder b = new StringBuilder();
	 *       |         e.voegTekstueleVoorstellingToeAan(b);
	 *       |         return b.toString();
	 *       |     }).collect(Collectors.joining(","))
	 *       |     + "]"
	 *       | )
	 * // Volstaat ook: builder.toString().startsWith(old(builder.toString()) + "[") && builder.toString().endsWith("]")
	 */
	@Override
	public void voegTekstueleVoorstellingToeAan(StringBuilder builder) {
		builder.append('[');
		if (!elementen.isEmpty()) {
			elementen.get(0).voegTekstueleVoorstellingToeAan(builder);
			for (int i = 1; i < elementen.size(); i++) {
				builder.append(',');
				elementen.get(i).voegTekstueleVoorstellingToeAan(builder);
			}
		}
		builder.append(']');
	}

	@Override
	public boolean heeftZelfdeInhoudAls(Objectknoop andere) {
		return andere instanceof Lijstobjectknoop l && elementen.equals(l.elementen);
	}
	
	public Stream<String> getTekstinhoudenVanElementenStream() {
		return elementen.stream().flatMap(e ->
			e instanceof Tekstobjectknoop t ? Stream.of(t.getTekstinhoud()) : null
		);
	}

}
