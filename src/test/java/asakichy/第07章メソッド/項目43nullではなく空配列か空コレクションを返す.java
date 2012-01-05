package asakichy.第07章メソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目43nullではなく空配列か空コレクションを返す {

	@Test
	public void nullを返すメソッド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("APIにおいて、要求されたものが存在しない場合、nullを返す。").e();

		o.l1("【どうすれば？】").e();
		o.l2("nullを返すAPIは作成しない。").e();

		o.l1("【どうして？】").e();
		o.l2("nullを処理するためにクライアントに余分なコードが必要になる。").e();

		o.l1("【たとえば？】").e();
		o.l2("CheeseShopByNullクラス").e();

		CheeseShopByNull shop = new CheeseShopByNull();
		Cheese[] cheeses = shop.getCheeses();
		// クライアントすべてに、この面倒なチェックコードが入る
		if (cheeses != null && Arrays.asList(cheeses).contains(Cheese.STILTON)) {
			assertThat(shop, anything("スティルトン売ってる"));
		}
		// if文内はこう書きたい
		// Arrays.asList(shop.getCheeses()).contains(Cheese.STILTON)
	}

	private static class CheeseShopByNull {
		private final List<Cheese> cheesesInStock = new ArrayList<Cheese>();

		public Cheese[] getCheeses() {
			if (cheesesInStock.size() == 0) {
				return null;
			}
			return (Cheese[]) cheesesInStock.toArray();
		}
	}

	private static class Cheese {
		public static final Cheese STILTON = new Cheese();
		public static final Cheese ROQUEFORT = new Cheese();
		public static final Cheese GORGONZOLA = new Cheese();
	}

	@Test
	public void 空を返すメソッド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("APIにおいて、要求されたものが存在しない場合、空配列や空コレクションを返す。").e();

		o.l1("【どうすれば？】").e();
		o.l2("nullを返すより、空を返す。").e();

		o.l1("【どうして？】").e();
		o.l2("クライアントが使いやすい。").e();
		o.l2("パフォーマンスに影響はない。").e();

		o.l1("【たとえば？】").e();
		o.l2("CheeseShopByEmptyクラス。").e();

		// 使う側のif文がシンプルになる
		// 空の配列やコレクションはあらかじめ用意してあるものを返すので、パフォーマンスも問題ない
		CheeseShopByEmpty shop = new CheeseShopByEmpty();
		if (Arrays.asList(shop.getCheeses()).contains(Cheese.ROQUEFORT)) {
			assertThat(shop, anything("ロックフォール売ってる"));
		}
		if (shop.getCheeseList().contains(Cheese.GORGONZOLA)) {
			assertThat(shop, anything("ゴルゴンゾーラ売ってる"));
		}
	}

	private static class CheeseShopByEmpty {
		private final List<Cheese> cheesesInStock = new ArrayList<Cheese>();

		private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

		// 空配列を返すイディオム
		public Cheese[] getCheeses() {
			// toArray(T[] a)のJavaDocより
			// 配列が十分な大きさを持つ場合は、リストの要素が格納される配列。
			// そうでない場合は、要素を格納するために同じ実行時の型の新しい配列が割り当てられる。

			// よって、この場合、Listが空なら、EMPTY_CHEESE_ARRAYがそのまま返る
			return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
		}

		// 空コレクションを返すイディオム
		public List<Cheese> getCheeseList() {
			if (cheesesInStock.isEmpty()) {
				// JDKが用意してくれている空List。
				// SetやMap用もある。
				return Collections.emptyList();
			} else {
				return new ArrayList<Cheese>(cheesesInStock);
			}

		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("空配列や空コレクションの代わりに、nullを返すべき理由は「ない」。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();
}
