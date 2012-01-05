package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Comparator;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目49ボクシングされた基本データより基本データ型を選ぶ {

	@Test
	public void ボクシングされた基本データとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("すべての基本データ型は「ボクシングされた基本データ」という参照型を持っている。").e();
		o.l3("基本データ型は値だけを持つが、ボクシングされた基本データは値＋別アイデンティティを持つ。").e();
		o.l3("基本データ型は機能する値だけを持つが、ボクシングされた基本データはnullを持つ。").e();
		o.l3("基本データ型は、ボクシングされた基本データより、時間的・空間的に効率が良い。").e();

		o.l1("【たとえば？】").e();
		o.l2("基本データ型がint、double、boolean。").e();
		o.l3("プリミティブ型ともいう。").e();
		o.l2("対応するボクシングされた基本データがInteger、Double、Boolean。").e();
		o.l3("いずれもjava.langパッケージ。").e();

	}

	@Test
	public void 自動ボクシングとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("基本データ型から、ボクシングされた基本データへの変換を「ボクシング」と呼ぶ。").e();
		o.l2("ボクシングされた基本データから、基本データ型への変換を「アンボクシング」と呼ぶ。").e();
		o.l2("これらの変換を自動的におこなうことを「自動ボクシング」「自動アンボクシング」と呼ぶ。").e();

		o.l1("【どうすれば？】").e();
		o.l2("意図しない自動変換に注意する。").e();
	}

	@Test
	public void 自動ボクシング注意点_比較() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ボクシングされた基本データ型の比較に「==」を使用してしまう。").e();

		o.l1("【たとえば？】").e();
		o.l2("Integer値を昇順にするためのコンパレータ。").e();

		Comparator<Integer> naturalOrder = new Comparator<Integer>() {
			public int compare(Integer first, Integer second) {
				// first < second は自動アンボクシングして比較
				// しかし、first == secondは、ボクシングのまま、オブジェクトIDの比較をすることになる。
				return first < second ? -1 : (first == second ? 0 : 1);
			}
		};
		int result = naturalOrder.compare(new Integer(42), new Integer(42));
		// ０が返ってこない！
		assertThat(result, is(not(0)));

		// 修正版
		naturalOrder = new Comparator<Integer>() {
			public int compare(Integer first, Integer second) {
				// 最初に「手動」アンボクシング
				int f = first;
				int s = second;
				return f < s ? -1 : (f == s ? 0 : 1);
			}
		};
		result = naturalOrder.compare(new Integer(42), new Integer(42));
		assertThat(result, is(0));
	}

	@Test(expected = NullPointerException.class)
	public void 自動ボクシング注意点_例外発生() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ボクシングされた基本データ型は、NullPointerExceptionを発生させる可能性がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("未初期化のボクシングされた基本データ型を、アンボクシング。").e();

		if (i == 42) { // NullPointerException
			assertThat(i, anything("unreachable!"));
		}
	}

	// 初期値は「null」
	private static Integer i;

	@Test
	public void 自動ボクシング注意点_パフォーマンス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ボクシングとアンボクシングによってプログラムが遅くなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("ループ内でボクシングを使用しているsumSlowly()関数。").e();
		assertThat(sumSlowly(10L), is(55L));
	}

	private long sumSlowly(long limit) {
		Long total = 0L;
		for (long i = 0; i <= limit; i++) {
			total += i; // ボクシングにより、Longインスタンスを毎回生成
		}
		return total;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ボクシングされたデータは、記述の冗長性は減らしてくれるが、危険性は減らしてくれない。").e();
		o.l1("プリミティブが使えないときだけ、使うようにする。").e();
		o.l2("コレクション内の要素、キー、値。").e();
		o.l2("パラメータ化された型の型パラメータ。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
