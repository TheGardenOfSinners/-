import java.util.*;

public class RoadAndPosSet {
	private ArrayList<Road> roadSet;
	private ArrayList<String> posSet;

	public RoadAndPosSet() {
		roadSet = new ArrayList<Road>();
		posSet = new ArrayList<String>();
	}

	public ArrayList<Road> getRoadSet() {
		return roadSet;
	}

	public ArrayList<String> getPosSet() {
		return posSet;
	}

	private boolean roadExist(Road road1) {
		if(road1.toItself()) {
			System.out.println("this Road is to itself!!");
			return false;
		}
		return true;
	}

	public boolean addRoad(Road road1) {
		if(!roadExist(road1)) {
			return false;
		}
		addPos(road1);
		for(int i = 0; i < roadSet.size(); i++) {
			if(road1.shorterThan(roadSet.get(i))) {
				roadSet.add(i, road1);
				return true;
			}
		}
		roadSet.add(road1);
		return true;
	}

	private void addPos(Road road1) {
		boolean flag = true;
		for(int i = 0;i < posSet.size(); i++) {
			if(posSet.get(i).equals(road1.getPos1())){
				flag = false;
				break;
			}
		}
		if(flag) {
			posSet.add(road1.getPos1());
		}
		flag = true;
		for(int i = 0;i < posSet.size(); i++) {
			if(posSet.get(i).equals(road1.getPos2())){
				flag = false;
				break;
			}
		}
		if(flag) {
			posSet.add(road1.getPos2());
		}
		return;
	}

	private boolean roadGoOutside(Road road1) {
        boolean flag1 = true;
        boolean flag2 = true;
        for(int i = 0;i < posSet.size(); i++) {
			if(posSet.get(i).equals(road1.getPos1())){
				flag1 = false;
				break;
			}
		}
		for(int i = 0;i < posSet.size(); i++) {
			if(posSet.get(i).equals(road1.getPos2())){
				flag2 = false;
				break;
			}
		}
		if(flag1 == true && flag2 == false)
			return true;
		if(flag2 == true && flag1 == false)
			return true;
		return false;
	}

	public boolean samePosNum(RoadAndPosSet another) {
		return posSet.size() == another.getPosSet().size();
	}

	public boolean mstSearch(RoadAndPosSet another) {
		if(another.getPosSet().isEmpty()) {
			System.out.println("Your target is Empty!!\nFail to Search!!");
			return false;
		}
		if(this.posSet.isEmpty()) {
			this.addRoad(another.getRoadSet().get(0));
		}
		boolean flag1 = true;
		for(int i = 0; i < another.getRoadSet().size(); i++) {
			if(roadGoOutside(another.getRoadSet().get(i))) {
				this.addRoad(another.getRoadSet().get(i));
				flag1 = false;
				break;
			}
		}
		if(flag1) {
			System.out.println("Cannot find a road go out side from this tree!!");
			return false;
		}
		if(this.samePosNum(another)) {
			System.out.println("Success!!");
			return true;
		}
		return this.mstSearch(another);
	}

	public void printTree() {
		System.out.println("the tree is:\nPosition Set:");
		for(int i = 0; i < posSet.size(); i++) {
			System.out.printf("%s ", posSet.get(i));
		}
		System.out.println("\nRoad Set:");
		for(int i = 0; i < roadSet.size(); i++) {
			roadSet.get(i).printRoad();
		}
		System.out.println();
	}
}