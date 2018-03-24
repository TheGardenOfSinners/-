#include <iostream>
#include <time.h>
#include <stdlib.h>
#include <algorithm>
#include <cmath>
#include <vector>
using namespace std;

int direction[4][2] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  // ��������
int current[3][3];  // ��ǰ״̬
int row_0, col_0;  // ��¼0������
int totalTrial;    // ͳ���ƶ�����

int Manhattan() {  // ���������پ���
    int sum = 0;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (current[i][j] == 0) continue;
            int row = current[i][j]/3;
            int col = current[i][j]%3;
            int distance = abs(row - i) + abs(col - j);
            sum += distance;
        }
    }
    return sum;
}

void print() {
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j)
            cout << current[i][j] << " ";
        cout << endl;
    }
    cout << endl;
}

void initial() {
    for (int i = 0; i < 3; ++i) {  // ��ʼ״̬ΪĿ��״̬
        for (int j = 0; j < 3; ++j) {
            current[i][j] = i*3+j;
        }
    }
    row_0 = 0, col_0 = 0;
    int last = -1;   // ��һ���ƶ�����
    for (int i = 0; i < 20; i++) {  // �������
        bool upset = false;
        while (!upset) {  // ���ҳɹ�������ѭ��
            int dir = rand() % 4;  // ���ѡȡһ������
            if (last != -1 && last != dir && abs(last-dir) == 2)  continue;  // ���ⷴ����
            int x = row_0 + direction[dir][0];
            int y = col_0 + direction[dir][1];
            if (x >= 0 && x < 3 && y >= 0 && y < 3) {  // �������
                swap(current[row_0][col_0], current[x][y]);  // ����0���������ֵ�λ��
                row_0 = x, col_0 = y;     // ����0������
                last = dir;               // ���´˴��ƶ�����
                upset = true;             // ��Ǵ��ҳɹ�
            }
        }
    }
}

// �ж��Ƿ��н�
bool check() {
    for (int i = 0; i < 3; ++i) {
        for (int j = 0; j < 3; ++j)
            if (current[i][j] != i*3+j)
                return false;
    }
    return true;
}

// ��ɽ��
bool hillClimbing() {
    for (int trial = 0; trial < 200; trial++) {
        int curManha = Manhattan();  // ��ǰ״̬
        int minMan = 99999, minX = 0, minY = 0;
        for (int i = 0; i < 4; i++) {  // �ں��״̬������Сֵ
            int x = row_0 + direction[i][0];
            int y = col_0 + direction[i][1];
            if (x >= 0 && x < 3 && y >= 0 && y < 3) {  // �������
                swap(current[row_0][col_0], current[x][y]);  // ����0������λ��
                int nextManha = Manhattan();
                if (nextManha < minMan) {  // ��ȡ��һ״̬����Сֵ
                    minMan = nextManha;
                    minX = x, minY = y;
                }
                swap(current[x][y], current[row_0][col_0]);  // ��ԭ0������λ��
            }
        }
        if (curManha > minMan) {  // ��Сֵ���ڵ�ǰ״̬
            swap(current[row_0][col_0], current[minX][minY]);
            row_0 = minX, col_0 = minY;
        }
        if (check()) {   // �ɹ��ҵ���
            totalTrial += trial;
            return true;
        }
    }
    return false;
}

// ��ѡ��ɽ��
bool firstchose() {
    for (int trial = 0; trial < 500; trial++) {
        // ���ѡȡ��һ�����ڵ�ǰ״̬����һ��
        bool next = false;
        int times = 0;
        while (!next) {
            int dir = rand() % 4;
            int curManha = Manhattan();
            int x = row_0 + direction[dir][0];
            int y = col_0 + direction[dir][1];
            if (x >= 0 && x < 3 && y >= 0 && y < 3) {  // �������
                swap(current[row_0][col_0], current[x][y]);
                int nextManha = Manhattan();
                if (nextManha < curManha) {
                    row_0 = x, col_0 = y;
                    next = true;
                } else {
                    swap(current[x][y], current[row_0][col_0]);
                }
            }
            if (++times > 20) break;
        }
        if (check()) {   // �ɹ��ҵ���
            totalTrial += trial;
            return true;
        }
    }
    return false;
}

// ģ���˻��㷨
bool simulated() {
    double temperature = 5;  // ��ʼ�¶�
    int trial = 0;
    while (temperature > 0.00001) {
        vector<int> v;    // ѡ�����еķ���
        for (int i = 0; i < 4; i++) {
            int x = row_0 + direction[i][0];
            int y = col_0 + direction[i][1];
            if (x >= 0 && x < 3 && y >= 0 && y < 3) {  // �������
                v.push_back(i);
            }
        }
        int curManha = Manhattan();      // ��ǰ״̬�������پ���֮��
        int dir = v[rand() % v.size()];  // ���ѡȡһ�����з���
        int x = row_0 + direction[dir][0];
        int y = col_0 + direction[dir][1];  
        swap(current[row_0][col_0], current[x][y]);  // ����0�����ڽڵ��λ��
        int nextManha = Manhattan();    // ����֮��������پ���֮��
        int E = nextManha - curManha;   
        if (E < 0) {                    // ��һ״̬���ڵ�ǰ״̬
            row_0 = x, col_0 = y;       // ����0��λ��
            trial++;
        } else if (exp((-1)*E/temperature) > ((double)(rand() % 1000) / 1000)) { // ��һ���ĸ���ѡȡ
            row_0 = x, col_0 = y;
            trial++;
        } else {  // ���ɹ��Ļ�����ԭ0�����ڽڵ��λ��
            swap(current[x][y], current[row_0][col_0]);
        }
        temperature *= 0.999;        // �¶��½�
        if (check()) {   // �ɹ��ҵ���
            totalTrial += trial;
            return true;
        }
    }
    return false;
}

// �������ɽ��
int steepestAscent() {
    int count = 0;
    for (int i = 0; i < 1000; i++) {
        initial();
        if (hillClimbing())
            count++;
    }
    return count;
}

// ��ѡ��ɽ��
int firstChose() {
    int count = 0;
    for (int i = 0; i < 1000; i++) {
        initial();
        if (firstchose())
            count++;
    }
    return count;
}

// ������¿�ʼ��ɽ��
int randomRestart() {
    bool find = false;
    while (!find) {
        initial();
        find = hillClimbing();
    }
    return find;
}

// ģ���˻�����
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
    system("pause"); 
    return 0;
}
