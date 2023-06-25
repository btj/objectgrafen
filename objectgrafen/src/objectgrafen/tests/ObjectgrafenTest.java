package objectgrafen.tests; // NIET IN ZELFDE PACKAGE!

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import objectgrafen.Lijstobjectknoop;
import objectgrafen.Objectknoop;
import objectgrafen.Tekstobjectknoop;

class ObjectgrafenTest {

	@Test
	void test() {
		Tekstobjectknoop tHallo = new Tekstobjectknoop("Hallo");
		assertEquals("Hallo", tHallo.getTekstinhoud());
		assertTrue(tHallo.getVerwijzers().isEmpty());
		
		Lijstobjectknoop l = new Lijstobjectknoop();
		assertTrue(l.getElementen().isEmpty());
		assertTrue(l.getVerwijzers().isEmpty());
		
		l.voegIn(0, tHallo);
		assertEquals(List.of(tHallo), l.getElementen());
		assertEquals(Set.of(l), tHallo.getVerwijzers());
		
		Tekstobjectknoop tWereld = new Tekstobjectknoop("Wereld");
		
		l.voegIn(1, tWereld);
		assertEquals(List.of(tHallo, tWereld), l.getElementen());
		
		{
			StringBuilder b = new StringBuilder();
			l.voegTekstueleVoorstellingToeAan(b);
			assertEquals("[\"Hallo\",\"Wereld\"]", b.toString());
		}
		
		l.voegIn(0, tHallo);
		
		Lijstobjectknoop l2 = new Lijstobjectknoop();
		l2.voegIn(0, l);
		
		{
			ArrayList<Objectknoop> linkseAfstammelingen = new ArrayList<>();
			for (Iterator<Objectknoop> i = l2.linkseAfstammelingenIterator(); i.hasNext(); )
				linkseAfstammelingen.add(i.next());
			assertEquals(List.of(l, tHallo), linkseAfstammelingen);
		}
		
		{
			ArrayList<Objectknoop> linkseAfstammelingen = new ArrayList<>();
			l2.forEachLinkseAfstammelingMetMinstens3Elementen(a -> linkseAfstammelingen.add(a));
			assertEquals(List.of(l), linkseAfstammelingen);
		}
		
		l.voegIn(0, l);
		assertEquals(List.of(l, tHallo, tHallo, tWereld), l.getElementen());
		assertEquals(Set.of(l), tHallo.getVerwijzers());
		assertEquals(Set.of(l2, l), l.getVerwijzers());
		
		l.voegIn(2, l);
		assertEquals(List.of(l, tHallo, l, tHallo, tWereld), l.getElementen());
		assertEquals(Set.of(l2, l), l.getVerwijzers());
		
		assertEquals(List.of("Hallo", "Hallo", "Wereld"), l.getTekstinhoudenVanElementenStream().toList());
		
		l.verwijder(2);
		assertEquals(List.of(l, tHallo, tHallo, tWereld), l.getElementen());
		assertEquals(Set.of(l2, l), l.getVerwijzers());
		
		l.verwijder(1);
		l.verwijder(1);
		assertEquals(List.of(l, tWereld), l.getElementen());
		assertTrue(tHallo.getVerwijzers().isEmpty());
		
		l.verwijder(0);
		assertEquals(List.of(tWereld), l.getElementen());
		assertEquals(Set.of(l2), l.getVerwijzers());
		
		Tekstobjectknoop tHallo2 = new Tekstobjectknoop("Hallo");
		Lijstobjectknoop l3 = new Lijstobjectknoop();
		l3.voegIn(0, tWereld);
		assertTrue(tHallo.heeftZelfdeInhoudAls(tHallo2));
		assertTrue(l.heeftZelfdeInhoudAls(l3));
		assertFalse(tHallo.heeftZelfdeInhoudAls(tWereld));
		assertFalse(tHallo.heeftZelfdeInhoudAls(l));
		assertFalse(l.heeftZelfdeInhoudAls(l2));
		assertFalse(l.heeftZelfdeInhoudAls(tHallo));
	}

}
