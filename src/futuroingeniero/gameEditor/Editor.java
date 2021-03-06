package futuroingeniero.gameEditor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import futuroingeniero.engineMain.MainGameLoop;

import java.awt.Panel;

public class Editor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static Canvas miCanvasOpenGL;
	private Panel panel;
	private JButton btnNewButton;

	/**
	 * @author Daniel Loza
	 * Interfaz del editor del juego Cantu�a
	 */

	/**
	 * M�todo Main del Editor
	 * @param args Argumento para el Editor
	 */
	public static void main(String[] args) {
		start();
		new MainGameLoop().run();
		// se debe crear un hilo separado para ejecutar el juego y la interfaz gr�fica al mismo tiempo
		// en un futuro me plantear� hacer el Thread por separado
	}
	
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor frame = new Editor();
					frame.setSize(1280, 720);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
					frame.setResizable(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// ---------- EDIT --------- //
	
	/*
	if(punto2D != null) {
		if(Mouse.isButtonDown(0)){
			GuiTexture temp = Maths.getElementoGuiCirculo(punto2D, guis);
			if(temp != null){
				temp.setPosition(punto2D);	
			}
		}
	}
	
	*/

	/**
	 * Create the frame.
	 */
	public Editor() {
		setTitle("Editor Cantu\u00F1a");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1129, 822);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panel = new Panel();
		contentPane.add(panel);
		panel.setLayout(null);
		
		btnNewButton = new JButton("New button");
		btnNewButton.setBounds(618, 5, 89, 23);
		panel.add(btnNewButton);
		
		miCanvasOpenGL = new Canvas();
		miCanvasOpenGL.setLocation(22, 33);
		panel.add(miCanvasOpenGL);
		miCanvasOpenGL.setSize(new Dimension(1280, 720));
		//miCanvasOpenGL.setMaximumSize(new Dimension(1280, 720));
		System.out.println("ancho " + miCanvasOpenGL.getWidth() + " Alto " + miCanvasOpenGL.getHeight());
		
		// ejemplo de c�mo realizar cambios en tiempo real al juego de Cantu�a
		JButton btnTest = new JButton("Saltar");
		btnTest.setBounds(481, 5, 89, 23);
		panel.add(btnTest);
		
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainGameLoop.crearEntidad();
			}
		});
	}
}
