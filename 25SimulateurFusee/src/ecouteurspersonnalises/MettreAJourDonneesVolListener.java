package ecouteurspersonnalises;

import java.util.EventListener;
/**
 * Classe de listener qui indique quand mettre � jour les donn�es de vol pendant le d�collage
 * @author Thomas Corbeil
 *
 */
public interface MettreAJourDonneesVolListener extends EventListener {
	/**
	 * Listener qui indique quand mettre � jour les donn�es de vol pendant le d�collage
	 */
	public void mettreAJourDonneesVol();
	
}
