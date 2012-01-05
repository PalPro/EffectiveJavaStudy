package asakichy.第07章メソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目42可変長引数を注意して使用する {

	@Test
	public void 可変長引数とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("正式には「可変引数メソッド」。").e();
		o.l2("指定された型の０個以上の引数を受け付ける。").e();

		{
			/** 【補】可変長引数の仕組み */
			// 呼び出し時点で渡された引数の数と同じ大きさの配列を生成し、
			// その配列に引数値を入れて、メソッドに渡している。
		}

		o.l1("【たとえば？】").e();
		o.l2("sum()関数。").e();
		assertThat(sum(), is(0));
		assertThat(sum(1, 2, 3, 4, 5), is(15));
	}

	private static int sum(int... args) {
		int sum = 0;
		for (int arg : args) {
			sum += arg;
		}
		return sum;
	}

	@Test
	public void イディオム_引数の数を強制する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("可変長引数のみだと、引数の数を強制できない").e();

		o.l1("【どうすれば？】").e();
		o.l2("通常パラメータと組み合わせる。").e();

		o.l1("【たとえば？】").e();
		o.l2("min()関数。").e();
		o.l2("第一引数に通常パラメータ、第二引数に可変長パラメータで、ひとつ以上の引数を強要。").e();
		assertThat(min(1), is(1));
		assertThat(min(1, 2), is(1));
	}

	private static int min(int firstArg, int... remainingArgs) {
		int min = firstArg;
		for (int arg : remainingArgs) {
			if (arg < min) {
				min = arg;
			}
		}
		return min;
	}

	@Test
	public void イディオム_パフォーマンス対策() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("可変長引数メソッドの呼び出しは、毎回配列を生成して初期化する。").e();
		o.l2("繰り返し呼び出すメソッドであれば、コストが高すぎる場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("可変長引数の柔軟性と、パフォーマンスを両立するインターフェイスにする。").e();
		o.l2("オーバーロードと可変長引数を組み合わせる。").e();

		o.l1("【たとえば？】").e();
		o.l2("引数が３個までの場合が、呼び出しの95％を占める場合。").e();
		o.l2("通常引数０～３個を持つメソッドと、通常引数３個＋可変引数をもつメソッドを提供する。").e();
		assertThat(foo(), is("0"));
		assertThat(foo(1), is("1"));
		assertThat(foo(1, 2), is("2"));
		assertThat(foo(1, 2, 3), is("3"));
		assertThat(foo(1, 2, 3, 4), is("vararg"));
	}

	private static String foo() {
		return "0";
	}

	private static String foo(int a) {
		return "1";
	}

	private static String foo(int a, int b) {
		return "2";
	}

	private static String foo(int a, int b, int c) {
		return "3";
	}

	private static String foo(int a, int b, int c, int... rest) {
		return "vararg";
	}

	@Test
	public void セオリー_適用しすぎない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("最後のパラメータとして配列を持つメソッドだからといって、");
		o.__("既存のメソッドをすべて修正すべきではない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("そのメソッドが、本当に可変長引数の値列に対して操作を行う場合にだけ使用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Arrays#asList()。").e();

		o.l2("可変引数がサポートされた。").e();
		List<String> homophones = Arrays.asList("to", "too", "two");
		assertThat(homophones, contains("to", "too", "two"));

		o.l2("ただし、これにより、配列の文字列化イディオムが使用できなくなった。").e();
		// オブジェクトの配列はまだいい
		String[] homophonArray = { "to", "too", "two" };
		assertThat(Arrays.asList(homophonArray), hasToString("[to, too, two]"));

		// プリミティブの配列は、かつてはコンパイルエラーとなっていた。
		// ＝＞asList(Object[])にasList(int[])はハマらないから。
		// しかし、可変長引数により、コンパイルは通り、意図しない動作をするようになった。
		// ＝＞asList(可変長)にハマり、List<int[]>が生成され、そのtoStringが呼ばれる。
		int[] digits = { 1, 2, 3, 4, 5 };
		assertThat(Arrays.asList(digits), hasToString(anything())); // ObjectのIDが出力

		{
			/** 【補】配列の文字列化 */
			// 配列の文字列化には、Arrays#toString()を使用する。
			assertThat(Arrays.toString(digits), is("[1, 2, 3, 4, 5]"));
			assertThat(Arrays.toString(homophonArray), is("[to, too, two]"));
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("可変長引数は、「ユーザ便宜を図れるか」視点で採用可否を決める。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();
}
