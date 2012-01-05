package asakichy.第09章例外;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.AbstractSequentialList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目61抽象概念に適した例外をスローする {

	@Test
	public void 例外翻訳とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("上位レイヤが、下位レベルの例外をキャッチして、").e();
		o.l2("上位レベルの抽象概念の観点から説明可能な例外をスローする。").e();

		o.l1("【どうして？】").e();
		o.l2("上位レイヤAPIが行っている処理と関係のない例外なので、使用側が混乱する。").e();
		o.l2("上位レイヤAPIを実装の詳細で汚染することになる。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.AbstractSequentialList#get()メソッド。").e();

		List<Integer> sequentialList = new AbstractSequentialList<Integer>() {

			@Override
			public Integer get(int index) {
				ListIterator<Integer> i = listIterator(index);
				try {
					return i.next();
				} catch (NoSuchElementException e) {
					// 例外翻訳
					throw new IndexOutOfBoundsException("Index:" + index);
				}

			}

			// dummy
			@Override
			public ListIterator<Integer> listIterator(int index) {
				return null;
			}

			// dummy
			@Override
			public int size() {
				return 0;
			}
		};
		assertThat(sequentialList, anything());

		o.l1("【どうすれば？】").e();
		o.l2("濫用はしない。").e();
		o.l3("下位レイヤAPIを例外が発生しないように呼び出せればベスト。").e();
		o.l3("それが叶わない場合、上位エリアAPIが例外を処理し、同時にログを出力しておく。").e();
		o.l3("そうすれば、API使用者やエンドユーザと、その問題を隔離できる。").e();
	}

	@Test
	public void 例外連鎖とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("例外翻訳の際、原因となった下位レベルの例外を上位レベルの例外へ格納しておく。").e();
		o.l2("上位レベルの例外は、その原因例外を取り出すメソッドを提供する。").e();

		o.l1("【どうして？】").e();
		o.l2("デバッグの時、下位レベルの例外情報が役に立つ場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("標準例外は、連鎖可能なコンストラクタで原因例外を渡す。").e();
		o.l2("コンストラクタがない場合は、Throwable#initCause()メソッドで原因例外を渡す。").e();
		o.l2("原因例外はThrowable#getCause()で取得する。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("例外翻訳は、チェック例外を包み隠すための必須イディオム。").e();
		o.l1("同時に例外連鎖させておくことは、開発効率・保守の観点から必須イディオム。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
