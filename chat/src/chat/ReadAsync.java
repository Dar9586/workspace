package chat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadAsync extends Thread 
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