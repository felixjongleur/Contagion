package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import controlP5.DropdownList;
import controlP5.ListBox;
import controlP5.Slider;

final public class BattleGround extends PApplet {
	
	ControlP5 controlP5;
	Button startButton;
	Button resetButton;
	Slider maxNum;
	Slider framesPerSecond;
	Slider renderScale;
	Slider dimensions;
		
	ControlWindow settingsWindow;

	ListBox allFiles;
	ListBox selectedFiles;
	
	private static final long serialVersionUID = -734490989373114569L;
	static int start = 0;
	int reset = 0;
	int scale = 20; // must be >=2 to render correctly
	int gridWidth = 20; // width in # of cells
	int gridHeight = 20; // height in # of cells
	int xSpacing = 10; //size of border around grid in pixels
	int ySpacing = 10; //size of border around grid in pixels
	int fps = 5; //fps to run animation at

	int maxNumber = 25;
	static boolean debug = false;

	static int totalMaxNumber = 25;
	
	private ArrayList<ArrayList<Cell>> grid; //Matrix of Cells
	private BattleGroundOverlord bgo;

	Map<String, Triplet> nameToNumberMap;
	
	public void setup() {
		
		size(scale * gridWidth + 175, scale * gridHeight + 200); //set size of the sketch
		
		frameRate(60); //set fps
		smooth(); //draw with anti-aliasings
		
		nameToNumberMap = new HashMap<String, Triplet>();
		
		grid = new ArrayList<ArrayList<Cell>>();
		
		for (int y = 0; y < gridHeight; y++) {
			grid.add(new ArrayList<Cell>());
			for (int x = 0; x < gridWidth; x++) {
				grid.get(y).add(new Cell(x, y));
			}
		}	
		
		bgo = new BattleGroundOverlord(grid, this);	

		StagingArea.createStagingArea();

		totalMaxNumber = maxNumber * StagingArea.listOfFiles.size();
			/*	
		String loadPath = selectInput();  // Opens file chooser
		if (loadPath == null) {
		  // If a file was not selected
		  println("No file was selected...");
		} else {
		  // If a file was selected, print path to file
		  println(loadPath);
		}*/

		controlP5 = new ControlP5(this);
		
		if(settingsWindow == null) {
			settingsWindow = controlP5.addControlWindow("settings", width + 10, 20, 300, 200);
			settingsWindow.hideCoordinates();
			settingsWindow.setTitle("Settings");
		//	settingsWindow.setUndecorated(true);			
			settingsWindow.papplet().frame.setAlwaysOnTop(true);
		}		
		
		framesPerSecond = controlP5.addSlider("framesPerSecond",1,60,5,10,10,100,15);
		framesPerSecond.captionLabel().set("Moves per Second");
		framesPerSecond.setWindow(settingsWindow);
		
		maxNum = controlP5.addSlider("maxNumber",1,(gridWidth * gridHeight)/StagingArea.listOfFiles.size(),25,10,30,100,15);
		maxNum.captionLabel().set("# of each Contagients");
		maxNum.setWindow(settingsWindow);
		
		renderScale = controlP5.addSlider("renderScale",2,((height - 200) / gridHeight),20,10,50,100,15);
		renderScale.captionLabel().set("Scale of each square (in pixels)");
		renderScale.setWindow(settingsWindow);
		
		dimensions = controlP5.addSlider("dimensions",sqrt(StagingArea.listOfFiles.size() * maxNumber),((height - 200) / scale),20,10,70,100,15);
		dimensions.captionLabel().set("# of Squares");
		dimensions.setWindow(settingsWindow);

		startButton = controlP5.addButton("start",1,10,scale * gridHeight + 34, 30,15);
		startButton.setColorActive(color(255,128));
		startButton.lock();
		
		resetButton = controlP5.addButton("reset",1,50,scale * gridHeight + 34, 30,15);
		resetButton.setColorActive(color(255,128));
		
		allFiles = controlP5.addListBox("AvailableFiles", 10, scale * gridHeight + 75, 120, 120);
		allFiles.setItemHeight(15);
		allFiles.setBarHeight(15);
		allFiles.captionLabel().toUpperCase(true);
		allFiles.captionLabel().set("Available Files");
		allFiles.captionLabel().style().marginTop = 3;
		allFiles.valueLabel().style().marginTop = 3; // the +/- sign
		
		for(int i=0;i<StagingArea.listOfFiles.size();i++) {
			allFiles.addItem(StagingArea.listOfFiles.get(i).getName(),i);
		}
		
		allFiles.setColorBackground(color(255,128));
		allFiles.setColorActive(color(0,0,255,128));
		allFiles.close();
		
		selectedFiles = controlP5.addListBox("SelectedFiles", 150, scale * gridHeight + 75, 120, 120);
		selectedFiles.setItemHeight(15);
		selectedFiles.setBarHeight(15);
		selectedFiles.captionLabel().toUpperCase(true);
		selectedFiles.captionLabel().set("Selected Files");
		selectedFiles.captionLabel().style().marginTop = 3;
		selectedFiles.valueLabel().style().marginTop = 3; // the +/- sign
		selectedFiles.setColorBackground(color(255,128));
		selectedFiles.setColorActive(color(0,0,255,128));
		selectedFiles.disableCollapse();
	} 
	
	public void start(int theValue) {
		start = theValue;
		startButton.lock();
		allFiles.close();
		allFiles.disableCollapse();
		maxNum.lock();
		renderScale.lock();
		dimensions.lock();
		bgo = new BattleGroundOverlord(grid, this);	
	}
	
	public void reset(int theValue) {
		reset = theValue;
		start = 0;
		
		gridWidth = 20;
		gridHeight = 20;
		maxNumber = 25;
		scale = 20;
		fps = 5;
		
		StagingArea.listOfActiveFiles.clear();
		StagingArea.listOfFiles.clear();
		allFiles.remove();
		selectedFiles.remove();
		
		setup();		
		
		startButton.lock();
		maxNum.unlock();
		renderScale.unlock();
		dimensions.unlock();
	}
	
	void dimensions(int number) {
		
		boolean changeOtherSettings = false;
		
		if(number != gridHeight)
			changeOtherSettings = true;
		
		println("a dimensions event. setting gridWidth and gridHeight to "+number);
		gridWidth = number;
		gridHeight = number;
		
		grid = new ArrayList<ArrayList<Cell>>();
		
		for (int y = 0; y < gridHeight; y++) {
			grid.add(new ArrayList<Cell>());
			for (int x = 0; x < gridWidth; x++) {
				grid.get(y).add(new Cell(x, y));
			}
		}
		
		if(changeOtherSettings) {
			renderScale.setMax((height - 200) / gridHeight);
			maxNum.setMax((gridWidth * gridHeight) / StagingArea.listOfFiles.size());
		}
	}
	
	void renderScale(int number) {
		
		boolean changeOtherSettings = false;
		
		if(number != scale)
			changeOtherSettings = true;
		
		println("a renderScale event. setting scale to "+number);
  		scale = number;
  		

		if(changeOtherSettings)
			dimensions.setMax(((height - 200) / scale));
  		
  	//	width = scale * gridWidth;
  	//	height = scale * gridHeight;
  		
	//	size((scale * gridWidth + 175), (scale * gridHeight + 150)); //set size of the sketch
	}
	
	void framesPerSecond(int number) {
		println("a framesPerSecond event. setting framesPerSecond to "+number);
  		fps = number;
		frameRate(fps);
	}
	
	void maxNumber(int number) {
		println("a maxNumber event. setting maxNumber to "+number);
		maxNumber = number;
		
		dimensions.setMin(sqrt(StagingArea.listOfFiles.size() * maxNumber));
		totalMaxNumber = maxNumber * StagingArea.listOfFiles.size();
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
			fill(0, 102, 153);
			text(text, (gridWidth * scale + xSpacing + 15), spacing);
			spacing+=15;
		}
		
		if(start == 0) {
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
		  if (theEvent.isGroup()) {
		    // check if the Event was triggered from a ControlGroup
		    println(theEvent.group().value()+" from "+theEvent.group());
		    
		    if(theEvent.group().name().equals("AvailableFiles")) {
		    	
		    	File temp = StagingArea.listOfFiles.get((int) theEvent.group().value());
		    	
		    	if(!StagingArea.listOfActiveFiles.contains(temp)) {
		    		StagingArea.listOfActiveFiles.add(temp);
		    		selectedFiles.addItem(temp.getName(), 1);
		    	} else {
		    		StagingArea.listOfActiveFiles.remove(temp);
		    		selectedFiles.removeItem(temp.getName());
		    	}
		    	
		    	if(StagingArea.listOfActiveFiles.size() >= 2)
		    		startButton.unlock();
		    	else
		    		startButton.lock();
		    }
		    
		  } else if(theEvent.isController()) {
		    println(theEvent.controller().value()+" from "+theEvent.controller());
		/*    
		    if(theEvent.controller().name().equals("Starting # of each")) {
		    	System.out.println(maxNumber);
		    	maxNumber = (int) theEvent.controller().value();
		    	System.out.println(StagingArea.listOfFiles.size());
				totalMaxNumber = maxNumber * StagingArea.listOfFiles.size();
		    }*/
		    
		  }
		}
}
