package sequences;

/**
 *
 * @author Mithun Acharya
 * A small program to show the bug in issta2006.BinTree.BinTree.java
 * exposed through OSM construction.
 *
 */
public class TestBinTree {
	public static void main(String[] args){
		BinTree t = new BinTree();
		if(t.find(9) == true)
			System.out.println("Not possible for sure!!");
		for (int i = 0; i < 100; i++)
			t.add(i);
		for (int i = 0; i < 100; i++)
			t.remove(i);
		for (int i = 0; i < 100; i++)
			if(t.find(i) == true){
				System.out.println("BUG");
				System.out.println("i = " + i);
			}
	}
}
