package asakichy.第09章例外;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目60標準例外を使用する {

	@Test
	public void 標準例外とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("JDKで提供されている例外クラスのこと。").e();

		o.l1("【どうすれば？】").e();
		o.l2("JDKが使用するだけのものではなく、アプリケーション実装者も利用する。").e();
	}

	@Test
	public void 再利用() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("API設計時において、独自の例外を作るのではなく、標準例外を利用する。").e();

		o.l1("【どうして？】").e();
		o.l2("APIが使用しやすい。").e();
		o.l3("プログラマが熟知した、馴染みのある慣例が使用できる。").e();
		o.l2("APIを使用したコードが読みやすい。").e();
		o.l3("見慣れない例外で混乱することがない。").e();
		o.l2("クラスは少ない方がいい。").e();
		o.l3("ロードのパフォーマンスやメモリ負荷が軽くなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("最も頻繁に再利用される例外。").e();

		o.l2("java.lang.IllegalArgumentException。").e();
		o.l3("不適切な引数。").e();
		o.l3("「回数」なのにマイナスを渡される、など").e();

		o.l2("java.lang.IllegalStateException。").e();
		o.l3("不正な状態。").e();
		o.l3("初期化される前に呼び出される、など。").e();

		o.l2("java.lang.NullPointerException。").e();
		o.l3("不適切な引数。").e();
		o.l3("で、特にNullの場合").e();

		o.l2("java.lang.IndexOutOfBoundsException。").e();
		o.l3("不適切な引数。").e();
		o.l3("で、特に範囲外の場合。").e();

		o.l2("java.util.ConcurrentMdificationException。").e();
		o.l3("シングルスレッドから使用されるか、");
		o.__("同期を伴って使用されるべきなのに、");
		o.__("並行して変更されようとした場合。").e();

		o.l2("java.lang.UnsupportedOperationException。").e();
		o.l3("操作をサポートしていない。").e();
		o.l3("インターフェイスで実装してないメソッドの通知、など。").e();

		o.l1("【どうすれば？】").e();
		o.l2("ムリヤリ再利用しない。").e();
		o.l3("セマンティックスに基づいて、要求に合致している時のみ。").e();
		o.l3("標準とちょっと違うだけなら、サブクラスを作るのもよい。").e();
		o.l2("再利用は相互排他じゃない。").e();
		o.l3("どれを使うかは、科学ではないので厳密ではない。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("JDKでも他のフレームワークでも、そのコンテキストで標準の例外を使用するのは、").e();
		o.l1("プログラムを読みやすくする一つの方法。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
