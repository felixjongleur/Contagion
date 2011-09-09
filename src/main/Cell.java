package main;




public class Cell {
	
	private int x; //x coordinate of this cell in the grid
	private int y; //y coordinate of this cell in the grid
	private Agent agent;
	
	//constructor
	public Cell(int x, int y) { 
		this.x = x;
		this.y = y;
		this.agent = null;
	}
	
	final void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	final Agent getAgent() {
		return agent;
	}
	
	//get x coordinate
	final int getX(){
		return x;
	}
	
	//get y coordinate
	final int getY(){
		return y;
	}
	
	//draw this cell to given Sketch
	public void drawCell(BattleGround s) {
		//calculate origin (top-left corner) of this cell in the grid
		int originx = s.xSpacing + x * s.scale;
		int originy = s.ySpacing + y * s.scale;

		
		s.stroke(153); //draw with light-gray
		s.line(originx, originy, originx + s.scale, originy);
		s.line(originx, originy + s.scale, originx + s.scale, originy + s.scale);
		s.line(originx + s.scale, originy, originx + s.scale, originy + s.scale);
		s.line(originx, originy, originx, originy + s.scale);
		
		if(agent != null)
			agent.drawAgent(s);
	}
}
