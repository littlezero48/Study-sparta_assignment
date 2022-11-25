public class Transportaion {
    int num;            // 차량번호
    int baseRate;       // 기본요금
    int refuelAmount;   // 주유량
    int nowSpeed;       // 지금 속력
    int stateNum;       // 지금 상태 0: 운행불가, 1: 운행가능, 2:제3의상태
    String state;

    public Transportaion (int refuelAmount){
        this.refuelAmount = refuelAmount;
        this.num =
        stateNum = 1;
    }

    public void run(){
        if(refuelAmount < 10){
            stateNum = 0;
        } else {
            stateNum = 1;
        }
    }

    // 상태변경 메소드 : 0: 운행불가, 1: 운행가능, 2:제3의상태
    public void stateChange(int x){
        if(x == 1){
            if(refuelAmount < 10){
                System.out.println("주유가 필요합니다");
                stateNum = 0;
            } else {
                stateNum = 1;
            }
        } else {
            stateNum = x;
        }
    }

    // 승객 탑승
    public void passengerRiding(){

    }

    // 속력변경 메소드 : Operator = '-','+'
    public void speedChange(char operator, int speed){
        if( stateNum != 0 && refuelAmount >= 10){
            if(operator == '+') nowSpeed += speed;
            if(operator == '-') nowSpeed -= speed;
        } else if (refuelAmount < 10){
            System.out.println("주유량을 확인해 주세요.");
        }
    }
}
