package lesson1CP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author Yuchen Cao 单词种类 
 * 1 关键字(complete) 
 * 2 标识符(complete) 
 * 3 十进制数(complete) 
 * 4 运算符(complete) 
 * 5 分隔符(complete) 
 * 6 字符串(complete) 
 * 7 八进制数(complete) 
 * 8 十六进制数(complete)
 */
public class LexicalAnalyzer {
	static String[] keyWord = { "if", "then", "else", "while", "do", "return", "int", "float", "include", "printf", "stdio", "main" };// 关键字
	static String[] operation = { "+", "-", "*", "/", ">", "<", "=", "(", ")" };// 运算符
	static String[] symbol = { "(", ")", ".", "#", "{", "}", ";" };// 分隔符
	static ArrayList<String> keyWords = null;
	static ArrayList<String> operations = null;
	static ArrayList<String> symbols = null;

	static int p, lines;

	public static void main(String[] args) throws FileNotFoundException {
		init();
		Scanner inputAddress = new Scanner(System.in);
		System.out.print("请输入文件地址 : ");
		String address = inputAddress.nextLine();
		File file = new File(address);
		// The following sentence is used in Test.
		// File file = new File("C:\\Users\\Schneider\\Desktop\\2.txt");
		lines = 1;
		try (Scanner input = new Scanner(file)) {
			while (input.hasNextLine()) {
				String str = input.nextLine();
				analyze(str);
				lines++;
			}
		}finally{
			inputAddress.close();
		}

	}

	// 初始化把数组转换为ArrayList
	public static void init() {
		keyWords = new ArrayList<>();
		operations = new ArrayList<>();
		symbols = new ArrayList<>();
		Collections.addAll(keyWords, keyWord);
		Collections.addAll(operations, operation);
		Collections.addAll(symbols, symbol);
	}

	public static void analyze(String str) {

		p = 0;
		char ch;
		str = str.trim(); // 去除字符串头尾空白字符
		for (; p < str.length(); p++) {
			ch = str.charAt(p); // 定位到p位置的字符

			if (Character.isDigit(ch)) {
				DigitCheck(str); // 判断常数

			} else if (Character.isLetter(ch) || ch == '_') {
				letterCheck(str); // 标识符和关键字
			} else if (ch == '"') {
				stringCheck(str); // 字符串判断
			} else if (ch == ' ') {
				continue;
			} else {
				symbolCheck(str);
			}
		}
	}

	/*
	 * 数字的识别 1、识别退出： 1.1、遇到空格符 1.2、遇到运算符或者界符 2、错误情况： 2.1、两个及以上小数点 2.2、掺杂字母
	 */
	public static void DigitCheck(String str) {
		String token = String.valueOf(str.charAt(p++)); // 转换成字符串

		String token1 = token; // 十六进制数判断
		token1 += str.charAt(p);

		// 判断数字的小数点是否有且是否大于1
		int flag = 0;
		boolean err = false;
		char ch;
		for (; p < str.length(); p++) {
			ch = str.charAt(p);
			if (ch == ' ' || (!Character.isLetterOrDigit(ch) && ch != '.')) {
				break;
			} else if (err) {
				token += ch;
			} else {
				token += ch;
				if (ch == '.') {
					if (flag == 1) {
						err = true;
					} else {
						flag++;
					}
				} else if (Character.isLetter(ch)) {
					err = true;
				}
			}
		}
		if (token1.equals("0x")) {
			System.out.println("(" + 8 + "," + token + ")");
			return;
		}

		if (token.charAt(token.length() - 1) == '.') {
			err = true;
		}
		if (err) {
			System.out.println("line" + lines + ": " + token + " is wrong");
		} else if (token.charAt(0) == '0' && !err) {
			System.out.println("(" + 7 + "," + token + ")");
		} else {
			System.out.println("(" + 3 + "," + token + ")");
		}
		if (p != str.length() - 1 || (p == str.length() - 1 && !Character.isDigit(str.charAt(p)))) {
			p--;
		}
	}

	// 标识符，关键字的识别
	public static void letterCheck(String str) {
		String token = String.valueOf(str.charAt(p++));
		char ch;
		for (; p < str.length(); p++) {
			ch = str.charAt(p);
			if (!Character.isLetterOrDigit(ch) && ch != '_') {
				break;
			} else {
				token += ch;
			}
		}
		if (keyWords.contains(token)) {
			System.out.println("(" + 1 + "," + token + ")");
		} else {
			System.out.println("(" + 2 + "," + token + ")");
		}
		if (p != str.length() - 1
				|| (p == str.length() - 1 && (!Character.isLetterOrDigit(str.charAt(p)) && str.charAt(p) != '_'))) {
			p--;
		}

	}

	// 符号的识别
	public static void symbolCheck(String str) {
		String token = String.valueOf(str.charAt(p++));
		char ch;
		if (symbols.contains(token)) {
			System.out.println("(" + 5 + "," + token + ")");
			p--;
		} else {
			if (operations.contains(token)) {
				if (p < str.length()) {
					ch = str.charAt(p);
					if (operations.contains(token + ch)) {
						token += ch;
						p++;
						if (p < str.length()) {
							ch = str.charAt(p);
							if (operations.contains(token + ch)) {
								token += ch;
								System.out.println("(" + 4 + "," + token + ")");
							} else {
								p--;
								System.out.println("(" + 4 + "," + token + ")");
							}
						} else {
							System.out.println("(" + 4 + "," + token + ")");
						}
					} else {
						p--;
						System.out.println("(" + 4 + "," + token + ")");
					}
				}
			} else {
				p--;
				System.out.println(lines + "line" + ": " + token + " is wrong");
			}
		}
	}

	// 字符串检查
	public static void stringCheck(String str) {
		String token = String.valueOf(str.charAt(p++));
		char ch;
		for (; p < str.length(); p++) {
			ch = str.charAt(p);
			token += ch;
			if (ch == '"') {
				break;
			}
		}
		if (token.charAt(token.length() - 1) != '"') {
			System.out.println(lines + "line" + ": " + token + " is wrong");
		} else {
			System.out.println("(" + 6 + "," + token + ")");
		}
	}

}
