package lesson1CP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author Yuchen Cao �������� 
 * 1 �ؼ���(complete) 
 * 2 ��ʶ��(complete) 
 * 3 ʮ������(complete) 
 * 4 �����(complete) 
 * 5 �ָ���(complete) 
 * 6 �ַ���(complete) 
 * 7 �˽�����(complete) 
 * 8 ʮ��������(complete)
 */
public class LexicalAnalyzer {
	static String[] keyWord = { "if", "then", "else", "while", "do", "return", "int", "float", "include", "printf", "stdio", "main" };// �ؼ���
	static String[] operation = { "+", "-", "*", "/", ">", "<", "=", "(", ")" };// �����
	static String[] symbol = { "(", ")", ".", "#", "{", "}", ";" };// �ָ���
	static ArrayList<String> keyWords = null;
	static ArrayList<String> operations = null;
	static ArrayList<String> symbols = null;

	static int p, lines;

	public static void main(String[] args) throws FileNotFoundException {
		init();
		Scanner inputAddress = new Scanner(System.in);
		System.out.print("�������ļ���ַ : ");
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

	// ��ʼ��������ת��ΪArrayList
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
		str = str.trim(); // ȥ���ַ���ͷβ�հ��ַ�
		for (; p < str.length(); p++) {
			ch = str.charAt(p); // ��λ��pλ�õ��ַ�

			if (Character.isDigit(ch)) {
				DigitCheck(str); // �жϳ���

			} else if (Character.isLetter(ch) || ch == '_') {
				letterCheck(str); // ��ʶ���͹ؼ���
			} else if (ch == '"') {
				stringCheck(str); // �ַ����ж�
			} else if (ch == ' ') {
				continue;
			} else {
				symbolCheck(str);
			}
		}
	}

	/*
	 * ���ֵ�ʶ�� 1��ʶ���˳��� 1.1�������ո�� 1.2��������������߽�� 2����������� 2.1������������С���� 2.2��������ĸ
	 */
	public static void DigitCheck(String str) {
		String token = String.valueOf(str.charAt(p++)); // ת�����ַ���

		String token1 = token; // ʮ���������ж�
		token1 += str.charAt(p);

		// �ж����ֵ�С�����Ƿ������Ƿ����1
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

	// ��ʶ�����ؼ��ֵ�ʶ��
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

	// ���ŵ�ʶ��
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

	// �ַ������
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
