package aaplication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import fenetresimulateur.FenetreSimulateur;
/**
 * Classe qui cr�e le menu de l'application
 * @author Thomas Corbeil
 * @author James Huynh
 *
 */
public class App25SimulateurFusee extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 140085098076651983L;
	private FenetreSimulateur fenetreSimulateur;
	private JPanel contentPane;
	private static App25SimulateurFusee frame = null;
	private ImageIcon imageIconLancement;
	private final String NOM_IMAGE_FOND = "/stationLancement.png";
	private JLabel lblImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new App25SimulateurFusee();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App25SimulateurFusee() {
		setTitle("La science des fus\u00E9es pour les nuls");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitre = new JLabel("La science des fus\u00E9es pour les nuls");
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setForeground(new Color(0, 0, 0));
		lblTitre.setFont(new Font("Agency FB", Font.BOLD, 40));
		lblTitre.setBounds(299, 200, 586, 38);
		contentPane.add(lblTitre);

		JButton btnDemarrer = new JButton("D\u00E9marrer");
		btnDemarrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procederProchainFenetre();
			}
		});
		btnDemarrer.setFont(new Font("Agency FB", Font.PLAIN, 25));
		btnDemarrer.setBounds(497, 450, 190, 32);
		contentPane.add(btnDemarrer);

		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Debut
				System.exit(0);
				//Fin
			}
		});
		btnQuitter.setFont(new Font("Agency FB", Font.PLAIN, 25));
		btnQuitter.setBounds(497, 565, 190, 32);
		contentPane.add(btnQuitter);

		initialiserImage();
	}

	//James
	/**
	 * M�thode qui fait passer l'application � la fen�tre principale
	 */
	private void procederProchainFenetre() {
		frame.setVisible(false);
		fenetreSimulateur = new FenetreSimulateur(this);
		fenetreSimulateur.setVisible(true);
		fenetreSimulateur.setLocationRelativeTo(null);
	}

	//James
	/**
	 * M�thode qui initialise et qui int�gre l'image dans la fen�tre principale
	 */
	private void initialiserImage() {
		Image imageLancement = null;
		URL urlImageLancement = App25SimulateurFusee.class.getResource(NOM_IMAGE_FOND);
		if(urlImageLancement == null) {
			System.out.println("L'image " + NOM_IMAGE_FOND + " est introuvable");
		}
		try {
			imageLancement = ImageIO.read(urlImageLancement);
		} catch (IOException e) {
			System.out.println("Il est impossible de lire le fichier" + NOM_IMAGE_FOND);
		}
		imageLancement = imageLancement.getScaledInstance(1200, 800, Image.SCALE_DEFAULT);
		imageIconLancement = new ImageIcon(imageLancement);
		lblImage = new JLabel(imageIconLancement);
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBounds(0, 0, 1200, 800);
		contentPane.add(lblImage);
	}
}
