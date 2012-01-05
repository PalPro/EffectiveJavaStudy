package asakichy.第09章例外;

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

public class 項目57例外的状態にだけ例外を使用する {

	@Test
	public void 例外とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("通常の制御フローではない、例外的なフローを簡潔に表現するための言語機能。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.NumberFormatException例外の処理。").e();

		int result = -1;
		String naturalNumber = "a"; // wrong!
		try {
			// 通常の制御フロー
			result = Integer.parseInt(naturalNumber);
			assertThat(result, greaterThan(0));
		} catch (NumberFormatException e) {
			// 例外的な制御フロー
			assertThat(result, is(-1));
		}

		{
			/** 【補】例外のマルチキャッチ */
			// Java7で複数例外を同時にキャッチする構文がサポートされた。
			// catch ( IOException | SQLException e) {}
		}
	}

	@Test
	public void 禁止_通常の制御フローに例外を使用() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("例外は、その名が示す通り、例外的条件に対してのみ使用する。").e();
		o.l2("通常の制御フローに対しては、決して使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("パフォーマンスが悪くなる。").e();
		o.l2("可読性が低くなる。").e();
		o.l2("関係ない例外を消費して、他のバグを見逃す危険がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("ループの終了条件に例外を使用。").e();
		StringBuilder result = new StringBuilder();
		String[] strings = { "1", "2", "3" };
		// 例外の酷い濫用
		try {
			int i = 0;
			while (true) {
				result.append(strings[i++]);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ループ終了
		}
		assertThat(result.toString(), is("123"));

		// 読みやすく、速い、標準イデオムで
		result = new StringBuilder();
		for (String s : strings) {
			result.append(s);
		}
		assertThat(result.toString(), is("123"));

		{
			/** 【補】「ループの終了検査」 vs 「例外」 のパフォーマンス */
			// 「ループの終了検査が冗長で、例外のほうが速い」というのは以下の理由で誤りです。

			// ・try-catchの中では行われない最適化がある。
			// ・ループの冗長検査は最適化で取り除かれる。
			// ・例外処理は、例外ゆえ、JVMのパフォーマンス改善でも後回し部分。
		}
	}

	@Test
	public void 禁止_通常の制御フローでクライアントに例外の使用を強要() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("上手く設計されたAPIは、通常の制御フローに例外を使用することを、クライアントに強要しない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("状態検査メソッドを提供する。").e();
		o.l3("読みやすいので、通常はこちらを使用。").e();
		o.l3("状態検査メソッドを呼び忘れれば、例外が発生するので、バグも発見しやすい。").e();
		o.l2("状態変化を区別できる値（nullなど）を返す。").e();
		o.l3("並行アクセスの可能性がある場合は、状態検査では隙が生まれるのでこちらを使用。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Iterator。").e();
		o.l3("例外発生を待つのではなく、状態検査メソッドhasNext()を提供。").e();
		List<String> strings = Arrays.asList("1", "2", "3");
		StringBuilder result = new StringBuilder();
		for (Iterator<String> iterator = strings.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			result.append(s);
		}
		assertThat(result.toString(), is("123"));
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("例外を通常のフローで使用しない。").e();
		o.l1("APIを作るときは、通常のフローでは例外を強要しない設計にする。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
