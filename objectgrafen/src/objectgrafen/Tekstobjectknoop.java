package objectgrafen;

import java.util.Iterator;

/**
 * @invar | getTekstinhoud() != null 
 */
public class Tekstobjectknoop extends Objectknoop {

	/**
	 * @invar | tekstinhoud != null
	 */
	String tekstinhoud;
	
	/**
	 * @immutable // Optional
	 */
	public String getTekstinhoud() { return tekstinhoud; }
	
	/**
	 * @throws IllegalArgumentException | tekstinhoud == null
	 * @post | getTekstinhoud() == tekstinhoud // of '.equals'; allebei goed
	 * @post | getVerwijzers().isEmpty()
	 */
	public Tekstobjectknoop(String tekstinhoud) {
		if (tekstinhoud == null)
			throw new IllegalArgumentException("`tekstinhoud` is null");
		this.tekstinhoud = tekstinhoud;
	}
	
	/**
	 * @throws IllegalArgumentException | builder == null
	 * @inspects | this
	 * @mutates | builder
	 * @post | builder.toString().equals(old(builder.toString()) + "\"" + getTekstinhoud() + "\"")
	 */
	@Override
	public void voegTekstueleVoorstellingToeAan(StringBuilder builder) {
		if (builder == null)
			throw new IllegalArgumentException("`builder` is null");
		builder.append('"');
		builder.append(tekstinhoud);
		builder.append('"');
	}
	
	@Override
	public boolean heeftZelfdeInhoudAls(Objectknoop andere) {
		return andere instanceof Tekstobjectknoop t && tekstinhoud.equals(t.tekstinhoud);
	}
}
