package asakichy.第05章ジェネリックス;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目24無検査警告を取り除く {

	@Test
	public void 無検査警告とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ジェネリックスでプログラムする際に、コンパイラが出力してくれる型安全に関する警告。").e();
		o.l3("無検査キャスト警告。").e();
		o.l3("無検査メソッド呼び出し警告。").e();
		o.l3("無検査ジェネリック配列生成警告。").e();
		o.l3("無検査変換警告。").e();

		o.l1("【どうすれば？】").e();
		o.l2("取り除くことが可能なすべての無検査警告を取り除くこと。").e();

		o.l1("【どうして？】").e();
		o.l2("すべての警告を取り除けば、コードが型安全であると保証される。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.HashSet原型を使用した場合。").e();
		/*
		 * 警告内容
		 * Type safety: The expression of type HashSet needs unchecked
		 * conversion to conform to Set<String>
		 */
		// Set<String> set = new HashSet();

		// 警告に従って修正
		Set<String> set = new HashSet<String>();
		assertThat(set, anything());
	}

	@Test
	public void 抑制() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("警告を取り除くことができなくて、");
		o.__("警告を起こしているコードが型安全だと明確に示すことができれば、");
		o.__("その時に限り@SuppressWarnings(\"unchecked\")アノテーションで警告を抑制。").e();

		o.l1("【どうして？】").e();
		o.l2("コードが型安全だと明確に示すことなく警告を抑制すると、").e();
		o.l2("いつか実行時例外に悩まされることになる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("SuppressWarningsアノテーションを、できる限り最小のスコープに対して使用。").e();
		o.l3("変数ないしメソッドで使用し、決してクラスに使用しない。").e();
		o.l2("型安全である理由をコメントに書くのは必須。").e();

		o.l1("【たとえば？】").e();
		o.l2("copyArray()関数。").e();
		o.l3("メソッド全体にSuppressWarningsを使用しない。").e();
		o.l3("戻り値をローカル変数に代入し、その式にSuppressWarningsを使用する。").e();
		String[] from = { "a", "b", "c" };
		String[] to = new String[1];
		String[] from2to = copyArray(from, to);
		assertThat(from2to, arrayContaining("a", "b", "c"));
	}

	private static <T> T[] copyArray(T[] from, T[] to) {
		if (to.length < from.length) {
			// T[]として渡されたのと同じ型の配列を生成するので、このキャストは妥当。
			@SuppressWarnings("unchecked")
			T[] result = (T[]) Arrays.copyOf(from, from.length, to.getClass());
			return result;
		}
		System.arraycopy(from, 0, to, 0, from.length);
		if (to.length > from.length) {
			to[from.length] = null;
		}
		return to;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("無検査警告は品質を高める上で重要な情報。").e();
		o.l1("無視しないで対処するか、無視する理由を記述する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
