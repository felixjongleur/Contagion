package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class StagingArea {
	
	private static int filePos = 0;
	static List<File> listOfFiles;
	
	static List<File> listOfActiveFiles;
	
	public StagingArea() {

	}
	
	public static void createStagingArea() {
		String path = "createdAgents";
		File folder = new File(path);
		File[] temp = folder.listFiles();
		listOfFiles = new ArrayList<File>();
		listOfActiveFiles = new ArrayList<File>();
		
		System.out.println("    COMPETITORS!!    ");
		System.out.println("---------------------");
		for(int i = 0; i < temp.length; i++) {
			if(temp[i].isFile()) {
				System.out.println(temp[i].getName());
				listOfFiles.add(temp[i]);
			}
		}
		
		/*
		for(int number = 0; number < maxNumber; number++) {
			for(File config : listOfFiles) {
				BattleGroundOverlord.enterAgent(new Agent(config));
			}
		}*/
	}
	
	public static File getNextFile() {
		File temp = listOfActiveFiles.get(filePos);
		
		filePos++;
		
		if(filePos == listOfActiveFiles.size())
			filePos = 0;
		
		return temp;
	}
}
