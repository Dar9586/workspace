import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

public class ZChat{
	private JFrame frame;
	private JPanel panel;
	private JPanel panel_1;
	private JTextField txtNick;
	private JTextField textPath;
	private JButton btnOpen;
	private JTextArea textMsg;
	private JButton btn;
	public JScrollPane scrollPane;
	private JFileChooser fc;
	public String path="C:\\Users\\stopp\\Desktop\\GIOCHI\\test.txt";
	//public String path="C:\\Users\\atopp\\Desktop\\Test.txt";
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
	public ReadAsync3 reader;
	public List<JLabel> connectedUser=new ArrayList<JLabel>();
	public JPanel panel_6;
	JTable textArea;
	private JPanel panel_7;
	public static void main(String[] args) {
		ZChat window = new ZChat();
		window.frame.setVisible(true);
	}
	public void finish() {
			removePlayer();
			frame.removeAll();
			frame=null;
			isDisposed=true;
			reader.isAlive=false;
			try {
				finalize();
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.exit(0);
				
	}
	public String getUser(String s){
	return s.substring(8, s.indexOf('§', 4));
}
	
	public ZChat() {
		initialize();
	}

	private void changeSize(Boolean what,Boolean inc){
		
	}
	private void Connect(){
		panel.setVisible(false);
		textMsg.setEnabled(true);
		path=textPath.getText();
		isConnected=true;
		btn.setText("Invia");
		addPlayer();
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { finish(); }
		});
		reader=new ReadAsync3(this);//Inizia a controllare se il file viene modificato
		reader.start();
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
			if(lines.size()==0){lines.add("");lines.add("");}
			lines.set(0,"█§"+mineColors+txtNick.getText()+lines.get(0));
			lines.set(lines.size()-1, lines.get(lines.size()-1)+"█j§"+mineColors+txtNick.getText()+"§000000 si e' unito alla chat.");
			Files.write(Paths.get(path), lines, StandardCharsets.UTF_8);
			rewriteLabels();
		}
		catch(IOException e){}
	}
	private void removePlayer(){
		try{
			List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
			String h[]=lines.get(0).split("█");
			for(int a=0;a<h.length;a++){
				if(h[a].contains(mineColors+txtNick.getText())){h[a]="";}
				else{h[a]+="█";}
			}
			String f="";
			for(int a=0;a<h.length;a++)f+=h[a];
			lines.set(0,f);
			lines.set(lines.size()-1, lines.get(lines.size()-1)+"█q§"+mineColors+txtNick.getText()+"§000000 ha lasciato la chat.");
			Files.write(Paths.get(path), lines, StandardCharsets.UTF_8);
		}
		catch(IOException e){}
	}
	public void rewriteLabels(){
		panel_6.removeAll();
		while(connectedUser.size()!=0){
			connectedUser.set(0,null);
			connectedUser.remove(0);
		}
		try{
			List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
			List<String>hh=new ArrayList<String>();
			
			String h[]=lines.get(0).split("█");
			for(int a=0;a<h.length;a++)if(h[a].length()>7)hh.add(h[a]);
			int maxSize=0;
			for(int a=0;a<hh.size();a++){

					connectedUser.add(new JLabel(hh.get(a).substring(7)));
					connectedUser.get(a).setForeground(Message.decodeColor(hh.get(a).substring(1, 7)));
					panel_6.add(connectedUser.get(a));
					int y=SwingUtilities.computeStringWidth(textArea.getFontMetrics(textArea.getFont()),connectedUser.get(a).getText())+20;
					if(y>maxSize)maxSize=y;
			}
			maxSize+=10;
			textArea.getColumnModel().getColumn(0).setMinWidth(maxSize);
			textArea.getColumnModel().getColumn(0).setPreferredWidth(maxSize);
			textArea.getColumnModel().getColumn(0).setMaxWidth(maxSize);
			textArea.getColumnModel().getColumn(0).setWidth(maxSize);
			frame.setSize(frame.getSize().width+1, frame.getSize().height);
			frame.setSize(frame.getSize().width-1, frame.getSize().height);
			}
		catch(IOException e){e.printStackTrace();}
		
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

	
	public void updateRowHeights()
	{
		int len=textArea.getColumnModel().getColumn(1).getWidth();
		int siz=textArea.getFont().getSize()+15;
	    for (int a = 0; a < textArea.getRowCount(); a++)
	    {
	        int rowHeight = SwingUtilities.computeStringWidth(textArea.getFontMetrics(textArea.getFont()),((String)textArea.getValueAt(a, 1)).replaceAll("\\<.*?>","") )+15;
	        textArea.setRowHeight(a, (rowHeight/len+1)*siz);
	    }
	}
	
	
	
	private void initialize() {
		frame=new JFrame();
		//Inizializza componenti
		fc = new JFileChooser();
		//frame = new JPanel();
		panel = new JPanel();
		txtNick = new JTextField();
		textPath = new JTextField();
		panel_1 = new JPanel();
		textPath = new JTextField();
		jColor=new JColorChooser();
		//Grafica
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 400);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		panel_7 = new JPanel();
		frame.getContentPane().add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(new BorderLayout(0, 0));
		scrollPane = new JScrollPane();
		panel_7.add(scrollPane);
		
		textArea = new JTable();
		textArea.setShowVerticalLines(false);
		textArea.setFillsViewportHeight(true);
		textArea.setModel(new DefaultTableModel(
			new Object[][] {
				
			},
			new String[] {
				"User", "Text"
			}
		));
		
		scrollPane.setViewportView(textArea);
		
		panel_6 = new JPanel();
		panel_7.add(panel_6, BorderLayout.NORTH);
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
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
		
		frame.addComponentListener(new ComponentListener() {
			@Override
		    public void componentResized(ComponentEvent e) {
				updateRowHeights();

		    }

		    @Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
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
		textPath.setToolTipText("Percorso");
		textPath.setColumns(10);

		textArea.getColumn("Text").setCellRenderer(new TextAreaRenderer2());
		textArea.getColumn("Text").setCellEditor(new TextAreaEditor2());
		textArea.getColumn("User").setCellRenderer(new TextAreaRenderer2());
		textArea.getColumn("User").setCellEditor(new TextAreaEditor2());
		
		
		
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
		
		btnX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				while(textArea.getRowCount()!=0){
					((DefaultTableModel)textArea.getModel()).removeRow(0);
				}
			}
		});
		
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
	    changeSize(false,true);
		
		
		
	}


}

class ReadAsync3 extends Thread { 
	/**/
	public Boolean isAlive=true;
	DefaultTableModel model;
	ZChat chat;
	public ReadAsync3(ZChat x){
		chat=x;
		model = (DefaultTableModel) chat.textArea.getModel();
	}
	/**Questo metodo viene eseguito alla chiamata della classe*/
	public void run(){ 
	  	//String path=chat.path;//percorso alla Chat
		File file = new File(chat.path);//file in quel percorso
		long last=file.length();//lunghezza del file
		ReadFunc(last);
		last=file.length();
		while(isAlive){
				if(file.length()>last){//se la dimensione del file viene modificata
					System.out.println("LEN1: "+file.length());
					ReadFunc(last);//scrivi l 'ultima riga
					System.out.println("LEN2: "+file.length());
					last=file.length();//modifica l' ultima lunghezza del file
				}
				
		}
		System.exit(0);
   }
	
	public void ReadFunc(long last){
			List<String> lins;
		try {

			lins = Files.readAllLines(Paths.get(chat.path),Charset.forName("UTF-8"));
			String fin="";
			
			StringBuilder sb = new StringBuilder();
			for(int a=1;a<lins.size();a++){
				sb.append('\n'+lins.get(a));
			}
			fin=sb.toString();
			System.out.println(fin);
			fin=fin.substring(fin.lastIndexOf('█')+1);
			
			Message x=new Message(fin);
			if(x.getType()!=Message.MsgType.Error){
			if(x.getType()==Message.MsgType.Join||x.getType()==Message.MsgType.Quit){
				chat.rewriteLabels();
			}
			model.addRow(new Object[]{x.getUserHTML(),x.getMessHTML()});
			chat.textArea.setRowHeight(chat.textArea.getRowCount()-1,(chat.textArea.getFont().getSize()+10)*x.getMessHTML().split("<br>").length );
			Thread.sleep(150);
			chat.scrollPane.getVerticalScrollBar().setValue(chat.scrollPane.getVerticalScrollBar().getMaximum());
		  }else{System.out.println("Errore ottenuto");} }
		catch (IOException|NullPointerException|ArrayIndexOutOfBoundsException|StringIndexOutOfBoundsException|InterruptedException e) {e.printStackTrace();}
		}
}
class Tuple<X,Y>{
	private X var1;
	private Y var2;
	  public Tuple(X x,Y y) {  
	    var1 = x;
	    var2 = y;   
	  }
	  public X getVar1() {
	    return var1;
	  }
	  public Y getVar2() {
	    return var2;
	  }
	  public void setVar1(X x) {
		    var1=x;
	  }
	  public void setVar2(Y y) {
		  var2=y;
	  }
	  public String toString() { 
	    return "(" + var1 + ", " + var2 + ")";  
	  }
}
class Message{
	public static enum MsgType{Error,Message,Join,Quit;}
	private MsgType type;
	private String htmlmess;
	private String htmluser;
	List<Tuple<Integer,Color>> fmsg=new ArrayList<Tuple<Integer,Color>>();
	public Message(String msg){
		System.out.println("MSG: "+msg);
		try{type=getType(msg.charAt(0));}
		catch(StringIndexOutOfBoundsException e){type=MsgType.Error;return;}
		generateHTML(msg);
	}
	public MsgType getType()   {return type;}
	public String getMessHTML()		{return htmlmess;}
	public String getUserHTML()		{return htmluser;}
	List<Tuple<Integer,Color>> getMap(){return fmsg;}
	MsgType getType(char x){
		System.out.println("CHAR 0: "+x);
		switch(x){
		case 'm':return MsgType.Message;
		case 'j':return MsgType.Join;
		case 'q':return MsgType.Quit;
		}
		return MsgType.Error;
	}

	public static String getUser(String msg){
		return msg.substring(8, msg.indexOf('§', 4));
	}
	void generateHTML(String msg){
		Boolean first=true;
		htmluser="<html><font color=\"#"+msg.substring(2,8)+"\">"+getUser(msg)+"</font></html>";
		String mess=msg.substring(msg.indexOf(':')+2);
		StringBuilder s=new StringBuilder("<html><div style=\"height:100%\">");
		for(int a=0;a<mess.length();a++){
			if(mess.charAt(a)=='§'){
				try{
				s.append((first?"":"</font>")+"<font color=\"#"+mess.substring(a+1,a+7)+"\">");
				a+=6;
				first=false;
				}catch(NumberFormatException e){s.append(mess.charAt(a));}
			}
			else
				s.append(mess.charAt(a));
		}
		s.append("</font></div></html>");
		htmlmess=s.toString();
		htmlmess=htmlmess.replaceAll("\n", "<br>");
	}
	public static Color decodeColor(String s){
		return Color.decode(Integer.toString(Integer.parseInt(s, 16)));
	}
}
class TextAreaRenderer2 extends JTextPane implements TableCellRenderer{
	private static final long serialVersionUID = 1L;
  
   public TextAreaRenderer2() {
	   setContentType("text/html");
   }

@Override
public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus,int row, int column){
	if (isSelected) {
        setForeground(table.getSelectionForeground());
        setBackground(table.getSelectionBackground());
        setForeground(table.getSelectionForeground());
        setBackground(table.getSelectionBackground());
     } else {
        setForeground(table.getForeground());
        setBackground(table.getBackground());
        setForeground(table.getForeground());
        setBackground(table.getBackground());
     }
	
	
	setText((String) value);
	return this;
}

}
class TextAreaEditor2 extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
   protected JTextPane textarea;
   protected String mess;
   public TextAreaEditor2() {
      super(new JCheckBox());
      textarea = new JTextPane(); 
      textarea.setContentType("text/html");
      textarea.setEditable(false);
   }
  
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	   mess=(String) value;
	   textarea.setText(mess);
	   return textarea;
   }
   public Object getCellEditorValue() {
	      return mess;
	   }
}

