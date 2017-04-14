import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.JTabbedPane;

public class Chat{
	private JFrame frame;
	public JTabbedPane tabbedPane;
	public List<XChat> chats = new ArrayList<XChat>();
	public List<JPanel> panels = new ArrayList<JPanel>();
	CheckDispose check;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	        		
	        }
	    }, "Shutdown-thread"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat window = new Chat();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Chat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		panels.add(new JPanel());
		chats.add(new XChat(panels.get(0)));
		tabbedPane.addTab("Chat",panels.get(0));
		
		panels.add(new JPanel());
		
		tabbedPane.addTab("+",panels.get(panels.size()-1));
		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	            if(tabbedPane.getSelectedIndex()==panels.size()-1){
	            	if(chats.get(chats.size()-1).isConnected){
	            	chats.add(new XChat(panels.get(panels.size()-1)));
	            	tabbedPane.setTitleAt(panels.size()-1, "Chat");
	            	panels.add(new JPanel());
	            	tabbedPane.addTab("+",panels.get(panels.size()-1));
	            	}
	            	else{tabbedPane.setSelectedIndex(panels.size()-2);}
	            }
	        }
	    });
		check=new CheckDispose(this);
		check.start();
	}
	public void removeChat(int a){
		chats.remove(a);
		tabbedPane.remove(a);
		panels.remove(a);
	}
	public void addChat(){
		chats.add(new XChat(panels.get(panels.size()-1)));
    	tabbedPane.setTitleAt(panels.size()-1, "Chat");
    	panels.add(new JPanel());
    	tabbedPane.addTab("+",panels.get(panels.size()-1));
	}
}

class CheckDispose extends Thread { 
	Chat chat;
	public Boolean isAlive=true;
	public CheckDispose(Chat x){
		chat=x;
	}
	public void run(){ 
		int a;
		while(isAlive){
			for(a=0;a<chat.chats.size();a++){
				if(chat.chats.get(a).isDisposed){
					chat.removeChat(a);
					if(chat.chats.size()==0)
						chat.addChat();
				}
			}
		}
   }
}






class XChat{

	private JPanel frame;
	private JPanel panel;
	private JPanel panel_1;
	private JTextField txtNick;
	private JTextField textPath;
	private JButton btnOpen;
	private JTextArea textMsg;
	private JButton btn;
	private JScrollPane scrollPane;
	public  JTextPane textArea;
	private JFileChooser fc;
	public String path="C:\\Users\\atopp\\Desktop\\Test.txt";
	private JPanel panel_2;
	private JButton btnNewButton;
	private JColorChooser jColor;
	private String mineColors="000000";
	private JPanel panel_3;
	private JButton btnX;
	private JPanel panel_4;
	private JButton btnAddcolor;
	private JScrollPane scrollPane_1;
	private JPanel panel_5;
	private JTable table;
	private JButton btnEmoji;
	public Boolean isConnected=false;
	public Boolean isDisposed=false;
	public ReadAsync reader;
	public void finish() {
		//ShutdownHook che scriver� che l'utente ha lasciato la chat
		if(btn.getText()=="Invia"){
			SendMessage("█q§"+mineColors+txtNick.getText()+"§000000 ha lasciato la chat.");
			//Component comp = SwingUtilities.getRoot(frame);
			removePlayer();
			frame.removeAll();
			frame=null;
			isDisposed=true;
			reader.isAlive=false;
		}
		
				
	}

	public String getUser(String s){
	return s.substring(8, s.indexOf('§', 4));
}
	
	public XChat(JPanel sup) {
		frame=sup;
		initialize();
	}

	private void changeSize(Boolean what,Boolean inc){
		
		(what?textMsg:textArea).setFont(
				new Font((what?textMsg:textArea).getFont().getName(),
						 (what?textMsg:textArea).getFont().getStyle(), 
						 (what?textMsg:textArea).getFont().getSize()+(inc?1:-1)));
	}
	private void Connect(){
		reader=new ReadAsync(this);//Inizia a controllare se il file viene modificato
		reader.start();
		/*txtNick.setEnabled(false);
		textPath.setEnabled(false);
		btnOpen.setEnabled(false);
		btnNewButton.setEnabled(false);*/
		panel.setVisible(false);
		textMsg.setEnabled(true);
		path=textPath.getText();
		isConnected=true;
		SendMessage("█e§"+mineColors+txtNick.getText()+"§000000 si e' unito alla chat.");
		btn.setText("Invia");
		
	}
	
	
	private void SendLogic(){
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
				SendMessage("█m§"+mineColors+txtNick.getText()+"§000000: "+textMsg.getText());
			}
		}
		textMsg.requestFocus();
		textMsg.setCaretPosition(textMsg.getDocument().getLength());
	}
	
	private void addPlayer(){
		try{
			List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
			if(lines.size()==0)lines.add("");
			lines.set(0,"█§"+mineColors+txtNick.getText()+lines.get(0));
			Files.write(Paths.get(path), lines, StandardCharsets.UTF_8);
		}
		catch(IOException e){}
	}
	private void removePlayer(){
		try{
			List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
			String h[]=lines.get(0).split("█");
			for(int a=0;a<h.length;a++){
				if(h[a].contains(mineColors+txtNick.getText())){h[a]="";break;}
			}
			String f="";
			for(int a=0;a<h.length;a++)if(h[a].length()>0)f+="█"+h[a];
			lines.set(0,f);
			Files.write(Paths.get(path), lines, StandardCharsets.UTF_8);
		}
		catch(IOException e){}
	}
	/**
	 * Metodo per Inviare il messaggio*/
	private void SendMessage(String s){
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
		//frame = new JPanel();
		panel = new JPanel();
		txtNick = new JTextField();
		textPath = new JTextField();
		panel_1 = new JPanel();
		scrollPane = new JScrollPane();
		textArea = new JTextPane();
		textPath = new JTextField();
		jColor=new JColorChooser();
		//Grafica
		
		frame.setBounds(100, 100, 450, 400);
		frame.setLayout(new BorderLayout(0, 0));
		frame.add(panel, BorderLayout.NORTH);
		frame.add(panel_1, BorderLayout.SOUTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		
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
		
		scrollPane.setViewportView(textArea);
		
		txtNick.setToolTipText("Nick");
		txtNick.setColumns(10);
		
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
		
		btnAddcolor = new JButton("🎨");
		panel_4.add(btnAddcolor, BorderLayout.EAST);
		
		btnEmoji = new JButton("\uD83D\uDE01");
		btnEmoji.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				table.setVisible(!table.isVisible());
				textMsg.requestFocus();
				textMsg.setCaretPosition(textMsg.getDocument().getLength());
			}
		});
		panel_4.add(btnEmoji, BorderLayout.WEST);
		
		scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);
		textMsg = new JTextArea();
		textMsg.setLineWrap(true);
		scrollPane_1.setViewportView(textMsg);
		
		textMsg.setEnabled(false);
		textMsg.setColumns(10);
		
		panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.SOUTH);
		panel_5.setLayout(new BorderLayout(0, 0));
		table = new JTable();
		table.setVisible(false);
		panel_5.add(table, BorderLayout.SOUTH);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(btn.getText()=="Invia")
				textMsg.setText(textMsg.getText()+(String)table.getValueAt(table.rowAtPoint(arg0.getPoint()), table.columnAtPoint(arg0.getPoint())));
				
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"\uD83D\uDE01","\uD83D\uDE02","\uD83D\uDE03","\uD83D\uDE04","\uD83D\uDE05","\uD83D\uDE06","\uD83D\uDE09","\uD83D\uDE0A","\uD83D\uDE0B","\uD83D\uDE0E","\uD83D\uDE0D","\uD83D\uDE18","\uD83D\uDE1A","\u263A"},
				{"\uD83D\uDE10","\uD83D\uDE36","\uD83D\uDE0F","\uD83D\uDE23","\uD83D\uDE25","\uD83D\uDE2A","\uD83D\uDE2B","\uD83D\uDE0C","\uD83D\uDE1C","\uD83D\uDE1D","\uD83D\uDE12","\uD83D\uDE13","\uD83D\uDE14","\uD83D\uDE32"},
				{"\u2639","\uD83D\uDE16","\uD83D\uDE1E","\uD83D\uDE22","\uD83D\uDE2D","\uD83D\uDE28","\uD83D\uDE29","\uD83D\uDE30","\uD83D\uDE31","\uD83D\uDE33","\uD83D\uDE35","\uD83D\uDE21","\uD83D\uDE20","\uD83D\uDE07"},
				{"\uD83D\uDE37","\uD83D\uDE08","\u2620","\uD83D\uDE3A","\uD83D\uDE38","\uD83D\uDE39","\uD83D\uDE3B","\uD83D\uDE3C","\uD83D\uDE3D","\uD83D\uDE40","\uD83D\uDE3F","\uD83D\uDE3E","\u200D\u2695","\u200D\u2696"},
				{"\u200D\u2708","\u200D\u2642","\u200D\u2640","\u261D","\u270C","\u270D","\u2764","\u2763","\uD83D\uDC35","\uD83D\uDC31","\uD83D\uDC2E","\uD83D\uDC2D","\u2618","\u2615"}
			},
			new String[] {
				"", "", "", "", "", "", "", "", "", "","","","",""
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false,false,false,false,false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(4).setResizable(false);
		table.getColumnModel().getColumn(5).setResizable(false);
		table.getColumnModel().getColumn(6).setResizable(false);
		table.getColumnModel().getColumn(7).setResizable(false);
		table.getColumnModel().getColumn(8).setResizable(false);
		table.getColumnModel().getColumn(9).setResizable(false);
		table.getColumnModel().getColumn(10).setResizable(false);
		table.getColumnModel().getColumn(11).setResizable(false);
		table.getColumnModel().getColumn(12).setResizable(false);
		table.getColumnModel().getColumn(13).setResizable(false);
		table.setFont(new Font(table.getFont().getName(), table.getFont().getStyle(), 19));//l'ultimo numero e' la grandezza del font
		table.setRowHeight(22);
		btnAddcolor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try{
				Color s=JColorChooser.showDialog(jColor, "Cambia colore testo:", Color.BLACK);
				String c= Integer.toHexString(s.getRed()*65536+ s.getGreen()*256+s.getBlue());
				while(c.length()<6)c="0"+c;
				textMsg.setText(textMsg.getText()+"§"+c);
				}catch(NullPointerException e){}
				textMsg.requestFocus();
				textMsg.setCaretPosition(textMsg.getDocument().getLength());
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
						if(btnOpen.isEnabled()){
						int returnVal = fc.showOpenDialog(btnOpen);
			            if (returnVal == JFileChooser.APPROVE_OPTION) {
			            	//se viene cliccato OK sostituisce il percorso con quello selezionato
			            	textPath.setText(fc.getSelectedFile().getPath());
			            }
					}}
				});
		
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()==2){
					try{
					textArea.setBackground(JColorChooser.showDialog(jColor, "Scegli colore sfondo:", Color.WHITE));
				}catch(NullPointerException e){}
				}
			}
		});
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnNewButton.isEnabled()){
					try{
				Color mineColor=JColorChooser.showDialog(jColor, "Scegli colore utente:", Color.BLACK);
				String s= Integer.toHexString(mineColor.getRed()*65536+ mineColor.getGreen()*256+mineColor.getBlue());
				while(s.length()<6)s="0"+s;
				mineColors=s;
					}catch(NullPointerException e1){}
				}
			}
		});
		
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				System.out.println(arg0.getKeyCode());
				if(arg0.getKeyCode()==27){finish();}
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
		scrollPane_1.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		
		
		
		InputMap input = textMsg.getInputMap();
	    KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
	    KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
	    input.put(shiftEnter, "insert-break");  // input.get(enter)) = "insert-break"
	    input.put(enter, "text-submit");

	    ActionMap actions = textMsg.getActionMap();
	    actions.put("text-submit", new AbstractAction() {

			@Override
	        public void actionPerformed(ActionEvent e) {
	            SendLogic();
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
	StyledDocument doc;
	/*private String getUser(String s){
		int j=s.substring(8, s.indexOf('§', 4)).length()+6;
		String x="\n";
		for(int a=0;a<j;a++){
			x+=' ';
		}
		return x;
	}*/
	public Boolean isAlive=true;
	XChat chat;
	public ReadAsync(XChat x){
		chat=x;
	}
	/**Questo metodo viene eseguito alla chiamata della classe*/
	public void run(){ 
	  	String path=chat.path;//percorso alla Chat
		File file = new File(path);//file in quel percorso
		long last=file.length();//lunghezza del file
		while(isAlive){
				if(file.length()!=last){//se la dimensione del file viene modificata
					ReadFunc(last);//scrivi l 'ultima riga
					last=file.length();//modifica l' ultima lunghezza del file
				}
		}
   }
  
	Color decodeColor(String s){
		return Color.decode(Integer.toString(Integer.parseInt(s, 16)));
	}
	
	public void ReadFunc(long last){
		List<String> lins;
		try {

			lins = Files.readAllLines(Paths.get(chat.path),Charset.forName("UTF-8"));
			String fin="";
			for(int a=0;a<lins.size();a++){
					fin+='\n'+lins.get(a);
			}
			fin=fin.substring(fin.lastIndexOf('█')+1);
			doc = chat.textArea.getStyledDocument();
			StyleContext context = new StyleContext();
			    Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
			    StyleConstants.setSpaceAbove(style, 4);
			    StyleConstants.setSpaceBelow(style, 4);
			    
			Color act;
			for(int a=1;a<fin.length();a++){
				if(fin.charAt(a)=='§'){
					try{
					act=decodeColor(fin.substring(a+1, a+7));
					StyleConstants.setForeground(style,act);
					a+=6;}
					catch(NumberFormatException e){doc.insertString(doc.getLength(), fin.substring(a,a+1), style);}
				}
				else
				doc.insertString(doc.getLength(), fin.substring(a,a+1), style);
			}
			doc.insertString(doc.getLength(), "\n", style);
		} 
		catch (IOException e) {}
		catch (BadLocationException e) {}
		catch(ArrayIndexOutOfBoundsException e){}
		catch(StringIndexOutOfBoundsException e){}
	}
}