package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.BitSet;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目15可変性を最小限にする {
	@Test
	public void 不変クラスとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インスタンスが変更できないクラス。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.String。").e();
		o.l2("プリミティブのラッパークラスはすべて不変。").e();
	}

	@Test
	public void 実装() throws Exception {
		o.l1("【どういうこと？】").e();

		o.l2("１").s("オブジェクトの状態を変更するいかなるメソッドも提供しない。").e();
		o.l3("ミューテータを用意しない、ということ。").e();

		o.l2("２").s("クラスが拡張出来ないことを保証する。").e();
		o.l3("final クラス。").e();
		o.l3("privateコンストラクタ＋staticファクトリ。").e();

		o.l2("３").s("すべてのフィールドをファイナルにする。").e();
		o.l3("遅延初期化のためのフィールドでは条件緩和しても良い。（hashのキャッシュなど）").e();
		o.l3("要は外部に対して不変であればよいので。").e();

		o.l2("４").s("すべてのフィールドをprivateにする。").e();
		o.l3("public finalは、技術的に問題ないが、内部表現変更出来ないので推奨されない。").e();

		o.l2("５").s("可変コンポーネントに対する独占的アクセスを保証する。").e();
		o.l3("クライアントに可変オブジェクトの参照を渡してはいけない。").e();
		o.l3("クライアントからもらった可変オブジェクトの参照をそのまま保管してはいけない。").e();

		o.l1("【たとえば？】").e();
		o.l2("Complex（複素数）クラス。").e();
		assertThat(Complex.valueOf(1, 1), isA(Complex.class));
		assertThat(Complex.valueOfPolar(1, 1), isA(Complex.class));
	}

	// ２ finalクラス
	@SuppressWarnings("unused")
	private static final class Complex {
		// ３ finalフィールド
		private final double re;
		private final double im;

		// ２ privateコンストラクタ
		private Complex(double re, double im) {
			this.re = re;
			this.im = im;
		}

		// ２ staticファクトリ
		public static Complex valueOf(double re, double im) {// 直交形式
			return new Complex(re, im);
		}

		public static Complex valueOfPolar(double r, double theta) {// 極形式
			return new Complex(r * Math.cos(theta), r * Math.sin(theta));
		}

		// 共有可能で、スレッドセーフな定数を提供できる
		public static final Complex ZERO = new Complex(0, 0);
		public static final Complex ONE = new Complex(1, 0);
		public static final Complex I = new Complex(0, 1);

		// １ ミューテーター提供しない
		public double realPart() {
			return re;
		}

		public double imaginaryPart() {
			return im;
		}

		// １ 演算の結果も新しいオブジェクトを返す（＝関数的）
		public Complex add(Complex c) {
			return new Complex(re + c.re, im + c.im);
		}

		public Complex subtract(Complex c) {
			return new Complex(re - c.re, im - c.im);
		}

		public Complex multiply(Complex c) {
			return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
		}

		public Complex divide(Complex c) {
			double tmp = c.re * c.re + c.im * c.im;
			return new Complex((re * c.re + im * c.im) / tmp, (im * c.re - re * c.im) / tmp);
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Complex)) {
				return false;
			}
			Complex c = (Complex) o;
			return Double.compare(re, c.re) == 0 && Double.compare(im, c.im) == 0;
		}

		@Override
		public int hashCode() {
			int result = 17 + hashDouble(re);
			result = 31 * result + hashDouble(im);
			return result;
		}

		private int hashDouble(double val) {
			long longBits = Double.doubleToLongBits(re);
			return (int) (longBits ^ (longBits >>> 32));
		}

		@Override
		public String toString() {
			return "(" + re + " + " + im + "i)";
		}
	}

	@Test
	public void 長所_シンプルである() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("不変式がずっと真のままが保証されるので、気にしなくてよい。").e();
	}

	@Test
	public void 長所_スレッドセーフである() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("同期処理が必要なくなる。").e();
	}

	@Test
	public void 長所_オブジェクトを共有できる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("よく使うヤツはpublic static finalで定数化。").e();
		o.l2("その定数をstaticファクトリメソッドで返すことも可能になる。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.BigInteger#ZERO。").e();
		assertThat(BigInteger.valueOf(0), sameInstance(BigInteger.ZERO));
	}

	@Test
	public void 長所_防御的コピー不要である() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コピー元はコピー先と永久に同値なので、本質的に防御的コピー不要となる。").e();
		o.l2("逆にcloneやコピーコンストラクタは用意するべきではない。").e();
	}

	@Test
	public void 長所_内部まで共有できる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l1("オブジェクト間で、オブジェクトの内部データも共有できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.BigInteger。").e();
		o.l3("「intの符号」と「int配列の大きさ」で内部構成されている。").e();
		o.l3("BigInteger#negative()は、新たなオブジェクトを作るが、");
		o.__("「int配列の大きさ」は元オブジェクトと共有している。").e();
		assertThat(BigInteger.valueOf(-1L).negate(), is(BigInteger.ONE));
	}

	@Test
	public void 長所_ほかのオブジェクトが使いやすい() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ほかのオブジェクトが、自分の構成要素とする際、使い勝手がよい。").e();

		o.l1("【たとえば？】").e();
		o.l2("マップのキーとして使うとき。").e();
		o.l2("キーがほかで変わらないことが保証される。").e();
	}

	@Test
	public void 短所_オブジェクトが多くなる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("個々の異なる値に対して別々のオブジェクトを必要とする。").e();

		o.l1("【どうして？】").e();
		o.l2("操作のたびに、別のオブジェクトを生成して返す。").e();
		o.l2("特にオブジェクトが大きい場合には、メモリやパフォーマンスのコストが高くつく。").e();

		o.l1("【どうすれば？】").e();
		o.l2("可変コンパニオンクラスを提供する。").e();

		o.l1("【たとえば？】").e();
		o.l2("不変のBigInteger#flipBit()。").e();
		o.l3("1ビットひっくり返すだけなのに、全データを複製している。").e();
		BigInteger int_11111111 = new BigInteger("11111111", 2/*基数*/);
		BigInteger int_11111110 = int_11111111.flipBit(0);//最下位ビットを反転
		assertThat(int_11111111, is(new BigInteger("11111111", 2)));
		assertThat(int_11111110, is(new BigInteger("11111110", 2)));
		assertThat(int_11111111, is(not(sameInstance(int_11111110))));

		o.l2("可変のBitSet#set()").e();
		o.l3("効率よく内部の1ビットのみをひっくり返している。").e();
		BitSet bs = new BitSet(8);
		bs.set(0/*from(inclusive)*/, 8/*to(exclusive)*/);// 全ビットtrue
		bs.set(0, false);// 最下位ビットだけfalseに
		assertThat(bs.get(0), is(not(true)));

		o.l2("不変クラスjava.lang.Stringに対する、可変コンパニオンクラスjava.lang.StringBuilder。").e();
		o.l3("演算は可変コンパニオンが行い、最終結果を不変として得る。").e();
		StringBuilder sb = new StringBuilder();
		sb.append("a").append("b").append("c");
		assertThat(sb.toString(), is("abc"));
	}

	@Test
	public void 可変クラスでも不変性を追求する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("不変にできない場合も、可変部分をできるだけ少なくする。").e();

		o.l1("【どうして？】").e();
		o.l2("不変に近づけることで、不変の長所を享受できる。").e();
		o.l2("つまり、シンプルで、品質が良くなる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("やむをえない場合以外、フィールドはすべてfinal。").e();
		o.l2("コンストラクタが、オブジェクトのすべての不変式が確立した、");
		o.__("完全に初期化された状態でオブジェクトを生成する。").e();
		o.l2("初期化・再初期化メソッドは提供しない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.TimerTask。").e();
		o.l3("可変クラスだが、変更できる状態は少ない。").e();
		o.l3("一旦タスクが終了した後、リスケジュールできない。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("可変にすべきかなり正当な理由がない限り、クラスは不変とする。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
