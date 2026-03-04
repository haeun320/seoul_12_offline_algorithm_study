import java.util.Deque;
import java.util.ArrayDeque;

class UserSolution {
	
	static int count[];
	static int left1[];
	static int left2[];
	static int right1[];
	static int right2[];
	
	public void init(int mCnt1, int mDigitList1[], int mCnt2, int mDigitList2[]){
		
		count = new int[1000]; // 항상 3자리
		left1 = new int[2]; left1[0] = mDigitList1[0]; left1[1] = mDigitList1[1];
		left2 = new int[2]; left2[0] = mDigitList2[0]; left2[1] = mDigitList2[1];
		right1 = new int[2]; right1[0] = mDigitList1[0]; right1[1] = mDigitList1[1];
		right2 = new int[2]; right2[0] = mDigitList2[0]; right2[1] = mDigitList2[1];
		
		// List1
		for (int i = 0; i < mCnt1; i++) {
			if (i < 2) continue;
			count[right1[0]*100 + right1[1]*10 + mDigitList1[i]]++;
			right1[0] = right1[1];
			right1[1] = mDigitList1[i];
		}
		
		// List2
		for (int i = 0; i < mCnt2; i++) {
			if (i < 2) continue;
			count[right2[0]*100 + right2[1]*10 + mDigitList2[i]]++;
			right2[0] = right2[1];
			right2[1] = mDigitList2[i];
		}
		
		// 연결 부분
		count[right1[0]*100 + right1[1]*10 + left2[0]]++;
		count[right1[1]*100 + left2[0]*10 + left2[1]]++;
		
	}

	public void append(int mDir, int mNum1, int mNum2) {
		
		int len = 4; int div = 1_000; // 최대 4자리
		int d1, d2;
		
		// 왼쪽 삽입
		if (mDir == 0) {
			for (int i = 0; i < len; i++) {
				d1 = mNum1 % 10;
				d2 = mNum2 % 10;
				if (d1 > 0) {
					count[d1*100 + left1[0]*10 + left1[1]]++;
					left1[1] = left1[0];
					left1[0] = d1;
				}
				if (d2 > 0) {
					// 수정 전 연결 부분 빼기
					count[right1[0]*100 + right1[1]*10 + left2[0]]--;
					count[right1[1]*100 + left2[0]*10 + left2[1]]--;
					
					count[d2*100 + left2[0]*10 + left2[1]]++;
					left2[1] = left2[0];
					left2[0] = d2;
					
					// 수정 후 연결 부분 더하기
					count[right1[0]*100 + right1[1]*10 + left2[0]]++;
					count[right1[1]*100 + left2[0]*10 + left2[1]]++;
				}
				mNum1 /= 10;
				mNum2 /= 10;
			}
		// 오른쪽	삽입
		} else {
			for (int i = 0; i < len; i++) {
				d1 = (mNum1 / div) % 10;
				d2 = (mNum2 / div) % 10;
				if (d1 > 0) {
					// 수정 전 연결 부분 빼기
					count[right1[0]*100 + right1[1]*10 + left2[0]]--;
					count[right1[1]*100 + left2[0]*10 + left2[1]]--;
					
					count[right1[0]*100 + right1[1]*10 + d1]++;
					right1[0] = right1[1];
					right1[1] = d1;
					
					// 수정 후 연결 부분 더하기
					count[right1[0]*100 + right1[1]*10 + left2[0]]++;
					count[right1[1]*100 + left2[0]*10 + left2[1]]++;
				}
				if (d2 > 0) {
					count[right2[0]*100 + right2[1]*10 + d2]++;
					right2[0] = right2[1];
					right2[1] = d2;
				}
				div /= 10;
			}
		}
		
	}

	public int countNum(int mNum) {
		
		return count[mNum];
		
	}
	
}
