package chat;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.LinkOption;
import java.nio.file.StandardOpenOption;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Chat {
	//componenti della finestra
	private JFrame frame;
	private static JPanel panel;
	private static JPanel panel_1;
	private static JTextField txtNick;
	private static JTextField textPath;
	private static JButton btnOpen;
	private static JTextField textMsg;
	private static JButton btn;
	private static JScrollPane scrollPane;
	public  static JTextArea textArea;
	private JFileChooser fc;
	public static String path="";
	
	public static void main(String[] args) {
		//ShutdownHook che scriverà che l'utente ha lasciato la chat
		 Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		        public void run() {
		        	if(btnOpen.getText()!="Connetti"){
		        		SendMessage(txtNick.getText()+" ha lasciato la chat.\n");
		        	}
		        }
		    }, "Shutdown-thread"));
		
		Chat window = new Chat();//Inizializza la finestra
		window.frame.setVisible(true);//Mostra la finestra
				
	}

	public Chat() {
		initialize();
	}

	
	private static void SendLogic(){
		if(btn.getText()=="Connetti"){//se deve ancora connettersi
			//controlla se il file esiste e non è una cartella ma un file
			
			if(txtNick.getText()!=""&&Files.exists(Paths.get(textPath.getText()),LinkOption.NOFOLLOW_LINKS)&&!
	            	Files.isDirectory(Paths.get(textPath.getText()), LinkOption.NOFOLLOW_LINKS)){
				txtNick.setEnabled(false);
				textPath.setEnabled(false);
				textMsg.setEnabled(true);
				path=textPath.getText();
				new ReadAsync().start();//Inizia a controllare se il file viene modificato
				SendMessage(txtNick.getText()+" si e' unito alla chat.\n");
				btn.setText("Invia");
			}
		}
		else{
			if(textMsg.getText().length()!=0){
				//altrimenti invia il messaggio
				SendMessage(txtNick.getText()+": "+textMsg.getText()+"\n");
			}
		}
	}
	
	/**
	 * Metodo per Inviare il messaggio*/
	private static void SendMessage(String s){
		//aggiunge il messaggio scritto al file della chat
		try {Files.write(Paths.get(path), s.getBytes(), StandardOpenOption.APPEND);}
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
		btnOpen = new JButton("Cerca");
		panel_1 = new JPanel();
		textMsg = new JTextField();
		btn = new JButton("Connetti");
		scrollPane = new JScrollPane();
		textArea = new JTextArea();
		textPath = new JTextField();
		
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
		panel.add(btnOpen, BorderLayout.EAST);

		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(textMsg, BorderLayout.CENTER);
		panel_1.add(btn, BorderLayout.EAST);
		
		scrollPane.setViewportView(textArea);
		
		txtNick.setToolTipText("Nick");
		txtNick.setColumns(10);
		
		textMsg.setEnabled(false);
		textMsg.setColumns(10);
		
		textPath.setToolTipText("Percorso");
		textPath.setColumns(10);
		
		textArea.setTabSize(4);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		//Fine Grafica
		
		//ActionListener eseguito quando viene premuto il pulsante Invia o Connetti
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendLogic();
			}
		});
		
		//ActionListener eseguito quando viene premuto il pulsante su tastiera Spazio
		textMsg.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==10){SendLogic();}
			}
		});
		
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
	}
}


/**
 * <p>
 * Classe asincrona che controlla se il file viene modificato e riporta l'ultima 
 * riga del file
 * <p>  
 * 
 * */
class ReadAsync extends Thread { 
	/**Questo metodo viene eseguito alla chiamata della classe*/
	public void run(){ 
	  	String path=Chat.path;//percorso alla chat
		File file = new File(path);//file in quel percorso
		long last=file.length();//lunghezza del file
		while(true){
				if(file.length()!=last){//se la dimensione del file viene modificata
					ReadFunc();//scrivi l 'ultima riga
					last=file.length();//modifica l' ultima lunghezza del file
				}
		}
   }
  
  public static void ReadFunc(){
		List<String> lins;//lista contenente le linee del file
		try {
			lins = Files.readAllLines(Paths.get(Chat.path));//ottiene tutte le linee del file
			Chat.textArea.append(lins.get(lins.size()-1)+"\n");//e scrive l'ultima
		} 
		catch (IOException e) {}
		catch(IndexOutOfBoundsException e){}
  }
}