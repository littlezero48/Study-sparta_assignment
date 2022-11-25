package assignment_class_modeling;

public class Taxi extends Transportaion {
    String destination;
    int basDistance = 3;            // 기본요금 거리 3km
    int distanceToDestination;      // 목적지까지의 거리
    int perDistanceRate = 200;      // 거리당 요금
    int sumDistanceRate;            // 거리요금 총합
    String[] stateStr = {"운행불가", "일반", "운행중"};

    // 생성자
    public Taxi (int refuelAmount){
        super(refuelAmount);

        if(refuelAmount < 10){
            stateNum = 0;
        }
        state = stateStr[stateNum];

        baseRate = 3300;
    }

    // 운행 시작
    public void run(){
        super.run();
        state = stateStr[stateNum];
    }

    // 상태변경 메소드 : 0: 운행불가, 1: 운행가능, 2:제3의상태
    public void stateChange(int x){
        super.stateChange(x);
        state = stateStr[stateNum];
    }

    // 승객 탑승
    public void passengerRiding(){
        if(stateNum == 1){
            stateNum = 2;
            state = stateStr[stateNum];
        } else {
            System.out.println("탑승 불가 입니다");
        }
    }

    // 속력변경 메소드 : Operator = '-','+'
    public void speedChange(char operator, int speed){
        super.speedChange(operator, speed);
    }

    // 거리당 요금 추가 메소드
    public void plusPerRate(String destination, int distanceToDestination){
        this.destination = destination;
        this.distanceToDestination = distanceToDestination;
        System.out.println("목적지 : "+this.destination);
        if(distanceToDestination > basDistance){
            sumDistanceRate =  perDistanceRate = (this.distanceToDestination - basDistance) * perDistanceRate;
        }
    }

    // 최종 요금 출력 메소드
    public void finalRateCaculate (){
        int finalRate = baseRate + sumDistanceRate;
        System.out.println("최종 요금은 " + finalRate + "원 입니다");
    }
}
