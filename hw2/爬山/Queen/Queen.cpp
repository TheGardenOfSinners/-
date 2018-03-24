#include <iostream>
#include <time.h>
#include <stdlib.h>
#include <algorithm>
// #include <windows.h>
#include <cmath>
using namespace std;

int queens[8][8];  // 8*8棋盘
int temp[8][8];
int totalTrial;   // 统计移动步数

// 随机生成初始状态
void initial() {
    for (int i = 0; i < 8; ++i) {
        for (int j = 0; j < 8; j++) {
            queens[i][j] = 0;
        }
    }
    for (int i = 0; i < 8; i++) {
        int num = rand() % 8;
        queens[i][num] = 1;
    }
}

void print() {
    for (int i = 0; i < 8; ++i) {
        for (int j = 0; j < 8; j++)
            cout << queens[i][j] << " ";
        cout << endl;
    }
}

// 统计在该位置下所有皇后的冲突个数
int findCollision(int row, int col) {
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

bool check(int h[8][8]) {
    for (int i = 0; i < 8; i++) {
        bool flag = false;
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

int ct = 0;

// 爬山法
bool hillClimbing() {
    // 尝试次数大于100则判定为无解
    for (int trial = 0; trial <= 100; trial++) {
        // 拷贝原始棋盘数据到temp
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                temp[i][j] = queens[i][j];
            }
        }
        int h[8][8];
        int minH = 9999, minX = 0, minY = 0, curState;
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
bool firstchose() {
    // 尝试次数大于100则判定为无解
    for (int trial = 0; trial <= 100; trial++) {
        // 拷贝原始棋盘数据到temp
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                temp[i][j] = queens[i][j];
            }
        }
        int h[8][8], curState;

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
        bool better = false;
        int next, nextState, times = 0;
        while (!better) {
            next = rand() % 64;
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
bool simulated() {
    double temperature = 5;
    int trial = 0;
    while (temperature > 0.00001) {
        // 拷贝原始棋盘数据到temp
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                temp[i][j] = queens[i][j];
            }
        }
        int h[8][8], curState;
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
        bool better = false;
        int next, nextState, times = 0;

        next = rand() % 64;
        nextState = h[next/8][next%8];
        int E = nextState - curState;
        if (E < 0) {
            better = true;
        } else if (exp((-1)*E/temperature) > ((double)(rand() % 1000) / 1000)) {
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

// 最陡上升爬山法
int steepestAscent() {
    int count = 0;
    for (int i = 0; i < 1000; i++) {
        initial();
        if (hillClimbing())
            count++;
    }
    return count;
}

// 首选爬山法
int firstChose() {
    int count = 0;
    for (int i = 0; i < 1000; i++) {
        initial();
        if (firstchose())
            count++;
    }
    return count;
}

// 随机重新开始爬山法
int randomRestart() {
    bool find = false;
    while (!find) {
        initial();
        find = hillClimbing();
    }
    return find;
}

// 模拟退火搜索
int simulatedAnnealing() {
    int count = 0;
    for (int i = 0; i < 1000; i++) {
        initial();
        if (simulated())
            count++;
    }
    return count;
}

int main(int argc, char const *argv[]) {
    srand((int)time(0));

    totalTrial = 0;
    cout << "steepest-Ascent searching..." << endl;
    int count = steepestAscent();
    cout << "steepest-Ascent[success/total]: " << count << "/1000" << endl;
    cout << "average steps: " << totalTrial/count << endl;

    totalTrial = 0;
    cout << "random-Restart searching..." << endl;
    int count2 = randomRestart();
    cout << "random-Restart: " << count2 << endl;
    cout << "average steps: " << totalTrial/count2 << endl;

    totalTrial = 0;
    cout << "first-Chose searching..." << endl;
    int count3 = firstChose();
    cout << "first-Chose[success/total]: " << count3 << "/1000" << endl;
    cout << "average steps: " << totalTrial/count3 << endl;

    totalTrial = 0;
    cout << "simulated-Annealing searching..." << endl;
    int count4 = simulatedAnnealing();
    cout << "simulated-Annealing[success/total]: " << count4 << "/1000" << endl;
    cout << "average steps: " << totalTrial/count4 << endl;

    return 0;
}