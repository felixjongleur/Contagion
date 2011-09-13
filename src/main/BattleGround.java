package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import controlP5.*;

final public class BattleGround extends PApplet {
	
	ControlP5 cp5;
	DropdownList d1;
	Button startButton;
	
	private static final long serialVersionUID = -734490989373114569L;
	static int start = 0;
	int scale = 20; // must be >=2 to render correctly
	int gridWidth = 20; // width in # of cells
	int gridHeight = 20; // height in # of cells
	int xSpacing = 10; //size of border around grid in pixels
	int ySpacing = 10; //size of border around grid in pixels
	int fps = 1000; //fps to run animation at

	int maxNumber = 25;
	static boolean debug = false;

	static int totalMaxNumber;
	
	private ArrayList<ArrayList<Cell>> grid; //Matrix of Cells
	private BattleGroundOverlord bgo;

	Map<String, Triplet> nameToNumberMap = new HashMap<String, Triplet>();
		
	public void setup() {
		size(scale * gridWidth + 175, scale * gridHeight + 150); //set size of the sketch
		frameRate(150); //set fps
		smooth(); //draw with anti-aliasings
		
		grid = new ArrayList<ArrayList<Cell>>();
		
		for (int y = 0; y < gridHeight; y++) {
			grid.add(new ArrayList<Cell>());
			for (int x = 0; x < gridWidth; x++) {
				grid.get(y).add(new Cell(x, y));
			}
		}
		
		bgo = new BattleGroundOverlord(grid, this);		

		StagingArea.createStagingArea();


		totalMaxNumber = maxNumber * StagingArea.listOfFiles.length;
	/*	
		String loadPath = selectInput();  // Opens file chooser
		if (loadPath == null) {
		  // If a file was not selected
		  println("No file was selected...");
		} else {
		  // If a file was selected, print path to file
		  println(loadPath);
		}*/
		
		cp5 = new ControlP5(this);
		startButton = cp5.addButton("start",1,10,scale * gridHeight + 34, 80,15);
		startButton.setColorActive(color(255,128));
		startButton.setColorBackground(color(60));
		d1 = cp5.addDropdownList("maxNumber", 100, scale * gridHeight + 50, 120, 100);
		customize(d1, "Max # of Each - 25","Max # of Each - ", 1, ((gridWidth * gridHeight) / StagingArea.listOfFiles.length));
	}

	public void start(int theValue) {
		println(theValue);
		start = theValue;
		startButton.lock();
		d1.disableCollapse();
	}
	
	public void maxNumber(int theValue) {
		println(theValue);
	}
	
	void customize(DropdownList ddl, String defaultText, String text, int min, int max) {
		  ddl.setBackgroundColor(color(190));
		  ddl.setItemHeight(20);
		  ddl.setBarHeight(15);
		  ddl.captionLabel().set(defaultText);
		  ddl.captionLabel().style().marginTop = 3;
		  ddl.captionLabel().style().marginLeft = 3;
		  ddl.valueLabel().style().marginTop = 3;
		  for(int i=min;i<=max;i++) {
		    ddl.addItem(text+i,i);
		  }
		  ddl.setColorBackground(color(60));
		  ddl.setColorActive(color(255,128));
		}

	
	public void draw() { //automatically loops at specified frameRate
		background(0); //black out background (clearing any drawing)
		
		for (ArrayList<Cell> a : grid) {
			for (Cell c : a) {
				c.drawCell(this);
			}
		}
		
		if(start == 1) {			
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
		
		if(start == 0) {
			
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
		
		if(start == 0) {
			frameRate(150);
			bgo.getAgentsTurns();
			bgo.executeAgentsTurns();
		}
	}
	
	public void controlEvent(ControlEvent theEvent) {
		  // PulldownMenu is if type ControlGroup.
		  // A controlEvent will be triggered from within the ControlGroup.
		  // therefore you need to check the originator of the Event with
		  // if (theEvent.isGroup())
		  // to avoid an error message from controlP5.
		System.out.println(theEvent);
System.out.println("HELLO!");
		  if (theEvent.isGroup()) {
		    // check if the Event was triggered from a ControlGroup
		    println(theEvent.group().value()+" from "+theEvent.group());
		    
		    if(theEvent.group().name().equals("maxNumber")) {
		    	maxNumber = (int) theEvent.group().value();
				totalMaxNumber = maxNumber * StagingArea.listOfFiles.length;
		    }
		    
		  } else if(theEvent.isController()) {
		    println(theEvent.controller().value()+" from "+theEvent.controller());
		  }
		}
}
