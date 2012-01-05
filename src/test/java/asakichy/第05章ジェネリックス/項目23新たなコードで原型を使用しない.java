package asakichy.第05章ジェネリックス;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目23新たなコードで原型を使用しない {

	@Test
	public void 原型とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("用語定義。").e();
		o.l3("ジェネリッククラス").s("型パラメータを宣言に持つクラス。").e();
		o.l3("ジェネリックインターフェイス").s("型パラメータを宣言に持つインターフェイス。").e();
		o.l3("ジェネリック型").s("ジェネリッククラス＋ジェネリックインターフェイス。").e();
		o.l3("仮型パラメータ").s("定義時、クラス名やインタフェース名の後に、アングルブラケットで囲まれたパラメータ。").e();
		o.l3("実型パラメータ").s("使用時、クラス名やインタフェース名の後に、アングルブラケットで囲まれたパラメータ。").e();
		o.l3("パラメータ化された型").s("実型パラメータのリストをもつ型。").e();

		o.l2("ジェネリック型は、パラメータ化された型の集合を定義。").e();
		o.l2("ジェネリック型は、パラメータを伴わないで使用される「原型」も定義。").e();

		o.l1("【たとえば？】").e();
		o.l2("ジェネリッククラス").s("「java.util.ArrayList<E>」。").e();
		o.l2("ジェネリックインターフェイス").s("「java.util.List<E>」。").e();
		o.l2("ジェネリック型").s("「java.util.ArrayList<E>」や「java.util.List<E>」。").e();
		o.l2("仮型パラメータ").s("java.util.List<E>の「E」。").e();
		o.l2("実型パラメータ").s("java.util.List<String>の「String」。").e();
		o.l2("パラメータ化された型").s("ジェネリック型java.util.List<E>に、");
		o.__("String実型パラメータをはめ込んだ「java.util.List<String>型」。").e();
		o.l2("原型").s("ジェネリック型java.util.List<E>に対する「java.util.List」。").e();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void 原則_使用しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ジェネリックがサポートされている環境では、原型は使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("誤った型の使用をコンパイル時に発見できない。").e();
		o.l2("誤った型をコレクションに登録した場合、実行時エラー（ClassCastException）が、");
		o.__("登録したのとは離れた場所で発生するリスクあり。").e();
		o.l2("ジェネリック型であれば、登録される型が保証され、取得時キャストも必要なくなる。").e();
		o.l2("使用すると警告が出力される。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.List原型。").e();
		List rawList = new ArrayList();
		rawList.add(Integer.valueOf(0));
		try {
			String s = (String) rawList.get(0);
			assertThat(s, is("0"));
		} catch (ClassCastException e) {
			// コンパイル時に発見できず、実行時ClassCastExceptionが発生。
		}

		o.l2("java.util.List<String>ジェネリック型。").e();
		List<String> genericList = new ArrayList<String>();
		// genericList.add(Integer.valueOf(0)); コンパイルエラー！
		genericList.add("0");
		String s = (String) genericList.get(0);
		assertThat(s, is("0"));
	}

	@Test
	public void 使用しない_実型パラメータObjectでもジェネリック型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("原型より、任意のオブジェクトの挿入が可能なパラメータ化された型を使用する方がよい。").e();
		o.l3("List<Object>でもListよりはマシ。").e();

		o.l1("【どうして？】").e();
		o.l2("原型Listは、ジェネリック型検査が一切行われない。").e();
		o.l2("List<Object>は、明示的にどのような型でも保持できることを、コンパイラに伝えている。").e();
		o.l2("型安全性は失われてない。").e();

		o.l1("【たとえば？】").e();
		o.l2("引数の型に、java.util.List原型を使用したunsafeAdd()関数。").e();
		List<String> strings = new ArrayList<String>();
		unsafeAdd(strings, Integer.valueOf(0));
		try {
			String s = strings.get(0); // コンパイラがキャストしてくれる、が、入っているのはInteger
			assertThat(s, is("0"));
		} catch (ClassCastException e) {
			// コンパイル時に発見できず、実行時ClassCastExceptionが発生。
		}
		o.l2("引数の型に、java.util.List<Object>ジェネリック型を使用したsafeAdd()関数。").e();
		// 一方、こちらは、コンパイルエラー
		// safeAdd(strings, Integer.valueOf(0));
	}

	// 原型List引数に、List<String>が渡せる
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void unsafeAdd(List list, Object o) {
		list.add(o);
	}

	// List<Object>引数に、List<String>は渡せない
	@SuppressWarnings("unused")
	private static void safeAdd(List<Object> list, Object o) {
		list.add(o);
	}

	@Test
	public void 使用しない_要素型不明コレクションにもジェネリック型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("要素型が何であるか気にしないコレクションに対しても、原型を使用してはいけない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("非境界ワイルドカード型を使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("非境界ワイルドカード型には（null以外の）いかなる要素も追加できない。").e();
		o.l3("追加したら「コンパイル」エラー。").e();
		o.l2("よって、コレクションの不変式が壊れなくなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("引数の型に、原型を使用したrawNumElementsInCommon()。").e();
		o.l2("引数の型に、境界ワイルドカード型を使用したnumElementsInCommon()。").e();
		Set<String> s1 = new HashSet<String>(Arrays.asList("1", "2", "3"));
		Set<String> s2 = new HashSet<String>(Arrays.asList("2", "3", "4"));
		// 結果は同じだが、関数内での引数の扱いに差がある
		assertThat(rawNumElementsInCommon(s1, s2), is(2));
		assertThat(numElementsInCommon(s1, s2), is(2));
	}

	@SuppressWarnings("rawtypes")
	private static int rawNumElementsInCommon(Set s1, Set s2) {
		// 引数のSetに、addできてしまう。
		int result = 0;
		for (Object o1 : s1) {
			if (s2.contains(o1)) {
				result++;
			}
		}
		return result;
	}

	private static int numElementsInCommon(Set<?> s1, Set<?> s2) {
		// 引数のSetに、addするとコンパイルエラー。
		int result = 0;
		for (Object o1 : s1) {
			if (s2.contains(o1)) {
				result++;
			}
		}
		return result;
	}

	@Test
	public void 例外_クラスリテラル() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスリテラルは原型以外使えない。").e();

		o.l1("【どうして？】").e();
		o.l2("ジェネリック型情報は、実行時には消されているため。").e();

		o.l1("【たとえば？】").e();
		o.l2("List.class、String[].class、int.classは存在する。").e();
		o.l2("List<String>.class、List<?>.classは存在しない。").e();
	}

	@Test
	public void 例外_instanceof演算子() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("非境界ワイルドカード型以外のパラメータ化された型に、instanceof演算子は使用できない。").e();
		o.l2("非境界ワイルドカード型を使っても、動作に何の影響も与えない。").e();

		o.l1("【どうして？】").e();
		o.l2("ジェネリック型情報は、実行時には消されているため。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Setの、instanceofでの正しい使用方法。").e();
		HashSet<String> set = new HashSet<String>();
		if (set instanceof Set) { // 原型
			Set<?> s = (Set<?>) set; // ワイルドカード型
			assertThat(s.isEmpty(), is(true));
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ジェネリックを使いってよい開発プロジェクトなら、原型は使用しないこと。").e();
		o.l1("未然に事故を防ぐため、「実行時エラーよりコンパイルエラー」を徹底。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
