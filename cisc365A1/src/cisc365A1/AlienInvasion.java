/*This Program was created for the purpose of the First Marked Assignment for cisc 365
 * 
 * Author: Kainoa Lloyd		10114858	13krl1@queensu.ca
 * 
 * 
 */

package cisc365A1; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.HashSet;


public class AlienInvasion {
	static int begin;
	static int end;
	static int current;
	static int total;
	static HashSet<List<Integer>> powerSet = new HashSet<List<Integer>>();
	
	public static void main(String[] args) throws Exception {
		
		
		Scanner scanner = new Scanner(new File("SpaceStation.txt"));
		int [] theFile = new int [123];
		int j = 0;
		while(scanner.hasNextInt()){
		   theFile[j++] = scanner.nextInt();
		}
		begin = theFile[0];
		end = theFile[1];
		total =theFile[2];
		int index = 0;
		//String[] theFile = readLines("/SpaceStation.txt");
		Project[] allProjects = new Project[total];
		for (int i = 3; i < theFile.length ; i++){
			Project item = new Project(theFile[i++], theFile[i++],theFile[i]);
			allProjects[index] = item;
			/*allProjects[index].id = theFile[i++];
			allProjects[index].start = theFile[i++];
			allProjects[index].finish = theFile[i];*/
			//allProjects[index].cost = allProjects[index].finish-allProjects[index].start;*/
			index++;
		}
		
		Project[] picked = GreedyAlgorithm(allProjects);
		Partition(picked);
	}
	
	

	    public static String[] readLines(String filename) throws IOException {
	        FileReader fileReader = new FileReader(filename);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        List<String> lines = new ArrayList<String>();
	        String line = null;
	        while ((line = bufferedReader.readLine()) != null) {
	            lines.add(line);
	        }
	        bufferedReader.close();
	        return lines.toArray(new String[lines.size()]);
	    }
	//}
	/*This greedy algorithm takes the array of projects, sorts them in ascending order by end time
	 * our assumption is that there is a set that works
	 *  Since it is sorted by start time we increment through the array until we reach a point where the 
	 *  start time of the selected files does not overlap with the max end time of the projects from the array.
	 *  then we add the last one that did over lap and continue up the array until the 
	 *  end time of the selected files (current) is equal to or greater than the overall end time 
	 */
	public static Project[] GreedyAlgorithm(Project List[]){
		current = begin;
		Project[] Selected = new Project[total];
		int i = 0;
		int index = 0;
		//sort array by ascending start time here
		Arrays.sort(List);
		//rest of algorithm
		Project best = List[0];
		while (current < end){
			if (List[i].start <= current && best.finish < end){
				if (List[i].finish >best.finish){
					best = List[i];
				}
				i++;
			}else{
					//System.out.println(best.id);
					Selected[index] = best;
					index++;
					current =best.finish;
					//i++;
				
			}
			/*if (i==40){
				System.out.println("total is: " + total + "list length: " + List.length + " current " +current + " end" + end);
			}
			System.out.println("this is i "+ i +" this is index " + index );*/
		}
		System.out.print("Selected Projects: \n");
		System.out.println("number   start   finish   cost");
		Project[] newSelected = new Project[index];
		for (int j = 0; j<index; j++){
			newSelected[j]= Selected[j];
			System.out.println( Selected[j].id+"      "+Selected[j].start+"      " + Selected[j].finish + "    " + Selected[j].cost);
		}
		System.out.print("\n");
		return newSelected;
	}
	
	/*after all the subsets of the set are built
	 * goes through the subsets 1 at a time and if the 
	 * total cost of that subset is more than the previous subset but less than half 
	 * the cost of all the projects +1 then that's the new top answer
	 * once we go through every subset the top answer will be found then we can just add the 
	 * other projects into account 2 ugg took so long to figure this out ended up with brute force method :'(
	 */
	public static  void Partition (Project List[]){
		
		
		
		//part 1
		int sum = 0;
		int n = List.length;
		int[] projectNum = new int[n];
		for (int i = 0; i<n; i++){
			projectNum[i] = List[i].id;
			sum += List[i].cost;
		};
		int supremeSum = sum;
		sum = sum/2;
		
		List<Integer> mainList = new ArrayList<Integer>(projectNum.length);
		for (int k = 0; k<projectNum.length;k++){
			mainList.add(projectNum[k]);
			
		}
		allSubsets(mainList, mainList.size());
		List<Integer> temp = null;
		//try this
		int last = 0;
			
		for (List<Integer> e: powerSet){
			int t = 0;
			
			for(int i : e){
	
				for( Project P : List){
					if (P.id ==i){
						t += P.cost;
					}
				}
			}
			if (t > last && t< sum+1 ){
				//System.out.println("this is t : " +t + " this is last" + last);
				temp = null;
				temp =  e;
				last = t;
			}
		}
		int c = 0;
		System.out.println("Account one projects will be:");
		for (int e: temp){
			System.out.print(e+"  ");
		}
		boolean inA =false;
		System.out.println("\nAccount two projects will be");
		for (Project P : List){
			inA = false;
			for (int e:temp){
				if (e == P.id){
					inA = true;
				}

			}
			if (!inA){
				c+= P.cost;
				System.out.print(P.id+"  ");
			}
		}
		System.out.println("\ncost of Account 1 : "+ (supremeSum-c) + " cost of Account 2: " +c);
		
		
	}
	
	
	//recursively finds all the subsets of a set by removing one element from the list
	//then calling the function again with that one element missing for every element in the list
	private static void allSubsets(List<Integer> list, int count){
		powerSet.add(list);
		for(int i = 0; i<list.size(); i++){
			List<Integer> temp = new ArrayList <Integer>(list);
			temp.remove(i);
			allSubsets(temp, temp.size());
		}
		//System.out.println("hi");
	}
	public static class Project  implements Comparable <Project>{
		int id;
		int start;
		int finish;
		int cost;
		public Project (int i, int s, int f){
			id = i;
			start = s;
			finish = f;
			cost = finish -start; 
		}
		public Project() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public int compareTo(Project compareProject){
			
			int compareQuantity = ((Project)compareProject).start;
			//ascending order
			return this.start-compareQuantity;
			//descending order
			//return compareQuantity -this.quantity;
		}

		
	}
}
