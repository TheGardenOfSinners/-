
import java.io.IOException;

import jigsaw.Jigsaw;
import jigsaw.JigsawNode;

public class Runner {
	/**测试脚本-1
	 * 实验任务一：利用广度优先搜索，求指定3*3拼图（8-数码问题）的最优解
	 * 要求：不修改脚本内容，程序能够运行，且得出预期结果
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int total = 0;
			// 检查节点维数是否为3
			if(JigsawNode.getDimension() != 3){
				System.out.print("节点维数不正确，请将JigsawNode类的维数dimension改为5");
				return;
			}
			int countSI = 0;
			int countFH = 0;
			int countHC = 0;
			int stepSI = 0;
			int stepFH = 0;
			int stepHC = 0;
			for(int i = 0; i < 1000; i++) {
				
				// 生成目标状态destNode：{9,1,2,3,4,5,6,7,8,0}
				JigsawNode destNode = new JigsawNode(new int[]{9,1,2,3,4,5,6,7,8,0});
				JigsawNode startNode = Jigsaw.scatter(destNode, 100);
		
				// 生成jigsaw对象：设置初始状态节点startNode和目标状态节点destNode
				Jigsaw jigsaw1 = new Jigsaw(startNode, destNode);
				Jigsaw jigsaw2 = new Jigsaw(startNode, destNode);
				Jigsaw jigsaw3 = new Jigsaw(startNode, destNode);
		
				// 执行模拟退火搜索示例算法
				if(jigsaw3.simulated()) {
					stepSI += jigsaw3.getCurrentJNode().getNodeDepth();
					countSI++;
					System.out.println("simulated success!!");
				}
				//最陡
				if(jigsaw2.climbHill()) {
					stepHC += jigsaw2.getCurrentJNode().getNodeDepth();
					countHC++;
					System.out.println("Highest-climb success!!");
				}
				//首选
				if(jigsaw1.firstChose()) {
					stepFH += jigsaw1.getCurrentJNode().getNodeDepth();
					countFH++;
					System.out.println("firstChose success!!");
				}
			}

			System.out.println("first-Chose[success/total]: " + countFH + "/1000.");
			System.out.println("average steps when success: " + (int)(stepFH/countFH) + ".");
			System.out.println("simulated-Annealing[success/total]: " + countSI + "/1000.");
			System.out.println("average steps when success: " + (int)(stepSI/countSI) + ".");
			System.out.println("Highest-climb[success/total]: " + countHC + "/1000.");
			System.out.println("average steps when success: " + (int)(stepHC/countHC) + ".");
			
	}
	
}