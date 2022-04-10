public class Main {
    public static void main(String[] args) throws Exception {
        BeeRot beeRot = new BeeRot();
        beeRot.dealRequest("我要请假");
        beeRot.dealRequest("1");
        beeRot.dealRequest("albin");
        beeRot.dealRequest("今天气温几度");
        beeRot.dealRequest("上海");
        beeRot.dealRequest("今天气温几度");
        beeRot.dealRequest("北京");
//        System.out.println((new BeeRot()).dealRequest("我要请假"));
    }
}
