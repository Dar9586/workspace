package chat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
class ShutDown extends Thread 
{ 
  public void run() 
  { 
	  System.out.println("chiuso");
  	if(InputChat.nick.length()>1)InputChat.WriteFunc(InputChat.nick+" ha lasciato la chat.\n");
   }
}
public class InputChat{
    static Scanner sc= new Scanner(System.in);
	public static String nick;
	static String path;
	public static void setup(){
		while(true){
		System.out.println("Inserisci nome utente: ");
		nick=sc.nextLine();
		if(nick.length()>1)break;
		}
		while(true){
		System.out.println("Inserisci percorso chat: ");
		path=sc.nextLine();
		if(Files.exists(Paths.get(path),LinkOption.NOFOLLOW_LINKS)&&!
            	Files.isDirectory(Paths.get(path), LinkOption.NOFOLLOW_LINKS)){break;}
		
		}
	}
    public static void main(String[] args){
    	System.out.println("lldslsalsadsdsad");
    	Runtime.getRuntime().addShutdownHook(new ShutDown());
    	System.out.println("lldslsalsadsdsad");
    	if(args.length==2){nick=args[0];path=args[1];}
    	else{setup();}
    	WriteFunc(nick+" si e' unito alla chat.\n");
    while(true)
    {
		System.out.print("> ");
    WriteFunc(nick+": "+sc.nextLine()+"\n");
    }

}

    public static void WriteFunc(String s){
		try {
			Files.write(Paths.get(path), s.getBytes(), StandardOpenOption.APPEND);
		}catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
