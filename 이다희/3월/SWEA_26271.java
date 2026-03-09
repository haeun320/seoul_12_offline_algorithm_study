import java.util.*;

class Node implements Comparable<Node> {
	
	int idx;
	int dist;
	
	public Node(int idx, int dist) {
		super();
		this.idx = idx;
		this.dist = dist;
	}

	@Override
	public int compareTo(Node o) {
		return this.dist - o.dist;
	}
	
}

class UserSolution {
	
	int n;
	ArrayList<Node>[] map;
	
	public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
		n = N;
		map = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			map[i] = new ArrayList<>();
		}
		for (int i = 0; i < K; i++) {
			map[sBuilding[i]].add(new Node(eBuilding[i], mDistance[i]));
			map[eBuilding[i]].add(new Node(sBuilding[i], mDistance[i]));
		}
		return;
	}

	public void add(int sBuilding, int eBuilding, int mDistance) {
		map[sBuilding].add(new Node(eBuilding, mDistance));
		map[eBuilding].add(new Node(sBuilding, mDistance));
		return;
	}

	public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
		int min = Integer.MAX_VALUE;
		int[] coffee = dijkstra(M, mCoffee);
		int[] bakery = dijkstra(P, mBakery);
		for (int i = 0; i < n; i++) {
			if (coffee[i] == 0 || bakery[i] == 0) continue;
			if (coffee[i] <= R && bakery[i] <= R) min = Integer.min(min, coffee[i] + bakery[i]);
		}
		if (min == Integer.MAX_VALUE) return -1;
		return min;
	}

	// 다중 시작점 다익스트라
	private int[] dijkstra(int cnt, int[] start) {
		int[] distance = new int[n];
		boolean[] visit = new boolean[n];
		PriorityQueue<Node> pq = new PriorityQueue<>();
		Arrays.fill(distance, Integer.MAX_VALUE);
		for (int i = 0; i < cnt; i++) {
			distance[start[i]] = 0;
			pq.add(new Node(start[i], 0));
		}
		while (!pq.isEmpty()) {
			Node pop = pq.poll();
			if (visit[pop.idx]) continue;
			visit[pop.idx] = true;
			for (Node node : map[pop.idx]) {
				if (distance[node.idx] > distance[pop.idx] + node.dist) {
					distance[node.idx] = distance[pop.idx] + node.dist;
					pq.add(new Node(node.idx, distance[node.idx]));
				}
			}
		}
		return distance;
	}
	
}
