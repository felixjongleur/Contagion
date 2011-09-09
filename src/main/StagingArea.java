package main;

import java.io.File;


public class StagingArea {
	
	static int totalMaxNumber;
	private static int filePos = 0;
	static File[] listOfFiles;
	
	public StagingArea() {

	}
	
	public static void createStagingArea() {
		String path = "createdAgents";
		File folder = new File(path);
		listOfFiles = folder.listFiles();

		if(BattleGround.debug) {
			System.out.println("    COMPETITORS!!    ");
			System.out.println("---------------------");
			for(int i = 0; i < listOfFiles.length; i++) {
				if(listOfFiles[i].isFile()) {
					System.out.println(listOfFiles[i].getName());				
				}
			}
		}
		
		totalMaxNumber = BattleGround.maxNumber * listOfFiles.length;
		/*
		for(int number = 0; number < maxNumber; number++) {
			for(File config : listOfFiles) {
				BattleGroundOverlord.enterAgent(new Agent(config));
			}
		}*/
	}
	
	public static File getNextFile() {
		File temp = listOfFiles[filePos];
		
		filePos++;
		
		if(filePos == listOfFiles.length)
			filePos = 0;
		
		return temp;
	}
}
