package jigsaw;
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.PrintWriter;  
import java.util.Iterator;  
import java.util.Vector;  
  
/** 在此类中填充算法，完成重拼图游戏（N-数码问题） 
 * @author zhengkaipei 
 * 
 */  
public class Jigsaw {  
    JigsawNode beginJNode;      // 拼图的起始状态节点  
    JigsawNode endJNode;        // 拼图的目标状态节点  
    JigsawNode currentJNode;    // 拼图的当前状态节点  
    private Vector<JigsawNode> openList;  // open表 ：用以保存已发现但未访问的节点  
    private Vector<JigsawNode> closeList; // close表：用以保存已访问的节点  
    private Vector<JigsawNode> solutionPath;// 解路径  ：用以保存从起始状态到达目标状态的移动路径中的每一个状态节点  
    private boolean isCompleted;    // 完成标记：初始为false;当求解成功时，将该标记至为true  
    private int searchedNodesNum;   // 已访问节点数： 用以记录所有访问过的节点的数量  
  
    public Jigsaw() {

    }

    /**拼图构造函数 
     * @param bNode - 初始状态节点 
     * @param eNode - 目标状态节点 
     */  
    public Jigsaw(JigsawNode bNode, JigsawNode eNode) {  
        this.beginJNode = new JigsawNode(bNode);  
        this.endJNode = new JigsawNode(eNode);  
        this.currentJNode = new JigsawNode(bNode);  
        this.openList = new Vector<JigsawNode>();  
        this.closeList = new Vector<JigsawNode>();  
        this.solutionPath = null;
        this.isCompleted = false;
        this.searchedNodesNum = 0;  
    }  
  
    /**此函数用于打散拼图：将输入的初始状态节点jNode随机移动len步，返回其打散后的状态节点 
     * @param jNode - 初始状态节点 
     * @param len - 随机移动的步数 
     * @return 打散后的状态节点 
     */  
    public static JigsawNode scatter(JigsawNode jNode, int len) {  
        int randomDirection;  
        len += (int) (Math.random() * 2);  
        JigsawNode jigsawNode = new JigsawNode(jNode);  
        for (int t = 0; t < len; t++) {  
            int[] movable = jigsawNode.canMove();  
            do{randomDirection = (int) (Math.random() * 4);}  
            while(0 == movable[randomDirection]);  
            jigsawNode.move(randomDirection);  
        }  
        jigsawNode.setInitial();  
        return jigsawNode;  
    }  
  
    /**获取拼图的当前状态节点 
     * @return currentJNode -  拼图的当前状态节点 
     */  
    public JigsawNode getCurrentJNode() {  
        return currentJNode;  
    }  
  
    /**设置拼图的初始状态节点 
     * @param jNode - 拼图的初始状态节点 
     */  
    public void setBeginJNode(JigsawNode jNode) {  
        beginJNode = jNode;  
    }  
  
    /**获取拼图的初始状态节点 
     * @return beginJNode - 拼图的初始状态节点 
     */  
    public JigsawNode getBeginJNode() {  
        return beginJNode;  
    }  
  
    /**设置拼图的目标状态节点 
     * @param jNode - 拼图的目标状态节点 
     */  
    public void setEndJNode(JigsawNode jNode) {  
        this.endJNode = jNode;  
    }  
  
    /**获取拼图的目标状态节点 
     * @return endJNode - 拼图的目标状态节点 
     */  
    public JigsawNode getEndJNode() {  
        return endJNode;  
    }  
  
    /**获取拼图的求解状态 
     * @return isCompleted - 拼图已解为true；拼图未解为false 
     */  
    public boolean isCompleted() {  
        return isCompleted;  
    }  
  
    /**计算解的路劲 
     * @return 若有解，则将结果保存在solutionPath中，返回true; 若无解，则返回false 
     */  
    private boolean calSolutionPath() {  
        if (!this.isCompleted()) {  
            return false;  
        } else {  
            JigsawNode jNode = this.currentJNode;  
            solutionPath = new Vector<JigsawNode>();  
            while (jNode != null) {  
                solutionPath.addElement(jNode);  
                jNode = jNode.getParent();  
            }  
            return true;  
        }  
    }  
  
    /**获取解路径文本 
     * @return 解路径solutionPath的字符串，若有解，则分行记录从初始状态到达目标状态的移动路径中的每一个状态节点； 
     * 若未解或无解，则返回提示信息。 
     */  
    public String getSolutionPath() {  
        String str = new String();  
        str += "Begin->";  
        if (this.isCompleted) {  
            for (int i = solutionPath.size()-1; i>=0; i--) {  
                str += solutionPath.elementAt(i).toString() + "->";  
            }  
            str+="End";  
        } else  
            str = "Jigsaw Not Completed.";  
        return str;  
    }  
  
    /**获取访问过的节点数searchedNodesNum 
     * @return 返回所有已访问过的节点总数 
     */  
    public int getSearchedNodesNum() {  
        return searchedNodesNum;  
    }  
      
    /**将搜索结果写入文件中，同时显示在控制台 
     * 若搜索失败，则提示问题无解，输出已访问节点数； 
     * 若搜索成功，则输出初始状态beginJnode，目标状态endJNode，已访问节点数searchedNodesNum，路径深度nodeDepth和解路径solutionPath。 
     * @param pw - 文件输出PrintWriter类对象，如果pw为null，则写入到D://Result.txt 
     * @throws IOException 
     */  
    public void printResult(PrintWriter pw) throws IOException{  
        boolean flag = false;  
        if(pw == null){  
            pw = new PrintWriter(new FileWriter("Result.txt"));// 将搜索过程写入D://BFSearchDialog.txt  
            flag = true;  
        }  
        if (this.isCompleted == true) {  
            // 写入文件  
            pw.println("Jigsaw Completed");  
            pw.println("Begin state:" + this.getBeginJNode().toString());  
            pw.println("End state:" + this.getEndJNode().toString());  
            pw.println("Solution Path: ");  
            pw.println(this.getSolutionPath());  
            pw.println("Total number of searched nodes:" + this.getSearchedNodesNum());  
            pw.println("Length of the solution path is:" + this.getCurrentJNode().getNodeDepth());  
  
              
            // 输出到控制台  
            System.out.println("Jigsaw Completed");  
            System.out.println("Begin state:" + this.getBeginJNode().toString());  
            System.out.println("End state:" + this.getEndJNode().toString());  
            System.out.println("Solution Path: ");  
            System.out.println(this.getSolutionPath());  
            System.out.println("Total number of searched nodes:" + this.getSearchedNodesNum());  
            System.out.println("Length of the solution path is:" + this.getCurrentJNode().getNodeDepth());  
  
              
        }   
        else {  
            // 写入文件  
            pw.println("No solution. Jigsaw Not Completed");  
            pw.println("Begin state:" + this.getBeginJNode().toString());  
            pw.println("End state:" + this.getEndJNode().toString());  
            pw.println("Total number of searched nodes:"  
                    + this.getSearchedNodesNum());  
              
            // 输出到控制台  
            System.out.println("No solution. Jigsaw Not Completed");  
            System.out.println("Begin state:" + this.getBeginJNode().toString());  
            System.out.println("End state:" + this.getEndJNode().toString());  
            System.out.println("Total number of searched nodes:"  
                    + this.getSearchedNodesNum());  
        }  
        if(flag)  
            pw.close();  
    }  
  
    /**探索所有与jNode邻接(上、下、左、右)且未曾被访问的节点 
     * @param jNode - 要探索的节点 
     * @return 包含所有与jNode邻接且未曾被访问的节点的Vector<JigsawNode>对象 
     */  
    private Vector<JigsawNode> findFollowJNodes(JigsawNode jNode) {  
        Vector<JigsawNode> followJNodes = new Vector<JigsawNode>();  
        JigsawNode tempJNode;  
        for(int i=0; i<4; i++){  
            tempJNode = new JigsawNode(jNode);  
            if(tempJNode.move(i) && !this.closeList.contains(tempJNode) && !this.openList.contains(tempJNode))  
                followJNodes.addElement(tempJNode);  
        }  
        return followJNodes;  
    }  
  
    /**排序插入openList：按照节点的代价估值(estimatedValue)与当前步数之和将节点插入openList中，和小的靠前。 
     * @param jNode - 要插入的状态节点 
     */  
    private void sortedInsertOpenList(JigsawNode jNode) {  
        this.estimateValue(jNode);  
        for (int i = 0; i < this.openList.size(); i++) {  
            if ((jNode.getEstimatedValue() + jNode.getNodeDepth()) < (this.openList.elementAt(i)  
                    .getEstimatedValue() + this.openList.elementAt(i).getNodeDepth())) {  
                this.openList.insertElementAt(jNode, i);  
                return;  
            }  
        }  
        this.openList.addElement(jNode);  
    }  
      
    
    /**（Demo+实验二）启发式搜索。访问节点数大于30000个则认为搜索失败。 
     * 函数结束后：isCompleted记录了求解完成状态； 
     *           closeList记录了所有访问过的节点； 
     *           searchedNodesNum记录了访问过的节点数； 
     *           solutionPath记录了解路径。 
     *  搜索过程和结果会记录在./DemoASearchDialog.txt中。 
     * @return 搜索成功返回true,失败返回false 
     * @throws IOException 
     */  
    public boolean ASearch() throws IOException{  
        // 将搜索过程写入ASearchDialog.txt  
        String filePath = "./ASearchDialog.txt";  
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));  
          
        // 访问节点数大于25000个则认为搜索失败  
        int maxNodesNum = 30000;    
          
        // 用以存放某一节点的邻接节点  
        Vector<JigsawNode> followJNodes = new Vector<JigsawNode>();   
          
        // 重置求解完成标记为false  
        isCompleted = false;             
          
        // (1)将起始节点放入openList中  
        this.sortedInsertOpenList(this.beginJNode);  
          
        // (2) 如果openList为空，或者访问节点数大于maxNodesNum个，则搜索失败，问题无解;否则循环直到求解成功  
        while (this.openList.isEmpty() != true && searchedNodesNum <= maxNodesNum) {  
              
            // (2-1)访问openList的第一个节点N，置为当前节点currentJNode  
            //若currentJNode为目标节点则搜索成功，设置完成标记isCompleted为true，计算解路径，退出。  
            this.currentJNode = this.openList.elementAt(0);  
            if (this.currentJNode.equals(this.endJNode)){
                isCompleted = true;
                this.calSolutionPath();
                break; 
            } 
              
            // (2-2)从openList中删除节点N,并将其放入closeList中，表示以访问节点           
            this.openList.removeElementAt(0);  
            this.closeList.addElement(this.currentJNode);  
            searchedNodesNum++;  
              
                // 记录并显示搜索过程  
                pw.println("Searching.....Number of searched nodes:" + this.closeList.size() + "   Current state:" + this.currentJNode.toString());  
                System.out.println("Searching.....Number of searched nodes:" + this.closeList.size() + "   Current state:" + this.currentJNode.toString());           
  
            // (2-3)寻找所有与currentJNode邻接且未曾被访问的节点，将它们按代价估值从小到大排序插入openList中  
            followJNodes = this.findFollowJNodes(this.currentJNode);  
            while (!followJNodes.isEmpty()) {  
                this.sortedInsertOpenList(followJNodes.elementAt(0));
                followJNodes.removeElementAt(0);  
            }  
  
            if( openList.size() > 2000 ){  
                int init = openList.size();  
                for (int i = init-1; i >= init - 100 ; i-- ) {  
                    openList.removeElementAt(i);  
                }  
            }  
        }  
          
        this.printResult(pw);   // 记录搜索结果
        pw.close();             // 关闭输出文件
        System.out.println("Record into " + filePath);  
        return isCompleted;  
    }  


     
    /**
     * Nillsion算法
     * 代价估计值:f(n)=m(n) + 3 * s(n)。
     * @param jNode - 要计算代价估计值的节点；此函数会改变该节点的estimatedValue属性值。 
     */ 
    private void estimateValue(JigsawNode jNode) {
        int dimension = jNode.getDimension();
//        int s =  3 * Nillsonsequence(jNode);
//        int s = Hannson(jNode);
        int s = JackMostow(jNode);

        int cirDis = 0;
        for(int i = 1; i <= dimension*dimension; i++){  
            for(int j = 1; j <= dimension*dimension; j++ ){  
                if(jNode.getNodesState()[i] != 0 && jNode.getNodesState()[i] == getEndJNode().getNodesState()[j]){
                    int x = Math.abs((i-1)/dimension - (j-1)/dimension);
                    int y = Math.abs((i+4)%dimension - (j+4)%dimension);
                    cirDis += (x + y); 
                    break;  
                }
            }
        }
        int total = (int)(cirDis + s);
        jNode.setEstimatedValue(total);
    }

    /**
     * Nillson sequence
     * @param  jNode intput jNode
     * @return s mean the score he get;
     */
    private int Nillsonsequence(JigsawNode jNode) {
        int s = 0;
        int dimension = jNode.getDimension();
        int max = dimension*dimension-1;
        int[] state = {0, 1, 2, 5, 8, 7, 6, 3, 4};
        int[] stateFan = new int[dimension*dimension];
        for(int i = 0; i < dimension*dimension; i++) {
            stateFan[state[i]] = i;
        }
        int[] match = new int[dimension*dimension];
        for(int i = 0; i < dimension * dimension; i++) {
            match[i] = endJNode.getNodesState()[i + 1];
        }
        for(int i = 0; i < dimension * dimension; i++) {
            if(i == max) {
                if(match[state[i]] == jNode.getNodesState()[state[i]]+1)s++;
            } else {
                int k = jNode.getNodesState()[state[i] + 1];
                int k1 = jNode.getNodesState()[(state[(i + 1)%max]) + 1];
                if(match[state[(stateFan[k]+1)%max]] != k1)
                    s+=2;
            }
        }
        return s;
    }

    /**
     * Hannson的算法
     * 线性冲突
     * @param jNode intput jNode
     * @return lc mean linean confliction
     */
    private int Hannson(JigsawNode jNode) {
        int dimension = jNode.getDimension();
        int lc = 0;
        for(int i = 1; i <= dimension*dimension;i++) {
            for(int j = 1; j <= dimension*dimension; j++) {
                if(jNode.getNodesState()[i] != 0 && jNode.getNodesState()[i] == getEndJNode().getNodesState()[j]) {
                    int xi = (i-1) / dimension;
                    int xj = (j-1) / dimension;
                    int yj = (j-1) % dimension;
                    int yi = (i-1) % dimension;
                    if(xi == xj) {
                        for(int k = dimension * xi + 1; k <= dimension*(xi+1); k++)
                            if(k != i)
                                for(int g = 1; g <= dimension * dimension; g++)
                                    if(jNode.getNodesState()[k] != 0 && jNode.getNodesState()[k] == getEndJNode().getNodesState()[g]) {
                                        int xk = (k-1)/dimension;
                                        int xg = (g-1)/dimension;
                                        if(xk == xg) {
                                            if(differfenceSign(i - k, j - k))lc++; 
                                        }
                                    }
                    } else if(yj == yi) {
                        for(int k = yi + 1; k <= dimension*dimension; k += dimension)
                            if(k != i)
                                for(int g = 1; g <= dimension * dimension; g++)
                                    if(jNode.getNodesState()[k] != 0 && jNode.getNodesState()[k] == getEndJNode().getNodesState()[g]) {
                                        int yk = (k-1)/dimension;
                                        int yg = (g-1)/dimension;
                                        if(yk == yg) {
                                            if(differfenceSign(i - k, j - k))lc++;
                                        }
                                    }
                    }
                }
            }
        }
        return lc;
    }

    /**
     * 看两个是数字符号是否一样
     * @param a 数字
     * @param b 数字
     */
    private boolean differfenceSign(int a, int b) {
        if(a > 0 && b >= 0)return false;
        if(a < 0 && b <= 0)return false;
        return true;
    }
    
    private int JackMostow(JigsawNode jNode) {
        int dimension = jNode.getDimension();
        int s = 0;
        int[] match = new int[dimension*dimension + 1];
        for(int i = 0;i < dimension * dimension; i++) {
            match[endJNode.getNodesState()[i]] = i;
        }
        int[] tmp = new int[dimension*dimension + 1];
        for(int i = 0; i <= dimension * dimension; i++) {
            tmp[i] = jNode.getNodesState()[i];
        }
        for(int i = 1;i < dimension * dimension; i++) {
            int posx = tmp[0] / dimension;
            int posnow = i / dimension;
            int posmatch = match[tmp[i]] / dimension;
            if(posnow != posmatch && i != tmp[0]) {
                if(posmatch == posx) {
                    tmp[tmp[0]] = tmp[i];
                    tmp[0] = i;
                    tmp[i] = 0;
                    s++;
                    i = 0;
                }
            }
        }
        for(int i = 0; i <= dimension * dimension; i++) {
            tmp[i] = jNode.getNodesState()[i];
        }
        for(int i = 1; i < dimension * dimension; i++) {
            int posy = tmp[0] % dimension;
            int posnow = i % dimension;
            int posmatch = match[tmp[i]] % dimension;
            if(posnow != posmatch && i != tmp[0]) {
                if(posmatch == posy) {
                    tmp[tmp[0]] = tmp[i];
                    tmp[0] = i;
                    tmp[i] = 0;
                    s++;
                    i = 0;
                }
            }
        }
        return s;
    }
}