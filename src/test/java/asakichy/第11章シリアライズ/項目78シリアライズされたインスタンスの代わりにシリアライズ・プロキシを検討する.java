package asakichy.第11章シリアライズ;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目78シリアライズされたインスタンスの代わりにシリアライズ・プロキシを検討する {

	@Test
	public void シリアライズ・プロキシ・パターンとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Serializeのリスクを大幅に減らす実装技法。").e();

		o.l1("【どうして？】").e();
		o.l2("Serializeの実装は、コンストラクタではない、言語外機構でインスタンスが生成されるということ。").e();
		o.l2("どうしても、バグやセキュリティのリスクが増大するので、対策が必要。").e();
	}

	@Test
	public void 実装() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("シリアライズ可能なクラスに、private staticのネストしたクラス（＝シリアライズ・プロキシ）を設計。").e();
		o.l3("シリアライズ・プロキシ自身もシリアライズ可能なクラス。").e();
		o.l2("シリアライズ・プロキシは、エンクロージングクラスのインスタンスの論理的状態を簡潔に表現。").e();
		o.l2("シリアライズ・プロキシのデフォルトのシリアライズ形式は、");
		o.__("エンクロージングクラスの完全なシリアライズ形式であることを意図している。").e();

		o.l1("【たとえば？】").e();
		o.l2("シリアライズ可能なPeriodクラスのシリアライズ・プロキシSerializationProxyクラス。").e();
		Calendar startCal = Calendar.getInstance(Locale.JAPAN);
		startCal.set(2011, Calendar.JANUARY, 1, 0, 0, 0);
		Calendar endCal = Calendar.getInstance(Locale.JAPAN);
		endCal.set(2011, Calendar.FEBRUARY, 1, 0, 0, 0);
		Date start = startCal.getTime();
		Date end = endCal.getTime();

		Period period = new Period(start, end);
		// シリアライズ
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(period);
		byte[] serializedPeriod = os.toByteArray();

		// デシリアライズ
		ByteArrayInputStream is = new ByteArrayInputStream(serializedPeriod);
		Period deserializedPeriod = (Period) new ObjectInputStream(is).readObject();

		assertThat(deserializedPeriod, hasToString("2011/01/01 - 2011/02/01"));
		assertThat(deserializedPeriod.start(), is(start));
		assertThat(deserializedPeriod.end(), is(end));

		{
			/** 【補】シリアライズ・プロキシ・パターンの動作シーケンス */
			// Periodシリアライズ時は、
			// PeriodインスタンスのwriteReplaceにより、SerializationProxyのインスタンスが生成され、
			// シリアライズされる。
			// つまり、シリアライズされるのは、実はSerializationProxyのほう。
			// SerializationProxyコンストラクタで、Periodの論理構造を表すデータが渡され、保管される。

			// Periodデシリアライズ時は、
			// Periodといいつつ、実際はSerializationProxyのインスタンスがシリアライズされているので、
			// SerializationProxyのreadResolveが呼び出される。
			// ここでは、SerializationProxyインスタンス生成時に保管されたデータをもとに、
			// Periodのコンストラクタを拝借して、Periodインスタンスを生成して、返している。
		}
	}

	private static final class Period implements Serializable {
		private static final long serialVersionUID = 1L;
		private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
		private final Date start;
		private final Date end;

		public Period(Date start, Date end) {
			this.start = new Date(start.getTime());
			this.end = new Date(end.getTime());
			if (this.start.compareTo(this.end) > 0)
				throw new IllegalArgumentException(start + " after " + end);
		}

		public Date start() {
			return new Date(start.getTime());
		}

		public Date end() {
			return new Date(end.getTime());
		}

		public String toString() {
			return formatter.format(start) + " - " + formatter.format(end);
		}

		// シリアライズ・プロキシ・クラス
		private static class SerializationProxy implements Serializable {
			// エンクロージングクラスの論理構造を保持
			private final Date start;
			private final Date end;

			// シリアライズ・プロキシ・パターンのためのコンストラクタ
			SerializationProxy(Period p) {
				// エンクロージングクラスを受け取り、単純に自身に保存するだけ
				this.start = p.start;
				this.end = p.end;
			}

			private static final long serialVersionUID = 234098243823485285L;

			// シリアライズ・プロキシ・パターンのためのreadResolve
			private Object readResolve() {
				// エンクロージングの代わりにシリアライズされたプロキシは、
				// デシリアライズのときは、逆にエンクロージングのインスタンスを返す
				return new Period(start, end);
			}
		}

		// シリアライズ・プロキシ・パターンのためのwriteReplace
		private Object writeReplace() {
			// エンクロージングは、自分の代わりにプロキシのインスタンスをシリアライズする
			return new SerializationProxy(this);
		}

		// シリアライズ・プロキシ・パターンのためのreadObject
		private void readObject(ObjectInputStream stream) throws InvalidObjectException {
			// エンクロージングは、シリアライズされないので、ここは呼ばれない
			throw new InvalidObjectException("Proxy required");
		}
	}

	@Test
	public void 利点_言語外特性排除() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("シリアライズの言語外特性を排除している。").e();
		o.l2("").e();

		o.l1("【どうして？】").e();
		o.l2("デシリアライズのように特殊ではない。").e();
		o.l2("インスタンスはProxyのメソッドを通じて「普通に」newされる。").e();
		o.l2("よって、余計な検証コードが必要ない。").e();
	}

	@Test
	public void 利点_堅牢性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("バイトストリーム攻撃を防ぐことができる。").e();
		o.l2("内部フィールド窃盗攻撃を防ぐことができる。").e();

		o.l1("【どうして？】").e();
		o.l2("防御的にreadObjectを書く方法よりも、かなりシンプルに防御実現できる。").e();
		o.l3("どのフィールドの正当性検査が必要か？とか、");
		o.__("どのフィールドを守らなければならないか？とか、考える必要がない。").e();
		o.l2("また、フィールドをfinalにすることができる。").e();
	}

	@Test
	public void 利点_柔軟性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("デシリアライズされたインスタンスが、").e();
		o.l2("元のシリアライズされたインスタンスとは異なるクラスを持つことを可能にする。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.EnumSet。").e();
		o.l2("staticファクトリメソッドで、その要素数により、2種類のインスタンスを生成している。").e();
		o.l3("要素が64以下の場合、RegularEnumSet。").e();
		o.l3("要素が64を超える場合、JumboEnumSet。").e();
		o.l2("このインスタンスの使い分けをシリアライズシステムでも実現するため、").e();
		o.l2("シリアライズ・プロキシ・パターンを使用している。").e();
	}

	@Test
	public void 欠点_パフォーマンス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("シリアライズ・プロキシを使用した場合、十数パーセント遅くなる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("シリアライズ・プロキシ・パターンの能力と安全性は、コストがかかる。").e();
		o.l2("しかし、パフォーマンスのペナルティより、優先されるべき").e();
	}

	@Test
	public void 制約() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("シリアライズ・プロキシ・パターンは、それが適用できない場合もある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("クライアントが拡張可能なクラスには使用できない。").e();
		o.l3("要は継承可能なクラスには使用できない。").e();
		o.l2("オブジェクトグラフが循環を含んでいるようなクラスには使用できない。").e();
		o.l3("シリアライズ・プロキシのreadResolveメソッド内から、");
		o.__("エンクロージングインスタンスに対してメソッドを呼び出そうとすると、");
		o.__("そのオブジェクトをまだ持っておらず、ClassCastExceptionとなる").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("シリアライズ・プロキシ・パターンは、簡単ではない不変式を持つオブジェクトを、");
		o.__("確実にシリアライズするための、最も簡単な方法。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
