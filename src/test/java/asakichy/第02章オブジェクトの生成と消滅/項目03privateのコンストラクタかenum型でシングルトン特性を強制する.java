package asakichy.第02章オブジェクトの生成と消滅;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目03privateのコンストラクタかenum型でシングルトン特性を強制する {

	@Test
	public void シングルトンとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("１度しかインスタンスが作成されないクラス。").e();
	}

	@Test
	public void 実装方法_privateコンストラクタ・フィールド使用版() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("privateコンストラクタ＋public static finalフィールド。").e();

		o.l1("【どうして？】").e();
		o.l2("シングルトンであることが明白になる。").e();
		o.l2("インライン化によるパフォーマンス向上も期待できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Elvis_fieldクラスのINSTANCEフィールド。").e();
		Elvis_field elvis = Elvis_field.INSTANCE;
		assertThat(elvis.leaveTheBuilding(), is("Whoa baby, I'm outta here!"));
	}

	private static class Elvis_field {
		public static final Elvis_field INSTANCE = new Elvis_field();

		private Elvis_field() {
		}

		private Object readResolve() {
			return INSTANCE; // シリアライズに対応
		}

		public String leaveTheBuilding() {
			return "Whoa baby, I'm outta here!";
		}
	}

	@Test
	public void 実装方法_privateコンストラクタ・メソッド使用版() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("privateコンストラクタ＋public staticメソッド。").e();

		o.l1("【どうして？】").e();
		o.l2("柔軟性がある。").e();
		o.l2("クライアントに影響を与えず、マルチインスタンスを扱うよう改造できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Elvis_methodクラスのgetInstance()メソッド。").e();
		Elvis_method elvis = Elvis_method.getInstance();
		assertThat(elvis.leaveTheBuilding(), is("Whoa baby, I'm outta here!"));
	}

	private static class Elvis_method {
		private static final Elvis_method INSTANCE = new Elvis_method();

		private Elvis_method() {
		}

		public static Elvis_method getInstance() {
			return INSTANCE;
		}

		public String leaveTheBuilding() {
			return "Whoa baby, I'm outta here!";
		}
	}

	@Test
	public void 実装方法_enum版() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("列挙型の単一要素。").e();

		o.l1("【どうして？】").e();
		o.l2("簡単で、頑強な、ベスト実装方法。").e();
		o.l2("シリアライズ対策は特段実装の必要なし。").e();
		o.l2("privateコンストラクタはリフレクションで呼べてしまうが、その穴もなし。").e();

		o.l1("【たとえば？】").e();
		o.l2("Elvis列挙型のINSTANCE要素。").e();
		Elvis elvis = Elvis.INSTANCE;
		assertThat(elvis.leaveTheBuilding(), is("Whoa baby, I'm outta here!"));
	}

	private enum Elvis {
		INSTANCE;

		public String leaveTheBuilding() {
			return "Whoa baby, I'm outta here!";
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("テストが難しくなるので、シングルトンは極力避ける。").e();
		o.l1("シングルトン最良の実装方法は、単一要素のenum型の使用である。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();


}
