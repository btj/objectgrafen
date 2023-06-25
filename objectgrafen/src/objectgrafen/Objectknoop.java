package objectgrafen;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @invar | getVerwijzers() != null
 * @invar | getVerwijzers().stream().allMatch(l -> l != null && l.getElementen().contains(this))
 */
public abstract class Objectknoop {

	/**
	 * @invar | verwijzers != null
	 * @invar | verwijzers.stream().allMatch(l -> l != null && l.elementen.contains(this))
	 * 
	 * @representationObject
	 * @peerObjects
	 */
	Set<Lijstobjectknoop> verwijzers = new HashSet<>();
	
	/**
	 * @creates | result
	 * @peerObjects
	 */
	public Set<Lijstobjectknoop> getVerwijzers() { return Set.copyOf(verwijzers); }
	
	Objectknoop() {}
	
	/*
	 * De subklassen van Objectknoop moeten gedragssubtypes zijn van Objectknoop. Dit vereist dat de specificatie
	 * van elke overschrijvende methode strenger is dan (of equivalent aan) de specificatie van de overschreven
	 * methode. Dit houdt in dit geval in dat ze oproepen met `builder != null` als geldig specificeren en
	 * in die gevallen garanderen dat de nieuwe inhoud van de builder begint met de oude inhoud.
	 */
	/**
	 * @pre | builder != null
	 * @inspects | this
	 * @mutates | builder
	 * @post | builder.toString().startsWith(old(builder.toString()))
	 */
	public abstract void voegTekstueleVoorstellingToeAan(StringBuilder builder);
	
	public abstract boolean heeftZelfdeInhoudAls(Objectknoop andere);
	
	public Iterator<Objectknoop> linkseAfstammelingenIterator() {
		return new Iterator<>() {
			Objectknoop k = Objectknoop.this;
			@Override
			public boolean hasNext() {
				return k instanceof Lijstobjectknoop l && !l.getElementen().isEmpty();
			}
			@Override
			public Objectknoop next() {
				k = ((Lijstobjectknoop)k).getElementen().get(0);
				return k;
			}
		};
	}

	public void forEachLinkseAfstammelingMetMinstens3Elementen(Consumer<? super Lijstobjectknoop> consumer) {
		Iterator<Objectknoop> i = linkseAfstammelingenIterator();
		while (i.hasNext()) {
			Objectknoop k = i.next();
			if (k instanceof Lijstobjectknoop l && l.getElementen().size() >= 3)
				consumer.accept(l);
		}
	}

}
