package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目31序数の代わりにインスタンスフィールドを使用する {

	@Test
	public void 序数とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("enum定数はordinalメソッドを持っている。").e();
		o.l2("enum型内の、各enum定数の位置、つまり「序数」を返す。").e();

		o.l1("【たとえば？】").e();
		o.l2("EnsembleSimple列挙型。").e();

		assertThat(EnsembleSimple.SOLO.numberOfMusicians(), is(1));
		assertThat(EnsembleSimple.DECTET.numberOfMusicians(), is(10));
	}

	// アンサンブル
	enum EnsembleSimple {
		// 0オーダで順にint数値が割り振られる。
		SOLO, DUET, TRIO, QUARTET, QUINTET, SEXTET, SEPTET, OCTET, NONET, DECTET;
		public int numberOfMusicians() {
			return ordinal() + 1;
		}
	}

	@Test
	public void 序数よりインスタンスフィールド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("序数は、定数の並び替えや、追加によって変更される。").e();

		o.l1("【どうすれば？】").e();
		o.l2("代わりに、インスタンスフィールドに、管理したい値を保存する。").e();

		o.l1("【たとえば？】").e();
		o.l2("Ensemble列挙型。").e();

		// DOUBLE_QUARTET/TRIPLE_QUARTETを追加
		assertThat(Ensemble.DOUBLE_QUARTET.numberOfMusicians(), is(8));
		assertThat(Ensemble.TRIPLE_QUARTET.numberOfMusicians(), is(12));
		// 間に入れても、数値がズレない
		assertThat(Ensemble.DECTET.numberOfMusicians(), is(10));
	}

	public enum Ensemble {
		SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5), SEXTET(6), SEPTET(7),
		// ダブル・カルテット（8人）や、トリプル・カルテット（12人）を追加
		OCTET(8), DOUBLE_QUARTET(8), NONET(9), DECTET(10), TRIPLE_QUARTET(12);

		private final int numberOfMusicians;

		Ensemble(int size) {
			this.numberOfMusicians = size;
		}

		public int numberOfMusicians() {
			return numberOfMusicians;
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ordinalメソッドは、一部API用のメソッドなので、使用しないこと。").e();
		o.l1("enumに結び付くデータがあれば、インスタンスフィールドで管理すること。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
