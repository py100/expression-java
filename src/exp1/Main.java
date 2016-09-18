package exp1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		if (s.charAt(0) == '-') {
			d = -1;
			s = s.substring(1);
		}
		String[] tmp = s.split("\\*");
		// tmp[].charAt()是未知数前面的系数　
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].charAt(0) <= '9' && tmp[i].charAt(0) >= '0') {
				d = d * Integer.valueOf(tmp[i]);
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

	Node simplify(char ch, int dig) {
		String tmp = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ch)
				d = d * dig;
			else
				tmp = tmp + str.charAt(i);
		}
		str = tmp;
		return this;
	}

	Node deri(char ch) {
		String tmp = "";
		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ch) {
				tmp = tmp + str.charAt(i);
			} else {
				if (n > 0)
					tmp = tmp + str.charAt(i);
				n++;
			}
		}
		d = n * d;
		str = tmp;
		return this;
	}

	void showNode() {
		System.out.print(d);
		for (int i = 0; i < str.length(); i++)
			System.out.print("*" + str.charAt(i));
	}

	void showNode1() {
		// String tmp = "";
		System.out.print(d);
		char pchar = '\0';
		int cnt = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != pchar) {
				if (cnt == 1)
					System.out.print("*" + pchar);
				else if (cnt > 1) {
					System.out.print("*" + pchar + "^" + cnt);
				}
				pchar = str.charAt(i);
				cnt = 1;
			} else
				cnt++;
		}
		if (cnt == 1)
			System.out.print("*" + pchar);
		else if (cnt > 1) {
			System.out.print("*" + pchar + "^" + cnt);
		}
	}
}

class expression {
	Vector<Node> vn;

	void init(String str) {
		String tmp1 = "";
		
		for(int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == '-')
				tmp1 = tmp1 + "+" + str.charAt(i);
			else if (str.charAt(i) == '^') {
				String dd = "";
				for (int j = i + 1; j < str.length(); j++) {
					char ch = str.charAt(j);
					if (ch >= '0' && ch <= '9') {
						dd = dd + ch;
						i++;
					} else
						break;
				}
				int d = Integer.valueOf(dd);
				char ch = tmp1.charAt(tmp1.length() - 1);
				for (int j = 1; j < d; j++)
					tmp1 = tmp1 + "*" + ch;
			} else
				tmp1 = tmp1 + str.charAt(i);
		}
		
		str = tmp1;
		vn = new Vector<Node>();
		// System.out.println(str);
		String[] tmp = str.split("\\+");
		for (int i = 0; i < tmp.length; i++) {
			vn.add(new Node(tmp[i]));
		}
	}

	void show() {
		for (int i = 0; i < vn.size(); i++) {
			if (i != 0 && vn.elementAt(i).d >= 0)
				System.out.print("+");
			vn.elementAt(i).showNode1();
		}
		
		if (vn.size() == 0)
			System.out.print("0");
		System.out.println();
	}

	void adjust() {
		for (int i = 0; i < vn.size(); i++)
			vn.elementAt(i).adjust();
	}

	void merge() {
		Vector<Node> tmp = new Vector<Node>();
		Node nd1;
		Node nd2;
		for (int i = 0; i < vn.size(); i++) {
			nd1 = vn.elementAt(i);
			for (int j = i + 1; j < vn.size(); j++) {
				nd2 = vn.elementAt(j);
				if (nd2.str.compareTo(nd1.str) == 0) {
					nd1.d += nd2.d;
					nd2.d = 0;
					vn.removeElementAt(j);
					vn.add(j, nd2);
				}
			}
			if (nd1.d != 0)
				tmp.add(nd1);
		}
		vn = tmp;
	}

	void simplify(char ch, int dig) {
		Vector<Node> tmp = new Vector<Node>(0);
		for (int i = 0; i < vn.size(); i++) {
			tmp.addElement(vn.elementAt(i).simplify(ch, dig));
		}
		vn = tmp;
	}

	void deri(char ch) {
		Vector<Node> tmp = new Vector<Node>(0);
		for (int i = 0; i < vn.size(); i++) {
			tmp.addElement(vn.elementAt(i).deri(ch));
		}
		vn = tmp;
	}
}

public class Main {
	private static Scanner cin;

	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream fis=new FileInputStream("in18.txt");  
        System.setIn(fis);
		cin = new Scanner(System.in);
		String cmd;
		expression ex = new expression();
		while (cin.hasNext()) {
			cmd = cin.nextLine();
			cmd = cmd.toLowerCase();
			// get command
			if (cmd.charAt(0) == '!') {
				// simplify
				if (cmd.charAt(1) == 's') {
					String[] tmp = cmd.split("\\s");
					tmp = tmp[1].split(",");
					for (int i = 0; i < tmp.length; i++) {
						String[] tosimp = tmp[i].split("=");
						char tmpchar = tosimp[0].charAt(0);
						int dig = Integer.parseInt(tosimp[1]);
						ex.simplify(tmpchar, dig);
						ex.merge();
					}
				}
				// d/dy
				else {
					char cmdchar = cmd.charAt(4);
					ex.deri(cmdchar);
					ex.merge();
				}
				ex.show();
			}
			// get expression
			else {
				for (int i = 0; i < cmd.length(); i++) 
				{
					if(cmd.charAt(i) == '/')
					{
						System.out.println("Error!");
						System.exit(0);
					}
					
				}
				ex.init(cmd);
				ex.adjust();
//				ex.show();
				ex.merge();
				ex.show();
			}
		}
		System.exit(0);
	}
}
