package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;

final public class BattleGround extends PApplet {
	
	private static final long serialVersionUID = -734490989373114569L;
	static boolean start = true;
	int scale = 15; // must be >=2 to render correctly
	int gridWidth = 40; // width in # of cells
	int gridHeight = 40; // height in # of cells
	int xSpacing = 10; //size of border around grid in pixels
	int ySpacing = 10; //size of border around grid in pixels
	int fps = 1000; //fps to run animation at

	private ArrayList<ArrayList<Cell>> grid; //Matrix of Cells
	private BattleGroundOverlord bgo;
		
	public void setup() {
		size(scale * gridWidth + 150, scale * gridHeight + 50); //set size of the sketch
		frameRate(fps); //set fps
		ellipseMode(CORNER); //ellipses drawn using top-left corner as origin
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
	}

	public void draw() { //automatically loops at specified frameRate
		background(0); //black out background (clearing any drawing)		
		
		for (ArrayList<Cell> a : grid) {
			for (Cell c : a) {
				c.drawCell(this);
			}
		}
		
		if(start) {			
			bgo.assignLocationAndDirectionToAgents();
		}
		
		Map<String, Integer> nameToNumberMap = new HashMap<String, Integer>();
		
		for(Agent a : BattleGroundOverlord.competitors) {
			String name = a.getName();
			
			if(nameToNumberMap.containsKey(name)) {
				int number = nameToNumberMap.get(name) + 1;
				nameToNumberMap.remove(name);
				nameToNumberMap.put(name, number);
			} else {
				nameToNumberMap.put(name, 1);
			}
		}
		int spacing = 15;
		for(Entry<String, Integer> nameNumber : nameToNumberMap.entrySet()) {
			String text = nameNumber.getKey() + " : " + nameNumber.getValue();
			text(text, (gridWidth * scale + xSpacing + 15), spacing);
			spacing+=15;
		}
		
		if(!start) {
			fps = 15;
			bgo.getAgentsTurns();
			bgo.executeAgentsTurns();
		}
	}
}
