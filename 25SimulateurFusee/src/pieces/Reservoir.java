package pieces;
/**
 * La classe repr�sente un r�servoir de fus�e
 * @author Thomas Corbeil
 *
 */
public class Reservoir extends Pieces {
	private double carburant, carburantRestant;
	/**
	 * Constructeur prenant en compte le nom de l'image de la pi�ce, sa masse et sa quantit� maximale de carburant
	 * @param nomPiece le nom de la pi�ce
	 * @param nomImage le nom du fichier de l'image de la pi�ce
	 * @param nomImageCarre le nom de l'image de format carr� de la pi�ce
	 * @param masse la masse de la pi�ce en kg
	 * @param carburant la quantit� maximale de carburant du r�servoir
	 */
	public Reservoir(String nomPiece, String nomImage, String nomImageCarre, double masse, double carburant) {
		super(nomPiece, nomImage, nomImageCarre, masse);
		this.carburant = carburant;
		this.carburantRestant = carburant;
	}
	//Javadoc d�ja g�n�r�e 
	public int verifierTypePiece() {
		return 2;
	}
	/**
	 * Setter qui assigne une quantit� maximale de carburant � la capsule
	 * @param carburant la quantit� maximale de carburant de la capsule en L
	 */
	public void setCarburant(double carburant) {
		this.carburant = carburant;
	}
	/**
	 * Getter qui retourne la quantit� maximale de carburant de la capsule
	 * @return la quantit� maximale de carburant de la capsule en L
	 */
	public double getCarburant() {
		return carburant;
	}
	/**
	 * Setter qui assigne une quantit� de carburant � la capsule
	 * @param carburantRestant la quantit� de carburant de la capsule en L
	 */
	public void setCarburantRestant(double carburantRestant) {
		this.carburantRestant = carburantRestant;
	}	
	/**
	 * Getter qui retourne la quantit� de carburant de la capsule
	 * @return la quantit�e de carburant de la capsule en L
	 */
	public double getCarburantRestant() {
		return carburantRestant;
	}
	/**
	 * Getter qui retourne la masse du r�servoir incluant celle du carburant
	 * @return la masse du r�servoir et du carburant en kg
	 */
	public double getMasseAvecCarburant() {
		return getMasse() + carburantRestant * 1.5;
	}

}
