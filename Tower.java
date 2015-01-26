package prog05;

import java.util.Stack;
import prog02.UserInterface;
import prog02.GUI;

public class Tower {
  static UserInterface ui = new GUI();

  static public void main (String[] args) {
    int n = getInt("How many disks?");
    if (n <= 0)
      return;
    Tower tower = new Tower(n);

    String[] commands = { "Human plays.", "Computer plays." };
    int c = ui.getCommand(commands);
    if (c == 0)
      tower.play();
    else
      tower.solve();
  }

  /** Get an integer from the user using prompt as the request.
   *  Return 0 if user cancels.  */
  static int getInt (String prompt) {
    while (true) {
      String number = ui.getInfo(prompt);
      if (number == null)
        return 0;
      try {
        return Integer.parseInt(number);
      } catch (Exception e) {
        ui.sendMessage(number + " is not a number.  Try again.");
      }
    }
  }

  int nDisks;
  StackInt<Integer>[] pegs = (StackInt<Integer>[]) new ArrayStack[3];

  Tower (int nDisks) {
    this.nDisks = nDisks;
    for (int i = 0; i < pegs.length; i++)
      pegs[i] = new ArrayStack<Integer>();

    // EXERCISE: Initialize game with pile of nDisks disks on peg 'a' (pegs[0]).
    for (; nDisks > 0; nDisks--)
        pegs[0].push(nDisks);
  }

  
  void play () {
    while (!(pegs[0].empty() && pegs[1].empty())) {
      displayPegs();
      String move = getMove();
      int from = move.charAt(0) - 'a';
      int to = move.charAt(1) - 'a';
      move(from, to);
    }

    ui.sendMessage("You win!");
  }

  String stackToString (StackInt<Integer> peg) {
    StackInt<Integer> helper = new ArrayStack<Integer>();

    // String to append items to.
    String s = "";
    while (!peg.empty()){
    helper.push(peg.pop());
    }
    while (!helper.empty()) {
		s += peg.push(helper.pop()).toString();
    }
    // EXERCISE:  append the items in peg to s from bottom to top.


    return s;
  }

  void displayPegs () {
    String s = "";
    for (int i = 0; i < pegs.length; i++) {
      char abc = (char) ('a' + i);
      s = s + abc + stackToString(pegs[i]);
      if (i < pegs.length-1)
        s = s + "\n";
    }
    ui.sendMessage(s);
  }

  String getMove () {
    String[] moves = { "ab", "ac", "ba", "bc", "ca", "cb" };
    return moves[ui.getCommand(moves)];
  }

  void move (int from, int to) {
    // EXERCISE:  move one disk form pegs[from] to pegs[to].
    // Don't allow illegal moves.  Send a warning message instead, like:
    // Cannot place 2 on top of 1.  Use ui.sendMessage().
	  if (pegs[from].empty()) {
			ui.sendMessage("Invalid move.");
		} else if (pegs[to].empty() || pegs[from].peek().compareTo(pegs[to].peek()) < 0) {
			pegs[to].push(pegs[from].pop());
		} else {
			ui.sendMessage("Cannot place " + pegs[from].peek() + " on top of " + pegs[to].peek());
		}
  }
  
  static String[] pegNames = { "a", "b", "c" };

  // EXERCISE:  create Goal class.
  class Goal {
    // Data.
	  int howMany;
	  char fromPeg;
	  char toPeg;
    // Constructor.
	  Goal (int howMany, char from, char to) {
	      this.howMany = howMany;
	      this.fromPeg = from;
	      this.toPeg = to;
	  }
      
    public String toString () {
      // Convert to String and return it.
    	return ("Move " + howMany + " disk(s) from " +
                pegNames[fromPeg] + " to " + pegNames[toPeg] + ".");
    }
  }
  

  // EXERCISE:  display contents of a stack of goals
  void displayGoals (StackInt<Goal> goals) {
	  String s = "";
	  StackInt<Goal> helps = new ArrayStack<Goal>();
	  while (!goals.empty())
		  helps.push(goals.pop());
		   while (!helps.empty())
		   {
		  	 
		  	 s = (helps.peek() + "\n" + s);	 
		  	 goals.push(helps.pop());
		   }	  
		   ui.sendMessage(s);
  }
  
  void solve () {
    // EXERCISE
	  StackInt<Goal> goals = (StackInt<Goal>) new ArrayStack();
		goals.push(new Goal(nDisks,'a','c'));
		displayPegs();
		while(!goals.empty()) {
			Goal currentGoal = goals.pop();
			if (currentGoal.howMany == 1) {
				move(currentGoal.fromPeg - 'a',currentGoal.toPeg - 'a');
				displayPegs();
			} else {
				if(currentGoal.fromPeg == 'a' && currentGoal.toPeg == 'b' || currentGoal.fromPeg == 'b' && currentGoal.toPeg == 'a') {
					goals.push(new Goal(currentGoal.howMany -1,'c',currentGoal.toPeg));
					goals.push(new Goal(1,currentGoal.fromPeg,currentGoal.toPeg));
					goals.push(new Goal(currentGoal.howMany -1,currentGoal.fromPeg,'c'));
				} else if(currentGoal.fromPeg == 'b' && currentGoal.toPeg == 'c' || currentGoal.fromPeg == 'c' && currentGoal.toPeg == 'b') {
					goals.push(new Goal(currentGoal.howMany -1,'a',currentGoal.toPeg));
					goals.push(new Goal(1,currentGoal.fromPeg,currentGoal.toPeg));
					goals.push(new Goal(currentGoal.howMany -1,currentGoal.fromPeg,'a'));
				} else {
					goals.push(new Goal(currentGoal.howMany -1,'b',currentGoal.toPeg));
					goals.push(new Goal(1,currentGoal.fromPeg,currentGoal.toPeg));
					goals.push(new Goal(currentGoal.howMany -1,currentGoal.fromPeg,'b'));
				}
			}
		}
	}        
}
