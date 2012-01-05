package asakichy.第03章すべてのオブジェクトに共通のメソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目09equalsをオーバーライドする時は常にhashCodeをオーバーライドする {

	@Test
	public void hashCodeメソッドとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトのハッシュコード値を返す。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Object#hashCode()をオーバライド。").e();
	}

	@Test
	public void 一般契約_冪等() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("equalsで使用されているフィールドに変更が無い場合、");
		o.__("何回呼ばれても首尾一貫して同じ整数を返す。").e();
	}

	@Test
	public void 一般契約_equalsが等しい場合() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("equalsで等しいオブジェクトは、hashCodeも等しい。").e();
	}

	@Test
	public void 一般契約_equalsが等しくない場合() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("equalsで等しくないオブジェクトは、hashCodeも等しくない、");
		o.__("ということは求められない。").e();
		// ただし、異なるhashCodeを返せばパフォーマンスが上がる！
	}

	@Test
	public void オーバライド忘れの弊害() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("hashCode()をオーバライドし忘れると、");
		o.__("「equalsで等しいオブジェクトは、hashCodeも等しい。」が破られる。").e();

		o.l1("【どうして？】").e();
		o.l2("契約が破られると、それを前提としたクラスとコラボできなくなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("HashMapから値が取り出せない。").e();
		Map<PhoneNumber, String> m = new HashMap<PhoneNumber, String>();
		m.put(new PhoneNumber(707, 867, 5309), "Jenny");

		// equalsは同値でも、hashCodeが異なるため、HashMapで検索できない
		// assertThat(m.get(new PhoneNumber(707, 867, 5309)), is("Jenny"));
		assertThat(m.get(new PhoneNumber(707, 867, 5309)), is(nullValue()));
	}

	private static class PhoneNumber {
		protected final short areaCode;
		protected final short prefix;
		protected final short lineNumber;

		public PhoneNumber(int areaCode, int prefix, int lineNumber) {
			rangeCheck(areaCode, 999, "area code");
			rangeCheck(prefix, 999, "prefix");
			rangeCheck(lineNumber, 9999, "line number");
			this.areaCode = (short) areaCode;
			this.prefix = (short) prefix;
			this.lineNumber = (short) lineNumber;
		}

		private static void rangeCheck(int arg, int max, String name) {
			if (arg < 0 || arg > max) {
				throw new IllegalArgumentException(name + ": " + arg);
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof PhoneNumber)) {
				return false;
			}
			PhoneNumber pn = (PhoneNumber) o;
			return pn.lineNumber == lineNumber && pn.prefix == prefix && pn.areaCode == areaCode;
		}

		// hashCodeがオーバライドされていない！
	}

	@Test
	public void 正しい実装_手順() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("１").s("0ではない定数<ex.17>を「int result」に保存。").e();

		o.l2("２").s("意味のあるフィールドすべてのハッシュ値をresultに積算。").e();
		o.l2("２").s("a").s("フィールドごとにハッシュコード「int hashCode」を計算。").e();
		o.l2("２").s("b").s("result に 「31 * result + hashCode」 を入れる。").e();
		o.l2("２").s("c").s("意味のあるフィールド分繰り返す。").e();
		// 31を使用しているのは、素数を使うのが伝統だから。
		// ただ 31 * i = (i << 5) - i なので、JVMが計算しやすいことは確か。
		o.l2("３").s("resultを返す。").e();
		o.l2("４").s("自問し、単体テストを書く。").e();

		o.l1("【たとえば？】").e();
		o.l2("PhoneNumber_hashCodeクラス。").e();
		final class PhoneNumber_hashCode extends PhoneNumber {
			public PhoneNumber_hashCode(int areaCode, int prefix, int lineNumber) {
				super(areaCode, prefix, lineNumber);
			}

			@Override
			public int hashCode() {
				int result = 17;
				result = 31 * result + areaCode;
				result = 31 * result + prefix;
				result = 31 * result + lineNumber;
				return result;
			}
		}
		Map<PhoneNumber_hashCode, String> m = new HashMap<PhoneNumber_hashCode, String>();
		m.put(new PhoneNumber_hashCode(707, 867, 5309), "Jenny");
		assertThat(m.get(new PhoneNumber_hashCode(707, 867, 5309)), is("Jenny"));
	}

	@Test
	public void 正しい実装_型ごとの計算方法() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("フィールドの型によって計算方法にセオリーがある。").e();

		o.l1("【たとえば？】").e();
		o.l2("型の種類ごとの計算例。").e();
		int result = 17;
		int hashCode = 0;

		boolean b = true;
		short s = 1;
		long l = 1L;
		float f = 1.1F;
		double d = 1.1D;
		String o = "object";
		String[] a = { "a1", "a2" };

		hashCode = b ? 0 : 1;
		result = 31 * result + hashCode;

		hashCode = (int) s;// byte,char,short,int はそのまま
		result = 31 * result + hashCode;

		hashCode = (int) (l ^ (l >>> 32));
		result = 31 * result + hashCode;

		hashCode = Float.floatToIntBits(f);
		result = 31 * result + hashCode;

		long dbl = Double.doubleToLongBits(d);
		hashCode = (int) (dbl ^ (dbl >>> 32));
		result = 31 * result + hashCode;

		hashCode = (o == null ? 0 : o.hashCode());
		result = 31 * result + hashCode;

		hashCode = Arrays.hashCode(a);
		result = 31 * result + hashCode;

		assertThat(result, greaterThanOrEqualTo(Integer.MIN_VALUE));
		assertThat(result, lessThanOrEqualTo(Integer.MAX_VALUE));
		assertThat(result, is(not(0)));
	}

	@Test
	public void 間違った実装_固定値を返す() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("固定値は返さない。").e();

		o.l1("【どうして？】").e();
		o.l2("みな同じバゲットに入ってしまうので。").e();
	}

	@Test
	public void 間違った実装_計算方法を厳密に定義して公開() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("計算方法を厳密に（API仕様として）定義しない。").e();

		o.l1("【どうして？】").e();
		o.l2("将来もっといい計算式が発明されたとき、気軽に差し替えたいので。").e();
	}

	@Test
	public void パフォーマンス改善_キャッシュ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスが不変で、計算コストが高い場合は、");
		o.__("遅延初期化＋キャッシュを検討してもよい。").e();

		o.l1("【たとえば？】").e();
		o.l2("PhoneNumber_hashCodeクラス。").e();
		final class PhoneNumber_hashCode extends PhoneNumber {
			public PhoneNumber_hashCode(int areaCode, int prefix, int lineNumber) {
				super(areaCode, prefix, lineNumber);
			}

			private volatile int hashCode;

			@Override
			public int hashCode() {
				int result = hashCode;
				if (result == 0) {
					result = 17;
					result = 31 * result + areaCode;
					result = 31 * result + prefix;
					result = 31 * result + lineNumber;
					hashCode = result;
				}
				return result;
			}
		}
		Map<PhoneNumber_hashCode, String> m = new HashMap<PhoneNumber_hashCode, String>();
		m.put(new PhoneNumber_hashCode(707, 867, 5309), "Jenny");
		assertThat(m.get(new PhoneNumber_hashCode(707, 867, 5309)), is("Jenny"));
	}

	@Test
	public void パフォーマンス改悪_フィールドを計算から除外() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("意味のあるフィールドを、計算から除外してはいけない。").e();

		o.l1("【どうして？】").e();
		o.l2("そこで計算量を節約しても、ハッシュが分散せず少数のバゲットに集中して、");
		o.__("結局全体としてパフォーマンス悪くなる。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("equalsとhashCodeは必ずセットで実装する。").e();

		o.l1("equals同様、品質向上・工数削減のため、ツールやライブラリの仕様を検討する。").e();
		o.l2("org.apache.commons.lang.builder.HashCodeBuilder。").e();
		o.l2("EclipseのSource Generate機能。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
