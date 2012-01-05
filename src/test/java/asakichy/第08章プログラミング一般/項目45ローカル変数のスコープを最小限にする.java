package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目45ローカル変数のスコープを最小限にする {

	@Test
	public void ローカル変数のスコープとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ローカル変数の有効範囲のこと。").e();
		o.l2("宣言した箇所から、宣言した箇所のブロックの最後まで。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スコープを最小限にし、コードの可読性と保守性を向上させる。").e();
	}

	@Test
	public void 指針_初めて使用される位置で宣言() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ローカル変数は、初めて使用される時に宣言すること。").e();

		o.l1("【どうして？】").e();
		o.l2("変数の定義と使用は近い方が見やすい。").e();
		o.l2("ローカル変数の出現が遅ければ遅いほど、そのローカル変数の有効範囲は狭まる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Javaでは、文が書ける場所であれば、どこでも変数を宣言できる。").e();
		o.l2("メソッドやブロックの先頭に書くのは悪習なので、やめる。").e();
	}

	@Test
	public void 指針_宣言と初期化を同時に行う() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ほとんどすべてのローカル変数宣言は、初期化子を含んでいるべき。").e();

		o.l1("【どうして？】").e();
		o.l2("バラバラにあると、それを読む人がつなげなければならない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("変数を初期化するに足る情報がなければ、情報が得られるまで宣言を先送りする。").e();
	}

	@Test
	public void 指針_ループ変数を利用する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ループ変数はスコープが狭い。").e();

		o.l1("【どうすれば？】").e();
		o.l2("while文よりfor文を利用する。").e();

		o.l1("【たとえば？】").e();

		List<String> strings = Arrays.asList("a", "b", "c");

		o.l2("while文。").e();
		StringBuilder concat = new StringBuilder();
		Iterator<String> i1 = strings.iterator();
		while (i1.hasNext()) {
			concat.append(i1.next());
		}
		assertThat(concat.toString(), is("abc"));

		o.l2("for文。").e();
		concat = new StringBuilder();
		for (Iterator<String> i2 = strings.iterator(); i2.hasNext();) {
			concat.append(i2.next());
		}
		assertThat(concat.toString(), is("abc"));

		o.l2("for-each文。").e();
		concat = new StringBuilder();
		for (String string : strings) {
			concat.append(string);
		}
		assertThat(concat.toString(), is("abc"));

		{
			/** 【補】for文ループのイディオム */
			// for文の終了条件で使用する値が計算値の場合、
			// それを初期化時にやることでコストを回避します。
			for (int i = 0, n = expensiveComputation(); i < n; i++) {
				// do something
			}
		}
	}

	private int expensiveComputation() {
		// ループの最大値。
		// 導出に時間のかかる、という体。
		return 10;
	}

	@Test
	public void 指針_メソッドを小さくする() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッドを小さくして「焦点」をはっきりさせる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("１つのメソッドの中に２つの処理があったら、");
		o.__("それぞれの処理でしか使用しないローカル変数があるかもしれない。").e();
		o.l2("その際はメソッドを分け、ローカル変数もそれぞれについて行かせる。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ローカル変数に限らず、プログラムは凝集させる（関連するモノのみをギュッと集める）ことが肝要。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
