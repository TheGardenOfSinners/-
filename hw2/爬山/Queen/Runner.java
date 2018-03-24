import java.io.IOException;



public class Runner {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
			int countSI = 0;
			int countFH = 0;
			int countHC = 0;
			int countRS = 0;
			int stepSI = 0;
			int stepFH = 0;
			int stepHC = 0;
			int stepRS = 0;
			for(int i = 0; i < 1000; i++) {
				
				Queen a1 = new Queen();
				a1.initial();
				Queen a2 = new Queen(a1);
				Queen a3 = new Queen(a1);
				Queen a4 = new Queen(a1);
		
				// 执行模拟退火搜索示例算法
				if(a1.simulated()) {
					stepSI += a1.getTotalTrial();
					countSI++;
					System.out.println("simulated success!!");
				}
				//最陡
				if(a2.hillClimbing()) {
					stepHC += a2.getTotalTrial();
					countHC++;
					System.out.println("Highest-climb success!!");
				}
				//首选
				if(a3.firstchose()) {
					stepFH += a3.getTotalTrial();
					countFH++;
					System.out.println("firstChose success!!");
				}
				//随即重来
				if(a4.randomRestart()) {
					stepRS += a4.getTotalTrial();
					countRS++;
					System.out.println("randomRestart success!!");
				}
			}

			System.out.println("first-Chose[success/total]: " + countFH + "/1000.");
			System.out.println("average steps when success: " + (int)(stepFH/countFH) + ".");
			System.out.println("simulated-Annealing[success/total]: " + countSI + "/1000.");
			System.out.println("average steps when success: " + (int)(stepSI/countSI) + ".");
			System.out.println("Highest-climb[success/total]: " + countHC + "/1000.");
			System.out.println("average steps when success: " + (int)(stepHC/countHC) + ".");
			System.out.println("random-Restart[success/total]: " + countRS + "/1000.");
			System.out.println("average steps when success: " + (int)(stepRS/countRS) + ".");
			
	}
}