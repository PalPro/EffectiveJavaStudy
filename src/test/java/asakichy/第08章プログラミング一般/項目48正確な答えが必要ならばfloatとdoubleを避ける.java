package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目48正確な答えが必要ならばfloatとdoubleを避ける {

	@Test
	public void float型とdouble型とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("主に科学計算と工学計算のために設計された型。").e();
		o.l2("2進浮動小数点算術を行う。").e();

		o.l1("【どうすれば？】").e();
		o.l2("正確ですばやい「近似」計算に使用する。").e();
	}

	@Test
	public void 金額計算に使用しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("正確な計算、特に金額計算には使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("正確に小数点以下を表現できない。").e();

		{
			/** 【補】 正確ではない、小数点以下の計算例 */
			assertThat(1.03 - .42, is(not(0.61)));
			assertThat(1.03 - .42, is(0.6100000000000001));

			assertThat(1.00 - 9 * .10, is(not(0.1)));
			assertThat(1.00 - 9 * .10, is(0.09999999999999998));
		}

		o.l1("【どうすれば？】").e();
		o.l2("「int」「long」「java.lang.BigDecimal」を使用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("1ドルで、10セント、20セント、30セント、、、と値のついたキャンディを順に何個買えるか。").e();
		o.l2("10+20+30+40=100で、4個買えるのが正解。").e();

		o.l2("doubleを使用するhowManyCandiesByDouble()関数は「３」と返してくる。").e();
		o.l3("３個買った時点の残金が「0.3999999999999999」ドルで、4個目が買えない。").e();
		assertThat(howManyCandiesByDouble(), is(not(4)));
		assertThat(howManyCandiesByDouble(), is(3));

		o.l2("BigDecimal使用して改造したhowManyCandiesByBigDecimal()関数は「４」で正解。").e();
		o.l3("正確に計算できるが、算術演算子が使えないので、ソースが若干読みにくい。").e();
		assertThat(howManyCandiesByBigDecimal(), is(4));

		o.l2("intを使用して改造したhowManyCandiesByInt()関数も「４」で正解。").e();
		o.l3("桁上げして整数のみを扱うようにしているので、ソースも見やすい。").e();
		assertThat(howManyCandiesByInt(), is(4));
	}

	private static int howManyCandiesByDouble() {
		double funds = 1.00;
		int itemsBought = 0;
		for (double price = .10; funds >= price; price += .10) {
			funds -= price;
			itemsBought++;
		}
		return itemsBought;
	}

	private static int howManyCandiesByBigDecimal() {
		final BigDecimal TEN_CENTS = new BigDecimal(".10");

		int itemsBought = 0;
		BigDecimal funds = new BigDecimal("1.00");
		for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)) {
			itemsBought++;
			funds = funds.subtract(price);
		}
		return itemsBought;
	}

	private static int howManyCandiesByInt() {
		int itemsBought = 0;
		int funds = 100;
		for (int price = 10; funds >= price; price += 10) {
			itemsBought++;
			funds -= price;
		}
		return itemsBought;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("正確な計算に、floatとdoubleは使わない。").e();
		o.l1("9桁までint、18桁までlong、それ以上はBigDecimalで。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
