package asakichy.第07章メソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目41オーバーロードを注意して使用する {

	@Test
	public void オーバーロードとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オーバーロードは、メソッド名が同一で、引数の型・数・並び順が異なるメソッドを複数定義すること。").e();
		o.l2("オーバーライドは、スーパークラスで定義されたメソッドを、サブクラスで再定義すること").e();

		o.l1("【どうして？】").e();
		o.l2("オーバーロードによって、同じ機能をもつメソッドに、同じ名前を付けられる。").e();
	}

	@Test
	public void 基本方針_オーバロードの使用は避ける() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("使用側を困惑させるオーバーロードの使用は避けること。").e();

		o.l1("【どうして？】").e();
		o.l2("オーバロードはコンパイル時にメソッド選択される。").e();
		o.l2("直感的でない動作をする場合がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("オーバーロードを持つ、CollectionClassifierクラス。").e();
		Collection<?>[] collections = { new HashSet<String>(), new ArrayList<BigInteger>(),
				new HashMap<String, String>().values() };

		// 実行時のインスタンスの型にかかわらず、すべて「Unknown Collection」と分類される
		for (Collection<?> c : collections) {
			assertThat(CollectionClassifier.classify(c), is("Unknown Collection"));
		}

		o.l2("オーバライドを持つ、Wineクラス群。").e();
		Wine[] wines = { new Wine(), new SparklingWine(), new Champagne() };

		// 実行時のインスタンスの型に沿って、ワインの名前が取得できる
		assertThat(wines[0].name(), is("wine"));
		assertThat(wines[1].name(), is("sparkling wine"));
		assertThat(wines[2].name(), is("champagne"));
	}

	// 「コレクションの種類を表示する」ことを意図したクラス
	@SuppressWarnings("unused")
	private static class CollectionClassifier {
		public static String classify(Set<?> s) {
			return "Set";
		}

		public static String classify(List<?> lst) {
			return "List";
		}

		public static String classify(Collection<?> c) {
			return "Unknown Collection";
		}

	}

	private static class Wine {
		String name() {
			return "wine";
		}
	}

	private static class SparklingWine extends Wine {
		@Override
		String name() {
			return "sparkling wine";
		}
	}

	private static class Champagne extends SparklingWine {
		@Override
		String name() {
			return "champagne";
		}
	}

	@Test
	public void 指針_同じパラメータ数のオーバーロードメソッドを提供しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("同じパラメータ数の2つのオーバーロードされたメソッドを提供しない。").e();
		o.l2("可変長のメソッドは決してオーバーロードしない。").e();

		o.l1("【どうして？】").e();
		o.l2("プログラマが疑問に思う余地を残さないため。").e();

		o.l1("【どうすれば？】").e();
		o.l2("オーバーロードの代わりに異なる名前を付ける。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.io.ObjectOutputStream。").e();
		o.l3("write(boolean)/write(int)ではなく、writeBoolean(boolean)/writeInt(int)となっている。").e();
		o.l3("かつ、読み込み側のカウンターパートjava.io.ObjectInputStreamも、");
		o.__("対称性を持ったreadBoolean(boolean)/readInt(int)を持っていて、わかりやすい。").e();
	}

	@Test
	public void 指針_オブジェクトの生成にはファクトリを提供する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトの生成は、コンストラクタよりファクトリを提供する。").e();

		o.l1("【どうして？】").e();
		o.l2("コンストラクタは、必ずオーバーロードになってしまう。").e();
		o.l2("意図のある名前をもつファクトリのほうがわかりやすい。").e();
	}

	@Test
	public void 指針_オーバロードの適用が明白なら使用してよい() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("仮パラメータの少なくとも１つが「根本的に異なる」型を持っている場合のこと。").e();

		o.l1("【どうして？】").e();
		o.l2("完全にパラメータの実行型で決まり、コンパイル時の型に影響を受けることがない。").e();
		o.l2("どのオーバーロードが適用されるか明白である。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.ArrayListはパラメータ１つのコンストラクタが２つある。").e();
		o.l2("ただし、１つはint、もう１つはCollectionなので、混乱の原因とならない。").e();

		{
			/** 【補】 オーバーロードと自動ボクシング */
			// 基本データ型と参照型は、根本的に異なる型だったが、
			// 自動ボクシングにより、必ずしもそうではなくなってしまった。
			Set<Integer> set = new TreeSet<Integer>();
			List<Integer> list = new ArrayList<Integer>();

			for (int i = -3; i < 3; i++) {
				set.add(i);
				list.add(i);
			}
			assertThat(set, contains(-3, -2, -1, 0, 1, 2));
			assertThat(list, contains(-3, -2, -1, 0, 1, 2));

			// 数値 0,1,2 を削除
			for (int i = 0; i < 3; i++) {
				set.remove(i);
				list.remove(i);
			}

			// 結果が異なる！
			assertThat(set, contains(-3, -2, -1));
			assertThat(list, contains(-2, 0, 2));

			// Set#remove(Object o)
			// 指定した要素を削除。
			// 0,1,2が自動ボクシングされ、消えた。

			// List#remove(int index)
			// 指定した順番を削除。
			// 0番目を消し、[-2, -1, 0, 1, 2]となり、この状態で、
			// 1番目を消し、[-2, 0, 1, 2]となり、この状態で、
			// 2番目を消し、[-2, 0, 2]となった。

			// 修正版
			set.clear();
			list.clear();
			for (int i = -3; i < 3; i++) {
				set.add(i);
				list.add(i);
			}
			assertThat(set, contains(-3, -2, -1, 0, 1, 2));
			assertThat(list, contains(-3, -2, -1, 0, 1, 2));

			// 数値 0,1,2 を削除
			for (int i = 0; i < 3; i++) {
				set.remove(i);
				list.remove(Integer.valueOf(i));
			}
			assertThat(set, contains(-3, -2, -1));
			assertThat(list, contains(-3, -2, -1));
		}
	}

	@Test
	public void 指針_同じことをするなら使用してよい() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("同じことをし、かつ、使う側が混乱しないなら、同系の型でも使用してよい。").e();
		o.l2("既存のクラスに、新たなインタフェースを実装させるなら、使用せざるを得ない場合もある。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.String#contentEquals()。").e();
		o.l2("JDK1.4のとき、contentEquals(StringBuffer sb)というメソッドがあった。").e();
		o.l2("JDK1.5のとき、StringBufferやStringなどを包括するインターフェイスCharSequenceが提供された。").e();
		o.l2("それに伴い、contentEquals(CharSequence cs)というメソッドが追加された。").e();
		o.l2("どちらが呼ばれても処理は同じなので、問題ない。").e();

		{
			/** 【補】 java.lang.StringのvalueOf()について */
			// valueOf(char[])とvalueOf(Object)と提供されているが、
			// 同じ参照を渡された場合、まったく異なる動作をする。
			// 悪例であり、反面教師とすべき
			char[] data = { 'a', 'b', 'c' };
			assertThat(String.valueOf(data), is("abc"));
			assertThat(String.valueOf((Object) data), is(not(("abc"))));
			// valueOf(Object)では、『[C@1855af5』のようなオブジェクトIDが出力される
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("オーバーライドは「普通」、オーバーロードは「特殊」「例外」。").e();
		o.l1("オーバーロードの使用は「原則」避けること。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();
}
