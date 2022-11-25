public class Main {
    public static void main(String[] args) {

        System.out.println("--------------------------------버스 객체 생성");
        Bus bus1 = new Bus(100);
        System.out.println("버스1 상태: " + bus1.state);
        System.out.println("버스1 번호: " + bus1.num);
        Bus bus2 = new Bus(5);
        System.out.println("버스2 상태: " + bus2.state);
        System.out.println("버스2 번호: " + bus2.num);

        System.out.println("--------------------------------버스 상태 변경");
        bus1.stateChange(0);
        System.out.println("버스 상태: " + bus1.state);
        bus1.run();
        System.out.println("run() 이후 버스상태: " + bus1.state);

        System.out.println("--------------------------------버스 승객 탑승");
        bus1.passengerRiding(13);
        System.out.println("현재 탑승자 수: " +bus1.nowPassengerCnt);
        System.out.println("현재 여유석 수: " +bus1.remainingSeat);
        bus1.passengerRiding(20);
        System.out.println("현재 탑승자 수: " +bus1.nowPassengerCnt);
        System.out.println("현재 여유석 수: " +bus1.remainingSeat);


        System.out.println("--------------------------------버스 속도 변경");
        bus1.speedChange('+', 100);
        System.out.println("버스 현재속력: " + bus1.nowSpeed);
        bus1.speedChange('-', 40);
        System.out.println("버스 현재속력: " + bus1.nowSpeed);

        System.out.println("--------------------------------택시 객체 생성");
        Taxi taxi = new Taxi(100);
        System.out.println("택시 상태: " + taxi.state);
        System.out.println("택시 번호: " + taxi.num);

        System.out.println("--------------------------------택시 상태 변경");
        taxi.stateChange(0);
        System.out.println("택시 상태: " + taxi.state);
        taxi.run();
        System.out.println("run() 이후 택시상태: " + taxi.state);
        taxi.stateChange(2);
        System.out.println("택시 상태: " + taxi.state);
        taxi.stateChange(1);
        System.out.println("택시 상태: " + taxi.state);

        System.out.println("--------------------------------택시 승객 탑승");
        taxi.passengerRiding();
        System.out.println("택시 상태: " + taxi.state);

        System.out.println("--------------------------------택시 속도 변경");
        taxi.speedChange('+', 150);
        System.out.println("택시 현재속력: " + taxi.nowSpeed);
        taxi.speedChange('-', 100);
        System.out.println("택시 현재속력: " + taxi.nowSpeed);

        System.out.println("--------------------------------택시 거리당 요금 추가");
        taxi.plusPerRate("춘천", 74);
        System.out.println("목적지 " + taxi.destination + "까지 거리당 추가 요금은 " + taxi.sumDistanceRate + "원 입니다." );

        System.out.println("--------------------------------택시 최종 요금");
        taxi.finalRateCaculate();

    }
}
