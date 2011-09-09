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
		
		Map<String, Triplet> nameToNumberMap = new HashMap<String, Triplet>();
		
		for(Agent a : BattleGroundOverlord.competitors) {
			String name = a.getName();
			
			if(nameToNumberMap.containsKey(name)) {
				int current = nameToNumberMap.get(name).getA() + 1;
				int max = Math.max(current, nameToNumberMap.get(name).getB());
				int min = Math.min(current, nameToNumberMap.get(name).getC());
				nameToNumberMap.remove(name);
				nameToNumberMap.put(name, new Triplet(current, max, min));
			} else {
				nameToNumberMap.put(name, new Triplet(1, 50, 50));
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
			frameRate(5);
			bgo.getAgentsTurns();
			bgo.executeAgentsTurns();
		}
	}
}
