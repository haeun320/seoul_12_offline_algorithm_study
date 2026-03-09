import java.util.Arrays;

class Structures implements Comparable<Structures> {
	
	int id;
	int[] len;
	int[] up;
	
	public Structures(int id, int[] len, int[] up) {
		super();
		this.id = id;
		this.len = Arrays.copyOf(len, len.length);
		this.up = Arrays.copyOf(up, up.length);
	}

	@Override
	public int compareTo(Structures o) {
		return this.id - o.id;
	}
	
}

class UserSolution {
	
	int n, w, h;
	int[] possible;
	Structures[] structures;
	
	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {
		
		n = N;
		w = mWidth;
		h = mHeight;
		
		structures = new Structures[n];
		for (int i = 0; i < n; i++) {
			structures[i] = new Structures(mIDs[i], mLengths[i], mUpShapes[i]);
		}
		
		// 어항의 ID가 작을수록 우선순위가 높다.
		Arrays.sort(structures);
		
	}

	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		
		int cnt = 0;
		possible = new int[n];
		
		for (int i = 0; i < n; i++) {
			
			// 같은 어항내에서는 왼쪽으로 갈수록 우선순위가 더 높다.
			boolean first = true;
			
			for (int j = 0; j < w - 2; j++) {
				
				// 주어지는 3개의 구조물들은 동일한 어항 내에 주어진 순서대로(주어진 배열의 index 순) 왼쪽에서 오른쪽으로 인접하게 설치되어야 한다.
				if (structures[i].up[j] != mDownShapes[0]) continue;
				if (structures[i].up[j+1] != mDownShapes[1]) continue;
				if (structures[i].up[j+2] != mDownShapes[2]) continue;
				
				// 설치되는 인접한 구조물들은 최소 한 셀의 면이 붙어 있어야 한다.
				int[] temp = new int[3];
				temp[0] = structures[i].len[j] + mLengths[0];
				temp[1] = structures[i].len[j+1] + mLengths[1];
				temp[2] = structures[i].len[j+2] + mLengths[2];
				if (temp[0] <= structures[i].len[j+1] || structures[i].len[j] >= temp[1]) continue;
				if (temp[1] <= structures[i].len[j+2] || structures[i].len[j+1] >= temp[2]) continue;
				
				// 설치되는 구조물은 어항의 높이보다 높게 설치되면 안된다.
				if (temp[0] > h || temp[1] > h || temp[2] > h) continue;
				
				// 구조물을 설치 할 수 있는 조건을 모두 충족시키면 값을 저장한다.
				if (first) {
					possible[i] = j+1; // 인덱스 1부터 시작
					first = false;
				}
				cnt++;
				
			}
		}
		
		return cnt;
		
	}

	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {
		
		int cnt = checkStructures(mLengths, mUpShapes, mDownShapes);
		if (cnt == 0) return 0;
		
		int result = 0;
		
		for (int i = 0; i < n; i++) {
			
			if (possible[i] == 0) continue;
			
			structures[i].len[possible[i]-1] += mLengths[0];
			structures[i].len[possible[i]] += mLengths[1];
			structures[i].len[possible[i]+1] += mLengths[2];
			
			structures[i].up[possible[i]-1] = mUpShapes[0];
			structures[i].up[possible[i]] = mUpShapes[1];
			structures[i].up[possible[i]+1] = mUpShapes[2];
			
			result = structures[i].id*1000 + possible[i];
			break;
			
		}
		
		return result;
		
	}

	public Solution.Result pourIn(int mWater) {
		
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;
		
		for (int i = 0; i < n; i++) {
			
			// 물은 최소 1이상을 사용해서 채워야 한다.
			int used = 0;
			int height = 1;
			int low = 2;
			int high = h;

			// 주어진 물의 양 내에서 물이 포함된 행에 빈 공간이 없도록 물을 채워야 한다.
			while (low <= high) {
				
				int water = 0;
				int mid = (low+high)/2;
				
				for (int j = 0; j < w; j++) {
					
					// 물은 어항의 높이보다 높게 채울 수 없다.
					if (mid <= structures[i].len[j]) continue;
					water += mid - structures[i].len[j];
					
				}
				
				if (water > mWater) high = mid - 1;
				else {
					
					// 주어진 물의 양 내에서 가장 높게 물을 채워야 한다.
					if (used < water) {
						used = water;
						height = mid;
					}
					low = mid + 1;
					
				}
				
			}
			
			// 물을 쏟아서 가장 높게 채울 수 있는 어항을 찾아야 한다.
			if (ret.height > height) continue;
			
			// 해당 높이까지 채우기 위해 사용하는 물의 양이 많을수록 우선순위가 높다.
			if (ret.height < height || (ret.height == height && ret.used < used)) {
				
				ret.ID = structures[i].id;
				ret.height = height;
				ret.used = used;
				
			}
			
		}
		
		return ret;
		
	}
	
}
