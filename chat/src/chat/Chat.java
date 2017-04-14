package chat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JTextArea;

class Chat {
	//componenti della finestra
	private JFrame frame;
	private static JPanel panel;
	private static JPanel panel_1;
	private static JTextField txtNick;
	private static JTextField textPath;
	private static JButton btnOpen;
	public static JTextArea textMsg;
	public static JButton btn;
	private static JScrollPane scrollPane;
	public  static JTextPane textArea;
	private JFileChooser fc;
	public static String path="C:\\Users\\atopp\\Desktop\\Chat\\test.txt";
	private JPanel panel_2;
	private static JButton btnNewButton;
	private JColorChooser jColor;
	static String mineColors="000000";
	static String mineColors2="000000";
	private JButton btnEmoji;
	private JPanel panel_3;
	private JButton btnX;
	private static Emoji emoj;
	static List<String>players;
	private JPanel panel_4;
	private JButton btnAddcolor;
	
	public static void main(String[] args) {
		emoj=new Emoji();
		players=new ArrayList<String>();
		//ShutdownHook che scriver� che l'utente ha lasciato la chat
		 Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		        public void run() {
		        	if(btnOpen.getText()!="Connetti"){
		        		SendMessage("q§"+mineColors+txtNick.getText()+"§000000 ha lasciato la chat.\n");
		        	}
		        }
		    }, "Shutdown-thread"));
		
		Chat window = new Chat();//Inizializza la finestra
		
		window.frame.setVisible(true);//Mostra la finestra
				
	}

	public Chat() {
		initialize();
	}

	private void changeSize(Boolean what,Boolean inc){
		
		(what?textMsg:textArea).setFont(
				new Font((what?textMsg:textArea).getFont().getName(),
						 (what?textMsg:textArea).getFont().getStyle(), 
						 (what?textMsg:textArea).getFont().getSize()+(inc?1:-1)));
	}
	private static void Connect(){
		txtNick.setEnabled(false);
		textPath.setEnabled(false);
		textMsg.setEnabled(true);
		btnOpen.setEnabled(false);
		btnNewButton.setEnabled(false);
		path=textPath.getText();
		
		
		
		//Inizia a controllare se il file viene modificato
		SendMessage("e§"+mineColors+txtNick.getText()+"§000000 si e' unito alla chat.\n");
		btn.setText("Invia");
	}
	
	
	private static void SendLogic(){
		if(btn.getText()=="Connetti"){//se deve ancora connettersi
			//controlla se il file esiste e non � una cartella ma un file
			
			if(txtNick.getText().length()>2&&Files.exists(Paths.get(textPath.getText()),LinkOption.NOFOLLOW_LINKS)&&!
	            	Files.isDirectory(Paths.get(textPath.getText()), LinkOption.NOFOLLOW_LINKS)){
				Connect();
			}
		}
		else{
			if(textMsg.getText().length()!=0){
				//altrimenti invia il messaggio
				SendMessage("m§"+mineColors+txtNick.getText()+"§000000: "+textMsg.getText()+"\n");
			}
		}
	}
	
	/**
	 * Metodo per Inviare il messaggio*/
	private static void SendMessage(String s){
		//aggiunge il messaggio scritto al file della chat
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path,true),Charset.forName("UTF-8").newEncoder()));
			out.append(s);
			out.close();
		} 
		catch (IOException e) {}
		textMsg.setText("");//pulisce la textbox
	}

	private void initialize() {
		//Inizializza componenti
		fc = new JFileChooser();
		frame = new JFrame();
		panel = new JPanel();
		txtNick = new JTextField();
		textPath = new JTextField();
		panel_1 = new JPanel();
		textMsg = new JTextArea();
		scrollPane = new JScrollPane();
		textArea = new JTextPane();
		textPath = new JTextField();
		jColor=new JColorChooser();
		
		//Grafica
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(txtNick, BorderLayout.WEST);
		panel.add(textPath, BorderLayout.CENTER);
		
		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new BorderLayout(0, 0));
		btnOpen = new JButton("Cerca");
		panel_2.add(btnOpen, BorderLayout.WEST);
		
		btnNewButton = new JButton("Col");
		
		panel_2.add(btnNewButton, BorderLayout.EAST);
		
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(textMsg, BorderLayout.CENTER);
		
		scrollPane.setViewportView(textArea);
		
		txtNick.setToolTipText("Nick");
		txtNick.setColumns(10);
		
		textMsg.setEnabled(false);
		textMsg.setColumns(10);
		
		panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		panel_3.setLayout(new BorderLayout(0, 0));
		btn = new JButton("Connetti");
		panel_3.add(btn);
		
		btnX = new JButton("X");
		panel_3.add(btnX, BorderLayout.EAST);
		
		panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.WEST);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		btnEmoji = new JButton("\uD83D\uDE00");
		
		btnAddcolor = new JButton("C");
		panel_4.add(btnAddcolor, BorderLayout.EAST);
		
		btnEmoji.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				emoj.frame.setVisible(!emoj.frame.isVisible());
				
			}
		});
		//Fine Grafica
		
		//ActionListener eseguito quando viene premuto il pulsante Invia o Connetti
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendLogic();
			}
		});
		textPath.setText(path);
;		textPath.setToolTipText("Percorso");
		textPath.setColumns(10);
		textArea.setEditable(false);
		
		//ActionListener che mostra l' OpenDialog
				btnOpen.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int returnVal = fc.showOpenDialog(btnOpen);
			            if (returnVal == JFileChooser.APPROVE_OPTION) {
			            	//se viene cliccato OK sostituisce il percorso con quello selezionato
			            	textPath.setText(fc.getSelectedFile().getPath());
			            }
					}
				});
		
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()==2){
					textArea.setBackground(JColorChooser.showDialog(jColor, "", Color.WHITE));
				}
			}
		});
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color mineColor=JColorChooser.showDialog(jColor, "", Color.BLACK);
				String s= Integer.toHexString(mineColor.getRed()*65536+ mineColor.getGreen()*256+mineColor.getBlue());
				while(s.length()<6)s="0"+s;
				if(SwingUtilities.isLeftMouseButton(e)){mineColors=s;}
				else{mineColors2=s;}
			}
		});
		
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==107){changeSize(false,true);}
				if(arg0.getKeyCode()==109){changeSize(false,false);}
			}
		});
		
		btnX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea.setText("");
			}
		});
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
}