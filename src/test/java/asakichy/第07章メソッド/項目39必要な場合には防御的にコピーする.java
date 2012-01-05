package asakichy.第07章メソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目39必要な場合には防御的にコピーする {

	@Test
	public void 防御的コピーとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クライアントに、オブジェクトへの参照をそのまま返すのではなく、その複製を返す。").e();

		o.l1("【どうして？】").e();
		o.l2("管理しているオブジェクトの整合性を守るため。").e();

		o.l1("【どうすれば？】").e();
		o.l2("「クライアントは、クラスの不変式を破壊するために徹底した努力をする」").e();
		o.l2("と想定して、防御的にプログラムしなければならない。").e();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void 防御的コピー_クラス不変式を守る() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスの不変式を守るために防御的コピーを使用する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("コンストラクタへの変更可能なパラメータを防御的にコピーする。").e();

		o.l1("【たとえば？】").e();
		o.l2("不完全な不変PeriodImmutableImmatureクラス。").e();
		{
			Calendar startCal = Calendar.getInstance(Locale.JAPAN);
			startCal.set(2011, Calendar.JANUARY, 1, 0, 0, 0);
			Calendar endCal = Calendar.getInstance(Locale.JAPAN);
			endCal.set(2011, Calendar.FEBRUARY, 1, 0, 0, 0);
			Date start = startCal.getTime();
			Date end = endCal.getTime();

			PeriodImmutableImmature periodImmutableImmature = new PeriodImmutableImmature(start, end);
			assertThat(periodImmutableImmature, hasToString(("2011/01/01 - 2011/02/01")));
			assertThat(periodImmutableImmature.start(), is(startCal.getTime()));
			assertThat(periodImmutableImmature.end(), is(endCal.getTime()));

			// 不変式が破られた
			periodImmutableImmature.start().setYear(2012);// アクセサ経由
			end.setYear(2010);// 元オブジェクト経由
			assertThat(periodImmutableImmature, not(hasToString(("2011/01/01 - 2011/02/01"))));
			assertThat(periodImmutableImmature.start(), is(not((startCal.getTime()))));
			assertThat(periodImmutableImmature.end(), is(not(endCal.getTime())));
		}

		o.l2("完全な不変PeriodImmutableクラス。").e();
		{
			Calendar startCal = Calendar.getInstance(Locale.JAPAN);
			startCal.set(2011, Calendar.JANUARY, 1, 0, 0, 0);
			Calendar endCal = Calendar.getInstance(Locale.JAPAN);
			endCal.set(2011, Calendar.FEBRUARY, 1, 0, 0, 0);
			Date start = startCal.getTime();
			Date end = endCal.getTime();

			PeriodImmutable periodImmutable = new PeriodImmutable(start, end);
			assertThat(periodImmutable, hasToString(("2011/01/01 - 2011/02/01")));
			assertThat(periodImmutable.start(), is(startCal.getTime()));
			assertThat(periodImmutable.end(), is(endCal.getTime()));

			// 不変式は破られない
			periodImmutable.start().setYear(2012);// アクセサ経由
			end.setYear(2010);// 元オブジェクト経由
			assertThat(periodImmutable, hasToString(("2011/01/01 - 2011/02/01")));
			assertThat(periodImmutable.start(), is((startCal.getTime())));
			assertThat(periodImmutable.end(), is(endCal.getTime()));
		}
	}

	private static final class PeriodImmutableImmature {
		private final Date start;
		private final Date end;

		private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);

		/**
		 * @param start
		 *            期間の開始
		 * @param end
		 *            期間の終わりで、開始より前であってはならない
		 * @throws IllegalArgumentException
		 *             startがendの後の場合
		 * @throws NullPointerException
		 *             startかendがnullの場合
		 */
		public PeriodImmutableImmature(Date start, Date end) {
			if (start.compareTo(end) > 0) {
				throw new IllegalArgumentException(start + " after " + end);
			}
			this.start = start;
			this.end = end;
		}

		public Date start() {
			return start;
		}

		public Date end() {
			return end;
		}

		public String toString() {
			return formatter.format(start) + " - " + formatter.format(end);
		}
	}

	private static final class PeriodImmutable {
		private final Date start;
		private final Date end;

		private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);

		public PeriodImmutable(Date start, Date end) {
			// 防御的にコピー
			// ただし、この場合、cloneは使わないこと
			// Dataはfinalでなく、cloneが正しいDateインスタンスを返すことを保証しないので
			this.start = new Date(start.getTime());
			this.end = new Date(end.getTime());

			// 正当性検査はコピー側に対して行う
			// コピー元はクライアントのものなので
			if (this.start.compareTo(this.end) > 0) {
				throw new IllegalArgumentException(start + " after " + end);
			}
		}

		public Date start() {
			// 防御的にコピー
			return new Date(start.getTime());
		}

		public Date end() {
			// 防御的にコピー
			return new Date(end.getTime());
		}

		public String toString() {
			return formatter.format(start) + " - " + formatter.format(end);
		}
	}

	@Test
	public void 防御的コピー_引数を守る() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("たとえ不変クラスでなくても、引数の防御的コピーを検討する。").e();

		o.l1("【どうして？】").e();
		o.l2("引数の参照をそのまま保持して使用する場合、").e();
		o.l2("クライアントがそれを変更し、クラスの動作が保証できなくなる可能性がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("クライアントからもらったSetを保持する場合。").e();
		o.l2("クライアントからもらったオブジェクトをMapのキーにしている場合。").e();
	}

	@Test
	public void 防御的コピー_戻り値を守る() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("たとえ不変クラスでなくても、戻り値の防御的コピーを検討する。").e();

		o.l1("【どうして？】").e();
		o.l2("内部管理しているオブジェクトをそのままクライアントに渡すと、").e();
		o.l2("クライアントがそれを変更し、クラスの動作が保証できなくなる可能性がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("配列やコレクションは、コピーや不変なビューを渡すこと。").e();
	}

	@Test
	public void 非防御的コピー_防御不要クラス化() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトの構成要素に不変オブジェクトだけを使用し、防御的コピーを不要な設計にする。").e();

		o.l1("【たとえば？】").e();
		o.l2("PeriodImmutableクラスの場合、");
		o.__("Dateオブジェクトを持つのではなく、getTime()で取得できるlong値を保持する。").e();
	}

	@Test
	public void 非防御的コピー_パフォーマンス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パフォーマンスのペナルティが大きすぎる場合、コピーによる安全性を諦める。").e();

		o.l1("【どうすれば？】").e();
		o.l2("パッケージ内でのみの使用など、クライアントと信頼関係があればコピーの必要はない。").e();
		o.l2("広く使われる場合は、「変更責任はクライアントにある」と文書化する。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("クライアントとやり取りする可変オブジェクトは、防御的コピーしておくこと。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
