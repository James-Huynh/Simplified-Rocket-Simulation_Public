package interfaces;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
/**
 * Cette interface rend un objet dessinable avec un grandissement
 * @author Thomas Corbeil
 *
 */
public interface DessinableGrandissement {
	/**
	 * M�thode qui dessine l'objet dans un monde d�fini avec des unit�s r�elles et avec un rapport d'homot�tie
	 * @param g2d le contexte graphique dans lequel on veut dessiner l'objet
	 * @param mat la matrice de transformation passant du monde r�el au monde des pixels
	 * @param rotation la rotation en degr�s par rapport au centre de l'objet � dessiner
	 * @param x la position x de la pi�ce en m
	 * @param y la position y de la pi�ce en m
	 * @param centreRotX la position du centre de la rotation x de la pi�ce en m
	 * @param centreRotY la position du centre de la rotation y de la pi�ce en m
	 * @param grandissement le rapport d'homot�tie avec lequel on veut dessiner la pi�ce
	 */
	public void dessinerAvecGrandissement(Graphics2D g2d, AffineTransform mat, double rotation, double x, double y, double centreRotX, double centreRotY, double grandissement);

}
