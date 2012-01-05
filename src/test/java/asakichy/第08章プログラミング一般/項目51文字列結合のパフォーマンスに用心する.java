package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目51文字列結合のパフォーマンスに用心する {

	@SuppressWarnings("unused")
	@Test
	public void 文字列結合のパフォーマンスについて() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("文字列結合演算子（「＋」）はパフォーマンスが悪いので、演算回数が多くなるケースでは使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("文字列は不変のため、新しいStringを作ったうえで、2つの文字列をコピーする処理が入る。").e();

		o.l1("【どうすれば？】").e();
		o.l2("文字列結合にはjava.lang.StringBuilderを使用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("80文字×100列の文字列結合処理。").e();

		List<String> strings = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			strings.add(String.format("%080d", i + 1));
		}

		o.l3("文字列結合演算子を使用。").e();
		long beginByOperation = System.nanoTime();
		String resultSlow = "";
		for (String string : strings) {
			resultSlow += string;
		}
		long endByOperation = System.nanoTime();
		assertThat(resultSlow.length(), is(80 * 100));

		o.l3("java.lang.StringBuilderを使用。").e();
		long beginByCompanion = System.nanoTime();
		StringBuilder work = new StringBuilder(80 * 100);
		for (String string : strings) {
			work.append(string);
		}
		String resultSpeedy = work.toString();
		long endByCompanion = System.nanoTime();
		assertThat(resultSpeedy.length(), is(80 * 100));

		// 計測結果
		// System.out.println(endByOperation - beginByOperation);
		// System.out.println(endByCompanion - beginByCompanion);

		{
			/** 【補】パフォーマンスの差 */
			// 書籍では「50倍」違うと書かれている。
			// 実際に計測してみると、80文字×100列という少量の演算でも、「60～70倍」程度の差が出た。
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("数個を超える文字列結合には、StringBuilderを使用する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
