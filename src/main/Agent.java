package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import processing.core.PImage;

public class Agent {
	
	private String name;
	private Cell location;
	private Direction facing;
	private String currentTurn;
	
	private String imageName;
	private PImage image;
	
	private Agent host;
		
	private boolean moveSelected;
	private boolean blocked;
	
	private int step = 0;
	private ArrayList<String> moves;
	static Map<String, PImage> images;
	
	
	public static enum Direction {
		NORTH, EAST, SOUTH, WEST
	}
	
	public static enum Instructions {
		LEFT, RIGHT, FORWARD, ATTACK, STAY,
		EMPTY, WALL, ENEMY, FRIEND,
		LOOK, IF, THEN, ELSE, END,
		GOTO
	}
	
	protected Agent(File configFile) {
		this.moves = new ArrayList<String>();
		this.moveSelected = false;
		this.host = null;
		Scanner scanner = null;
		
		if(images == null) {
			images = new HashMap<String, PImage>();
		}
		
		try {
			scanner = new Scanner(configFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		String config = "";
		
		while (scanner.hasNextLine()) {
			config += scanner.nextLine() + " ";			
		}
		
		StringTokenizer tk = new StringTokenizer(config);
		
		while(tk.hasMoreTokens()) {
			
			String token = tk.nextToken();
			
			if(token.equals("#NAME")) {
				this.name = tk.nextToken();
			} else if(token.equals("#IMAGE_FILE_NAME")) {
				this.imageName = tk.nextToken();
			} else if(token.equals("#INSTRUCTIONS")) {
				while(tk.hasMoreTokens()) {
					moves.add(tk.nextToken());
				}
			}
		}
	/*	
		for(String move : moves) {
			System.out.println(move);
		}*/
	}
	
	protected void setVariables(Cell location, Direction facing, BattleGround s) {
		this.location = location;
		this.facing = facing;
		this.currentTurn = null;
		
		if(!images.containsKey(imageName)) {

			PImage temp = s.loadImage(imageName);
			images.put(imageName, temp);
		}

		this.image = images.get(imageName);
		
		image.resize(0, s.scale - 1);
	}
		
	public void setCurrentMove() {
		currentTurn = moves.get(step);
		
		step++;
		if(step >= moves.size())
			step = 0;
	}
	
	public String getCurrentMove() {		
		return currentTurn;
	}
	
	public void executeCurrentMove(boolean allowed) {
		
		if(allowed) {
			if(currentTurn.equals("LEFT")) turnLeft();
			if(currentTurn.equals("RIGHT")) turnRight();
			if(currentTurn.equals("ATTACK")) infectHost();
		}		
	}
	
	private void infectHost() {
		if(BattleGround.debug)
			System.out.println(name + " : INFECTS : " + host.getName());
		host.setName(getName());
		host.setMoves(getMoves());
		host.setImage(getImage());
		host.setStep(0);
		this.host = null;
	}
	
	final void turnLeft() {
		if(BattleGround.debug)
			System.out.println(name + " : LEFT");
		switch (facing) {
			case NORTH : { facing = Direction.WEST; break; }
			case EAST  : { facing = Direction.NORTH; break; }
			case SOUTH : { facing = Direction.EAST; break; }
			case WEST  : { facing = Direction.SOUTH; break; }
		}
	}
	
	final void turnRight() {
		if(BattleGround.debug)
			System.out.println(name + " : RIGHT");
		switch (facing) {
			case NORTH : { facing = Direction.EAST; break; }
			case EAST  : { facing = Direction.SOUTH; break; }
			case SOUTH : { facing = Direction.WEST; break; }
			case WEST  : { facing = Direction.NORTH; break; }
		}
	}
	
	final void drawAgent(BattleGround s) {
		int scale = s.scale;
		
		int x = location.getX() * scale + s.xSpacing + 1;
		int y = location.getY() * scale + s.ySpacing + 1;
		
		s.image(image, x, y);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cell getLocation() {
		return location;
	}

	public void setLocation(Cell location) {
		this.location = location;
	}

	public Direction getFacing() {
		return facing;
	}

	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	public String getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(String currentTurn) {
		this.currentTurn = currentTurn;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public PImage getImage() {
		return image;
	}

	public void setImage(PImage image) {
		this.image = image;
	}

	public Agent getHost() {
		return host;
	}

	public void setHost(Agent host) {
		this.host = host;
	}

	public boolean hasMoveSelected() {
		return moveSelected;
	}

	public void setMoveSelected(boolean moved) {
		this.moveSelected = moved;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public ArrayList<String> getMoves() {
		return moves;
	}

	public void setMoves(ArrayList<String> moves) {
		this.moves = moves;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isBlocked() {
		return blocked;
	}
}

