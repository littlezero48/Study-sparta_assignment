public class Bus extends Transportaion {
    int maxPassengerCnt;
    int nowPassengerCnt;
    int remainingSeat;
    String[] stateStr = {"차고지행","운행"};

    public Bus(int refuelAmount){
        super(refuelAmount);
        state = stateStr[stateNum];

        maxPassengerCnt = 30;
        remainingSeat = 30;
        nowPassengerCnt = 0;
    }

    public void run(){
        super.run();
        state = stateStr[stateNum];
    }

    // 상태변경 메소드 : 0: 운행불가, 1: 운행가능
    public void stateChange(int x){
        super.stateChange(x);
        state = stateStr[stateNum];
    }

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
    public void speedChange(char operator, int speed){
        super.speedChange(operator, speed);
    }

}
