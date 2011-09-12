package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;

final public class BattleGround extends PApplet {
	
	
	private static final long serialVersionUID = -734490989373114569L;
	static boolean start = true;
	int scale = 20; // must be >=2 to render correctly
	int gridWidth = 30; // width in # of cells
	int gridHeight = 30; // height in # of cells
	int xSpacing = 10; //size of border around grid in pixels
	int ySpacing = 10; //size of border around grid in pixels
	int fps = 1000; //fps to run animation at

	static int maxNumber = 25;
	static boolean debug = false;

	private ArrayList<ArrayList<Cell>> grid; //Matrix of Cells
	private BattleGroundOverlord bgo;

	Map<String, Triplet> nameToNumberMap = new HashMap<String, Triplet>();
		
	public void setup() {
		size(scale * gridWidth + 175, scale * gridHeight + 100); //set size of the sketch
		frameRate(150); //set fps
		smooth(); //draw with anti-aliasing
		
		grid = new ArrayList<ArrayList<Cell>>();
		
		for (int y = 0; y < gridHeight; y++) {
			grid.add(new ArrayList<Cell>());
			for (int x = 0; x < gridWidth; x++) {
				grid.get(y).add(new Cell(x, y));
			}
		}
		
		bgo = new BattleGroundOverlord(grid, this);		

		StagingArea.createStagingArea();	
		
		String loadPath = selectInput();  // Opens file chooser
		if (loadPath == null) {
		  // If a file was not selected
		  println("No file was selected...");
		} else {
		  // If a file was selected, print path to file
		  println(loadPath);
		}
	}

	public void draw() { //automatically loops at specified frameRate
		background(0); //black out background (clearing any drawing)		
		
		for (ArrayList<Cell> a : grid) {
			for (Cell c : a) {
				c.drawCell(this);
			}
		}
		
		if(start) {			
			bgo.enterAgent();
		}		
		
		for(Agent a : BattleGroundOverlord.competitors) {
			String name = a.getName();
			
			if(nameToNumberMap.containsKey(name))
				nameToNumberMap.get(name).setA(0);
		}
		
		for(Agent a : BattleGroundOverlord.competitors) {
			String name = a.getName();
			
			if(nameToNumberMap.containsKey(name))
				nameToNumberMap.get(name).setA(nameToNumberMap.get(name).getA() + 1);
			else
				nameToNumberMap.put(name, new Triplet(1, maxNumber, maxNumber));
		}
		

		for(Entry<String, Triplet> nameNumber : nameToNumberMap.entrySet()) {
			boolean found = false;
			for(Agent a : BattleGroundOverlord.competitors) {
				if(nameNumber.getKey().equals(a.getName()))
					found = true;
			}
			if(!found) {
				nameToNumberMap.get(nameNumber.getKey()).setA(0);
			}
		}
		
		if(!start) {
			
			for(Entry<String, Triplet> nameNumber : nameToNumberMap.entrySet()) {
				String name = nameNumber.getKey();
				int current = nameNumber.getValue().getA();

				int max = Math.max(current, nameToNumberMap.get(name).getB());
				int min = Math.min(current, nameToNumberMap.get(name).getC());

				nameToNumberMap.get(name).setB(max);
				nameToNumberMap.get(name).setC(min);
			}
		}
		
		int spacing = 15;
		for(Entry<String, Triplet> nameNumber : nameToNumberMap.entrySet()) {
			String text = nameNumber.getKey() + " : " + nameNumber.getValue().getA()
			+ " : " + nameNumber.getValue().getB()
			+ " : " + nameNumber.getValue().getC();
			text(text, (gridWidth * scale + xSpacing + 15), spacing);
			spacing+=15;
		}
		
		if(!start) {
			frameRate(150);
			bgo.getAgentsTurns();
			bgo.executeAgentsTurns();
		}
	}
}
