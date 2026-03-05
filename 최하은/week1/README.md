# 다익스트라


참고 블로그

https://blog.encrypted.gg/1037

# 플로이드 알고리즘: 모든 정점 쌍 사이의 최단거리

<img width="1069" height="467" alt="Image" src="https://github.com/user-attachments/assets/8ccd7bb7-60b6-4f58-940e-1989115abe38" />

방향/무방향 그래프에서 모든 정점 쌍의 최단거리를 구한다.

간선의 값이 음수여도 잘 동작하지만, **음수인 사이클**이 있으면 문제 有

시간복잡도: $O(V^{3})$

### 동작 과정

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/7b0aa6c2-c326-4850-ae07-1b4ec19d84cc" />

1단계: 각 정점에서 간선 1개로 갈 수 있는 정점까지의 비용을 저장 ⇒ 중간에 다른 정점을 거치지 않고 갈 수 있는 최단거리

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/a3e55ec8-a525-4058-9292-ac8a1adc7c7e" />

2단계: min((1단계의 최단거리), (1번 정점을 거쳐갈 때의 최단거리))

- s에서 t로 갈 때 1번 정점을 거쳐가는 최단 거리는 D[s][1] + D[1][t]
- D[s][t]보다 D[s][1] + D[1][t]가 작을 경우 D[s][t]를 D[s][1] + D[1][t]로 갱신

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/61322676-06c4-4dcf-bad2-9bb85e787113" />

3단계: min((2단계의 최단거리), (1,2번 정점을 거쳐갔을 때의 최단거리))

- 2번 정점을 거쳐가는 최단 거리는 D[s][2] + D[2][t]
- D[s][t]보다 D[s][2] + D[2][t]가 작을 경우 D[s][t]를 D[s][2] + D[2][t]로 갱신

이후: 1~5번 정점 거쳐갔을때 최단거리까지 반복

### 구현

1. 인접리스트를 최댓값으로 초기화 (overflow 방지를 위해 `MAX = 987654321`로 설정)
2. 하나의 간선으로 갈 수 있는 비용 저장
3. 3중 for문을 사용해 1~k번 노드를 거쳐 갈때의 최소 비용 저장

```java
for (int k = 1; k <= N; k++) {
	for (int i = 1; i <= N; i++) {
		for (int j = 1; j <= N; j++) {
			// i->j 바로 가기 vs. i->k->j 거쳐 가기
			d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
		}
	}
}
```

[플로이드 11404]([https://www.acmicpc.net/problem/11404](https://www.acmicpc.net/problem/11404))

- 구현 전체 코드
    
    ```java
    import java.util.*;
    import java.io.*;
    
    public class Main {
    	static int N, M; // 도시 개수, 버스 개수
    	static int[][] d;
    	static int MAX = 987654321; // int overflow 방지를 위한 최대값 초기화
    
    	public static void main(String[] args) throws Exception {
    		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		StringTokenizer st;
    		StringBuilder sb = new StringBuilder();
    
    		N = Integer.parseInt(br.readLine());
    		M = Integer.parseInt(br.readLine());
    
    		d = new int[N+1][N+1];
    		for (int i = 1; i <= N; i++) {
    			Arrays.fill(d[i], MAX);
    		}
    
    		for (int i = 0; i < M; i++) {
    			st = new StringTokenizer(br.readLine(), " ");
    			int a = Integer.parseInt(st.nextToken());
    			int b = Integer.parseInt(st.nextToken());
    			int c = Integer.parseInt(st.nextToken());
    
    			// 시작 도시-도착 도시 노선이 하나가 아닐 수 있기 때문에, 최솟값으로 저장
    			d[a][b] = Math.min(d[a][b], c);
    		}
    
    		// 자기 자신으로 가는 비용 0으로 초기화
    		for (int i = 1; i <= N; i++) d[i][i] = 0;
    
    		// 3중 for문을 사용한 플로이드 알고리즘 구현
    		for (int k = 1; k <= N; k++) {
    			for (int i = 1; i <= N; i++) {
    				for (int j = 1; j <= N; j++) {
    					// i->j 바로 가기 vs. i->k->j 거쳐 가기
    					d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
    				}
    			}
    		}
    
    		for (int i = 1; i <= N; i++) {
    			for (int j = 1; j <= N; j++) {
    				sb.append(d[i][j] == MAX ? 0 : d[i][j]).append(" ");
    			}
    			sb.append("\\n");
    		}
    		System.out.println(sb);
    	}
    }
    ```
    

### 최단경로 복원

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/befda8fc-53a5-449f-9311-a802e2cb0eb7" />

최단거리를 구하는 과정 중에 `next` 테이블에 경로를 저장한다.

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/22f6cfcc-29f1-498e-bb81-6b34e7831e96" />

1단계: nxt[i][j] = 노드 한개를 거쳐서 갈 수 있을때 도착 노드 번호

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/072f32e7-6278-4710-a50e-43b81ad7e2a1" />

2단계~: 최단거리 갱신이 일어났을 때 거쳐가는 노드를 k라 하면,  nxt[i][j] = k로 업데이트

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/b406ae57-78b2-4560-983a-d300e61ff6ed" />

최종 저장된 테이블의 모습

3→5로 가는 경로

1. nxt[3][5] = 1
2. nxt[1][5] = 4
3. nxt[4][5] = 5

⇒ 3 → 1 → 4 → 5

nxt 테이블을 사용한 경로 복원 코드

```java
ArrayList<Integer> path = new ArrayList<>();
int start = i;
while (start != j) {
    path.add(start);
    start = nxt[start][j];
}
path.add(j);
```

- [플로이드2 11780]([https://www.acmicpc.net/problem/11780](https://www.acmicpc.net/problem/11780)) 구현 코드
    
    ```java
    import java.util.*;
    import java.io.*;
    
    public class Main {
    	static int N, M; // 도시 개수, 버스 개수
    	static int[][] d;
    	static int[][] nxt;
    	static int MAX = 987654321; // int overflow 방지를 위한 최대값 초기화
    
    	public static void main(String[] args) throws Exception {
    		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		StringTokenizer st;
    		StringBuilder sb = new StringBuilder();
    
    		N = Integer.parseInt(br.readLine());
    		M = Integer.parseInt(br.readLine());
    
    		d = new int[N+1][N+1];
    		nxt = new int[N+1][N+1];
    		for (int i = 1; i <= N; i++) {
    			Arrays.fill(d[i], MAX);
    			Arrays.fill(nxt[i], MAX);
    		}
    
    		for (int i = 0; i < M; i++) {
    			st = new StringTokenizer(br.readLine(), " ");
    			int a = Integer.parseInt(st.nextToken());
    			int b = Integer.parseInt(st.nextToken());
    			int c = Integer.parseInt(st.nextToken());
    
    			if (c < d[a][b]) {
    		        d[a][b] = c;
    		        nxt[a][b] = b;
    		    }
    		}
    
    		// 자기 자신으로 가는 비용 0으로 초기화
    		for (int i = 1; i <= N; i++) d[i][i] = 0;
    
    		// 3중 for문을 사용한 플로이드 알고리즘 구현
    		for (int k = 1; k <= N; k++) {
    			for (int i = 1; i <= N; i++) {
    				for (int j = 1; j <= N; j++) {
    					// i->j 바로 가기 vs. i->k->j 거쳐 가기
    					if(d[i][k] + d[k][j] < d[i][j]) {
    						d[i][j] = d[i][k] + d[k][j];
    						nxt[i][j] = nxt[i][k];
    			        }
    				}
    			}
    		}
    
    		for (int i = 1; i <= N; i++) {
    			for (int j = 1; j <= N; j++) {
    				sb.append(d[i][j] == MAX ? 0 : d[i][j]).append(" ");
    			}
    			sb.append("\\n");
    		}
    
    		for (int i = 1; i <= N; i++) {
    		    for (int j = 1; j <= N; j++) {
    		        if (d[i][j] == 0 || d[i][j] == MAX) {
    		            sb.append(0).append("\\n");
    		            continue;
    		        }
    
    		        // 경로 복원하기
    		        ArrayList<Integer> path = new ArrayList<>();
    		        int start = i;
    
    		        while (start != j) {
    		            path.add(start);
    		            start = nxt[start][j];
    		        }
    		        path.add(j);
    
    		        sb.append(path.size()).append(" ");
    		        for (int node : path) {
    		            sb.append(node).append(" ");
    		        }
    		        sb.append("\\n");
    		    }
    		}
    
    		System.out.println(sb);
    	}
    }
    ```
    

# 다익스트라 알고리즘: 하나의 정점에서 다른 모든 정점까지의 최단거리

<img width="1152" height="469" alt="Image" src="https://github.com/user-attachments/assets/6e46694e-fe33-4a8e-aaeb-4025bd13321e" />

왼쪽 테이블: 오른쪽 그래프에서, 1번 정점에서 다른 정점으로 가는 최단거리를 저장

음수의 가중치를 갖는 간선이 있으면 사용 불가 (⇒ 벨만포드 사용)

### 작동 원리

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/47c81f83-0bf1-4bd5-ba48-d0466e3e77fa" />

시간복잡도: $O(V^{2}+E)$

1. 초기화 세팅: 출발지 자기 자신까지의 거리는 0, 아직 가보지 않은 나머지 모든 정점까지의 거리는 무한대로 설정
2. 가장 가까운 정점 선택: 아직 방문하지 않은 정점 중에서, 현재까지 알려진 거리가 가장 짧은 정점을 찾아 선택
3. 종료 조건 확인: 만약 방문할 정점이 더 이상 없거나, 남은 정점들이 출발지에서 아예 갈 수 없는 곳이라면 탐색을 종료
4. 방문 완료(최단 거리 확정)**:** 2번에서 선택된 정점은 출발지로부터의 최단 거리를 찾은 것이므로 '방문 완료' 처리하여 그 거리를 확정 짓기
5. **주변 정점 거리 갱신:** 방금 확정된 정점과 연결된 이웃 정점들을 모두 확인
**'기존에 알고 있던 이웃까지의 거리'**보다 **'방금 확정된 정점을 거쳐서 이웃으로 가는 거리'**가 더 짧다면 더 작은 값으로 거리를 업데이트

### 우선순위 큐를 사용한 다익스트라 구현

<img width="1280" height="720" alt="Image" src="https://github.com/user-attachments/assets/0780da65-5127-4500-a7d2-750fd2c00aba" />

시간복잡도: $O(ElogE)$

```java
import java.util.*;
import java.io.*;

// 1. 노드 정보와 '거리 비교'를 위한 클래스 정의
class Node implements Comparable<Node> {
    int index;
    int cost;

    public Node(int index, int cost) {
        this.index = index;
        this.cost = cost;
    }

    // 비용(cost)을 기준으로 오름차순 정렬되도록 설정
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.cost, other.cost);
    }
}

public class Main {
    static int N, M;
    static ArrayList<ArrayList<Node>> graph;
    static int[] d;
    static final int INF = 987654321; // 안전한 무한대 값

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
        // 정점과 간선의 개수 입력
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        
        // 그래프 초기화
        graph = new ArrayList<>();
        for (int i = 0; i <= N; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 최단 거리 테이블 초기화
        d = new int[N + 1];
        Arrays.fill(d, INF);
        
        // 간선 정보 입력
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            
            // 방향 그래프 기준: u에서 v로 가는 비용 w
            graph.get(u).add(new Node(v, w));
        }
        
        int startNode = 1; // 시작 노드 (예시)
        dijkstra(startNode);
        
        // 결과 출력
        for (int i = 1; i <= N; i++) {
            if (d[i] == INF) {
                System.out.println("INF");
            } else {
                System.out.println(d[i]);
            }
        }
    }

    // 2. 다익스트라 핵심 로직
    static void dijkstra(int start) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        // 시작 노드 설정
        pq.offer(new Node(start, 0));
        d[start] = 0;

        while (!pq.isEmpty()) {
            // 거리가 가장 짧은 노드를 꺼냄
            Node current = pq.poll();
            int dist = current.cost;
            int now = current.index;

            // 💡 핵심 최적화: 이미 처리된 적이 있는 노드라면 무시
            // 큐에 들어간 시점의 거리(dist)보다 현재 기록된 최단 거리(d[now])가 더 짧다면, 
            // 이미 다른 경로를 통해 최적화가 끝난 상태이므로 넘어갑니다.
            if (d[now] < dist) continue;

            // 현재 노드와 연결된 다른 인접 노드들을 확인
            for (Node next : graph.get(now)) {
                int cost = d[now] + next.cost; // 현재 노드를 거쳐서 가는 비용 계산
                
                // 거쳐서 가는 것이 바로 가는 것보다 더 짧은 경우
                if (cost < d[next.index]) {
                    d[next.index] = cost;
                    pq.offer(new Node(next.index, cost)); // 갱신된 정보를 큐에 삽입
                }
            }
        }
    }
}
```

- [최단경로 1753]([https://www.acmicpc.net/problem/1753](https://www.acmicpc.net/problem/1753))
    
    ```java
    	import java.util.*;
    	import java.io.*;
    	
    	class Node implements Comparable<Node>{
    		int index;
    		int cost;
    		Node(int i, int c) {
    			this.index = i;
    			this.cost = c;
    		}
    		@Override
    		public int compareTo(Node o) {
    			return Integer.compare(this.cost, o.cost);
    		}
    	}
    	
    	public class Main {
    		
    		
    		static int N, M; // 정점개수, 간선개수
    		static List<List<Node>> graph;
    		static int[] d;
    		static int MAX = 987654321; // int overflow 방지를 위한 최대값 초기화 
    		
    		public static void main(String[] args) throws Exception {
    			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    			StringTokenizer st;
    			StringBuilder sb = new StringBuilder();
    			
    			st = new StringTokenizer(br.readLine(), " ");
    			N = Integer.parseInt(st.nextToken());
    			M = Integer.parseInt(st.nextToken());
    			int start = Integer.parseInt(br.readLine());
    			
    			d = new int[N+1];
    			Arrays.fill(d, MAX);
    			
    			graph = new ArrayList<>();
    			for (int i = 0; i <= N; i++) {
    				graph.add(new ArrayList<>());
    			}
    			
    			for (int i = 0; i < M; i++) {
    				st = new StringTokenizer(br.readLine(), " ");
    				int a = Integer.parseInt(st.nextToken());
    				int b = Integer.parseInt(st.nextToken());
    				int c = Integer.parseInt(st.nextToken());
    				
    				graph.get(a).add(new Node(b, c));
    			}
    			
    			dijkstra(start);
    			
    			for (int i = 1; i <= N; i++) {
    				sb.append(d[i] == MAX ? "INF" : d[i]).append("\n");
    			}
    			System.out.println(sb);
    		}
    		
    		public static void dijkstra(int start) {
    			PriorityQueue<Node> pq = new PriorityQueue<>();
    			
    			// 시작 노드 설정
    			pq.add(new Node(start, 0));
    			d[start] = 0;
    			
    			while (!pq.isEmpty()) {
    				Node n = pq.poll();
    				
    				if (d[n.index] < n.cost) continue;
    				
    				// 인접한 노드 확인
    				for (Node next: graph.get(n.index)) {
    					int cost = d[n.index] + next.cost; // 현재 노드를 거쳐가는 비용
    					// 다음 노드로 바로 가는거보다 거쳐 가는게 더 저렴할때
    					if (cost < d[next.index]) {
    						d[next.index] = cost;
    						pq.add(new Node(next.index, cost));
    					}
    				}
    			}
    		}
    	}
    ```
    

## 경로 복원

다익스트라 과정 중에 경로가 갱신될 때마다 이전에 어떤 노드를 거쳐왔는지 `prev` 테이블에 저장하기

1. prev 초기화

```java
// 클래스 변수 영역에 추가
static int[] prev;

// main 메서드 안에서 초기화 (정점 개수 N+1 만큼)
prev = new int[N + 1];
```

2. 거리가 갱신될 때 이전 노드 기록하기 

```java
// dijkstra 메서드 내부
for (Node next: graph.get(n.index)) {
    int cost = d[n.index] + next.cost;
    
    // 거리가 갱신될 때!
    if (cost < d[next.index]) {
        d[next.index] = cost;
        prev[next.index] = n.index; // 💡 핵심: next로 가기 직전의 노드는 n이다!
        pq.add(new Node(next.index, cost));
    }
}
```

3. 목적지부터 경로를 거꾸로 역추적 (스택 활용)

```java
// 예시: start에서 end까지의 경로를 복원하는 로직
int end = 5; // 도착지 예시

// 도착지까지 갈 수 없는 경우 처리
if (d[end] == MAX) {
    System.out.println("경로가 없습니다.");
} else {
    Stack<Integer> path = new Stack<>();
    int current = end;
    
    // 출발지에 도달할 때까지 (출발지의 prev는 초기값 0이므로) 역추적
    while (current != 0) {
        path.push(current);
        current = prev[current]; // 이전 노드로 이동
    }
    
    // 스택에서 꺼내며 출력 (출발지 -> ... -> 도착지 순서로 나옴)
    StringBuilder pathSb = new StringBuilder();
    while (!path.isEmpty()) {
        pathSb.append(path.pop()).append(" ");
    }
    
    System.out.println("최단 경로: " + pathSb.toString());
}
```