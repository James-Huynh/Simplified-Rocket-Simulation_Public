package zonededessin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import ballondessinable.BallonCapsule;
import ballondessinable.BallonGenerique;
import ballondessinable.BallonMoteur;
import ballondessinable.BallonReservoir;
import ecouteurspersonnalises.SelectionPiecesListener;
import pieces.Capsule;
import pieces.Moteur;
import pieces.Pieces;
import pieces.Reservoir;

/**
 * La classe ZoneDessinAssemblage sert � la construction de la fus�e � partir d'un ensemble de pi�ces, l'utilisateur peut personnaliser sa fus�e � sa guise
 * @author James Huynh
 *
 */
public class ZoneDessinAssemblage extends JPanel {

	private static final long serialVersionUID = 6867462788558769791L;
	private int indexZoneSelectionne, indexZoneSurvole, sectionFusee, ratioImagePiece = 4;
	private boolean premiereFois = true, pieceClique = false, pieceSurvole = false;

	private int longueurBallon = 180, hauteurBallonCapsule = 60, hauteurBallonReservoir = 60, hauteurBallonMoteur = 100;
	private double pointX, pointY;

	private BallonGenerique ballon;

	private String nomImageCarre = "/carrePointille.png";
	private String txtCapsule = "Capsule", txtReservoir = "Reservoir", txtMoteur = "Moteur"; 
	private Color couleurTxt = new Color(48, 63, 66);

	private double hauteurPanel, largeurPanel, tiersEnX, tiersEnY, demiTiersY;
	private double positionXCarre, positionYCarre, hauteurCarre, largeurCarre, positionXCarreInitiale, positionYCarreInitiale;
	private double positionXImage, largeurImageCourante, hauteurImageCourante, positionXPetitePiece, positionYPetitePiece;
	private double[] tableauCoordYImages= new double[3];

	private ArrayList<Shape> listeCarreSelectionnable = new ArrayList<Shape>();
	private Rectangle2D.Double zoneDroiteSup, zoneDroiteCentre, zoneDroiteInf;
	private int indiceZoneDroite;

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();

	private Pieces pieceCourante = null, pieceCapsule = null, pieceMoteur = null, pieceReservoir = null;
	private Pieces[] tableauPieces = null;

	private Image[] tableauImagesCarres = null;
	private Image imageCarre;

	/**
	 * Le constructeur de la zone de dessin pour l'assemblage de la fus�e
	 * @param pieces Un tableau contenant des pi�ces ordonn�es selon son type
	 */
	public ZoneDessinAssemblage(Pieces[] pieces) {
		tableauPieces = pieces;
		tableauImagesCarres = new Image[9];

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//Debut
				if (e.getButton() == MouseEvent.BUTTON1) {
					clicGauche(e.getPoint());
				}

				if (e.getButton() == MouseEvent.BUTTON3) {
					clicDroite(e.getPoint());
				}
				//Fin
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				//Debut
				sourisBouge(e.getPoint());
			}
			//Fin
		});

		setBackground(Color.lightGray);

		URL urlImageCarre = ZoneDessinAssemblage.class.getResource(nomImageCarre); //Cherche l'image du contour carr�e des petites pi�ces
		if(urlImageCarre == null) {
			System.out.println("L'image" + nomImageCarre + "est introuvable");
		}
		try {
			imageCarre = ImageIO.read(urlImageCarre);
		} catch (IOException e) {
			System.out.println("le fichier image du carre est impossible � lire");
		}

		initialiserImagesCarres();
	}

	/**
	 * Le paintComponent pour dessiner tous les �l�ments reli�s � la phase de l'assemblage
	 * @param g Le contexte graphique
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Color couleurInitiale = g2d.getColor();

		if (premiereFois) {
			premiereFois = false;
			hauteurPanel = getHeight();
			largeurPanel = getWidth();
			tiersEnX = largeurPanel/3;
			tiersEnY = hauteurPanel/3;
			demiTiersY = tiersEnY/2;
			hauteurCarre = tiersEnX/4.5; //� ajuster
			largeurCarre = hauteurCarre;
			positionXCarreInitiale = tiersEnX/4 - largeurCarre/2;
			positionYCarreInitiale = tiersEnY - demiTiersY - hauteurCarre/2;

			tableauCoordYImages[0] = 1*tiersEnY - tiersEnY/2;
			tableauCoordYImages[1] = 2*tiersEnY - tiersEnY/2;
			tableauCoordYImages[2] = 3*tiersEnY - tiersEnY/2;

			imageCarre = imageCarre.getScaledInstance((int) largeurCarre, (int) hauteurCarre, Image.SCALE_SMOOTH); 	 
		}

		g2d.setColor(couleurTxt);
		g2d.drawString(txtCapsule, 20, 0 + 20); //Dessiner le petit texte
		g2d.drawString(txtReservoir, 20, (int) (tiersEnY + 20));
		g2d.drawString(txtMoteur, 20, (int) (2*tiersEnY + 20));
		g2d.setColor(couleurInitiale);
		
		listeCarreSelectionnable.removeAll(listeCarreSelectionnable);
		positionXCarre = positionXCarreInitiale;
		positionYCarre = positionYCarreInitiale;

		creerZonesDroites();

		int n = 0;
		for (int k = 0; k < 3; k++) { //Pour dessiner les carres pointill�s et pieces miniatures
			for (int i = 0; i < 3; i++) {
				listeCarreSelectionnable.add(creerZoneSelectionnable (positionXCarre,  positionYCarre));
				g2d.drawImage(imageCarre, (int) positionXCarre, (int) positionYCarre, null);

				positionXPetitePiece = positionXCarre + largeurCarre/2 - tableauImagesCarres[n].getWidth(null)/2;
				positionYPetitePiece = positionYCarre + hauteurCarre/2 - tableauImagesCarres[n].getHeight(null)/2;
				g2d.drawImage(tableauImagesCarres[n], (int) positionXPetitePiece, (int) positionYPetitePiece, null);

				positionXCarre =  positionXCarre + tiersEnX/3.8;

				++n;
			}
			positionXCarre = positionXCarreInitiale;
			positionYCarre = positionYCarre + tiersEnY;
		}

		for (int k = 1; k <= 2; k++) { //Pour dessiner les limitations des sections horizontales
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(0, (int) tiersEnY*k, (int) tiersEnX, (int) tiersEnY*k); 
		}
		g2d.drawLine((int) tiersEnX, 0, (int) tiersEnX, (int) hauteurPanel); 

		if (!(pieceCapsule == null)) { //Dessine la capsule � droite
			hauteurImageCourante = pieceCapsule.getHauteur()*ratioImagePiece;
			pieceCapsule.dessinerPixel(g2d, (int) positionXImage, (int) (tableauCoordYImages[0]-hauteurImageCourante/2), 
					(int) pieceCapsule.getLargeur()*ratioImagePiece, (int) pieceCapsule.getHauteur()*ratioImagePiece, 0, 0, 0);
		}
		if (!(pieceReservoir == null)) { //Dessine le r�servoir � droite
			hauteurImageCourante = pieceReservoir.getHauteur()*ratioImagePiece;
			pieceReservoir.dessinerPixel(g2d, (int) positionXImage, (int) (tableauCoordYImages[1]-hauteurImageCourante/2), 
					(int) pieceReservoir.getLargeur()*ratioImagePiece, (int) pieceReservoir.getHauteur()*ratioImagePiece, 0, 0, 0);
		}
		if (!(pieceMoteur == null)) { //Dessine le moteur � droite
			hauteurImageCourante = pieceMoteur.getHauteur()*ratioImagePiece;
			pieceMoteur.dessinerPixel(g2d, (int) positionXImage, (int) (tableauCoordYImages[2]-hauteurImageCourante/2), 
					(int) pieceMoteur.getLargeur()*ratioImagePiece, (int) pieceMoteur.getHauteur()*ratioImagePiece, 0, 0, 0);
		}

		if(pieceSurvole) { //Dessiner le ballon survolant les pi�ces
			if (indexZoneSurvole > 0) {
				if (indexZoneSurvole < 4) {
					ballon.dessinerBallon(g2d, (int) pointX, (int) (pointY - hauteurBallonCapsule), longueurBallon, hauteurBallonCapsule);
				} else {
					if (indexZoneSurvole < 7) {
						ballon.dessinerBallon(g2d, (int) pointX, (int) (pointY - hauteurBallonReservoir), longueurBallon, hauteurBallonReservoir);	
					} else {
						ballon.dessinerBallon(g2d, (int) pointX, (int) (pointY - hauteurBallonMoteur), longueurBallon, hauteurBallonMoteur);	
					}
				}	
			}	
		}
	}

	/**
	 * M�thode cr�ant un rectangle (Shape) fant�me 
	 * @param positionX La position (pixels) en x du rectangle
	 * @param positionY La position (pixels) en y du rectangle
	 * @return Le rectangle fant�me
	 */
	private Shape creerZoneSelectionnable (double positionX, double positionY) {
		Shape rectangleFantome = new Rectangle2D.Double(positionX, positionY, largeurCarre, hauteurCarre);
		return rectangleFantome;
	}

	/**
	 * M�thode pour cr�er les r�gions d�tectables dans la partie droite de l'assemblage
	 */
	private void creerZonesDroites() {
		zoneDroiteSup = new Rectangle2D.Double(tiersEnX, 0, 2*tiersEnX, tiersEnY);
		zoneDroiteCentre = new Rectangle2D.Double(tiersEnX, tiersEnY, 2*tiersEnX, tiersEnY);
		zoneDroiteInf = new Rectangle2D.Double(tiersEnX, 2*tiersEnY, 2*tiersEnX, tiersEnY);

	}

	/**
	 * M�thode pour v�rifier si une des pi�ces � droite est cliqu�e
	 * @param point Les coordonn�es d'un clic droit de la souris sous la forme d'un point
	 * @return Vrai si une des grosses pi�ces est cliqu�e, faux dans le cas contraire
	 */
	private boolean verifierZoneDroiteSelectionne(Point point) {
		if (zoneDroiteSup.contains(point)) {
			indiceZoneDroite = 1;
			return true;
		}

		if (zoneDroiteCentre.contains(point)) {
			indiceZoneDroite = 2;
			return true;
		}

		if (zoneDroiteInf.contains(point)) {
			indiceZoneDroite = 3;
			return true;
		}
		return false;
	}

	/**
	 * M�thode pour v�rifier si une case d'une pi�ce a �t� s�lectionn�e
	 * @param point Les coordonn�es d'un clic gauche de la souris sous la forme d'un point
	 * @return Vrai si une case a �t� s�lectionn�e, faux dans le cas contraire
	 */
	private boolean verifierBonneCaseSelectionneEtSurvol (Point point) {
		if (listeCarreSelectionnable.get(0).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 1;
			}
			indexZoneSurvole = 1;
			ballon = new BallonCapsule((Capsule) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonCapsule);
			return true;
		}

		if (listeCarreSelectionnable.get(1).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 2;
			}
			indexZoneSurvole = 2;
			ballon = new BallonCapsule((Capsule) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonCapsule);
			return true;
		}

		if (listeCarreSelectionnable.get(2).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 3;
			}
			indexZoneSurvole = 3;
			ballon = new BallonCapsule((Capsule) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonCapsule);
			return true;
		}

		if (listeCarreSelectionnable.get(3).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 4;
			}
			indexZoneSurvole = 4;
			ballon = new BallonReservoir((Reservoir) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonReservoir);
			return true;
		}

		if (listeCarreSelectionnable.get(4).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 5;
			}
			indexZoneSurvole = 5;
			ballon = new BallonReservoir((Reservoir) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonReservoir);
			return true;
		}

		if (listeCarreSelectionnable.get(5).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 6;
			}
			indexZoneSurvole = 6;
			ballon = new BallonReservoir((Reservoir) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonReservoir);
			return true;
		}

		if (listeCarreSelectionnable.get(6).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 7;
			}
			indexZoneSurvole = 7;
			ballon = new BallonMoteur((Moteur) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonMoteur);
			return true;
		}

		if (listeCarreSelectionnable.get(7).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 8;
			}
			indexZoneSurvole = 8;
			ballon = new BallonMoteur((Moteur) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonMoteur);
			return true;
		}

		if (listeCarreSelectionnable.get(8).contains(point)) {
			if (pieceClique) {
				indexZoneSelectionne = 9;
			}
			indexZoneSurvole = 9;
			ballon = new BallonMoteur((Moteur) tableauPieces[indexZoneSurvole-1], longueurBallon, hauteurBallonMoteur);
			return true;
		}

		return false;
	}

	/**
	 * M�thode qui g�re le clic gauche
	 * @param point Le point associ� au clic gauche
	 */
	private void clicGauche(Point point) {
		pieceClique = true;
		if (verifierBonneCaseSelectionneEtSurvol(point)) {
			pieceCourante = tableauPieces[indexZoneSelectionne-1]; 
			leveEvenSelecPieces(indexZoneSelectionne-1);	
			dessinerImagePiece();
		}
		pieceClique = false;
	}

	/**
	 * M�thode qui g�re le clic droit
	 * @param point Le point associ� au clic droit
	 */
	private void clicDroite(Point point) {
		if (verifierZoneDroiteSelectionne(point)) {
			if (indiceZoneDroite == 1) {
				pieceCapsule = null;
			}
			if (indiceZoneDroite == 2) {
				pieceReservoir = null;
			} 
			if (indiceZoneDroite == 3) {
				pieceMoteur = null;	
			}
			leveEvenEnlevPieces(indiceZoneDroite);
			repaint();
		}
	}

	/**
	 * M�thode qui g�re les cons�quences du d�placement de la souris
	 * @param point Le point associ� au curseur de la souris
	 */
	private void sourisBouge(Point point) {
		if (verifierBonneCaseSelectionneEtSurvol(point)) {
			pointX = point.getX();
			pointY = point.getY();
			pieceSurvole = true;
		} else {
			pieceSurvole = false;
		}
		repaint();			
	}

	/**
	 * M�thode pour enregistrer un objet comme �couteur
	 * @param obj Un �l�ment de la classe d'�couteurs personnalis�s
	 */
	public void addSelectionPiecesListenerListener (SelectionPiecesListener obj) {
		OBJETS_ENREGISTRES.add(SelectionPiecesListener.class, obj);
	}

	/**
	 * M�thode permettant de faire une lev�e d'�v�nement pour signaler la s�lection d'une pi�ce
	 * @param n L'index de la pi�ce dans le tableau de pi�ces
	 */
	private void leveEvenSelecPieces(int n) {
		for (SelectionPiecesListener ecout: OBJETS_ENREGISTRES.getListeners(SelectionPiecesListener.class)) {
			ecout.envoyerPieces(n);			
		}
	}

	/**
	 * M�thode permettant de faire une lev�e d'�v�nement pour signaler la d�selection d'une pi�ce
	 * @param n L'indice de la pi�ce repr�sentant son type
	 */
	private void leveEvenEnlevPieces(int n) {
		for (SelectionPiecesListener ecout: OBJETS_ENREGISTRES.getListeners(SelectionPiecesListener.class)) {
			ecout.enleverPieces(n);
		}
	}

	/**
	 * M�thode pour dessiner l'image de la pi�ce s�lectionn�e dans la section droite de la zone de dessin
	 */
	private void dessinerImagePiece() {
		sectionFusee = pieceCourante.verifierTypePiece();
		largeurImageCourante = pieceCourante.getLargeur()*ratioImagePiece;
		hauteurImageCourante = pieceCourante.getHauteur()*ratioImagePiece;
		if(sectionFusee == 1) {
			pieceCapsule = pieceCourante;
		} else {
			if(sectionFusee == 2) {
				pieceReservoir = pieceCourante;
			} else {
				pieceMoteur = pieceCourante;
			}
		}
		positionXImage = 2*tiersEnX - (largeurImageCourante/2); 
		repaint();
	}

	/**
	 * M�thode qui initialise l'image de chaque pi�ce de la fus�e dans le tableau d�sign�
	 */
	private void initialiserImagesCarres() {
		for (int k = 0; k < 9; k++) {
			tableauImagesCarres[k] = tableauPieces[k].getImageCarre(); 
		}
	}

	/**
	 * M�thode qui r�initialise les choix des pi�ces dans la phase de l'assemblage
	 */
	public void reinitialiserValeursAssemblage() {
		pieceCourante = null;
		pieceCapsule = null;
		pieceMoteur = null;
		pieceReservoir = null;
	}

}