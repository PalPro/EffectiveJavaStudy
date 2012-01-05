package asakichy.第09章例外;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目58回復可能な状態にはチェックされる例外をプログラミングエラーには実行時例外を使用する {

	@Test
	public void 例外の種類とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("チェックされる例外（checked exception）。").e();
		o.l2("チェックされない例外。").e();
		o.l3("実行時例外（runtime exception）。").e();
		o.l3("エラー（error）。").e();

		o.l1("【どうすれば？】").e();
		o.l2("チェックされる例外。").s("java.lang.Exceptionを継承。").e();
		o.l2("チェックされない例外。").e();
		o.l3("実行時例外。").s("java.lang.RuntimeExceptionを継承。").e();
		o.l3("エラー。").s("java.lang.Errorを継承。").e();

		{
			/** 【補】 例外の種類の親子関係 */
			// java.lang.Throwable
			// ---- java.lang.Exception
			// -------- java.lang.RuntimeException
			// ---- java.lang.Error

			// この例外ツリーに属さない、完全に独立した例外を作ることは可能だが、
			// 使用する側の混乱を招くので、決してやらないこと。
		}
	}

	@Test
	public void チェックされる例外() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("catchするか、伝播することを強要する例外。").e();

		o.l1("【どうして？】").e();
		o.l2("例外状態から回復可能な、「準」通常制御フローを表現する。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.io.IOException。").e();

		o.l1("【どうすれば？】").e();
		o.l2("API作成者は、API呼び出し側が適切に回復できるような状況で使用する。").e();
		o.l3("その際、例外の中に復旧に足る情報を詰め込んでおくこと。").e();
		o.l2("API呼び出し側は、その意図を汲み、「無視しないで」回復に努める。").e();
	}

	@Test
	public void チェックされない例外_実行時例外() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("catchするか、伝播することを強要しない例外。").e();
		o.l2("と、いうより、catchするべきではない例外。").e();

		o.l1("【どうして？】").e();
		o.l2("たいてい事前条件を満たしていないことによる実装のエラー。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.ArrayIndexOutOfBoundsException。").e();

		o.l1("【どうすれば？】").e();
		o.l2("API作成者は、API呼び出し側のプログラミングエラー通知に使用する。").e();
		o.l2("API呼び出し側は、それをcatchせず、開発中に気づいて修正する。").e();
	}

	@Test
	public void チェックされない例外_エラー() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("catchするか、伝播することを強要しない例外。").e();
		o.l2("と、いうより、catchするべきではない例外。").e();
		o.l2("かつ、JVM専用の例外。").e();

		o.l1("【どうして？】").e();
		o.l2("JVMが実行を継続できないことを表現するためのもの。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.OutOfMemoryError。").e();

		o.l1("【どうすれば？】").e();
		o.l2("API作成者は、エラーを発行してはいけない。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("例外に原因を含めたそのときのコンテキスト情報を詰め込んでおくことは、運用障害時に絶大な価値を生む。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
