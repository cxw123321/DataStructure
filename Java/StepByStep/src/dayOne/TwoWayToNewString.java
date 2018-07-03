package dayOne;

public class TwoWayToNewString {
    public static void main(String[] args){
        String a = "abc";
        String b = "abc";

        String c = new String("efg");
        String d = new String("efg");

        String e = new String("hij").intern();
        String f = new String("hij").intern();

        if(a == b){
            System.out.println("a与b引用地址相同");
        }else{
            System.out.println("a与b引用地址不同");
        }

        if(c == d){
            System.out.println("c与d引用地址相同");
        }else{
            System.out.println("c与d引用地址不同");
        }

        if(e == f){
            System.out.println("e与f引用地址相同");
        }else{
            System.out.println("e与f引用地址不同");
        }
    }
}
