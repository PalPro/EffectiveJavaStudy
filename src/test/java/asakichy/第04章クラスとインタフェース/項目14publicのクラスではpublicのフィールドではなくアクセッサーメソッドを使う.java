package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目14publicのクラスではpublicのフィールドではなくアクセッサーメソッドを使う {
	@Test
	public void publicフィールド禁止とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("publicフィールドは、情報隠蔽・カプセル化の恩恵を受けることができない。").e();
		o.l2("原則、設計の選択肢から外すべき。").e();

		{
			/** 【補】JDKにおけるpublicフィールド保持クラス */
			// JDKでこの原則を破っているクラスがあるが、
			// 見習うのではなく、反面教師とすべき。
			// java.awt.Point
			// java.awt.Dimension
		}

		o.l1("【どうして？】").e();
		o.l2("APIを変更することなく、クラスの表現形式を変更することができない。").e();
		o.l2("不変式を強制することができない。").e();
		o.l2("フィールドが変更された時に何らかの補助処理を行うこともできない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("privateのフィールドを、");
		o.__("publicのアクセッサーソッド（getter/setter）に置き換える。").e();
		o.l3("setterは「ミューテータメソッド」ともいわれる。").e();

		o.l1("【たとえば？】").e();
		o.l2("publicフィールドを使用したPointDiscloseクラス。").e();
		PointDisclose pd = new PointDisclose();
		pd.x = 0.0D;
		pd.y = 0.0D;
		assertThat(pd.x, is(0.0D));
		assertThat(pd.y, is(0.0D));

		o.l2("アクセッサーメソッドを使用したPointクラス。").e();
		Point p = new Point(1.1D, 2.2D);
		assertThat(p.getX(), is(1.1D));
		assertThat(p.getY(), is(2.2D));
		p.setX(3.3D);
		p.setY(4.4D);
		assertThat(p.getX(), is(3.3D));
		assertThat(p.getY(), is(4.4D));
	}

	public static class PointDisclose {
		public double x;
		public double y;
	}

	public static class Point {
		private double x;
		private double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public void setX(double x) {
			this.x = x;
		}

		public void setY(double y) {
			this.y = y;
		}
	}

	@Test
	public void 例外_クラスがprivate() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスがパッケージプライベート、あるいは、privateのネストしたクラスの場合。").e();

		o.l1("【どうして？】").e();
		o.l2("クライアントに迷惑をかけずに、後から変更できる。").e();
	}

	@Test(expected = IllegalArgumentException.class)
	public void 例外_不変クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("フィールドがfinalの場合。").e();

		o.l1("【どうして？】").e();
		o.l2("最低限、不変式は強制できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Timeクラス。").e();
		assertThat(new Time(25, 61).hour, is(25));
		
		o.l1("【どうすれば？】").e();
		o.l2("（相対的に）被害は少ないとはいえ、");
		o.__("情報隠蔽・カプセル化の、そのほかの恩恵は失うので、推奨されない。").e();
	}

	public static class Time {
		private static final int HOURS_PER_DAY = 24;
		private static final int MINUTES_PER_HOUR = 60;

		public final int hour;
		public final int minute;

		public Time(int hour, int minute) {
			if (hour < 0 || hour >= HOURS_PER_DAY) {
				throw new IllegalArgumentException("Hour: " + hour);
			}
			if (minute < 0 || minute >= MINUTES_PER_HOUR) {
				throw new IllegalArgumentException("Min: " + minute);
			}
			this.hour = hour;
			this.minute = minute;
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("クラスの内部表現/処理を変更する柔軟性を保つため、");
		o.__("フィールドはアクセッサーでカプセル化しておく。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
