import java.io.FileWriter;  
import java.io.IOException;  
import java.io.PrintWriter;  
import java.util.*;


public class Queen {
    private int[][] queens;
    private int[][] temp;
    private int totalTrial;

    public Queen() {
        queens = new int[8][8];
        totalTrial = 0;
        temp = new int[8][8];
    }
    public Queen(Queen a) {
        this.setQueens(a.getQueens());
        totalTrial = a.getTotalTrial();
        temp = new int[8][8];
    }

    public int[][] getQueens() {
        return queens;
    }

    public int[][] getTemp() {
        return temp;
    }

    public int getTotalTrial() {
        return totalTrial;
    }

    public void setTotalTrial(int a) {
        totalTrial = a;
    }

    public void setQueens(int[][] a) {
        queens = new int[8][8];
        for(int i = 0; i < 8;i++) {
            for(int j = 0; j < 8; j++) {
                queens[i][j] = a[i][j];
            }
        }
        
    }

    public void setTemp(int[][] a) {
        temp = a;
    }

    public void initial() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; j++) {
                queens[i][j] = 0;
            }
        }
        for (int i = 0; i < 8; i++) {
            int num = new Random().nextInt(8);
            queens[i][num] = 1;
        }
    }

    public void print() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; j++)
                System.out.printf("%d ", queens[i][j]);
            System.out.println();
        }
    }

    // 统计在该位置下所有皇后的冲突个数
    public int findCollision(int row, int col) {
        int count = 0;
        // 该位置为1
        temp[row][col] = 1;
        for (int k = 0; k < 64; k++) {
            if (temp[k/8][k%8] == 1) {
                for (int i = 0; i < 8; i++)                             // 同一列
                    if (i != k/8 && temp[i][k%8] == 1)
                        count++;
                for (int i = k/8, j = k%8; i < 8 && j < 8; i++, j++)    // 右下方
                    if (i != k/8 && temp[i][j] == 1) 
                        count++;
                for (int i = k/8, j = k%8; i >= 0 && j >= 0; i--, j--)  // 左上方
                    if (i != k/8 && temp[i][j] == 1) 
                        count++;
                for (int i = k/8, j = k%8; i < 8 && j >= 0; i++, j--)   // 左下方
                    if (i != k/8 && temp[i][j] == 1) 
                        count++;
                for (int i = k/8, j = k%8; i >= 0 && j < 8; i--, j++)   // 右上方
                    if (i != k/8 && temp[i][j] == 1)
                        count++;
            }
        }
        temp[row][col] = 0;  // 复原位置
        return count/2;
    }

    public boolean check(int[][] h) {
        for (int i = 0; i < 8; i++) {
            boolean flag = false;
            for (int j = 0; j < 8; j++) {
                if (queens[i][j] == 1 && h[i][j] == 0) { //皇后所在位置没有冲突
                    flag = true;
                    break;
                }
            }
            if (!flag) { // 皇后所在位置仍有冲突，还需要继续查找
                return false;
            }
        }
        return true;
    }

    // 最陡爬山法
    public boolean hillClimbing() {
        // 尝试次数大于100则判定为无解
        for (int trial = 0; trial <= 100; trial++) {
            // 拷贝原始棋盘数据到temp
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    temp[i][j] = queens[i][j];
                }
            }
            int h[][] = new int[8][8];
            int minH = 9999;
            int minX = 0;int minY = 0;
            int curState = 8;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    // 在计算h(i, j)之前，对i行所有位置赋值为0
                    for (int k = 0; k < 8; k++)
                        temp[i][k] = 0;
                    // 查找h(i, j)
                    h[i][j] = findCollision(i, j);
                    // 当前状态的h值
                    if (queens[i][j] == 1) {
                        curState = h[i][j];
                    }
                    // 先找出冲突个数最小的位置
                    if (h[i][j] < minH) {
                        minH = h[i][j];
                        minX = i;
                        minY = j;
                    }
                    // 计算h(i,j)之后要复原数据，避免计算错误
                    for (int k = 0; k < 8; k++)
                        temp[i][k] = queens[i][k];
                }
            }

            // 将皇后放在该行冲突最少的位置处
            if (curState > minH) {
                for (int i = 0; i < 8; i++)
                    queens[minX][i] = 0;
                queens[minX][minY] = 1;
            }

            // 判断是否找到解, 有解则返回值为真
            if (check(h)) {
                totalTrial += trial;
                return true;
            }
        }
        return false;
    }

    // 首选爬山法
    boolean firstchose() {
        // 尝试次数大于100则判定为无解
        for (int trial = 0; trial <= 100; trial++) {
            // 拷贝原始棋盘数据到temp
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    temp[i][j] = queens[i][j];
                }
            }
            int h[][] = new int[8][8];
            int curState = 8;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    // 在计算h(i, j)之前，对i行所有位置赋值为0
                    for (int k = 0; k < 8; k++)
                        temp[i][k] = 0;
                    // 查找h(i, j)
                    h[i][j] = findCollision(i, j);
                    // 当前状态的h值
                    if (queens[i][j] == 1) {
                        curState = h[i][j];
                    }
                    // 计算h(i,j)之后要复原数据，避免计算错误
                    for (int k = 0; k < 8; k++)
                        temp[i][k] = queens[i][k];
                }
            }

            // 随机选取第一个优于当前状态的下一状态
            boolean better = false;
            int next = new Random().nextInt(64);
            int nextState = 8;
            int times = 0;
            while (!better) {
                next = new Random().nextInt(64);
                nextState = h[next/8][next%8];
                if (nextState < curState) {
                    better = true;
                }
                if (++times > 100) break;
            }

            if (better) {
                for (int i = 0; i < 8; i++)
                    queens[next/8][i] = 0;
                queens[next/8][next%8] = 1;  // 放置皇后
            }

            // 判断是否找到解, 有解则返回值为真
            if (check(h)) {
                totalTrial += trial;
                return true;
            }
        }
        return false;
    }

    // 模拟退火搜索 
    public boolean simulated() {
        double temperature = 5;
        int trial = 0;
        while (temperature > 0.00001) {
            // 拷贝原始棋盘数据到temp
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    temp[i][j] = queens[i][j];
                }
            }
            int h[][] = new int[8][8];
            int curState = 8;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    // 在计算h(i, j)之前，对i行所有位置赋值为0
                    for (int k = 0; k < 8; k++)
                        temp[i][k] = 0;
                    // 查找h(i, j)
                    h[i][j] = findCollision(i, j);
                    // 当前状态的h值
                    if (queens[i][j] == 1) {
                        curState = h[i][j];
                    }
                    // 计算h(i,j)之后要复原数据，避免计算错误
                    for (int k = 0; k < 8; k++)
                        temp[i][k] = queens[i][k];
                }
            }

            // 随机选取一个下一状态
            boolean better = false;
            int next = new Random().nextInt(64);
            int nextState = h[next/8][next%8];
            int times = 0;

            
            int e1 = nextState - curState;
            
            if (e1 < 0) {                    // 下一状态优于当前状态
                better = true;
            } else if (Math.exp((-1)*e1/temperature) > (new Random().nextDouble())) { // 以一定的概率选取
                better = true;
            }

            if (better) {
                for (int i = 0; i < 8; i++)
                    queens[next/8][i] = 0;
                queens[next/8][next%8] = 1;  // 放置皇后
                trial++;
            }


            // 判断是否找到解, 有解则返回值为真
            if (check(h)) {
                totalTrial += trial;
                return true;
            }

            temperature *= 0.99;
        }
        return false;
    }

    // 随机重新开始爬山法
    boolean randomRestart() {
        boolean find = false;
        while (!find) {
            initial();
            find = hillClimbing();
        }
        return find;
    }

}

