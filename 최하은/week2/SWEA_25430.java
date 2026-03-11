import java.util.*;

// 제과점 & 커피점 D6
class UserSolution {
	public class Node implements Comparable<Node> {
		int index, cost;
		Node(int i, int c){
			index = i;
			cost = c;
		}
		@Override
		public int compareTo(UserSolution.Node o) {
			return Integer.compare(cost, o.cost);
		}
	}
	
	int N; // 건물 수 
	List<List<Node>> graph; // 양방향그래프 인접리스트 
	static final int INF = 987654321;
	boolean[] house;
	
	/**
	 * 인접리스트 초기화 
	 * @param N 건물 수 
	 * @param K 도로 수
	 * @param sBuilding 도로와 연결된 시작 건물 
	 * @param eBuilding 도로와 연결된 도착 건물 
	 * @param mDistance 연결된 도로 거리 
	 */
	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		this.N = N;
		graph = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			graph.add(new ArrayList<>());
		}
		
		for (int i = 0; i < K; i++) {
			graph.get(sBuilding[i]).add(new Node(eBuilding[i], mDistance[i]));
			graph.get(eBuilding[i]).add(new Node(sBuilding[i], mDistance[i]));
		}
		
		house = new boolean[N];
	}

	/**
	 * 인접리스트에 간선 추가 
	 * @param sBuilding 도로와 연결된 시작 건물 
	 * @param eBuilding 도로와 연결된 도착 건물 
	 * @param mDistance 연결된 도로 거리 
	 */
	public void add(int sBuilding, int eBuilding, int mDistance) {
		graph.get(sBuilding).add(new Node(eBuilding, mDistance));
		graph.get(eBuilding).add(new Node(sBuilding, mDistance));
	}
	
	/**
	 * 
	 * @param M 커피점 개수 
	 * @param mCoffee 커피점 인덱스 배열 
	 * @param P 제과점 개수 
	 * @param mBakery 제과점 인덱스 배열 
	 * @param R 제한거리 
	 * @return 거리 합 최솟값 (없으면 -1)
	 */
	public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
		// 다중 시작점 다익스트라
		// 커피 시작점 다익스트라 dm[], 제과점 시작점 다익스트라 dp[]
		// 두 배열 순회하면서: 커피점도 제과점도 아닌 인덱스에 대해, 각 거리가 R 이하 ==> 최솟값 비교 후 갱신 
		int result = Integer.MAX_VALUE;
		
		int[] dm = dijkstra(M, mCoffee, R);
		int[] dp = dijkstra(P, mBakery, R);
		
		// 주택 확인하기
		Arrays.fill(house, true);
		for (int i = 0; i < M; i++) {
			house[mCoffee[i]] = false;
		}
		for (int i = 0; i < P; i++) {
			house[mBakery[i]] = false;
		}
		
		for (int i = 0; i < N; i++) {
			if (!house[i]) continue; // 주택이 아니면 건너뛰기 
			
			if (dm[i] <= R && dp[i] <= R) {
				result = Math.min(result, dm[i] + dp[i]);
			}
		}
		
		return result != Integer.MAX_VALUE ? result : -1;
	}
	
	/**
	 * 
	 * @param m 시작점 배열 길이 
	 * @param mStart 시작점 배열 
	 * @param R 제한거리 
	 * @return
	 */
	private int[] dijkstra(int m, int mStart[], int R) {
		int[] d = new int[N];
		Arrays.fill(d, INF);
		
		PriorityQueue<Node> pq = new PriorityQueue<>();
		for (int i = 0; i < m; i++) {
			pq.add(new Node(mStart[i], 0)); // (시작점 번호, 거리) 
			d[mStart[i]] = 0;
		}
		
		while (!pq.isEmpty()) {
			Node curr = pq.poll();
			int now = curr.index;
			int dist = curr.cost;
			
			if (d[now] < dist) continue;
			
			for (Node next: graph.get(now)) {
				int cost = d[now] + next.cost;
				
				if (cost > R) continue; // 제한거리 넘어가는거 가지치기 
				
				if (cost < d[next.index]) {
					d[next.index] = cost;
					pq.add(new Node(next.index, cost));
				}
			}
		}
		
		return d;
	}
}