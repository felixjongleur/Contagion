package main;

import java.io.File;


public class StagingArea {
	
	private static int maxNumber = 50;
	
	public StagingArea() {

	}
	
	public static void createStagingArea() {
		String path = "createdAgents";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		System.out.println("    COMPETITORS!!    ");
		System.out.println("---------------------");
		for(int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile()) {
				System.out.println(listOfFiles[i].getName());				
			}
		}
		
		for(int number = 0; number < maxNumber; number++) {
			for(File config : listOfFiles) {
				BattleGroundOverlord.enterAgent(new Agent(config));
			}
		}
	}
}
