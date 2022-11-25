package assignment_class_modeling;

public class Bus extends Transportaion {
    int maxPassengerCnt;
    int nowPassengerCnt;
    int remainingSeat;
    String[] stateStr = {"차고지행","운행"};

    // 생성자
    public Bus(int refuelAmount){
        super(refuelAmount);
        state = stateStr[stateNum];

        maxPassengerCnt = 30;
        remainingSeat = 30;
        nowPassengerCnt = 0;
    }

    // 운행 시작
    public void run(){
        super.run();
        state = stateStr[stateNum];
    }

    // 상태변경 메소드 : 0: 운행불가, 1: 운행가능
    public void stateChange(int x){
        super.stateChange(x);
        state = stateStr[stateNum];
    }

    // 승객 탑승
    public void passengerRiding(int personCnt){
        if(stateNum == 1){
            if(maxPassengerCnt > (nowPassengerCnt + personCnt)){
                nowPassengerCnt += personCnt;
                remainingSeat = maxPassengerCnt - nowPassengerCnt;
            } else {
                System.out.println("최대 승객 수가 초과하여 탑승할 수 없습니다.");
            }
        }
    }

    // 속력변경 메소드 : Operator = '-','+'
    public void speedChange(char operator, int speed){
        super.speedChange(operator, speed);
    }

}