import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;

/**
 * (에디터 한 줄 길이 ≤ 30,000) + (append 길이 ≤ 9999)
 * countNum() 함수는 최대 30,000 회 호출 
 * append() 함수는 최대 30,000 회 호출
 * 
 * ⇒ 3자리 수 등장 횟수 세자고 매번 반복문 돌리면 최대 
 * (30,000 + (4 * 30,000)) * 2 * 30,000 = 9,000,000,000 => 90억번 계산
 * 
 * 3자리 등장 횟수를 미리 한번에 세서 저장한 후, 연결부위만 새로 확인하기
 */

class UserSolution {
    static Deque<Integer> editor1 = new ArrayDeque<>();
    static Deque<Integer> editor2 = new ArrayDeque<>();
    static int[] patternCnt = new int[1000]; // 3자리수 등장 횟수 저장, 111 ~ 999 까지 사용 

    public void init(int mCnt1, int mDigitList1[], int mCnt2, int mDigitList2[]) {
        // 전역 변수 초기화 
        editor1.clear(); editor2.clear();
        Arrays.fill(patternCnt, 0);

        for (int i = 0; i < mCnt1; i++) {
            editor1.addLast(mDigitList1[i]);
            if (i >= 2) { // 세 자리 수 등장 빈도 세기
                int p = mDigitList1[i - 2] * 100 + mDigitList1[i - 1] * 10 + mDigitList1[i];
                patternCnt[p]++;
            }
        }

        for (int i = 0; i < mCnt2; i++) {
            editor2.addLast(mDigitList2[i]);
            if (i >= 2) {
                int p = mDigitList2[i - 2] * 100 + mDigitList2[i - 1] * 10 + mDigitList2[i];
                patternCnt[p]++;
            }
        }
    }

    public void append(int mDir, int mNum1, int mNum2) {
        update(editor1, mNum1, mDir);
        update(editor2, mNum2, mDir);
    }

    private void update(Deque<Integer> dq, int mNum, int mDir) {
        int[] digits = getDigits(mNum); // 덧붙일 숫자를 배열로 변환
        int len = digits.length;

        if (mDir == 1) { // 오른쪽에 추가
            int last1 = 0, last2 = 0;
            Iterator<Integer> it = dq.descendingIterator();
            if (it.hasNext()) last1 = it.next(); // 마지막
            if (it.hasNext()) last2 = it.next(); // 마지막 직전

            // 임시 배열 구성: [마지막직전, 마지막, 새숫자1, 새숫자2...]
            int[] temp = new int[len + 2];
            temp[0] = last2; temp[1] = last1;
            for (int i = 0; i < len; i++) {
                temp[i + 2] = digits[i];
                dq.addLast(digits[i]);
            }

            for (int i = 2; i < temp.length; i++) {
                patternCnt[temp[i-2] * 100 + temp[i-1] * 10 + temp[i]]++;
            }
        } 
        else { // 왼쪽에 추가
            int first1 = 0, first2 = 0;
            Iterator<Integer> it = dq.iterator();
            if (it.hasNext()) first1 = it.next(); // 첫번째
            if (it.hasNext()) first2 = it.next(); // 두번째

            int[] temp = new int[len + 2];
            for (int i = 0; i < len; i++) temp[i] = digits[i];
            temp[len] = first1; temp[len + 1] = first2;

            for (int i = len - 1; i >= 0; i--) {
                dq.addFirst(digits[i]);
            }
            
            for (int i = 2; i < temp.length; i++) {
                patternCnt[temp[i-2] * 100 + temp[i-1] * 10 + temp[i]]++;
            }
        }
    }

    public int countNum(int mNum) {
        int total = patternCnt[mNum];

        // 연결부위 editor1의 끝(2개) + editor2의 시작(2개)
        Iterator<Integer> it1 = editor1.descendingIterator();
        int e1_last = it1.next();
        int e1_last_prev = it1.next();

        Iterator<Integer> it2 = editor2.iterator();
        int e2_first = it2.next();
        int e2_first_next = it2.next();

        // [e1_last_prev, e1_last, e2_first]
        if (e1_last_prev * 100 + e1_last * 10 + e2_first == mNum) total++;
        // [e1_last, e2_first, e2_first_next]
        if (e1_last * 100 + e2_first * 10 + e2_first_next == mNum) total++;

        return total;
    }

    private int[] getDigits(int n) {
        String s = String.valueOf(n);
        int[] res = new int[s.length()];
        for (int i = 0; i < s.length(); i++) res[i] = s.charAt(i) - '0';
        return res;
    }
}