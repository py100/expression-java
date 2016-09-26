package exp1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

// Node : 保存每一个项
// Node.d 系数
// Node.str 未知数 ： string
class Node {
	String str;
	int d;

	Node(String s) {
		StringBuilder sb = new StringBuilder();
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
				sb.append(tmp[i].charAt(0));
			}
		}
		this.str = sb.toString();
	}

	void adjust() {
		int[] cnt = new int[26];
		for (int i = 0; i < str.length(); i++) {
			cnt[str.charAt(i) - 'a']++;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < cnt[i]; j++) {
				sb.append((char) (i + 'a'));
			}
		}
		str = sb.toString();
	}

	Node simplify(char ch, int dig) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ch)
				d = d * dig;
			else
				sb.append(str.charAt(i));
		}
		str = sb.toString();
		return this;
	}

	Node deri(char ch) {
		StringBuilder sb = new StringBuilder();
		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ch) {
				sb.append(str.charAt(i));
			} else {
				if (n > 0)
					sb.append(str.charAt(i));
				n++;
			}
		}
		d = n * d;
		str = sb.toString();
		return this;
	}

	void showNode() {
		System.out.print(d);
		for (int i = 0; i < str.length(); i++)
			System.out.print("*" + str.charAt(i));
	}

	// *TODO*
	// change plain syso to String
	void showNode1() {
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
	ArrayList<Node> an;
	
	boolean isch(char ch) {
		return ch >= 'a' && ch <= 'z';
	}
	boolean isdig(char ch) {
		return ch >= '0' && ch <= '9';
	}
	boolean init(String str) {
		str = str.replace(" ", "");
		// empty string
		if (str.length() < 1) return false;
		
		for (int i = 0; i < str.length(); i++) {
			//System.out.println(i+"  "+str.charAt(i));
			if (str.charAt(i) == '^') {
				if (i == 0) return false;
				if (!isch(str.charAt(i-1))) return false;
				if (i + 1 >= str.length()) return false;
				if (!isdig(str.charAt(i+1))) return false;
			}
			else if (str.charAt(i) == '+' || str.charAt(i) == '-') {
				if (i-1 >= 0 && !isch(str.charAt(i-1)) && !isdig(str.charAt(i-1))) return false;
				if (i+1 >= str.length()) return false;
				if (!isch(str.charAt(i+1)) && !isdig(str.charAt(i+1))) return false;
			}
			else if (str.charAt(i) == '*') {
				if (i-1 < 0 || i+1 >= str.length()) return false;
				if (!isch(str.charAt(i-1)) && !isdig(str.charAt(i-1))) return false;
				if (!isch(str.charAt(i+1)) && !isdig(str.charAt(i+1))) return false;
			}
			else if (isch(str.charAt(i))) {
				if (i-1>=0) {
					char ch = str.charAt(i-1);
					if (ch != '+' && ch != '-' && ch != '*' && ch != '^') return false;
				}
				if (i+1<str.length()) {
					char ch = str.charAt(i+1);
					if (ch != '+' && ch != '-' && ch != '*' && ch != '^') return false;
				}
			}
			else if (isdig(str.charAt(i))) {
				if (i-1>=0 && isch(str.charAt(i-1))) return false;
			}
			else return false;
		}
		
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '-') {
				sb.append('+');
				sb.append(str.charAt(i));
			} else if (str.charAt(i) == '^') {
				StringBuilder sbb = new StringBuilder();
				for (int j = i + 1; j < str.length(); j++) {
					char ch = str.charAt(j);
					if (ch >= '0' && ch <= '9') {
						sbb.append(ch);
						i++;
					} else
						break;
				}
				int d = Integer.valueOf(sbb.toString());
				char ch = sb.charAt(sb.length() - 1);
				for (int j = 1; j < d; j++) {
					sb.append('*');
					sb.append(ch);
				}
			} else
				sb.append(str.charAt(i));
		}
		str = sb.toString();
		an = new ArrayList<Node>();
		String[] tmp = str.split("\\+");
		for (int i = 0; i < tmp.length; i++) {
			an.add(new Node(tmp[i]));
		}
		return true;
	}

	void show() {
		for (int i = 0; i < an.size(); i++) {
			if (i != 0 && an.get(i).d >= 0)
				System.out.print("+");
			an.get(i).showNode1();
		}

		if (an.size() == 0)
			System.out.print("0");
		System.out.println();
	}

	void adjust() {
		for (int i = 0; i < an.size(); i++)
			an.get(i).adjust();
	}

	void merge() {
		ArrayList<Node> atmp = new ArrayList<Node>();
		Node nd1;
		Node nd2;
		for (int i = 0; i < an.size(); i++) {
			nd1 = an.get(i);
			for (int j = i + 1; j < an.size(); j++) {
				nd2 = an.get(j);
				if (nd2.str.compareTo(nd1.str) == 0) {
					nd1.d += nd2.d;
					nd2.d = 0;
					an.set(j, nd2);
				}
			}
			if (nd1.d != 0)
				atmp.add(nd1);
		}
		an = atmp;
	}

	void simplify(char ch, int dig) {
		ArrayList<Node> tmp = new ArrayList<Node>();
		for (int i = 0; i < an.size(); i++) {
			tmp.add(an.get(i).simplify(ch, dig));
		}
		an = tmp;
	}

	void deri(char ch) {
		ArrayList<Node> tmp = new ArrayList<Node>();
		for (int i = 0; i < an.size(); i++) {
			tmp.add(an.get(i).deri(ch));
		}
		an = tmp;
	}
}

public class Main {
	private static Scanner cin;

	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream("ine18.txt");
		System.setIn(fis);
		cin = new Scanner(System.in);
		String cmd;
		expression ex = new expression();
		while (cin.hasNext()) {
			cmd = cin.nextLine();
			cmd = cmd.toLowerCase();
			//System.out.println(cmd);
			// get command
			if (cmd.charAt(0) == '!') {
				// simplify
				if (cmd.charAt(1) == 's') {
					String[] tmp = cmd.split("\\s");
					
					if (!tmp[0].equals("!simplify")) {
						System.out.println("Input Error!!\nNo such operation!!");
						System.exit(0);
//						continue;
					}
					tmp = tmp[1].split(",");
					for (int i = 0; i < tmp.length; i++) {
						tmp[i] = tmp[i].replace(" ", "");
						String[] tosimp = tmp[i].split("=");
						if (tosimp.length != 2 || tosimp[0].length() != 1 || (!(tosimp[0].charAt(0) >= 'a' && tosimp[0].charAt(0)<='z'))) {
							System.out.println("Input Error!!\nNo such operation!!");
							System.exit(0);
						}
						char tmpchar = tosimp[0].charAt(0);
						int dig = 1;
						try
						{
							dig = Integer.parseInt(tosimp[1]);
						}catch(Exception e){
							System.out.println("Input Error");
							System.exit(0);
						}
						ex.simplify(tmpchar, dig);
						ex.merge();
					}
				}
				// d/dy
				else if (cmd.charAt(1) == 'd') {
					if (cmd.length() != 5 || !cmd.substring(1, 4).equals("d/d")) {
						System.out.println("Input Error!!\nNo such operation!!");
						System.exit(0);
					}
					char cmdchar = cmd.charAt(4);
					if (!(cmdchar >= 'a' && cmdchar <= 'z')) {
						System.out.println("Input Error!!\nNo such operation!!");
						System.exit(0);
					}
					ex.deri(cmdchar);
					ex.merge();
				}
				else {
					System.out.println("Input Error!!\nNo such operation!!");
				}
				ex.show();
			}
			// get expression
			else {
				if (!ex.init(cmd)) {
					System.out.println("Input Error!!");
					System.exit(0);
					//continue;
				}
				ex.adjust();
				// ex.show();
				ex.merge();
				ex.show();
			}
		}
		System.exit(0);
	}
}
