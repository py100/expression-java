package exp1;

import java.util.Scanner;
import java.util.Vector;

// Node : 保存每一个项
// Node.d 系数
// Node.str 未知数 ： string
class Node {
	String str;
	int d;

	Node(String s) {
		this.str = "";
		d = 1;
		String[] tmp = s.split("\\*");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].charAt(0) <= '9' && tmp[i].charAt(0) >= '0') {
				int t = 0;
				for (int j = 0; j < tmp[i].length(); j++) {
					t = t * 10 + tmp[i].charAt(j) - '0';
				}
				d = d * t;
			} else {
				str = str + tmp[i].charAt(0);
			}
		}
	}

	void adjust() {
		int[] cnt = new int[26];
		for (int i = 0; i < str.length(); i++) {
			cnt[str.charAt(i) - 'a']++;
		}
		String tmp = "";
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < cnt[i]; j++) {
				tmp = tmp + (char) (i + 'a');
			}
		}
		str = tmp;
	}

	void showNode() {
		System.out.print(d);
		for (int i = 0; i < str.length(); i++)
			System.out.print("*" + str.charAt(i));
	}
}

class expression {
	Vector<Node> vn;

	void init(String str) {
		vn = new Vector<Node>();
		String[] tmp = str.split("\\+");
		for (int i = 0; i < tmp.length; i++) {
			vn.add(new Node(tmp[i]));
		}
	}

	void show() {
		for (int i = 0; i < vn.size(); i++) {
			if (i != 0)
				System.out.print("+");
			vn.elementAt(i).showNode();
		}
		System.out.println();
	}

	void adjust() {
		for (int i = 0; i < vn.size(); i++)
			vn.elementAt(i).adjust();
	}
	void merge() {
		
	}
}

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner cin = new Scanner(System.in);

		String cmd;
		expression ex = new expression();
		while (cin.hasNext()) {
			cmd = cin.nextLine();

			// get command
			if (cmd.charAt(0) == '!') {
				System.out.println("233");
			}
			// get expression
			else {
				ex.init(cmd);
				ex.adjust();
				ex.show();
			}
		}
	}
}
//gSFvxzvvfvfvxdv