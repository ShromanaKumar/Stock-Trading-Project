import java.util.ArrayList;
import java.util.Scanner;

public class StocksDriver {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String prog = args[0];
		// String prog = sc.nextLine();
		
		Problem1 problem1 = new Problem1();
		Problem2 problem2 = new Problem2();
		Problem3 problem3 = new Problem3();
		int[] resProblem1;
		ArrayList<ArrayList<Integer>> resProblem2;
		ArrayList<ArrayList<Integer>> resProblem3;
		
		switch(prog) {
			case "1": problem1.getInput(); 
					resProblem1 = problem1.bruteForce();
					problem1.printOutput(resProblem1);
					break;
			case "2": problem1.getInput(); 
					resProblem1 = problem1.greedy();
					problem1.printOutput(resProblem1);
					break;
			case "3a": problem1.getInput(); 
					resProblem1 = problem1.dpTopDownDriver();
					problem1.printOutput(resProblem1);
					break;
			case "3b": problem1.getInput(); 
					resProblem1 = problem1.dpBottomUp();
					problem1.printOutput(resProblem1);
					break;
			case "4": problem2.getInput();
					ReturnValueBF2 bfResProblem2 = problem2.bruteForce(null, null, 0, problem2.k, false, 0, new ArrayList<ArrayList<Integer>>());
					problem2.printOutput(bfResProblem2.txnList, true);
					break;
			case "5": problem2.getInput();
					resProblem2 = problem2.dpN2();
					problem2.printOutput(resProblem2, false);
					break;
			case "6a": Task6A task6a = new Task6A();
					task6a.getInput();
					task6a.maximumProfit();
					break;
			case "6b": problem2.getInput();
					resProblem2 = problem2.dpNBottomUp();
					problem2.printOutput(resProblem2, false);
					break;
			case "7": problem3.getInput();
					ReturnValueBF3 bfResProblem3 = problem3.bruteForce(null, null, 0, false, 0, new ArrayList<ArrayList<Integer>>());
					problem3.printOutput(bfResProblem3.txnList, true);
					break;
			case "8": problem3.getInput();
					resProblem3 = problem3.task8(); 
					problem3.printOutput(resProblem3, false);
					break;
			case "9a": Task9A task9a = new Task9A();
                    task9a.getInput();
                    task9a.maximumProfit();
                    break;
			case "9b": problem3.getInput();
					resProblem3 = problem3.task9b(); 
					problem3.printOutput(resProblem3, false);
					break;
		}
	}

}
