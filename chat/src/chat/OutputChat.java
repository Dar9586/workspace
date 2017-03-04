package chat;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class OutputChat {
	static Scanner sc;
	static File file;
	static String path;
	static void setup(){
		sc=new Scanner(System.in);
		while(true){
			System.out.println("Inserisci percorso chat: ");
			path=sc.nextLine();
			//controlla che il file esista e che non sia una cartella
			if(Files.exists(Paths.get(path),LinkOption.NOFOLLOW_LINKS)&&!
	            	Files.isDirectory(Paths.get(path), LinkOption.NOFOLLOW_LINKS))break;
			}
	}
	public static void main(String[] args) throws IOException{
		/*se l'utente ha passato gli argomenti vendono settati 
   	 altrimenti chiama la funzione per settari*/
		if(args.length==1)path=args[0];
		else setup();
		file = new File(path);//file della chat
		long last=file.length();//dimensione file
		while(true){
				if(file.length()!=last){
					//se viene modificata scrivi la modifica e modifica last
					ReadFunc();last=file.length();
				}
			}
	}
	
	public static void ReadFunc(){
		List<String> lins;//lista contenente le linee del file
		try {
			lins = Files.readAllLines(Paths.get(path));//ottiene tutte le linee del file
			System.out.println(lins.get(lins.size()-1));//e scrive l'ultima
		} 
		catch (IOException e) {}
		catch(IndexOutOfBoundsException e){}
  }

}
