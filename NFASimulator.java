import java.io.*;
import java.util.*;

public class NFASimulator {
	
	public static void execute(ArrayList<String> states,ArrayList<String> startStates,ArrayList<String> finalStates, ArrayList<Character> inputChars,ArrayList<Character> uniqueInputChars, ArrayList<ArrayList<Pair>>transitions){
		boolean debug = false;
		
		ArrayList<String> currentStates = new ArrayList<String>(); //keeps track of current states 
		ArrayList<String>  acceptStates = new ArrayList<String> (); // used to list accept states
		
		for(int i = 0; i < startStates.size();i++){
			currentStates.add(startStates.get(i)); //make all start states current states
		}
			
		for(int i = 0; i < inputChars.size();i++){ //process each input character
			
			int uniqueCharIndex = uniqueInputChars.indexOf(inputChars.get(i)); //index of character being read in the unique character set
			
			ArrayList<Pair> listAtIndex = transitions.get(uniqueCharIndex); //list of possible transitions at input character

			ArrayList<Pair> pairsWithCurrState = new ArrayList<Pair>();
			
			for(int j = 0; j < listAtIndex.size();j++){ //go through each transition pair in list
				
				Pair currPair = listAtIndex.get(j); //the current pair we are examining in this loop
				
				if(debug)System.out.println(currPair);
				
				String firstState = listAtIndex.get(j).getFirst(); // first state in pair
				
				boolean hasCurrentState = currentStates.contains(firstState); //true if the transition starts at the current state, set will take of duplicates
				
				if(hasCurrentState){
					pairsWithCurrState.add(currPair); //make a list of valid transition pairs
				}
				
			}
			
			currentStates.clear(); //clear the current states, we're going to replace them with the new ones
			
			for(int k = 0; k < pairsWithCurrState.size();k++){
				
				String nextState = pairsWithCurrState.get(k).getSecond(); // state being transitioned to in pair
				
				if(!currentStates.contains(nextState)){
					currentStates.add(nextState); //transition to the next set of states
				}
				
			}
			
			if(debug)System.out.println();
			
		} //done processing input
		
		for(int i = 0; i < finalStates.size();i++){ //check if we end up in an accept state
			if(currentStates.contains(finalStates.get(i))){
				acceptStates.add(finalStates.get(i));
			}
		}
		
		if(!acceptStates.isEmpty()){ //if we end up in an accept, print them
			System.out.print("accept ");
			for(int i = 0; i < acceptStates.size();i++){
				System.out.print(acceptStates.get(i) + " ");
			}
		}else{ //if we don't, print all final states
			System.out.print("reject ");
			for(int i = 0; i < currentStates.size();i++){
				System.out.print(currentStates.get(i) + " ");
			}
		}
		
		System.out.println();
	}
	
	static class Pair{
		    private String first; //first member of pair
		    private String second; //second member of pair

		    public Pair(String first, String second) {
		        this.first = first;
		        this.second = second;
		    }

		    public void setFirst(String first) {
		        this.first = first;
		    }

		    public void setSecond(String second) {
		        this.second = second;
		    }

		    public String getFirst() {
		        return first;
		    }

		    public String getSecond() {
		        return second;
		    }
		    
		    public String toString(){
		    	return "(" + first + ", " + second + ")";
		    }
		}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		if(args.length < 2){
			System.out.println("Not enough arguments -- Format: <filename>.txt <input string>");
		}else if(args.length > 2){
			System.out.println("Too many arguments -- Format: <filename>.txt <input string>");
		}else{
			File file = new File(args[0]);
			String inputString = args[1];
			
			ArrayList<Character> inputChars = new ArrayList<Character>();
			
			ArrayList<Character> uniqueInputChars = new ArrayList<Character>();
			
			ArrayList<String> states = new ArrayList<String>();
			ArrayList<String> startStates = new ArrayList<String>();
			ArrayList<String> finalStates = new ArrayList<String>();
		
			ArrayList<ArrayList<Pair>> transitions = new ArrayList<ArrayList<Pair>>();
			
			ArrayList<Pair> pairList = new ArrayList<Pair>();
			
			ArrayList<String> pList = new ArrayList<String>();
			ArrayList<String> xList = new ArrayList<String>();
			ArrayList<String> qList = new ArrayList<String>();
			
			String p = "", q = "";
			
			boolean consolePrint = false;
			boolean debug = false;
			
			Scanner input = new Scanner(file);
			int lineCount = 0;
			
			for (int i = 0; i < inputString.length(); i++){
	    	    char c = inputString.charAt(i);   
	    	    if(!uniqueInputChars.contains(c)){
	    	    	uniqueInputChars.add(c);
	    	    }
	    	    inputChars.add(c);
	    	}
			
			
		
			
			while(input.hasNext()) {
				lineCount++;
				if(consolePrint)System.out.print("Line " + lineCount + ": ");;
		    	String nextToken = input.next();
		    	if(consolePrint)System.out.print(nextToken);
		    	String nextLine = input.nextLine();
		    	String str[] = nextLine.split("\\s");
		    	
	    		
		    	for(int i = 0; i < str.length - 1;i++){
		    		if(nextToken.equals("state")){
			    		//make state
			    		if(str[i].equals("start")){
			    			startStates.add(states.get(lineCount-1));
			    		}else if(str[i].equals("accept")){
			    			finalStates.add(states.get(lineCount-1));
			    		}else if(str[i] != null && !str[i].isEmpty() && !states.contains(str[i])){
			    			states.add(str[i]);
			    		}
			    	}else if(nextToken.equals("transition")){
			    		//make transition
			    		if(i == 1){
			    			p = str[1];
			    			pList.add(str[1]);
			    			if(consolePrint)System.out.println("\np: " + str[1]);
			    		}else if(i == 2){
			    			xList.add(str[2]);
			    			if(consolePrint)System.out.println("x: " + str[2]);
			    		}
			    	}
		    		
		    		if(consolePrint)System.out.print(str[i] + ", ");
		    	}
		    	
		    	if(nextToken.equals("state")){
		    		if(str[str.length-1].equals("start")){
		    			startStates.add(states.get(lineCount-1));
		    		}else if(str[str.length-1].equals("accept")){
		    			finalStates.add(states.get(lineCount-1));
		    		}else if(str[str.length-1] != null && !str[str.length-1].isEmpty() && !states.contains(str[str.length-1])){
		    			states.add(str[str.length-1]);

		    		}
		    	}else if(nextToken.equals("transition")){
		    		if(str.length == 4)
		    			q = str[3];
		    			qList.add(str[3]);
		    			if(consolePrint)System.out.println("q: " + str[3]);
		    		
		    	}
		    	
	    		Pair pair;
	    		if(!p.isEmpty() && !q.isEmpty()){
		    		pair = new Pair(p, q);
		    		pairList.add(pair);
		    	}
		    	
		    	
		    	if(consolePrint)System.out.print(str[str.length-1]);
		    	if(consolePrint)System.out.println();

			}
			
			for(int i = 0; i < uniqueInputChars.size(); i++){
				ArrayList<Pair> symbolList = new ArrayList<Pair>();
			
				for(int j = 0; j < pairList.size(); j++){
					if(consolePrint)System.out.print(Character.toString(uniqueInputChars.get(i)) + "--");
					if(consolePrint)System.out.print(xList.get(j) + "--");
					if(consolePrint)System.out.println(pairList.get(j));
					
					if(xList.get(j).equals(Character.toString(uniqueInputChars.get(i)))){
						if(consolePrint)System.out.println("add to list");
						symbolList.add(pairList.get(j));
					}
				}
				if(consolePrint)System.out.println(symbolList);
				
				transitions.add(symbolList);
			}
			
			execute(states,startStates,finalStates,inputChars,uniqueInputChars,transitions);

		}
	}
	
	

}
