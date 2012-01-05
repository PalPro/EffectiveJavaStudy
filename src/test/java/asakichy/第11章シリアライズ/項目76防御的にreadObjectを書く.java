package asakichy.第11章シリアライズ;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

public class 項目76防御的にreadObjectを書く {

	@Test
	public void readObjectとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("デシリアライズを行うメソッド。").e();
		o.l2("実質的に「もうひとつのpublicコンストラクタ（バイトストリームが引数）」。").e();

		o.l1("【どうすれば？】").e();
		o.l2("コンストラクタ相当のことをして、クラスの堅牢性を守らなければならない。").e();
	}

	@Test
	public void ガイドライン_不変式検査() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("不変式を検査しなければならない。").e();

		o.l1("【どうして？】").e();
		o.l2("不整合なバイトストリームを作ることで、不変式を破ることができる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("検査をして、検査に失敗したら、java.io.InvalidObjectExceptionを発行する。").e();

		o.l1("【たとえば？】").e();
		o.l2("不変式を保っている堅牢なPeriodクラスに、単にSerializableを実装。").e();
		o.l2("このままだと、不整合なデータ（start > end）を持ったバイトストリームでコンストラクトできる。").e();

		// 攻撃用バイトストリーム
		final byte[] serializedForm = new byte[] { (byte) 0xac, (byte) 0xed, (byte) 0x00, (byte) 0x05, (byte) 0x73, (byte) 0x72,
				(byte) 0x00, (byte) 0x52, (byte) 0x61, (byte) 0x73, (byte) 0x61, (byte) 0x6b, (byte) 0x69, (byte) 0x63,
				(byte) 0x68, (byte) 0x79, (byte) 0x2e, (byte) 0xe7, (byte) 0xac, (byte) 0xac, (byte) 0x31, (byte) 0x31,
				(byte) 0xe7, (byte) 0xab, (byte) 0xa0, (byte) 0xe3, (byte) 0x82, (byte) 0xb7, (byte) 0xe3, (byte) 0x83,
				(byte) 0xaa, (byte) 0xe3, (byte) 0x82, (byte) 0xa2, (byte) 0xe3, (byte) 0x83, (byte) 0xa9, (byte) 0xe3,
				(byte) 0x82, (byte) 0xa4, (byte) 0xe3, (byte) 0x82, (byte) 0xba, (byte) 0x2e, (byte) 0xe9, (byte) 0xa0,
				(byte) 0x85, (byte) 0xe7, (byte) 0x9b, (byte) 0xae, (byte) 0x37, (byte) 0x36, (byte) 0xe9, (byte) 0x98,
				(byte) 0xb2, (byte) 0xe5, (byte) 0xbe, (byte) 0xa1, (byte) 0xe7, (byte) 0x9a, (byte) 0x84, (byte) 0xe3,
				(byte) 0x81, (byte) 0xab, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x64, (byte) 0x4f, (byte) 0x62,
				(byte) 0x6a, (byte) 0x65, (byte) 0x63, (byte) 0x74, (byte) 0xe3, (byte) 0x82, (byte) 0x92, (byte) 0xe6,
				(byte) 0x9b, (byte) 0xb8, (byte) 0xe3, (byte) 0x81, (byte) 0x8f, (byte) 0x24, (byte) 0x50, (byte) 0x65,
				(byte) 0x72, (byte) 0x69, (byte) 0x6f, (byte) 0x64, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x4c,
				(byte) 0x00, (byte) 0x03, (byte) 0x65, (byte) 0x6e, (byte) 0x64, (byte) 0x74, (byte) 0x00, (byte) 0x10,
				(byte) 0x4c, (byte) 0x6a, (byte) 0x61, (byte) 0x76, (byte) 0x61, (byte) 0x2f, (byte) 0x75, (byte) 0x74,
				(byte) 0x69, (byte) 0x6c, (byte) 0x2f, (byte) 0x44, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x3b,
				(byte) 0x4c, (byte) 0x00, (byte) 0x05, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x74,
				(byte) 0x71, (byte) 0x00, (byte) 0x7e, (byte) 0x00, (byte) 0x01, (byte) 0x78, (byte) 0x70, (byte) 0x73,
				(byte) 0x72, (byte) 0x00, (byte) 0x0e, (byte) 0x6a, (byte) 0x61, (byte) 0x76, (byte) 0x61, (byte) 0x2e,
				(byte) 0x75, (byte) 0x74, (byte) 0x69, (byte) 0x6c, (byte) 0x2e, (byte) 0x44, (byte) 0x61, (byte) 0x74,
				(byte) 0x65, (byte) 0x68, (byte) 0x6a, (byte) 0x81, (byte) 0x01, (byte) 0x4b, (byte) 0x59, (byte) 0x74,
				(byte) 0x19, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x78, (byte) 0x70, (byte) 0x77, (byte) 0x08,
				(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x2d,

				/* start 部分の入れ替えデータ */
				(byte) 0x3c, (byte) 0xf1, (byte) 0x43, (byte) 0xd7,
				/* 「2月」の部分 */

				(byte) 0x78, (byte) 0x73, (byte) 0x71, (byte) 0x00, (byte) 0x7e, (byte) 0x00, (byte) 0x03, (byte) 0x77,
				(byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x2d,

				/* end 部分の入れ替えデータ */
				(byte) 0xdc, (byte) 0x96, (byte) 0x69, (byte) 0x5d,
				/* 「1月」の部分 */

				(byte) 0x78 };

		InputStream is = new ByteArrayInputStream(serializedForm);
		ObjectInputStream ois = new ObjectInputStream(is);
		// コンストラクト
		Period p = (Period) ois.readObject();
		// 開始日が終了日より遅い、不整合なインスタンスが生成された！
		assertThat(p, hasToString("2011/02/01 - 2011/01/01"));

		o.l2("この対策に、デシリアライズされたオブジェクトの正当性を検査するreadObjectメソッドを提供。").e();
		@SuppressWarnings("unused")
		class PeriodRobust {
			private Date start;
			private Date end;

			// ....省略

			private void readObject(ObjectInputStream s) throws Exception, ClassNotFoundException {
				s.defaultReadObject();
				// 不変式が満足されているかを検査する
				if (start.compareTo(end) > 0) {
					throw new InvalidObjectException(start + "after" + end);
				}
			}

			// ....省略
		}

		{
			/** 【補】攻撃用バイナリデータ作成に使用したプログラム */
			Calendar startCal = Calendar.getInstance(Locale.JAPAN);
			startCal.set(2011, Calendar.JANUARY, 1, 0, 0, 0);
			Calendar endCal = Calendar.getInstance(Locale.JAPAN);
			endCal.set(2011, Calendar.FEBRUARY, 1, 0, 0, 0);
			Date start = startCal.getTime();
			Date end = endCal.getTime();

			Period period = new Period(start, end);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			new ObjectOutputStream(os).writeObject(period);
			byte[] serialized = os.toByteArray();

			for (int i = 0; i < serialized.length; i++) {
				// バイナリ出力
				// System.out.printf("(byte) 0x%02x,", serialized[i]);
				// System.out.println();
			}
		}
	}

	private static final class Period implements Serializable {
		private static final long serialVersionUID = 1L;
		private final Date start;
		private final Date end;

		private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);

		public Period(Date start, Date end) {
			this.start = new Date(start.getTime());
			this.end = new Date(end.getTime());

			if (this.start.compareTo(this.end) > 0) {
				throw new IllegalArgumentException(start + " after " + end);
			}
		}

		@SuppressWarnings("unused")
		public Date start() {
			return new Date(start.getTime());
		}

		@SuppressWarnings("unused")
		public Date end() {
			return new Date(end.getTime());
		}

		public String toString() {
			return formatter.format(start) + " - " + formatter.format(end);
		}

	}

	@SuppressWarnings("deprecation")
	@Test
	public void ガイドライン_防御的コピー() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクト参照フィールドを持つ場合、防御的コピーが必要となる。").e();

		o.l1("【どうして？】").e();
		o.l2("フィールドへの余分な参照を追加したバイトストリームを作ることで、可変化できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Periodクラスの不変式を破壊する、MutablePeriodクラス。").e();
		MutablePeriod mp = new MutablePeriod();
		Period p = mp.period;
		Date pStart = mp.start;
		Date pEnd = mp.end;

		// 月を入れ替え
		pStart.setMonth(Calendar.FEBRUARY);
		pEnd.setMonth(Calendar.JANUARY);
		// 開始日が終了日より遅い、不整合なインスタンスに変更された！
		assertThat(p, hasToString("2011/02/01 - 2011/01/01"));

		o.l2("この対策に、デシリアライズされたオブジェクトを防御的コピーするreadObjectメソッドを提供。").e();
		@SuppressWarnings("unused")
		class PeriodRobust {
			private Date start;
			private Date end;

			// ....省略

			private void readObject(ObjectInputStream s) throws Exception, ClassNotFoundException {
				s.defaultReadObject();

				// 可変要素を防御的にコピーする
				start = new Date(start.getTime());
				end = new Date(end.getTime());

				// 不変式が満足されているかを検査する
				if (start.compareTo(end) > 0) {
					throw new InvalidObjectException(start + "after" + end);
				}
			}

			// ....省略
		}

		{
			/** 【補】 ObjectOutputStream#writeUnshared()/readUnshared()は使用しない */
			// 防御的コピーのコストがなく、不正オブジェクト参照攻撃を防御する目的で、
			// JDK1.4から、ObjectOutputStreamにwriteUnshared()/readUnshared()が提供されている。
			// これらは防御的コピーより確かに速いが、必要な安全性が保証されていない。
			// 脆弱なので、決して使用しないこと。
		}
	}

	public class MutablePeriod {
		// 攻撃ターゲットのPeriodインスタンス
		public final Period period;

		// 本来アクセスできない、Periodのstartフィールドのオブジェクト参照
		public final Date start;

		// 本来アクセスできない、Periodのendフィールドのオブジェクト参照
		public final Date end;

		public MutablePeriod() {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos);

				// 正当なPeriodインスタンスをシリアライズする
				Calendar startCal = Calendar.getInstance(Locale.JAPAN);
				startCal.set(2011, Calendar.JANUARY, 1, 0, 0, 0);
				Calendar endCal = Calendar.getInstance(Locale.JAPAN);
				endCal.set(2011, Calendar.FEBRUARY, 1, 0, 0, 0);
				out.writeObject(new Period(startCal.getTime(), endCal.getTime()));

				// Period内のDateフィールドへの不正な「previous object refs」を追加。
				byte[] ref = { 0x71, 0, 0x7e, 0, 5 }; // Ref #5
				bos.write(ref); // startフィールド
				ref[4] = 4; // Ref # 4
				bos.write(ref); // endフィールド

				// Periodと、盗んだDateへの参照をデシリアライズ
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
				period = (Period) in.readObject();
				start = (Date) in.readObject();
				end = (Date) in.readObject();
			} catch (Exception e) {
				throw new AssertionError(e);
			}
		}
	}

	@Test
	public void ガイドライン_オーバーライド可能メソッド呼び出し禁止() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("直接的でも間接的でも、クラス内のオーバーライド可能なメソッドを呼び出してはいけない。").e();

		o.l1("【どうして？】").e();
		o.l2("オーバーライドしているメソッドは、サブクラスの状態がデシリアライズされる前に実行される。").e();
		o.l2("その結果は、プログラムの失敗となる。").e();
	}

	@Test
	public void ガイドライン_オブジェクトグラフ正当性検査() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("デシリアライズされた後に、オブジェクトグラフ全体の正当性を検査しなければならない場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("java.io.ObjectInputValidationインターフェイスを使用する。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("デシリアライズは「もうひとつのコンストラクタ」であることを忘れずに。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
