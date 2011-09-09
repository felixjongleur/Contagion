This is a simple outbreak simulator.
Each person can create there own contagient
to compete against each other.

There are civilians on the map as well which wander aimlessly and can't attack
but can be infected by any contagient.

The first number next to a name is the current number of that contagient
The second number is the max of that contagient that has existed
The third number is the minimum of that contagient that has existed

Last contagient standing is the winner!


1. File Input Structure
	#NAME
	Name of Contagient
	#IMAGE_FILE_NAME (if not present random default assigned)
	Name of image file
	#INSTRUCTIONS
	List of instructions for contagient to loop on
	
2. Instructions Structure
	- Basic Instructions
		STAY
		LEFT
		RIGHT
		ATTACK
		MOVE
	- Advanced Instructions
		LOOK
		
3. Order of Moves
	LOOK
	STAY
	LEFT and RIGHT
	ATTACK
	MOVE
	
4. Can only attack an opponent if
	- they are not facing you
	- you are facing them
	- mark all contagients with attacked = true
	- then update all contagients
	
5. Can only move to a square if destination
	- is not through a wall
	- is not currently occupied
	- if two or more contagients try to move to the same open square
		- one is chosen at random to move

6. LOOK
	- Does not take a turn (only basic instructions take a turn)
	- This enables your contaigent to LOOK one space in front of it.
	  The BattleGroundOverlord will return one of the following responses :
	  
	- RESPONSES
		EMPTY
		WALL
		ENEMY
		FRIEND
		
	- KEYWORDS
		IF
		THEN
		ELSE
		END
		OPERATORS
		
	- OPERATORS
		EQUALS
		NOTEQUALS	
		
	- Basic Format
		IF
		LOOK operator RESPONSES
		THEN
		Basic Instruction (One or many)
		ELSE / END
		Basic Instruction (One or many)
		END
		
	- Example
	
		IF LOOK EQUALS WALL
		THEN
			LEFT
		ELSE
			IF LOOK EQUALS ENEMY
			THEN
				ATTACK
			END
			FORWARD
		END