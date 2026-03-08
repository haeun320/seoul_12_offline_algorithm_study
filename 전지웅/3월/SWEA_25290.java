import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/*
* checkStructures() 호출 횟수가 10,000번이라 시간초과 발생
* 결합판 경우의 수는 {0,0,0} ~ {3,3,3} 총 64가지
* 경우의 수가 적으니 미리 캐싱해두면 시간 단축할 수 있을 듯
*/

class UserSolution {
    static int width, height;
    static Tank[] tanks;
    static List<List<int[]>> shapeCache; // 결합판 조합을 저장하는 캐시 리스트

    static final int H = 0; // Height (높이)
    static final int S = 1; // Shape (결합판 모양)

    // 어항 클래스
    class Tank {
        int id;
        int[][] cells; // cells[열 번호] = {높이, 위쪽 결합판}

        Tank(int id, int[][] cells) {
            this.id = id;
            this.cells = cells;
        }
    }

    // 3개의 결합판 정보를 4진법 인덱스(0~63)로 변환하는 헬퍼 메서드
    private int getHash(int s1, int s2, int s3) {
        return (s1 * 16) + (s2 * 4) + s3;
    }

    public void init(int N, int mWidth, int mHeight, int[] mIDs, int[][] mLengths, int[][] mUpShapes) {
        width = mWidth;
        height = mHeight;

        tanks = new Tank[N + 1];

        for (int i = 1; i <= N; i++) {
            int[][] cells = new int[width + 1][2];
            for (int j = 1; j <= width; j++) {
                cells[j] = new int[] { mLengths[i - 1][j - 1], mUpShapes[i - 1][j - 1] };
            }
            tanks[i] = new Tank(mIDs[i - 1], cells);
        }

        shapeCache = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            shapeCache.add(new ArrayList<>());
        }

        // 어항 결합판 3칸씩 묶어서 캐싱
        for (int n = 1; n <= N; n++) {
            int[][] cells = tanks[n].cells;
            for (int w = 1; w <= width - 2; w++) {
                int hash = getHash(cells[w][S], cells[w + 1][S], cells[w + 2][S]);
                shapeCache.get(hash).add(new int[] { n, w });
            }
        }
    }

    public int checkStructures(int[] mLengths, int[] mUpShapes, int[] mDownShapes) {
        int cnt = 0;
        int targetHash = getHash(mDownShapes[0], mDownShapes[1], mDownShapes[2]);
        List<int[]> candidates = shapeCache.get(targetHash);

        for (int i = 0; i < candidates.size(); i++) {
            Tank tank = tanks[candidates.get(i)[0]];
            int w = candidates.get(i)[1];

            if (isValidPlacement(tank, w, mLengths)) {
                cnt++;
            }
        }
        return cnt;
    }

    public int addStructures(int[] mLengths, int[] mUpShapes, int[] mDownShapes) {
        // 우선순위: 1. ID 오름차순, 2. 열(w) 오름차순
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                Comparator.comparingInt((int[] o) -> o[0])
                        .thenComparingInt(o -> o[2]));

        int targetHash = getHash(mDownShapes[0], mDownShapes[1], mDownShapes[2]);
        List<int[]> candidates = shapeCache.get(targetHash);

        // 1. 설치 가능한 모든 위치 큐에 삽입
        for (int i = 0; i < candidates.size(); i++) {
            Tank tank = tanks[candidates.get(i)[0]];
            int w = candidates.get(i)[1];

            if (isValidPlacement(tank, w, mLengths)) {
                pq.add(new int[] { tank.id, candidates.get(i)[0], w });
            }
        }

        if (pq.isEmpty())
            return 0;

        // 2. 우선순위 높은 위치 꺼내기
        int[] best = pq.poll();
        int bestId = best[0];
        int n = best[1]; // 어항 인덱스
        int w = best[2]; // 설치할 열(w) 위치

        // 3. 기존 캐시 제거 (설치 위치 기준 좌우 2칸)
        for (int i = w - 2; i <= w + 2; i++) {
            if (i < 1 || i + 2 > width)
                continue;

            int oldHash = getHash(tanks[n].cells[i][S], tanks[n].cells[i + 1][S], tanks[n].cells[i + 2][S]);
            List<int[]> targetList = shapeCache.get(oldHash);

            for (int j = 0; j < targetList.size(); j++) {
                if (targetList.get(j)[0] == n && targetList.get(j)[1] == i) {
                    targetList.remove(j);
                    break;
                }
            }
        }

        // 4. 어항 정보 갱신 (구조물 설치)
        for (int i = 0; i < 3; i++) {
            tanks[n].cells[w + i][H] += mLengths[i];
            tanks[n].cells[w + i][S] = mUpShapes[i];
        }

        // 5. 새로운 정보로 캐시 업데이트
        for (int i = w - 2; i <= w + 2; i++) {
            if (i < 1 || i + 2 > width)
                continue;

            int newHash = getHash(tanks[n].cells[i][S], tanks[n].cells[i + 1][S], tanks[n].cells[i + 2][S]);
            shapeCache.get(newHash).add(new int[] { n, i });
        }

        return (bestId * 1000) + w;
    }

    public Solution.Result pourIn(int mWater) {
        Solution.Result ret = new Solution.Result();
        ret.ID = ret.height = ret.used = 0;

        // 우선순위: 1. 높이 내림차순, 2. 사용한 물 내림차순, 3. ID 오름차순
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                Comparator.comparingInt((int[] o) -> -o[0])
                        .thenComparingInt(o -> -o[1])
                        .thenComparingInt(o -> o[2]));

        for (int n = 1; n < tanks.length; n++) {
            int top = 1;
            int water = mWater;
            int usedWater = 0;
            int prevWater = water;

            while (water > 0 && top <= height) {
                for (int i = 1; i <= width; i++) {
                    if (tanks[n].cells[i][H] < top) {
                        water--;
                    }
                }

                if (water >= 0) {
                    top++;
                    usedWater += (prevWater - water);
                    prevWater = water;
                }
            }

            if (usedWater > 0) {
                pq.add(new int[] { top - 1, usedWater, tanks[n].id });
            }
        }

        if (!pq.isEmpty()) {
            int[] result = pq.poll();
            ret.height = result[0];
            ret.used = result[1];
            ret.ID = result[2];
        }

        return ret;
    }

    // 구조물 설치 조건 체크
    private boolean isValidPlacement(Tank tank, int idx, int[] lengths) {
        // 1. 어항 높이 초과 여부 체크
        for (int i = 0; i < 3; i++) {
            if (tank.cells[idx + i][H] + lengths[i] > height) {
                return false;
            }
        }

        // 2. 구조물 인접면(맞닿음) 조건 체크
        if (tank.cells[idx][H] + lengths[0] > tank.cells[idx + 1][H]
                && tank.cells[idx + 1][H] + lengths[1] > tank.cells[idx][H]
                && tank.cells[idx + 1][H] + lengths[1] > tank.cells[idx + 2][H]
                && tank.cells[idx + 2][H] + lengths[2] > tank.cells[idx + 1][H]) {
            return true;
        }

        return false;
    }
}