package asakichy.第10章並行性;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目71遅延初期化を注意して使用する {

	@Test
	public void 遅延初期化とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("フィールドの値が必要となるまで、そのフィールドの初期化を遅らせること。").e();

		o.l1("【どうして？】").e();
		o.l2("最適化のため。").e();

		o.l1("【どうすれば？】").e();
		o.l2("特にマルチスレッド下における、遅延初期化の制御は面倒。").e();

		o.l1("【たとえば？】").e();
		o.l2("InitializationNormalクラス。").e();
		InitializationLazy init = new InitializationLazy();
		assertThat(init.field1, isA(FieldType.class));
		assertThat(init.getField2(), isA(FieldType.class));
	}

	private static class InitializationLazy {

		// 宣言とともに初期化
		private final FieldType field1 = computeFieldValue();

		// 遅延初期化・マルチスレッド対応版
		private FieldType field2;

		synchronized FieldType getField2() {
			if (field2 == null) {
				field2 = computeFieldValue();
			}
			return field2;
		}

		// 負荷の高いフィールドの初期化、という設定。。。
		private static FieldType computeFieldValue() {
			return new FieldType();
		}
	}

	private static class FieldType {
	}

	@Test
	public void イディオム_ホルダークラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("staticフィールドに対して、パフォーマンスのために遅延初期化を行う場合に使用。").e();
		o.l2("クラスが使用されるまでクラスが初期化されないことが保証されることを利用している。").e();

		o.l1("【どうすれば？】").e();
		o.l2("InitializationHolderクラス。").e();
		assertThat(InitializationHolder.getField3(), isA(FieldType.class));
	}

	private static class InitializationHolder {
		private static class FieldHolder {
			static final FieldType field = computeFieldValue();
		}

		static FieldType getField3() {
			return FieldHolder.field;
		}

		private static FieldType computeFieldValue() {
			return new FieldType();
		}
	}

	@Test
	public void イディオム_二重チェック() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インスタンスフィールドに対して、パフォーマンスのために遅延初期化を行う場合に使用。").e();
		o.l2("フィールドが初期化された後にアクセスされた場合の、ロックのコストを回避する。").e();

		o.l1("【たとえば？】").e();
		o.l2("InitializationDoubleCheckクラス。").e();
		InitializationDoubleCheck init = new InitializationDoubleCheck();
		assertThat(init.getField4(), isA(FieldType.class));
		assertThat(init.getField4(), isA(FieldType.class));
	}

	private static class InitializationDoubleCheck {
		private volatile FieldType field4;

		FieldType getField4() {
			FieldType result = field4;// ローカル変数に移し変えることで、更なるパフォーマンス稼ぎ
			if (result == null) { // 最初のチェックはロックなし
				synchronized (this) {
					result = field4;
					if (result == null) // ロック後に再チェック
						field4 = result = computeFieldValue();
				}
			}
			// （初期化済以降は上記if文に入らず、ロックが回避される。）
			return result;
		}

		private static FieldType computeFieldValue() {
			return new FieldType();
		}
	}

	@Test
	public void イディオム_単一チェック() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複数回の初期化を許容できるインスタンスフィールドに対して、");
		o.__("パフォーマンスのために遅延初期化を行う場合に使用。").e();
		o.l2("2重チェックイディオムの変形で、2回目のチェックを省略する。").e();

		o.l1("【たとえば？】").e();
		o.l2("InitializationSingleCheckクラス。").e();
		InitializationSingleCheck init = new InitializationSingleCheck();
		assertThat(init.getField5(), isA(FieldType.class));
		assertThat(init.getField5(), isA(FieldType.class));
	}

	private static class InitializationSingleCheck {
		private volatile FieldType field5;

		private FieldType getField5() {
			FieldType result = field5;
			if (result == null) {
				field5 = result = computeFieldValue();
			}
			return result;
		}

		private static FieldType computeFieldValue() {
			return new FieldType();
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("遅延初期化より、普通の初期化がシンプルでよい。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
