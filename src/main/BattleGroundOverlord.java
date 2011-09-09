package main;

import java.util.ArrayList;
import java.util.Random;

import main.Agent.Direction;

public class BattleGroundOverlord {

	static ArrayList<Agent> competitors;
	int competitorsPosition = 0;
	BattleGround s;
	ArrayList<ArrayList<Cell>> grid;
	Integer[][] moveMap;
	
	BattleGroundOverlord(ArrayList<ArrayList<Cell>> grid, BattleGround s) {		
		competitors = new ArrayList<Agent>();
		moveMap = new Integer[grid.size()][grid.size()];
		
		for (int y = 0; y < grid.size(); y++) {
			for (int x = 0; x < grid.size(); x++) {
				moveMap[y][x] = 0;
			}
		}
		
		printMoveMap();
		
		this.grid = grid; 
		this.s = s;
	}
	
	private void printMoveMap() {
		System.out.println();
		
		for (int y = 0; y < moveMap.length; y++) {
			System.out.print("|");
			for (int x = 0; x < moveMap.length; x++) {
				if(moveMap[y][x] < 1)
					System.out.print("-");
				else
					System.out.print(moveMap[y][x]);
				System.out.print("|");
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
	private void printGrid() {
		System.out.println();
		
		for (int y = 0; y < grid.size(); y++) {
			System.out.print("|");
			for (int x = 0; x < grid.size(); x++) {
				if(grid.get(y).get(x).getAgent() != null)
					System.out.print(grid.get(y).get(x).getAgent().getName().charAt(0));
				else
					System.out.print("-");

				System.out.print("|");
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
	public static void enterAgent(Agent a) {
		competitors.add(a);
	}
	
	public void assignLocationAndDirectionToAgents() {
		
		Random rand = new Random();
		
		Agent a = competitors.get(competitorsPosition);

		Cell location = null;
		Direction facing = null;
		
		while(location == null) {
			int x = rand.nextInt(s.gridWidth);
			int y = rand.nextInt(s.gridHeight);
			
/*			System.out.println("X = " + x);
			System.out.println("Y = " + y);*/
			
			if(grid.get(y).get(x).getAgent() == null)				
				location = grid.get(y).get(x);
			else
				location = null;
		}
		
		int facingRoll = rand.nextInt(3) + 1;
		
		if(facingRoll == 1)
			facing = Direction.NORTH;
		if(facingRoll == 2)
			facing = Direction.EAST;
		if(facingRoll == 3)
			facing = Direction.SOUTH;
		if(facingRoll == 4)
			facing = Direction.WEST;
		
		a.setVariables(location, facing, s);
		
		location.setAgent(a);
		
		competitorsPosition++;
		
		if(competitorsPosition == competitors.size()) {
			BattleGround.start = false;
		}
	}
	
	public void getAgentsTurns() {
		
		for(Agent a : competitors) {
			a.setMoveSelected(false);
			a.setCurrentMove();
			while(!a.hasMoveSelected()) {
				
				if(a.getCurrentMove().equals("END")) {
					a.setCurrentMove();
				} else if(a.getCurrentMove().equals("ELSE")) {
					while(!a.getCurrentMove().equals("END")) {
						a.setCurrentMove();
					}
				} else if(a.getCurrentMove().equals("IF")) {
					a.setCurrentMove();
					
					if(a.getCurrentMove().equals("LOOK")) {
						a.setCurrentMove();

						Agent inFront = null;
						
						int x = a.getLocation().getX();
						int y = a.getLocation().getY();						

						boolean wall = true;				
						switch(a.getFacing()) {
							case NORTH : if(y - 1 >= 0) wall = false; break;
							case EAST  : if(x + 1 < s.gridWidth) wall = false; break;
							case SOUTH : if(y + 1 < s.gridHeight) wall = false; break;
							case WEST  : if(x - 1 >= 0) wall = false; break;
						}
						if(!wall) {
						
							switch(a.getFacing()) {
								case NORTH : if(grid.get(y - 1).get(x).getAgent() != null) inFront = grid.get(y - 1).get(x).getAgent(); break;
								case EAST  : if(grid.get(y).get(x + 1).getAgent() != null) inFront = grid.get(y).get(x + 1).getAgent(); break;
								case SOUTH : if(grid.get(y + 1).get(x).getAgent() != null) inFront = grid.get(y + 1).get(x).getAgent(); break;
								case WEST  : if(grid.get(y).get(x - 1).getAgent() != null) inFront = grid.get(y).get(x - 1).getAgent(); break;
							}
						}
						
						String query = a.getCurrentMove();
						a.setCurrentMove();
						String expected = a.getCurrentMove();
						
						boolean result = false;
						
						if(query.equals("EQUALS")) {
							if(expected.equals("WALL") && wall) {
								result = true;
							}
							if(!wall) {
								if(expected.equals("EMPTY") && inFront == null) {
									result = true;
								}
								if(inFront != null) {
									if(expected.equals("ENEMY") && !inFront.getName().equals(a.getName())) {
										result = true;
									}
									if(expected.equals("FRIEND") && inFront.getName().equals(a.getName())) {
										result = true;
									}
								}
							}							
						}
						
						// Remove "THEN"
						a.setCurrentMove();
						
						if(result) {
							a.setCurrentMove();
							a.setMoveSelected(true);
						} else {
							// Remove ELSE or END
							while(!a.getCurrentMove().equals("ELSE") && !a.getCurrentMove().equals("END")) {
								a.setCurrentMove();
							}
							a.setCurrentMove();
							if(!a.getCurrentMove().equals("IF")) {
								a.setMoveSelected(true);
							}
						}
					}
				} else if(a.getCurrentMove().equals("RANDOM")){
					// 10% stay
					// 50% forward
					// 20% right or left
					
					Random rand = new Random();
					
					int roll = rand.nextInt(99) + 1;
					
					if(roll <= 50 && roll >= 1)
						a.setCurrentTurn("FORWARD");
					if(roll > 50 && roll <= 60)
						a.setCurrentTurn("STAY");
					if(roll > 60 && roll <= 80)
						a.setCurrentTurn("LEFT");
					if(roll > 80 && roll <= 100)
						a.setCurrentTurn("RIGHT");
					
					a.setMoveSelected(true);
					
				} else {
					a.setMoveSelected(true);
				}
			}
		}
	}
	
	public void executeAgentsTurns() {

		//Stay
		for(Agent a : competitors) {
			if(a.getCurrentMove().equals("STAY"))
				a.executeCurrentMove(true);
		}		

		//Turns
		for(Agent a : competitors) {
			if(a.getCurrentMove().equals("LEFT") || a.getCurrentMove().equals("RIGHT")) {
				a.executeCurrentMove(true);
			}
		}
		
		//Attacks (Set all hosts)
		for(Agent a : competitors) {
			if(a.getCurrentMove().equals("ATTACK")) {
			//	System.out.println(a.getName() + " : ATTACK " + a.getFacing());
				
				int x = a.getLocation().getX();
				int y = a.getLocation().getY();
				
				switch(a.getFacing()) {
					case NORTH : {
						if(y - 1 >= 0) {						
							if(grid.get(y - 1).get(x).getAgent() != null
						//	&& !(grid.get(y - 1).get(x).getAgent().getFacing() == Direction.SOUTH)		
							&& !(grid.get(y - 1).get(x).getAgent().getName().equals(a.getName()))) {						
								
								a.setHost(grid.get(y - 1).get(x).getAgent());								
							}
						}
						break;
					}
					case EAST  : { 
						if(x + 1 < s.gridWidth) {				
							if(grid.get(y).get(x + 1).getAgent() != null
						//	&& !(grid.get(y).get(x + 1).getAgent().getFacing() == Direction.WEST)
							&& !grid.get(y).get(x + 1).getAgent().getName().equals(a.getName())) {

								a.setHost(grid.get(y).get(x + 1).getAgent());	
							}
						}
						break;
					}
					case SOUTH : {
						if(y + 1 < s.gridHeight) {				
							if(grid.get(y + 1).get(x).getAgent() != null
						//	&& !(grid.get(y + 1).get(x).getAgent().getFacing() == Direction.NORTH)	
							&& !grid.get(y + 1).get(x).getAgent().getName().equals(a.getName())) {

								a.setHost(grid.get(y + 1).get(x).getAgent());	
							}
						}
						break;
					}
					case WEST  : { 
						if(x - 1 >= 0) {				
							if(grid.get(y).get(x - 1).getAgent() != null
						//	&& !(grid.get(y).get(x - 1).getAgent().getFacing() == Direction.EAST)	
							&& !grid.get(y).get(x - 1).getAgent().getName().equals(a.getName())) {								

								a.setHost(grid.get(y).get(x - 1).getAgent());	
							}
						}
						break;
					}
				}
				
			}
		}
		
		//Attacks (Contagients infect)
		for(Agent a : competitors) {
			if(a.getHost() != null) {
				a.executeCurrentMove(true);
			} else if(a.getCurrentMove().equals("ATTACK")) {
				a.executeCurrentMove(false);
			}
		}

		//Moves
		moveMap = new Integer[moveMap.length][moveMap.length];
		
		for (int y = 0; y < grid.size(); y++) {
			for (int x = 0; x < grid.size(); x++) {
				moveMap[y][x] = 0;
			}
		}
	//	printGrid();
		for(Agent a : competitors) {
			if(a.getCurrentMove().equals("FORWARD")) {
				int x = a.getLocation().getX();
				int y = a.getLocation().getY();

				// First Check if Destination is Not a Wall
				boolean notWall = false;				
				switch(a.getFacing()) {
					case NORTH : if(y - 1 >= 0) notWall = true; break;
					case EAST  : if(x + 1 < s.gridWidth) notWall = true; break;
					case SOUTH : if(y + 1 < s.gridHeight) notWall = true; break;
					case WEST  : if(x - 1 >= 0) notWall = true; break;
				}
				if(notWall) {
					// Add one to moveMap at destination if open space
					switch(a.getFacing()) {
						case NORTH : if(grid.get(y - 1).get(x).getAgent() == null) moveMap[y - 1][x]++; break;
						case EAST  : if(grid.get(y).get(x + 1).getAgent() == null) moveMap[y][x + 1]++; break;
						case SOUTH : if(grid.get(y + 1).get(x).getAgent() == null) moveMap[y + 1][x]++; break;
						case WEST  : if(grid.get(y).get(x - 1).getAgent() == null) moveMap[y][x - 1]++; break;
					}	
				}
			}			
		}
		
	//	printMoveMap();
		
		for(Agent a : competitors) {
			if(a.getCurrentMove().equals("FORWARD")) {
				int x = a.getLocation().getX();
				int y = a.getLocation().getY();

				// First Check if Destination is Not a Wall
				boolean notWall = false;				
				switch(a.getFacing()) {
					case NORTH : if(y - 1 >= 0) notWall = true; break;
					case EAST  : if(x + 1 < s.gridWidth) notWall = true; break;
					case SOUTH : if(y + 1 < s.gridHeight) notWall = true; break;
					case WEST  : if(x - 1 >= 0) notWall = true; break;
				}
				if(notWall) {
					
					// Check to make sure only one contagient is moving to a specific square
					switch(a.getFacing()) {
						case NORTH : {
							if(moveMap[y - 1][x] == 1) {
								System.out.println(a.getName() + " : FORWARD");
								grid.get(y - 1).get(x).setAgent(a);
								grid.get(y).get(x).setAgent(null);
								a.setLocation(grid.get(y - 1).get(x));							
							} else {
								moveMap[y - 1][x]--;
								a.executeCurrentMove(false);
							}							
							break;
						}
						case EAST  : { 
							if(moveMap[y][x + 1] == 1) {
								System.out.println(a.getName() + " : FORWARD");
								grid.get(y).get(x + 1).setAgent(a);
								grid.get(y).get(x).setAgent(null);
								a.setLocation(grid.get(y).get(x + 1));							
							} else {
								moveMap[y][x + 1]--;
								a.executeCurrentMove(false);
							}							
							break;
						}
						case SOUTH : {
							if(moveMap[y + 1][x] == 1) {
								System.out.println(a.getName() + " : FORWARD");
								grid.get(y + 1).get(x).setAgent(a);
								grid.get(y).get(x).setAgent(null);
								a.setLocation(grid.get(y + 1).get(x));							
							} else {
								moveMap[y + 1][x]--;
								a.executeCurrentMove(false);
							}							
							break;
						}
						case WEST  : {  
							if(moveMap[y][x - 1] == 1) {
								System.out.println(a.getName() + " : FORWARD");
								grid.get(y).get(x - 1).setAgent(a);
								grid.get(y).get(x).setAgent(null);
								a.setLocation(grid.get(y).get(x - 1));							
							} else {
								moveMap[y][x - 1]--;
								a.executeCurrentMove(false);
							}							
							break;
						}
					}
				}				
			}
		}	
	}
}
