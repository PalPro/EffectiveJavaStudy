package asakichy.第09章例外;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目59チェックされる例外を不必要に使用するのを避ける {

	@Test
	public void チェックされる例外とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("プログラムの信頼性を向上させる、Javaオリジナルの例外機構。").e();

		o.l1("【どうして？】").e();
		o.l2("リターンコードと異なり、プログラマに例外状態の処理を強制する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("APIで濫用しない。").e();
		o.l3("使用側のプログラマの負荷が増え、APIが使いにくくなる。").e();
	}

	@Test
	public void 適しているケース() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("APIの適切な使用では例外状態を防ぐことができない、");
		o.__("かつ、例外発生時、API使用者が有用な処理をほどこせる場合。").e();

		o.l1("【どうして？】").e();
		o.l2("この両方を満たさない限り、プログラマへの負荷やプログラムを複雑にすることを正当化できない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("補足した側が何ができるかどうかを検討する。").e();
		o.l2("何もできないなら、チェックされる例外を使用しない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.CloneNotSupportedExceptionは悪例。").e();
		o.l3("チェックされる例外だが、補足しても何もできない。").e();

		{
			/** 【補】チェックされる例外の手間 */
			// チェックされる例外はcatchを強要する。
			// try/catchを新たに書き起こすことは、
			// プログラマに結構な（というより、大きな）面倒をかけることになる。
		}
	}

	public void 適していないケース() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("try/catchを書く負荷をプログラマに課したくない場合。").e();
		o.l2("try/catchの実行時の負荷を無視できない場合。").e();

		o.l1("【どうすれば？】").e();
		o.l2("チェックされない例外への変換を検討する。").e();

		o.l1("【たとえば？】").e();
		o.l2("Commandクラス。").e();
		o.l2("「チェックされる例外」を、「チェックされない例外」＋「状態検査関数」へ。").e();

		Command command = new Command();

		// 「チェックされる例外」
		try {
			command.executeThrowException();
		} catch (Exception e) {
			// 例外処理
			// .....
			assertThat(command.result(), is(false));
		}

		// 「チェックされない例外」＋「状態検査関数」
		if (command.executePermitted()) {
			command.executeThrowRuntimeException();
		} else {
			// 例外処理
			// .....
			assertThat(command.result(), is(false));
		}
	}

	private static class Command {
		private boolean result = true;

		// チェックされる例外
		public void executeThrowException() throws Exception {
			result = false;
			throw new Exception();
		}

		// チェックされない例外
		public void executeThrowRuntimeException() {
			result = false;
			throw new RuntimeException();
		}

		// 実行可否を検査できる関数
		public boolean executePermitted() {
			return false;
		}

		public boolean result() {
			return result;
		};

	}

	@Test
	public void まとめ() throws Exception {
		o.l1("議論は尽くされ、世の趨勢は、「チェックされる例外は使用しない」へ。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
