#include<vector>
#include<algorithm>
#include<queue>
 
using namespace std;
 
vector<pair<int, int>> adj[10000];//{거리, 다음 노드}
int cnt;
bool bbang_cafe[10000];
int distBbang[10000], distCafe[10000];
 
struct Node {
    int num, dist;
 
    bool operator < (const Node& other) const {
        return dist > other.dist;
    }
};
 
void dijkstra(int siz, int not_house[], int dist[], int R) {//모든 빵집/카페를 넣고 최단 거리 갱신
    for (int i = 0; i < cnt; i++) dist[i] = 1e9;
    priority_queue<Node> pq;
    for (int i = 0; i < siz; i++) {
        dist[not_house[i]] = 0;
        pq.push({ not_house[i], 0 });
    }
 
    while (!pq.empty()) {
        Node tmp = pq.top();
        pq.pop();
        int cur = tmp.num;
        int d = tmp.dist;
 
        if (d > dist[cur]) continue;
 
        for (auto &edge: adj[cur]) {
            int next = edge.second;
            int next_dist = d + edge.first;
 
            if (next_dist > R) continue;
 
            if (dist[next] > next_dist) {
                dist[next] = next_dist;
                pq.push({ next, next_dist });
            }
        }
    }
}
 
void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
    for (int i = 0; i < N; i++) {
        bbang_cafe[i] = false;
        adj[i].clear();
    }
    cnt = N;
    //인접리스트에 저장
    for (int i = 0; i < K; i++) {
        adj[sBuilding[i]].push_back({ mDistance[i], eBuilding[i] });
        adj[eBuilding[i]].push_back({ mDistance[i], sBuilding[i] });
    }
}
 
void add(int sBuilding, int eBuilding, int mDistance) {
    //인접 리스트에 추가만 하기
    adj[sBuilding].push_back({ mDistance, eBuilding });
    adj[eBuilding].push_back({ mDistance, sBuilding });
}
 
int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
    //bool 배열 bbang_cafe를 통해 주택 확인
    for (int i = 0; i < M; i++) bbang_cafe[mCoffee[i]] = true;
    for (int i = 0; i < P; i++) bbang_cafe[mBakery[i]] = true;
 
    int res = R * 2 + 1;
 
    dijkstra(M, mCoffee, distCafe, R);
    dijkstra(P, mBakery, distBbang, R);
 
    for (int i = 0; i < cnt; i++) {
        if (bbang_cafe[i]) continue;
        if (distCafe[i] <= R && distBbang[i] <= R) {
            res = min(res, distCafe[i] + distBbang[i]);
        }
    }
    //돌려놓기
    for (int i = 0; i < M; i++) bbang_cafe[mCoffee[i]] = false;
    for (int i = 0; i < P; i++) bbang_cafe[mBakery[i]] = false;
 
    if (res == R * 2 + 1) return -1;
    return res;
}
