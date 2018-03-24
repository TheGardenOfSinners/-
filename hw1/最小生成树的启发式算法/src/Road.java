import java.util.*;

public class Road {
	private String pos1;
	private String pos2;
	private int length;

	public Road() {
		pos1 = null;
		pos2 = null;
		length = -1;
	}

	public boolean setRoad(String[] roadStr) throws NumberFormatException{
		if(roadStr.length != 3) {
			System.out.println("Input is Wrong!!");
			return false;
		}
		pos1 = roadStr[0];
		pos2 = roadStr[1];
		length = Integer.valueOf(roadStr[2]);
		return true;
	}

	public String getPos1() {
		return pos1;
	}

	public String getPos2() {
		return pos2;
	}

	public int getLength() {
		return length;
	}

	public boolean toItself() {
		return pos2 == pos1;
	}

	public void printRoad() {
		System.out.printf("(%s,%s,%d)", pos1, pos2, length);
	}

	public boolean shorterThan(Road another) {
		return (length < another.getLength());
	}
}