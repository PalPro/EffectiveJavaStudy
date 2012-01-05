package asakichy.第02章オブジェクトの生成と消滅;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目04privateのコンストラクタでインスタンス化不可能を強制する {

	@Test
	public void ユーティリティクラスとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ユーティリティstaticメソッドをまとめたクラス。").e();
		o.l2("インスタンス化不可能にしておく。").e();

		o.l1("【どうして？】").e();
		o.l2("インスタンス化に意味のないクラスは、それを明確に表現するべき。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Collections。").e();

		// Collections collections = new Collections(); // コンパイルエラー
	}

	@Test
	public void 誤った実現方法_抽象クラス化() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスを抽象化することで、インスタンス化不可を表現してはいけない。").e();

		o.l1("【どうして？】").e();
		o.l2("サブクラスを作ると、インスタンス化できてしまう。").e();
		o.l2("継承のために設計されている、と誤解される。").e();
	}

	@Test
	public void 正しい実現方法_パラメータなしprivateコンストラクタ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("唯一の明示的なパラメータなしprivateコンストラクタを実装して、");
		o.__("インスタンス化を不可能にする。").e();

		o.l1("【どうして？】").e();
		o.l2("デフォルトコンストラクタをなくせる。").e();
		o.l2("サブクラスが作れなくなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("UtilityClassクラス。").e();
		// UtilityClass util = new UtilityClass(); // コンパイルエラー
		assertThat(UtilityClass.serve(), anything());
	}

	private static class UtilityClass {
		// インスタンス化できないようにデフォルトコンストラクタを抑制
		private UtilityClass() {
			throw new AssertionError();// 内部呼び出しに備え、念のため。
		}

		public static String serve() {
			return "UtilityClass";
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ユーティリティクラスはインスタンス化不可能に設計する。").e();
		o.l1("その方法はコンストラクタのprivate化で。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
