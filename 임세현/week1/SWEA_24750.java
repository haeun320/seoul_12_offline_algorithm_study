import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
 
class UserSolution {
  
    private static int[] cnt;//숫자별 등장횟수 저장 배열
 
    private static int[] n1, n2;//두 수
    private static int fir1, fir2, end1, end2;//두 수의 시작,끝 포인터. fir~end까지가 수의 범위
 
    public void init(int mCnt1, int mDigitList1[], int mCnt2, int mDigitList2[]){
 
        cnt = new int[1000];//~999
        n1 = new int[200_000];//길이는 이정도면 충분
        n2 = new int[200_000];
 
        fir1 = fir2 = end1 = end2 = 85_000;//대략적인 중간값 -> 앞뒤 8만개씩 들어와도 여유있게

        //숫자 2개를 배열에 저장
        for(int i=0;i<mCnt1;i++){
            n1[end1++] = mDigitList1[i];
        }
        for(int i=0;i<mCnt2;i++) {
            n2[end2++] = mDigitList2[i];
        }

        //현재 상태에서 count
        for(int i=fir1;i<end1-2;i++){
            int tmp = n1[i]*100 + n1[i+1]*10 + n1[i+2];
            cnt[tmp]++;
        }
 
        for(int i=fir2;i<end2-2;i++){
            int tmp = n2[i]*100 + n2[i+1]*10 + n2[i+2];
            cnt[tmp]++;
        }
    }
 
    public void append(int mDir, int mNum1, int mNum2) {
 
        String tmp1 = String.valueOf(mNum1);
        String tmp2 = String.valueOf(mNum2);
 
        if(mDir==0) {//왼쪽에 더할 때 : 끝에서부터 하나씩 넣기
            for(int i=tmp1.length()-1;i>=0;i--) {
                n1[--fir1] = tmp1.charAt(i) - '0';
            }
            for(int i=tmp2.length()-1;i>=0;i--) {
                n2[--fir2] = tmp2.charAt(i) - '0';
            }//새로 들어온 부분 count 갱신
            for(int i=fir1;i<fir1+tmp1.length();i++) {
                int tmp = n1[i]*100 + n1[i+1]*10 + n1[i+2];
                cnt[tmp]++;
            }
            for(int i=fir2;i<fir2+tmp2.length();i++) {
                int tmp = n2[i]*100 + n2[i+1]*10 + n2[i+2];
                cnt[tmp]++;
            }
        }
        else {//오른쪽에 더할 때
            int prev1 = end1;
            int prev2 = end2;
            for(int i=0;i<tmp1.length();i++) {
                n1[end1++] = tmp1.charAt(i) - '0';
            }
            for(int i=0;i<tmp2.length();i++) {
                n2[end2++] = tmp2.charAt(i) - '0';
            }
            //새로 들어온 부분 count 갱신
            for(int i=prev1-2;i<end1-2;i++) {
                int tmp = n1[i]*100 + n1[i+1]*10 + n1[i+2];
                cnt[tmp]++;
            }
            for(int i=prev2-2;i<end2-2;i++) {
                int tmp = n2[i]*100 + n2[i+1]*10 + n2[i+2];
                cnt[tmp]++;
            }
        }
    }
 
    public int countNum(int mNum) {//두 수를 합쳤을 때 만들어지는 수까지 확인한 뒤 반환
        int tmp = 0;
        int tmp1 = n1[end1-2]*100+n1[end1-1]*10 + n2[fir2];//n1 끝 2개 + n2 앞 1개
        int tmp2 = n1[end1-1]*100+n2[fir2]*10+n2[fir2+1];//n1 끝 1개 + n2 앞 2개
        if(tmp1==mNum) tmp++;
        if(tmp2==mNum) tmp++;
 
        return cnt[mNum] + tmp;
    }
}
