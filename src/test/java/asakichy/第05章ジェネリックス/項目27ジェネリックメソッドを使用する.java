package asakichy.第05章ジェネリックス;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目27ジェネリックメソッドを使用する {

	@Test
	public void ジェネリックメソッドとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッドだけでもジェネリック化できる。").e();

		o.l1("【どうして？】").e();
		o.l2("クラスのジェネリック化と、同等の利益が得られる。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Collections#binarySearch()やsort()。").e();

		o.l1("【どうすれば？】").e();
		o.l2("型パラメータリストをメソッドの修飾子とメソッドの戻り値型の間に入れる。").e();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void 使用_原型メソッドのジェネリック化() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("原型コレクションを利用している関数をジェネリック化。").e();

		o.l1("【どうして？】").e();
		o.l2("原型は型危険なので、ジェネリック化して型安全へ。").e();
		o.l2("キャストも要らないので、使いやすいし。").e();

		o.l1("【たとえば？】").e();
		o.l2("原型を使用したunionByRawtypes()関数。").e();
		o.l2("ジェネリック化したunionByGenerics()関数。").e();

		// 型危険
		// 定義側も、使用側も、無検査警告が出力されまくり
		Set sr1 = new HashSet(Arrays.asList("a", "b")); // [warning]
		Set sr2 = new HashSet(Arrays.asList("c", "d")); // [warning]
		Set sr3 = unionByRawtypes(sr1, sr2); // [warning]
		assertThat(sr3, containsInAnyOrder("a", "b", "c", "d")); // [warning]

		// 型安全
		Set<String> sg1 = new HashSet<String>(Arrays.asList("a", "b"));
		Set<String> sg2 = new HashSet<String>(Arrays.asList("c", "d"));
		Set<String> sg3 = unionByGenerics(sg1, sg2);
		assertThat(sg3, containsInAnyOrder("a", "b", "c", "d"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Set unionByRawtypes(Set s1, Set s2) { // [warning]
		Set result = new HashSet(s1);// [warning]
		result.addAll(s2);// [warning]
		return result;
	}

	private static <E/* ここが型パラメータ */> Set<E> unionByGenerics(Set<E> s1, Set<E> s2) {
		Set<E> result = new HashSet<E>(s1);
		result.addAll(s2);
		return result;
	}

	@Test
	public void 使用_ジェネリックstaticファクトリメソッド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ジェネリックを多く含む型がある場合、staticファクトリメソッドを提供する。").e();

		o.l1("【どうして？】").e();
		o.l2("パラメータ化された型のインスタンスを生成する手順を軽減するため。").e();

		o.l1("【どうすれば？】").e();
		o.l2("ジェネリックメソッド呼び出しが提供する型推論を活用。").e();

		o.l1("【たとえば？】").e();
		o.l2("newHashMap()ファクトリメソッド。").e();

		// コンストラクタを使うと、左辺と右辺に記述が重複
		Map<String, List<String>> construct = new HashMap<String, List<String>>();
		assertThat(construct, anything());

		// 型推論により、右辺がスッキリ
		Map<String, List<String>> factory = newHashMap();
		assertThat(factory, anything());

		{
			/** 【補】 ダイヤモンド・オペレータ */
			// Java7から、ジェネリック型生成時の記述省略が、言語レベルでサポートされました。
			// ダイヤモンドオペレータ（「<>」のこと）を使用します。
			//
			// Map<String, List<String>> construct = new HashMap<>();
		}
	}

	private static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	@Test
	public void 使用_ジェネリックシングルトン() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("多くの異なった型に適用可能な、共有の不変オブジェクトを生成できる。").e();
		o.l2("その共有オブジェクトを使いまわすため、staticファクトリーメソッドを書く。").e();

		o.l1("【どうして？】").e();
		o.l2("ジェネリックスはイレイジャにより実装されているので、");
		o.__("必要とされたすべての型パラメータ化に対して、単一のオブジェクトを使用できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("GenericSingletonFactoryクラス。").e();

		// 型パラメータがStringであると推論
		UnaryFunction<String> sameString = GenericSingletonFactory.identityFunction();
		String[] strings = { "jute", "hemp", "nylon" };
		for (String s : strings) {
			assertThat(sameString.apply(s), is(s));
		}

		// 型パラメータがNumberであると推論
		UnaryFunction<Number> sameNumber = GenericSingletonFactory.identityFunction();
		Number[] numbers = { 1, 2.0, 3L };
		for (Number n : numbers) {
			assertThat(sameNumber.apply(n), is(n));
		}
	}

	interface UnaryFunction<T> {
		T apply(T arg);
	}

	private static class GenericSingletonFactory {
		// もらったものをまったく変えないで返す関数（恒等関数）の関数オブジェクトのシングルトン
		private static UnaryFunction<Object> IDENTITY_FUNCTION = new UnaryFunction<Object>() {
			public Object apply(Object arg) {
				return arg;
			}
		};

		// IDENTITY_FUNCTIONは状態を持たないで、かつ非境界パラメータなので、
		// すべての型に対して1つのインスタンスを共有しても安全である。
		@SuppressWarnings("unchecked")
		public static <T> UnaryFunction<T> identityFunction() {
			return (UnaryFunction<T>) IDENTITY_FUNCTION;
		}
	}

	@Test
	public void 使用_再帰型境界ジェネリックメソッド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("型パラメータが、その型パラメータ自身が関係する何らかの式で制限される場合がある。").e();
		o.l2("その制約に再帰型境界を使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("リストの最大値や最小値を比較計算する場合、");
		o.__("そのリストの要素は相互比較可能でなければならない。").e();
		o.l2("より具体的にいうと、比較関数に渡される");
		o.__("リストの要素「T」は「Comparable<T>」を実装していていなければならない。").e();

		o.l1("【たとえば？】").e();
		o.l2("リストの最大値を計算する関数max()。").e();
		List<Integer> numbers = Arrays.asList(1, 2, 3);
		assertThat(max(numbers), is(3));
	}

	// 相互比較可能にするために再帰型境界を使用。
	// 型境界<T extends Comparable<T>>は、「自分自身と比較可能なすべての型Tに関して」と読める。
	private static <T extends Comparable<T>> T max(List<T> list) {
		Iterator<T> i = list.iterator();
		T result = i.next();
		while (i.hasNext()) {
			T t = i.next();
			if (t.compareTo(result) > 0)
				result = t;
		}
		return result;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("既存メソッドをジェネリック化して、安全性と使いやすさをクライアントに提供する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
