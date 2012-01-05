package asakichy.第07章メソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目38パラメータの正当性を検査する {

	@Test
	public void パラメータの正当性とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッドは、パラメータとして渡される値に関して何らかの「制約」を持っている。").e();

		o.l1("【たとえば？】").e();
		o.l2("インデックス値は、負であってはいけない。").e();
		o.l2("オブジェクト参照は、nullであってはいけない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("「制約」は、明確に文書化すべき。").e();
		o.l2("「制約」は、メソッド本体の初めに検査することで強制すべき。").e();

		o.l1("【どうして？】").e();
		o.l2("エラーの発生する確率を事前に下げる。").e();
		o.l2("エラーが検出される可能性を高くする。").e();
		o.l2("エラーが発見された際、その原因の特定を容易にする。").e();
	}

	@Test
	public void 制約方法_例外発行() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("公開メソッドの場合、メソッドの先頭でパラメータの正当性を検査。").e();
		o.l2("制約を満たさない場合、その場で例外を発行。").e();

		o.l1("【どうして？】").e();
		o.l2("エラーの発生場所を明確にするため。").e();
		o.l2("不正なパラメータのまま処理が行われると、想定外の例外が発生したり、");
		o.__("誤った計算結果を返して、他の場所でエラーが発生してしまう。").e();

		{
			/** 【補】 例外翻訳 */
			// パラメータに対する必要な正当性検査を、暗黙に行っている場合がある。
			// 不正なパラメータ値の結果としてその計算が自然にスローする例外が、
			// メソッドがスローすると文書化した例外と一致しない場合は、
			// 例外翻訳イディオムを使用する。
		}
	}

	@Test
	public void 制約方法_例外文書化() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("公開メソッドの場合、制約違反時にスローする例外を文書化。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Javadocの@throwsタグを使用。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.BigInteger#mod()のJavadocコメント。").e();

		/**
		 * 値が (this mod m) の BigInteger を返します。
		 * このメソッドは、常に「負でない」 BigInteger を返す remainder とは異なります。
		 * 
		 * @param m
		 *            モジュラス
		 * @throws ArithmeticException
		 *             m <= 0の場合
		 * @return this mod m.
		 * 
		 */
		// public BigInteger mod(BigInteger m)
		// ......
	}

	@Test
	public void 制約方法_アサート() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("公開されていないメソッドの場合、アサーションでパラメータの正当性を検査").e();

		o.l1("【どうして？】").e();
		o.l2("内部メソッドなのでパラメータの正当性は保証されているが、").e();
		o.l2("バグの早期発見のため、アサートを埋め込んでおく。").e();

		o.l1("【たとえば？】").e();
		o.l2("sort()関数。").e();

		int[] target = { 100, 10, 1 };
		sort(target, 0, 3);
		int[] result = { 1, 10, 100 };
		assertThat(target, is(result));
	}

	private static void sort(int a[], int offset, int length) {
		assert a != null;
		assert offset >= 0 && offset <= a.length;
		assert length >= 0 && length <= a.length - offset;
		// ソート処理
		Arrays.sort(a, offset, offset + length);
	}

	@Test
	public void 検証基準_後で使うパラメータは検査する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("そのメソッドでは使用されなくても、");
		o.__("後で使用するために保存するパラメータは、その場で正当性を検査する。").e();

		o.l1("【たとえば？】").e();
		o.l2("int配列を取って、その配列のListビューを返すintArrayAsList()。").e();

		int[] intArray = {1,2,3};
		List<Integer> list = intArrayAsList(intArray);
		Collections.shuffle(list);
		assertThat(list, containsInAnyOrder(1,2,3));
		// 仮に、intArrayAsListがnullチェックを行っていなかったら、、、
		// クライアントがListを使い始めたところで、nullPointerExceptionが発生。
		// これだと、本当の原因の発生場所が特定しにくくなる。

		o.l1("【どうすれば？】").e();
		o.l2("特に、コンストラクタでは必ず検討する。").e();
		o.l2("クラスの不変式を破っているオブジェクト生成を防ぐため。").e();
	}

	private static List<Integer> intArrayAsList(final int[] a) {
		// パラメータの正当性を確認
		if (a == null) {
			throw new NullPointerException();
		}

		return new AbstractList<Integer>() {
			public Integer get(int i) {
				return a[i];
			}

			@Override
			public Integer set(int i, Integer val) {
				int oldVal = a[i];
				a[i] = val;
				return oldVal;
			}

			public int size() {
				return a.length;
			}
		};
	}

	@Test
	public void 検証基準_コストが高いかつ自明であれば検査しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("正当性検査のコストが高いか現実的でなく、かつ、").e();
		o.l2("正当性検査が計算の処理の中で暗黙に行われる場合は検査を省略してもよい。").e();

		o.l1("【たとえば？】").e();
		o.l2("Collections#sort(List)メソッドについて。");
		o.l3("リスト個々のオブジェクトに対して「比較可能か」と検査するなんていうのは、");
		o.__("どうせ比較処理の中でついでに行われていることだから、事前に検証するのは無駄。").e();
	}

	@Test
	public void 検証基準_検査を目的にしない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("制約は必要だが、少ないに越したことはない。").e();

		o.l1("【どうして？】").e();
		o.l2("制約が多いと、クライアントの使い勝手が悪くなる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("どんなパラメータでも動けるようにするのが良いメソッド設計。").e();
		o.l2("制約はそれからのハナシ。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("メソッドパラメータの検査をするという「防御」習慣は、必ず報われる。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
