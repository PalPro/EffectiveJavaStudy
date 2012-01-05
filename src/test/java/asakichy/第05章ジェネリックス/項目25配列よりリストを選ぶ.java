package asakichy.第05章ジェネリックス;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目25配列よりリストを選ぶ {

	@Test(expected = java.lang.ArrayStoreException.class)
	public void 変性とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("元の型の親子関係に対して、それを「配列」化、「ジェネリック」化したとき、");
		o.__("親子関係がどのように変化するのか、のこと。").e();

		o.l2("変性は3種類。").e();
		o.l2("PがCのスーパータイプであるとき、ジェネリック型A＜P＞とA＜C＞の関係を考える。").e();
		o.l3("共変（covariant）").s("A＜P＞はA＜C＞のスーパータイプ。").e();
		o.l3("反変（contravariant）").s("A＜P＞はA＜C＞のサブタイプ。").e();
		o.l3("非変（invariant）").s("A＜P＞とA＜C＞との間に型の親子関係は存在しない。").e();

		{
			/** 【補】invariantの訳語 */
			// 「Effective Java」本書では「不変」という訳があてられているが、
			// 「immutable」と紛らわしいので、ここでは「非変」としている。
		}

		o.l2("配列は共変。").e();
		o.l3("P［］はC［］のスーパータイプ").e();
		o.l2("ジェネリックは非変。").e();
		o.l3("A＜P＞とA＜C＞との間に型の親子関係は存在しない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Object［］とjava.lang.String［］。").e();
		o.l2("java.lang.ArrayList＜Object＞とjava.lang.ArrayList＜String＞。").e();

		// 共変なので問題なし
		Object[] stringArray = new String[1];
		// しかし、要素に互換のない型を入れれば「実行時」エラー
		stringArray[0] = Integer.MIN_VALUE;// java.lang.ArrayStoreException

		// 非変なので「コンパイル」エラー
		// ArrayList<Object> stringList = new ArrayList<String>();
	}

	@Test
	public void 実行時型情報とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("「配列」や「ジェネリック」が、コンパイル時の要素型情報を、実行時に保持しているか。").e();
		o.l2("2パターンある。").e();
		o.l2("具現化（reified）").s("実行時に、コンパイル時の要素型を知っていて、型を強制する。").e();
		o.l2("イレイジャ（erasure）").s("コンパイル時のみ要素型を強制し、実行時は型を破棄している。").e();

		o.l2("配列は具現化。").e();
		o.l3("実行時に型違反があると、java.lang.ArrayStoreExceptionが発生。").e();
		o.l2("ジェネリックはイレイジャ。").e();
		o.l3("ジェネリックを使用していない既存コードと相互運用できている。").e();
	}

	@Test
	public void 配列とジェネリックの組み合わせ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ジェネリックと配列の組み合わせは許可されていない。").e();

		o.l1("【どうして？】").e();
		o.l2("型安全性が失われるため。").e();

		o.l1("【たとえば？】").e();
		o.l2("ジェネリック配列が生成できるとしたときをシュミレート。").e();

		/*
		 * List<String>の配列を要素数１つで生成。
		 * （本来はコンパイルエラー。）
		 */
		// List<String>[] stringLists = new List<String>[1];

		/*
		 * List<Integer>を生成。
		 */
		// List<Integer> intList = Arrays.asList(42);

		/*
		 * 配列は共変なので、List<String>[]をObject[]に入れることは許可される。
		 */
		// Object[] objects = stringLists;

		/*
		 * List<Integer>はイレイジャされて、実行時は単なるListになっている。
		 * ListインスタンスをObject[]の要素に代入するのは問題ない。
		 * 
		 * つまり、この時点で、List<String>[]と宣言した配列に、
		 * List<String>でない、List<Integer>を代入できてしまった！。
		 */
		// objects[0] = intList;

		/*
		 * List<String>からの要素の取り出しなので、自動キャスト。
		 * しかし、入っているのはList<Integer>のIntegerなので、ClassCastExceptionが発生！。
		 */
		// String s = stringLists[0].get(0);

		{
			/** 【補】 具象化不可能型 */
			// E､List<E>、List<String>は、技術的には具象化不可能型という。
			// その実行時の表現が、コンパイル時の表現よりも情報が少ない型のこと。
		}

	}

	@Test
	public void リストとジェネリックの組み合わせ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("配列より、リスト＋ジェネリックを選択する。").e();

		o.l1("【どうして？】").e();
		o.l2("配列によるパフォーマンスや簡潔さを多少犠牲にしても、").e();
		o.l2("ジェネリックを組み合わせ可能なリストを選択し、型安全と相互運用性を取る。").e();

		o.l1("【たとえば？】").e();
		o.l2("reduce系関数（リストの値を一つにまとめる関数）を作成。").e();
		o.l2("配列を利用した、reduceArray関数。").e();
		o.l3("動作はするが、型危険。").e();
		List<Integer> intList = Arrays.asList(1, 2, 3);
		Integer resultA = reduceArray(intList, SUM, 0);
		assertThat(resultA, is(6));

		o.l2("ジェネリックを利用した、reduceGeneric関数。").e();
		o.l3("多少冗長でも、型安全。").e();
		Integer resultG = reduceGeneric(intList, SUM, 0);
		assertThat(resultG, is(6));
	}

	interface Function<T> {
		T apply(T arg1, T arg2);
	}

	// 集計関数オブジェクト
	private static final Function<Integer> SUM = new Function<Integer>() {
		public Integer apply(Integer i1, Integer i2) {
			return i1 + i2;
		}
	};

	private static <E> E reduceArray(List<E> list, Function<E> f, E initVal) {
		// 防御コピーの処理 {

		// コンパイルエラー
		// E[] snapshot = list.toArray();

		// キャストしても、無検査警告は消えない
		@SuppressWarnings("unchecked")
		E[] snapshot = (E[]) list.toArray();

		// }

		E result = initVal;
		for (E o : snapshot) {
			result = f.apply(result, o);
		}
		return result;
	}

	private static <E> E reduceGeneric(List<E> list, Function<E> f, E initVal) {
		List<E> snapshot;
		synchronized (list) {
			snapshot = new ArrayList<E>(list);// 防御コピー
		}
		E result = initVal;
		for (E e : snapshot) {
			result = f.apply(result, e);
		}
		return result;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("配列は共変で具現。").e();
		o.l1("ジェネリックは非変でイレイジャ。").e();
		o.l1("配列を使っていて、型安全にまつわるエラーや警告が出た場合には、");
		o.__("コンパイル時型安全を保証してくれる、ジェネリック＋リストに変換する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
