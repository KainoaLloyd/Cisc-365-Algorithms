/*
 * THis code is used to complete Assignment 2 for cisc 365
 * 
 * name: Kainoa Lloyd		Student number: 10114858		netID: 13krl1@queensu.ca
 * IN here contains my class for a minHeap of partial soltions, items class and a partial solutions class 
 * to solve a branch and bound 0/1 knapsack problem
 * 
 * 
 */


import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AlienBaB {
	static List<item> sortedItems = new ArrayList<item>();
	static item[] itemArray;
	static int MAX = 0;
	static int GUB;
	public static void main(String[] args) throws Exception {
		//AlienBaB solution = new AlienBaB();
		//AlienBaB.getSortedItems("B+B 2015 Data 20151124 1226  Size 20.txt");
		String name = "B+B 2015 Data 20151124 1226  Size ";
		int number = 20;
		for (int i = 20; i <51; i++){
			getSortedItems(name+i+".txt");
			sortedItems = new ArrayList<item>();
			itemArray = null;
			
		}
		//getSortedItems("B+B 2015 Data 20151124 1226  Size 50.txt");

		
		
	}
	// this method creates the partial solutions and stores them in a heap then takes out each one to see if it is a full solution
	public static void getSortedItems(String filename) throws Exception{
		
		
		String[] theFile = readIn(filename);
		String problem = theFile[0];
		String[] tokensA = theFile[1].split(" +");
		MAX = Integer.parseInt(tokensA[0]);
		int total = Integer.parseInt(tokensA[1].trim());
		//List<item> sortedItems = new ArrayList<item>();
		for (int i = 2; i<theFile.length; i++){
			
			String[] tokens = theFile[i].split(" +");
			item luggage = new item(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()));
			sortedItems.add(luggage);
		}
		//sort items here;
		item[] notSorted = new item[sortedItems.size()];
		for ( item e: sortedItems){
			int i = 0;
			notSorted[i] = new item(e.id,e.value, e.weight);
		}
		Collections.sort(sortedItems);
		itemArray = new item[sortedItems.size()];
		itemArray =sortedItems.toArray(itemArray);
		//intialize partial solution
		minHeap H = new minHeap(2679999);
		//minHeap H = new minHeap((int) Math.pow(total, total/5));
		int[] firstD = new int[sortedItems.size()];
		for ( int e: firstD){
			e = 0;
		}
		partialSolutions firstSol = new partialSolutions(firstD.clone(),0,0, true, 0);
		partialSolutions secondSol = new partialSolutions(firstD.clone(),0,0, false, 0);
		GUB = firstSol.UB;
		//System.out.print(GUB);
		//loop this
		H.add(firstSol);
		H.add(secondSol);
		boolean done = false;
		partialSolutions answer = null;
		partialSolutions best = null;
		while( !done){
			answer = H.remove();
			if (answer.LB == answer.UB){
				best = answer.clone();
				done = true;
				break;
						
			}
			//System.out.println("this is the answer"+ answer.place +  "  CW: " +answer.CW + "   CSF:  " + answer.CSF);
			String dec = "";
			/*for (int e: answer.decisions){
				dec += " "+e;
			}
			System.out.println(dec);*/
			if (answer.place <20){


				partialSolutions taken = new partialSolutions(answer.decisions.clone(), answer.place,answer.CSF, true, answer.CW);
				partialSolutions not = new partialSolutions(answer.decisions.clone(), answer.place,answer.CSF, false, answer.CW);
			//take the sol with the lowest UB
					//System.out.println("reached part A");
				/*if (taken.place >15){
					System.out.println("place is:    " +taken.place);
				}	*/
					if (taken.LB <= GUB && taken.CW<=MAX){
						if(taken.UB<GUB){
							GUB = taken.UB;
						}
						if (best == null && taken.place == 20){
							best = taken.clone();
						}
						/*if(taken.LB == taken.UB && taken.CSF<best.CSF){
							best = taken.clone();
							System.out.println("this is CW"+ taken.CW + " this is place "+best.place);
						}*/
						if(best!= null && taken.CSF<=best.CSF && taken.place == 20){
							best = taken.clone();
							//if (best.UB == best.LB)break;
							//System.out.println("this is CW of taken"+ taken.CW + " this is place "+taken.place+"UB is: "+taken.UB + " LB: "+ taken.LB );
						}
						//System.out.println("reached part B " +taken.CW);
						H.add(taken);
					}
					if (not.LB <= GUB && not.CW<= MAX){
						if(not.UB<GUB){
							GUB = taken.UB;
						}
						if (best == null && not.place == 20){
							best = not.clone();
						}
						/*if(not.LB == not.UB && not.CSF<best.CSF){
							System.out.println("this is CW of not"+ not.CW+ " this is place "+best.place);
							best = not.clone();
						}*/
						if(best != null && not.CSF<best.CSF && not.place == 20){
							//System.out.println("this is CW of not"+ not.CW+ " this is place "+best.place);
							best = not.clone();
						}
					//	System.out.println("reached part C  "+not.CW);
						H.add(not);
					}
					
					/*if (taken.LB == taken.UB && taken.CW<MAX){
						done = true;
						answer = taken;
						System.out.println(taken.CW);
						break;
					}
					if (not.LB == not.UB && not.CW<MAX){
						answer = not;
						done = true;
						System.out.println("cheese");
						break;
					}*/
			}
			if (H.size == 0) done = true;
		}
		System.out.println("  this is problem: "+problem );
		best.toString();
		//System.out.println(" this is CSF of best: "+ best.CSF+ "  this is problem: "+problem + "  Max is " + MAX);
		System.out.println("The number of partial solutions generated: " + H.total);
		//System.out.println("this is place "+best.place);
		//answer.toString();
	}
	
	//method not used
	public int GlobalUpperBound(List<item> allItems, int max){
		int GUB = 0;
		int cost = 0;
		int Cweight = 0;
		for(item e: allItems){
			if (Cweight+e.weight<=max){
				cost+= e.value;
			}else{
				GUB += e.value;
				
			}
		}
		return GUB;
	}
	
	//this class creats an object for items that are used in luggage so that they can be compared by  ratio of value/weight
	public static class item implements Comparable<item>{
		int weight;
		int value;
		double ratio;
		int id;
		public item(int num, int v, int w){
			weight = w;
			value = v;
			ratio = (double)v/(double)w;
			id = num;
		}
		@Override
	
		public int compareTo(item compareItem){
			
			double compareQuantity = ((item)compareItem).ratio;
			//ascending order
			//return (int) (this.ratio-compareQuantity);
			//descending order
			double answer =(compareQuantity-this.ratio);
			if (answer<0) return -1;
			if (answer>0) return 1;
			return 0;
	
		}


	}
		// this class is for holding partial solutions
	public static class partialSolutions{
		int CW;
		int LB;
		int UB;
		int CSF;
		int RCapacity;
		int place;
		int[] decisions;
		//ArrayList decisions = new ArrayList();
		List<item> remItems= new ArrayList<item>();
		//for cloning
		public partialSolutions(partialSolutions whole){
			decisions = whole.decisions.clone();
			place = whole.place-1;
			CSF = whole.CSF;
			CW = whole.CW;
			CUpperBound4();
			CLowerBound();
			place++;
			//CLowerBound();
		}
		//main constructor
		public partialSolutions(int[] d, int p, int nCSF, boolean taken, int weight){
			decisions = d;
			place = p;
			if (taken == true){
				CSF = nCSF;
				CW= weight+ itemArray[p].weight;
				decisions[p]=1;
			}else{
				int cost = itemArray[p].value;
				CSF = cost + nCSF;
				CW = weight;
				
			}

			CUpperBound4();
			CLowerBound();
			//CLowerBound();
			
			place++;
			if(LB == UB){
				place = 20;
			}
			//remItems = allItems;
		}
		
		//calulates lower bound
		public int CLowerBound2(){
			LB = CLowerBound();
			LB += GFC();
			return LB;
		}
		
		//calculates guaranteed future costs
		public int GFC(){
			int cost = 0;
			int index = place+1;
			int c = 0;
			while(index<20){
				
			int lowest = itemArray[index].value;
			int tempW = CW;
				while( tempW <= MAX && index<20){
					if (itemArray[index].weight+CW<MAX){
						tempW += itemArray[index].weight;
						if (itemArray[index].value< lowest){
							lowest = itemArray[index].value;
						}
					}
					index++;
				}
				if (tempW> MAX){
					cost+= lowest;
					tempW = CW;
				}
				c++;
				if (c >20)break;
			}
			return cost;
		}
		
		//calculates lower bound
		public int CLowerBound(){
			LB = CSF;
			int weight = CW;
			for (int i = place+1; i< decisions.length;i++){
				if (itemArray[i].weight+weight>MAX){
					LB+= itemArray[i].value;
				}
				
			}
			return LB;
		}
		//another way to calculate upper bound
		public int CUpperBound4(){
			UB = CSF;


			for (int i = place+1; i <decisions.length; i++){

					UB+=itemArray[i].value;
	
			}
			return UB;
		}
		//another way to calulate upper bound
		public int CUpperBound3(){
			UB = CSF;
			int temp = CSF;
			int weight=0;
			for (int i = 0; i<=place && i<20; i++){
				if (decisions[i] ==1){
					weight+= itemArray[i].weight;
					
				}
			}
			for (int i = place+1; i <decisions.length; i++){
				if (itemArray[i].weight+weight<=MAX){
					if (i%2 == 0){
						weight += itemArray[i].weight;
					}else{
						temp+=itemArray[i].value;
					}
					
				}else{
					temp+=itemArray[i].value;
				}
			}
			if (temp <CUpperBound()  ){
				UB = temp;
			}
			return UB;
		}
		
		//doesn't work
		public int CUpperBound2(){
			UB = CSF;
			int weight=0;
			for (int i = 0; i<=place && i<20; i++){
				if (decisions[i] ==1){
					weight+= itemArray[i].weight;
					
				}
			}
			for (int i = place+1; i <decisions.length; i++){
				if (itemArray[i].weight+weight<=MAX){
					weight += itemArray[i].weight;
				}else{
					UB+=itemArray[i].value;
				}
			}
			
			int second = CSF;
			boolean flag = true;
			int global = UB;
			for (int i = 0;  i <2; i++){
				if (place == 18){
					partialSolutions tempA = new partialSolutions(this.decisions.clone(), this.place, this.CSF, flag, this.CW);
					partialSolutions tempC = new partialSolutions(tempA.decisions.clone(), tempA.place, tempA.CSF, true, tempA.CW);
					partialSolutions tempD = new partialSolutions(tempA.decisions.clone(), tempA.place, tempA.CSF, false, tempA.CW);
					if (tempD.UB< global){
						global = tempD.UB;
					}
					if (tempC.UB < global){
						global = tempC.UB;
					}
				}
				flag = false;
			}
			if (global < UB){
				UB = global;
			}
			return UB;
		}
		
		//calculates upper bound
		public int CUpperBound(){
			UB = CSF;
			int weight=0;
			for (int i = 0; i<=place && i<20; i++){
				if (decisions[i] ==1){
					weight+= itemArray[i].weight;
					
				}
			}
			for (int i = place+1; i <decisions.length; i++){
				if (itemArray[i].weight+weight<=MAX){
					weight += itemArray[i].weight;
				}else{
					UB+=itemArray[i].value;
				}
			}
			return UB;
		}
		
		
		//calculates cost so far
		public int costSoFar(){
			//everything not taken so far
			return 1;
		}
		
		public int VofRemItems(){
			int value = 0;
			for(item e:remItems){
				value += e.value;
			}
			return value;
		}
		@Override
		public String toString(){
			int Tval = 0;
			int Tweight = 0;
			for (int i = 0; i< decisions.length; i++){
				if (decisions[i]==1){
					Tval+=itemArray[i].value;
					Tweight += itemArray[i].weight;
					System.out.println("id: "+ itemArray[i].id+"  weight: " +itemArray[i].weight+ "   value: "+itemArray[i].value);
				}
				
			}
			
			System.out.println("Total value: "+ Tval+ "  Total weight: "+ Tweight + " Max weight: "+ MAX);
			return decisions.toString();
		}
		@Override
		public partialSolutions clone(){
			partialSolutions  twin = new partialSolutions(this);
			return twin;
		}
	}
	// this is a min heap that stores values by LB refer to Dawes' notes for concept of how this minHeap is implemented
	public static class minHeap{
		public partialSolutions[] theHeap;
		int size = 0;
		int total = 0;
		public  minHeap(int size){

				theHeap = new partialSolutions[size];
		}	
			public minHeap(){
				//does nothing
			}
			
		//adds a node
		public void add(partialSolutions node){
			//locate first vacancy
			int vacancy =  this.getVacancy();
			//call this location in p
			int index = vacancy;
			while (index != 1 && theHeap[this.getParent(index)].LB>node.LB){
				theHeap[index]= theHeap[this.getParent(index)];
				index = this.getParent(index);
			}
			while( index!= 1 && theHeap[this.getParent(index)].LB>node.LB && theHeap[this.getParent(index)].UB>node.UB){
				theHeap[index]= theHeap[this.getParent(index)];
				index = this.getParent(index);
				
			}
			theHeap[index] = node;
			size++;
			total++;
			if (size-1 == theHeap.length){
				partialSolutions[] temp = new partialSolutions[size+50];
				for(int i = 0; i<size; i++){
					temp[i] = theHeap[i];
				}
				theHeap = new partialSolutions[size+50];
				theHeap = temp;
			}
		}
		//removes a node
		public partialSolutions remove(){
			partialSolutions min_value = theHeap[1];
			partialSolutions mover = theHeap[this.lastPos()];
			//remove the right-most position in the bottom level
			int temp_pos = 1;
			boolean done = false;
			
			while(!done){
				if (!this.leftChildExists(temp_pos) || !this.rightChildExists(temp_pos)){
					done = true;
				}else{
					int chosen = getLeftChild(temp_pos);
					int rightChild = theHeap[this.getRightChild(temp_pos)].LB;
					int leftChild = theHeap[this.getLeftChild(temp_pos)].LB;
					if (rightChildExists(temp_pos) && (rightChild< leftChild)){
						chosen = this.getRightChild(temp_pos);
					}
					if (mover.LB <= theHeap[chosen].LB){
						done = true;
					}else{
						theHeap[temp_pos]= theHeap[chosen];
						temp_pos = chosen;
					}	
					
				}
			}
			theHeap[temp_pos] =mover;
			size -= 1;
			return min_value;
		}
		public int getParent(int index){
			return index/2;
		}
		public int getLeftChild(int index){
			return index*2;
		}
		public int getRightChild(int index){
			return index*2+1;
		}
		public int getVacancy(){
			return size+1;
		}
		public  int lastPos(){
			return size;
		}
		public boolean leftChildExists(int index){
			if (index*2 > size){
				return false;
			}
			return true;
		}
		public boolean rightChildExists(int index){
			if (index*2+1 > size){
				return false;
			}
			return true;
		}
	}
	
	//file input
	 public static String[]   readIn(String filename) throws Exception {
	      Path filePath = new File(filename).toPath();
	      Charset charset = Charset.defaultCharset();        
	      List<String> stringList = Files.readAllLines(filePath, charset);
	      String[]  StringA = new String[stringList.size()];
	      stringList.toArray(StringA);
	      return StringA;
	  }
}

