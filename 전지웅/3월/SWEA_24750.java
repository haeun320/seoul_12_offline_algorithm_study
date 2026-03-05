class UserSolution {

    static int[] basicCnt;

    // 수열1과 수열2의 양 끝 숫자
    static String num1L, num1R, num2L, num2R;

    public void init(int mCnt1, int mDigitList1[], int mCnt2, int mDigitList2[]) {
        // mNum 등장횟수 저장할 배열
        basicCnt = new int[1000];

        // num1의 처음 두자리, 마지막 두 자리
        num1L = "" + mDigitList1[0] + mDigitList1[1];
        num1R = "" + mDigitList1[mCnt1 - 2] + mDigitList1[mCnt1 - 1];

        // num2의 처음 두자리, 마지막 두 자리
        num2L = "" + mDigitList2[0] + mDigitList2[1];
        num2R = "" + mDigitList2[mCnt2 - 2] + mDigitList2[mCnt2 - 1];

        // mDigitList1 순회하면서 배열에 mNum 등장횟수 저장
        String str = "";
        for (int i = 0; i < mCnt1; i++) {
            str += mDigitList1[i];
        }
        func(str);

        // mDigitList2 순회하면서 배열에 mNum 등장횟수 저장
        str = "";
        for (int i = 0; i < mCnt2; i++) {
            str += mDigitList2[i];
        }
        func(str);

    }

    public void append(int mDir, int mNum1, int mNum2) {
        String str = "";

        if (mDir == 0) { // 왼쪽에 붙이기
            // num1L에 붙이기
            str = mNum1 + num1L;
            // basicCnt 갱신하고
            func(str);
            // num1L 갱신
            num1L = "" + str.charAt(0) + str.charAt(1);

            // num2L에 붙이기
            str = mNum2 + num2L;
            // basicCnt 갱신
            func(str);
            // num2L 갱신
            num2L = "" + str.charAt(0) + str.charAt(1);

        } else if (mDir == 1) { // 오른쪽에 붙이기
            // num1R에 붙이기
            str = num1R + mNum1;
            // basicCnt 갱신
            func(str);
            // num1R 갱신
            num1R = "" + str.charAt(str.length() - 2) + str.charAt(str.length() - 1);

            // num2R에 붙이기
            str = num2R + mNum2;
            // basicCnt 갱신하고
            func(str);
            // num2R 갱신
            num2R = "" + str.charAt(str.length() - 2) + str.charAt(str.length() - 1);
        }
    }

    // str 문자열에서 mNum 등장횟수 찾기
    public static void func(String str) {
        for (int i = 0; i < str.length() - 2; i++) {
            String n = "" + str.charAt(i) + str.charAt(i + 1) + str.charAt(i + 2);
            basicCnt[Integer.parseInt(n)]++;
        }
    }

    public int countNum(int mNum) {
        // 저장된 mNum 가져오기
        int totalCount = basicCnt[mNum];

        // 중간지점
        String str = num1R + num2L;

        // 중간지점의 mNum 더하기 
        for (int i = 0; i <= 1; i++) {
            String n = "" + str.charAt(i) + str.charAt(i + 1) + str.charAt(i + 2);
            if (Integer.parseInt(n) == mNum) {
                totalCount++;
            }
        }

        return totalCount;
    }
}