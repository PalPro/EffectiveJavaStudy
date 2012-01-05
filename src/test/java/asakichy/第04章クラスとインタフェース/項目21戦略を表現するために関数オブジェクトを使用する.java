package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目21戦略を表現するために関数オブジェクトを使用する {

	@Test
	public void 戦略とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Strategyパターン＠GoFのこと。").e();

		o.l1("【どうすれば？】").e();
		o.l2("関数オブジェクトを具象戦略（Concrete Strategy）として使用する。").e();
		o.l3("他のオブジェクトに対して一つだけ操作を行い、");
		o.__("その操作を公開しているクラスのインスタンス。").e();

		{
			/** 【補】関数オブジェクトのさまざまな呼び名 */
			// プログラムが特定の関数を保存して、
			// その関数を呼び出せるように受け渡すことについて、
			// 言語によってさまざまな呼称があります。
			// ・関数ポインタ
			// ・委譲
			// ・ラムダ式
			// ・高階関数
		}

		o.l1("【たとえば？】").e();
		o.l2("ソート処理における、java.util.Comparator。").e();

	}

	@Test
	public void 実装方法() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("戦略を表すインタフェースと、");
		o.__("個々の具象戦略に関してそのインタフェースを実装しているクラスを宣言。").e();

		o.l1("【たとえば？】").e();
		o.l2("戦略インターフェイスにjava.util.Comparator。").e();
		o.l2("具象戦略にStringLengthComparatorクラス。").e();
		String[] stringArray = new String[] { "333", "4444", "1", "22" };
		Arrays.sort(stringArray, new StringLengthComparator());
		assertThat(stringArray, arrayContaining("1", "22", "333", "4444"));

	}

	private static class StringLengthComparator implements Comparator<String> {
		@Override
		public int compare(String s1, String s2) {
			return s1.length() - s2.length();
		}

	}

	@Test
	public void イディオム_一度しか使用しない戦略() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("戦略が１度しか使用されない場合、無名クラスで宣言およびインスタンス化。").e();

		o.l1("【どうして？】").e();
		o.l2("クラス定義をしなくてもよいので、実装が簡便化。").e();

		o.l1("【たとえば？】").e();
		o.l2("戦略インターフェイスにjava.util.Comparator。").e();
		o.l2("具象戦略に無名クラス。").e();
		String[] stringArray = new String[] { "333", "4444", "1", "22" };
		Arrays.sort(stringArray, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		});
		assertThat(stringArray, arrayContaining("1", "22", "333", "4444"));
	}

	@Test
	public void イディオム_繰り返し使用する戦略() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("戦略が繰り返し利用される場合、戦略はprivate staticのメンバークラスとして、");
		o.__("戦略インタフェースの型を持つpublic static finalのフィールドを通して提供する。").e();

		o.l1("【どうして？】").e();
		o.l2("状態を持たなければ、インスタンスはシングルトンにして、生成やメモリのコストを節約できる。").e();
		o.l2("具象クラスをクライアントから隠蔽できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("具象戦略を提供するHostクラス。").e();
		String[] stringArray = new String[] { "333", "4444", "1", "22" };
		Arrays.sort(stringArray, Host.STRING_LENTH_COMPARATOR);
		assertThat(stringArray, arrayContaining("1", "22", "333", "4444"));
	}

	private static class Host {
		// クライアントにこのクラスは公開されない
		private static class StringLengthComparator implements Comparator<String> {
			@Override
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		}

		// 唯一のオブジェクトを使いまわせる。
		public static final Comparator<String> STRING_LENTH_COMPARATOR = new StringLengthComparator();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("Strategyパターンはオブジェクト指向の要。").e();
		o.l1("その実装方法に関数オブジェクトが利用できる。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
