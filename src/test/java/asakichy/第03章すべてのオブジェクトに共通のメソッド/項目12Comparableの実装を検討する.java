package asakichy.第03章すべてのオブジェクトに共通のメソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目12Comparableの実装を検討する {
	@Test
	public void Comparebleとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("java.lang.Comparebleインターフェイス。").e();
		o.l2("実装しているクラスが自然順序を持つことを示す。").e();
		o.l2("唯一のメソッド「compareTo()」を持つ。").e();

		{
			/** 【補】注意：compareTo()の所属 */
			// 本章の題（「すべてのオブジェクトに共通のメソッド」）にそぐわず、
			// 「compareTo」はObjectのメソッドではない。
		}

		o.l1("【どうして？】").e();
		o.l2("Comparableを実装することで、多くの一般的なアルゴリズムや、");
		o.__("Comparableに依存しているコレクション実装を使用できるようになる。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.TreeSet。").e();
		TreeSet<String> treeSet = new TreeSet<String>();// StringはComparable実装
		treeSet.add("c");
		treeSet.add("a");
		treeSet.add("b");
		treeSet.add("a");// 重複
		// 重複のない昇順のコレクション
		assertThat(treeSet, contains("a", "b", "c"));
	}

	@Test
	public void 一般契約_順序比較() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("実装側のオブジェクトと渡されたオブジェクトを比較して整数を返す。").e();

		o.l1("【どうすれば？】").e();
		o.l2("実装側オブジェクトが小さい").s("負の数").e();
		o.l2("等しい").s("0").e();
		o.l2("実装側オブジェクトが大きい").s("正の数").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Integer。").e();
		Integer one = Integer.valueOf(1);
		Integer two = Integer.valueOf(2);
		assertThat(one.compareTo(two), lessThan(0));
		assertThat(two.compareTo(one), greaterThan(0));
		assertThat(one.compareTo(one), is(0));
	}

	@Test
	public void 一般契約_型違い() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("型が違えば比較の義務はない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("ClassCastExceptionの発行を許している。").e();
		o.l2("正式な契約ではないが、JDKはみなこうしている。").e();
	}

	@Test
	public void 一般契約_対称性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("sgn(x.compareTo(y)) == -sng(y.compareTo(x))。").e();

		{
			/** 【補】sgn(expression)について */
			// 符号関数をあらわす。
			// expressionの値が、負、ゼロ、正のどれであるかに応じて、
			// －１，０，１を返すと定義されている。
			//
		}

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Integer。").e();
		Integer one = Integer.valueOf(1);
		Integer two = Integer.valueOf(2);
		assertThat(sgn(one.compareTo(two)), is(-sgn(two.compareTo(one))));
	}

	private static int sgn(int i) {
		if (i > 0) {
			return 1;
		} else if (i < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	@Test
	public void 一般契約_推移性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("x.compareTo(y)>0 && y.compareTo(z)>0ならx.compareTo(z)>0。").e();
		o.l2("x.compareTo(y)==0ならsgn(x.compareTo(z)) == sgn(y.compareTo(z))。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Integer。").e();
		Integer one = Integer.valueOf(1);
		Integer two = Integer.valueOf(2);
		Integer three = Integer.valueOf(3);
		assertThat(three.compareTo(two), greaterThan(0));
		assertThat(two.compareTo(one), greaterThan(0));
		assertThat(three.compareTo(one), greaterThan(0));

		Integer ten_a = Integer.valueOf(10);
		Integer ten_b = Integer.valueOf(10);
		Integer ten_c = Integer.valueOf(10);
		assertThat(ten_a.compareTo(ten_b), is(0));
		assertThat(sgn(ten_a.compareTo(ten_c)), is(sgn(ten_b.compareTo(ten_c))));
	}

	@Test
	public void 一般契約_同値性がequalsと一致している() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("compareTo==0ならequals==true。").e();
		o.l2("厳密には、契約ではないが、強く推奨される事項。").e();

		o.l1("【どうすれば？】").e();
		o.l2("これを破るときにはドキュメントに明記する。").e();
		o.l3("「このクラスは、equalsと一致しない自然な順序を持っています。」").e();
		o.l2("この場合、「equalsとは矛盾している」という。").e();

		/** 【矛盾例】BigInteger */
		o.l1("【たとえば？】").e();
		o.l2("java.lang.BigIntegerのcompareTo()は「equals()とは矛盾している」。").e();
		BigDecimal oneZero = new BigDecimal("1.0");
		BigDecimal oneZeroZero = new BigDecimal("1.00");

		HashSet<BigDecimal> hashSet = new HashSet<BigDecimal>();
		hashSet.add(oneZero);
		hashSet.add(oneZeroZero);
		// equals的には等しくないので、２つとも追加
		assertThat(hashSet.size(), is(2));

		TreeSet<BigDecimal> treeSet = new TreeSet<BigDecimal>();
		treeSet.add(oneZero);
		treeSet.add(oneZeroZero);
		// compareToでは等しいとみなされ、重複として排除される
		assertThat(treeSet.size(), is(1));

	}

	@Test
	public void 実装方法() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("equals()と似ているが、異なる部分もある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("1").s("引数がnullなら、NullPointerException発行。").e();
		o.l2("2").s("意味のあるフィールドをすべて比較。").e();
		o.l2("2").s("a").s("オブジェクトなら、再帰的にcompareTo呼び出し。").e();
		o.l2("2").s("b").s("基本型なら、「<」「>」で。").e();
		o.l2("2").s("c").s("浮動小数点なら、「Double#compare()」「Float#compare()」で。").e();
		o.l2("2").s("d").s("コレクションなら、個々の要素にルールを適用。").e();
		o.l2("3").s("意味の強い順に比較して、大小決着つけば正負値を直ちに返す。").e();
		o.l2("4").s("すべてのフィールドが同じ場合は0を返す。").e();

		o.l1("【たとえば？】").e();
		o.l2("PhoneNumberクラス。").e();
		PhoneNumber max = new PhoneNumber(999, 999, 9999);
		PhoneNumber max2 = new PhoneNumber(999, 999, 9999);
		PhoneNumber min = new PhoneNumber(0, 0, 0);
		assertThat(max.compareTo(min), is(1));
		assertThat(min.compareTo(max), is(-1));
		assertThat(max.compareTo(max2), is(0));
	}

	public static final class PhoneNumber implements Comparable<PhoneNumber> {
		private final short areaCode;
		private final short prefix;
		private final short lineNumber;

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

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + areaCode;
			result = 31 * result + prefix;
			result = 31 * result + lineNumber;
			return result;
		}

		@Override
		public String toString() {
			return String.format("(%03d) %03d-%04d", areaCode, prefix, lineNumber);
		}

		public short getAreaCode() {
			return areaCode;
		}

		public short getPrefix() {
			return prefix;
		}

		public short getLineNumber() {
			return lineNumber;
		}

		@Override
		public int compareTo(PhoneNumber pn) {
			if (areaCode < pn.areaCode) {
				return -1;
			}
			if (areaCode > pn.areaCode) {
				return 1;
			}

			if (prefix < pn.prefix) {
				return -1;
			}
			if (prefix > pn.prefix) {
				return 1;
			}

			if (lineNumber < pn.lineNumber) {
				return -1;
			}
			if (lineNumber > pn.lineNumber) {
				return 1;
			}
			return 0;
		}

		// ※速度改良版
		public int compareToFast(PhoneNumber pn) {
			int areaCodeDiff = areaCode - pn.areaCode;
			if (areaCodeDiff != 0)
				return areaCodeDiff;

			int prefixDiff = prefix - pn.prefix;
			if (prefixDiff != 0)
				return prefixDiff;

			return lineNumber - pn.lineNumber;
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("Comparable自身にも、それを前提にしたクラスにも、多くの契約がある。").e();
		o.l1("そして、それを恣意的に違反している場合もある。").e();
		o.l1("作るときも、使うときも、ドキュメントを精読して理解してから。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
