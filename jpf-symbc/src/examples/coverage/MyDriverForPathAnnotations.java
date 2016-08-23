package coverage;

public class MyDriverForPathAnnotations {
    // The test driver
    public static void main(String[] args) {
    	int A[] = new int[100];
    	int B[] = new int[100];

        MyClassWithPathAnnotations mca = new MyClassWithPathAnnotations();
        //mca.myMethod(1, 2);
        //mca.sample1(0, 1, 2);
        //mca.sample2(1);
        //mca.sample3(1);
        //mca.sample4(1);
        //mca.quickSort(array, 1, 2);
        //mca.sample5(1, 1, 1);
        //mca.sample6(1, 1);
        //mca.test1(A, B);
        //mca.min(1, 2, 3);
        //mca.sum(1, 1, 1);
        mca.f(1, 2);
    }
}
