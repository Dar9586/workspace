package chat;
<<<<<<< HEAD
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
=======

>>>>>>> origin/master
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
<<<<<<< HEAD
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
=======
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chat {
>>>>>>> origin/master

	private JFrame frame;
	private JPanel panel;
	private JPanel panel_1;
<<<<<<< HEAD
	private static JTextField txtNick;
	private JTextField textField_1;
	private static JButton btnNewButton;
	private static JTextField textField;
=======
	private JTextField txtNick;
	private JTextField textField_1;
	private JButton btnNewButton;
	private JTextField textField;
>>>>>>> origin/master
	private JButton btnInvia;
	private JScrollPane scrollPane;
	public static JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
<<<<<<< HEAD
		 Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		        public void run() {
		        	if(btnNewButton.getText()!="Connetti"){SendMessage(txtNick.getText()+" ha lasciato la chat.\n");}
		            
		        }
		    }, "Shutdown-thread"));
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
	private static void SendMessage(String s){
		try {
			Files.write(Paths.get(path), s.getBytes(), StandardOpenOption.APPEND);
=======
	private void SendMessage(){
		try {
			Files.write(Paths.get(path), (txtNick.getText()+": "+textField.getText()+"\n").getBytes(), StandardOpenOption.APPEND);
>>>>>>> origin/master
		} catch (IOException e) {
		}
		textField.setText("");
	}
	public static String path="";
	
	private void initialize() {
		final JFileChooser fc = new JFileChooser();
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		txtNick = new JTextField();
		txtNick.setToolTipText("Nick");
		panel.add(txtNick, BorderLayout.WEST);
		txtNick.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("Percorso");
		panel.add(textField_1, BorderLayout.CENTER);
		textField_1.setColumns(10);
		btnNewButton = new JButton("Cerca");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(btnNewButton);
				 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	textField_1.setText(fc.getSelectedFile().getPath());
	                //This is where a real application would open the file.
	            }
			}
		});
		panel.add(btnNewButton, BorderLayout.EAST);
		
		panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
<<<<<<< HEAD
				if(arg0.getKeyCode()==10){SendMessage(txtNick.getText()+": "+textField.getText()+"\n");}
=======
				if(arg0.getKeyCode()==10){SendMessage();}
>>>>>>> origin/master
			}
		});
		panel_1.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		btnInvia = new JButton("Connetti");
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnInvia.getText()=="Connetti"){
					if(txtNick.getText()!=""&&Files.exists(Paths.get(textField_1.getText()),LinkOption.NOFOLLOW_LINKS)&&!
			            	Files.isDirectory(Paths.get(textField_1.getText()), LinkOption.NOFOLLOW_LINKS)){
						txtNick.setEnabled(false);
						textField_1.setEnabled(false);
						textField.setEnabled(true);
						new ReadAsync().start();
						path=textField_1.getText();
<<<<<<< HEAD
						SendMessage(txtNick.getText()+" si e' unito alla chat.\n");
=======
>>>>>>> origin/master
						btnInvia.setText("Invia");
					}
				}
				else{
<<<<<<< HEAD
					if(textField.getText().length()!=0){SendMessage(txtNick.getText()+": "+textField.getText()+"\n");}
=======
					if(textField.getText().length()!=0){SendMessage();}
>>>>>>> origin/master
				}
			}
		});
		panel_1.add(btnInvia, BorderLayout.EAST);
		
		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setTabSize(4);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
<<<<<<< HEAD
	}
}

class ReadAsync extends Thread 
{ 
  //This method will be executed when this thread is executed 
  public void run() 
  { 

	  	String path=Chat.path;
		File file = new File(path);
		long last=2;
		while(true){
				if(file.length()!=last){ ReadFunc();last=file.length();}
			}
   }
  
  public static void ReadFunc(){
		List<String> lins;
		try {
			lins = Files.readAllLines(Paths.get(Chat.path));
			try{Chat.textArea.append(lins.get(lins.size()-1)+"\n");
			}catch(IndexOutOfBoundsException e){
			}
		} catch (IOException e) {
		}
	}
}
=======
	}
}
>>>>>>> origin/master
