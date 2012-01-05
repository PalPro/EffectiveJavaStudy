package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目19型を定義するためだけにインタフェースを使用する {

	@Test
	public void 定数クラスとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インターフェイスだが、定数（static finalフィールド）のみ定義してある。").e();
		o.l2("定数を使用するクラスは、クラス名で定数名を修飾する必要を回避するために、インターフェイスを実装。").e();

		o.l1("【どうすれば？】").e();
		o.l2("定数用途のインターフェイスは使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("インターフェイスなのに、「自分は何ができるか」の通知を目的としていない。").e();
		o.l2("インターフェイスなのに、「実装の詳細」を取り扱ってる。").e();
		o.l2("インターフェイス実装先クラスの名前空間を汚している。").e();

		{
			/** 【補】インターフェイスは型 */
			// インタフェースは、
			// それを実装しているクラスのインスタンスを参照するのに使用できる型である。
			// 型とは、クライアントは何ができるかについて述べているモノ。
			// 型以外の目的でインターフェイスを使用するのは不適切。
		}

		o.l1("【たとえば？】").e();
		o.l2("PhysicalConstantsIFインターフェイス。").e();
		o.l2("java.io.ObjectStreamConstantsも悪い例。").e();
		class Physics implements PhysicalConstantsIF {
			double avogadros() {
				return AVOGADROS_NUMBER; // クラス名を指定しなくてよい
			}
		}
		assertThat(new Physics().avogadros(), is(6.02214199e23));
	}

	interface PhysicalConstantsIF {
		static final double AVOGADROS_NUMBER = 6.02214199e23;
		static final double BOLTZMANN_CONSTANT = 1.3806503e-23;
		static final double ELECTRON_MASS = 9.10938188e-31;
	}

	@Test
	public void 代替案_関連クラス所属() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("定数が意味的に所属すべきクラスやインターフェイスへ移動。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Integer.MAX_VALUE。").e();
	}

	@Test
	public void 代替案_enum化() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("値にそれほど意味がなければ列挙型を使用。").e();

		o.l1("【たとえば？】").e();
		o.l2("Fruit列挙型。").e();
	}

	enum Fruit {
		ORANGE, APPLE, STRAWBERRY
	}

	// PhysicalConstantsを「無理矢理」enum化すると、こうなる。
	enum PhysicalConstantsEnum {
		AVOGADROS_NUMBER(6.02214199e23), BOLTZMANN_CONSTANT(1.3806503e-23), ELECTRON_MASS(9.10938188e-31);

		private PhysicalConstantsEnum(double val) {
			this.val = val;
		}

		private double val;

		public double getVal() {
			return val;
		}

	}

	@Test
	public void 代替案_ユーティリティクラス化() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("定数が意味的に所属すべきクラスやインターフェイスへ。").e();

		o.l1("【たとえば？】").e();
		o.l2("PhysicalConstantsクラス。").e();
		o.l2("クラス名修飾はstatic importで回避できる。").e();
		assertThat(PhysicalConstants.AVOGADROS_NUMBER, is(6.02214199e23));
	}

	public class PhysicalConstants {
		private PhysicalConstants() {
		}

		public static final double AVOGADROS_NUMBER = 6.02214199e23;
		public static final double BOLTZMANN_CONSTANT = 1.3806503e-23;
		public static final double ELECTRON_MASS = 9.10938188e-31;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("インターフェイスは「型」を定義するもので、「定数」ではない。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
