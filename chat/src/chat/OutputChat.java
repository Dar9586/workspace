package chat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class OutputChat {
	
	static File file;
	
	public static void main(String[] args) throws IOException{
		String path=args[0];
		file = new File(path);
		long last=2;
		int ind=0;
		
			
		while(true){
				if(file.length()!=last){ReadFunc();last=file.length();}
			}
			//WriteFunc();
			
			//WriteFunc("");
			//ReadFunc();
			
	}
	
	/*public static void ClearFunc(){
		WriteFunc("");
	}
	public static void WriteFunc(){
		String s=dd.nextLine();
		WriteFunc(s);
	
	}*/
	
	
	public static void ReadFunc(){
		List<String> lins;
		try {
			lins = Files.readAllLines(Paths.get(file.getPath()));
			try{System.out.println(lins.get(lins.size()-1));}catch(IndexOutOfBoundsException e){
			}
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

}
