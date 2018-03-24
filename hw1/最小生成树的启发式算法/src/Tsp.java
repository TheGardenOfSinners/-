import java.util.*;
import java.io.*;
import java.lang.*;

public class Tsp {
	private static final String TESTCASE = "./../testcase/test1.txt";

//	private static final String TESTCASE = "./../testcase/test2.txt";

	public static void main(String[] args) throws IOException {
		try {
    		File fileName = new File(TESTCASE);
    		Scanner sc = new Scanner(fileName);
    		String road;
    		String[] roadStr;
    		RoadAndPosSet g1 = new RoadAndPosSet();
    		RoadAndPosSet g2 = new RoadAndPosSet();
    		boolean flag = false;
    		while(sc.hasNextLine()) {
    			road = sc.nextLine();
    			System.out.println(road);
    			roadStr = road.split(" ");
    			Road a = new Road();
    			flag = a.setRoad(roadStr);
    			g1.addRoad(a);
    			flag = false;
    		}
    		System.out.println("The Tree in the begin:");
    		g1.printTree();
    		g2.mstSearch(g1);
    		System.out.println("The MST:");
    		g2.printTree();


    	} catch (Exception e) {
    		System.out.println("read data wrong!!");
    	}

	}
}