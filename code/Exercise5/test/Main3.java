package pluh;
public class A {

    public int max(int a, int b, int c) {
        if (a > b) {
            if(a > c) {
                return a;
            }
            else {
                return c;
            }
        }
        else {
            if (b > c) {
                return b;
            }
            else {
                return c;
            }
        }
    }

    public class B {
        int[] l = new int[10];
        public void coolMethod(){
            for(int i = 0; i<12; i++){
                int a = 1;
		if(a>b){
		   int a = 1;
		}
            }
            for(int i:l){
                int a = 1;
            }
            while(true){
                int a = 1;
            }
        }
    }
}