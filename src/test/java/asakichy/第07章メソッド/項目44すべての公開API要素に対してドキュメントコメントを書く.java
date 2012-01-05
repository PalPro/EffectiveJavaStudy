package asakichy.第07章メソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目44すべての公開API要素に対してドキュメントコメントを書く {

	@Test
	public void ドキュメントコメントとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("公開されているすべてのAPI要素に対して、ドキュメントコメントを提供する。").e();

		o.l1("【どうして？】").e();
		o.l2("使用する人、継承する人に適切な情報を提供する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Javadocを書き、ドキュメント作成を自動化する。").e();
		o.l2("すべての公開されているクラス、インタフェース、コンストラク夕、メソッド、フィールド宣言の前に書く。").e();
		o.l2("保守に役立つなら、公開されていない要素に対してもドキュメントコメントを書く。").e();
	}

	@Test
	public void 要素_メソッド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッドとそのクライアント間の契約を簡潔に記述。").e();

		o.l1("【どうすれば？】").e();
		o.l2("事前条件を書く。").e();
		o.l2("事後条件を書く").e();
		o.l2("副作用を書く。").e();
		o.l2("スレッド安全性を書く。").e();

		o.l1("【たとえば？】").e();
		o.l2("getメソッドのドキュメントコメント。").e();
		assertThat(get(0), nullValue());
	}

	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * <p>
	 * This method is <i>not</i> guaranteed to run in constant time. In some
	 * implementations it may run in time proportional to the element position.
	 * 
	 * @param index
	 *            index of element to return;
	 *            must be non-negative and less than the size of this list
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 *             ({@code index < 0 || index >= this.size()})
	 * 
	 * 
	 */
	private static <E> E get(int index) {
		// dummy
		return null;
	}

	@Test
	public void 要素_クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラス全体にかかわる属性、クラスの特徴をコメントする。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スレッド安全性を書く。").e();
		o.l2("シリアライズ可能性を書く。").e();
	}

	@Test
	public void 要素_パッケージ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("包括的な、パッケージレベルのコメントも書く。").e();

		o.l1("【どうすれば？】").e();
		o.l2("リリース1.5以降は、package.htmlではなく、package-info.javaに書く。").e();

		{
			/** 【補】 package-info.java の中身 */
			// コメントの他に、必須ではないが、
			// パッケージ宣言とパッケージアノテーションを含むことができる。
		}
	}

	@Test
	public void 要素_ジェネリック() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ジェネリック型やジェネリックメソッドを文書化する際には、");
		o.__("すべての型パラメータを文書化する。").e();

		o.l1("【たとえば？】").e();
		o.l2("Mapインターフェイス").e();

		Map<String, String> map = new Map<String, String>() {
		};
		assertThat(map, anything());
	}

	/**
	 * An object that maps keys to values.
	 * A map cannot contain duplicate keys;
	 * each key can map to at most one value.
	 * 
	 * @param <K>
	 *            the type of keys maintained by this map
	 * @param <V>
	 *            the type of mapped values
	 */

	interface Map<K, V> {
		// ....
	}

	@Test
	public void 要素_列挙型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("enum型を文書化する際、型とすべてのpublicのメソッドだけでなく、");
		o.__("定数も文書化することを忘れない。").e();

		o.l1("【たとえば？】").e();
		o.l2("OrchestraSection列挙型。").e();
		assertThat(OrchestraSection.WOODWIND, anything());
	}

	/**
	 * An instrument section of a symphony orchestra.
	 * 
	 */
	enum OrchestraSection {
		/** Woodwinds, such as flute, clarinet, and oboe. */
		WOODWIND,
		/** Brass instruments, such as french horn and trumpet. */
		BRASS,
		/** Percussion instruments, such as timpani and cymbals */
		PERCUSSION,
		/** Stringed instruments, such as violin and cello. */
		STRING;
	}

	@Test
	public void 要素_アノテーション() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("アノテーションを文書化する際には、その型自身だけでなく、");
		o.__("すべてのメンバーを文書化する。").e();

		o.l1("【たとえば？】").e();
		o.l2("ExceptionTestアノテーション。").e();
	}

	/**
	 * Indicates that the annotated method is a test method that
	 * must throw the designated exception to succeed.
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface ExceptionTest {
		/**
		 * The exception that the annotated test method must throw in order to
		 * pass.(The test is permitted to throw any subtype of the type
		 * described by this class object.)
		 */
		Class<? extends Exception> values();
	}

	@Test
	public void 記述_概要説明() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("各ドキュメントコメントの最初の文は、コメントが関係している要素の概要説明となる。").e();
		o.l2("同クラス・インターフェイス内の２つのメンバーあるいはコンストラクタは、");
		o.__("同じ概要説明を持つべきではない。").e();

		{
			/** 【補】ドキュメントコメントの最初の文とは */
			// 最初に見つけたピリオド＋ホワイトスペースが最初の文と判断される。
			// 省略単語（B.C.等）によって区切られたくなければ、「@literal」を使えば良い。

			// {@literal B.C.}
		}

		o.l1("【どうして？】").e();
		o.l2("ドキュメントの一覧部分をみたときに、同じ文章が並ぶと混乱する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("オーバーロードに対しては、同じ説明文になりがちなので、特に注意を払う。").e();

		{
			/** 【補】概要説明は「文」？ */
			// 最初の一「文」といつつ、「句」であることが多い。
			// メソッド・コンストラクタは動詞句が多い。
			// クラス・インターフェイス・フィールドは名詞句が多い。
		}
	}

	@Test
	public void 記述_Javadocタグ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("リッチな表現に、HTMLのタグも使用できるが、より便利なJavadocのタグを使用する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("コードを書くには{@code ....}。").e();
		o.l3("HTMLエスケープ必要ない。").e();
		o.l3("複数行なら＜pre＞{@code ...}＜/pre＞とする。").e();
		o.l2("文字列を書くには{@literal ...}。").e();
		o.l3("HTMLエスケープ必要ない").e();
	}

	@Test
	public void 記述_コメント継承() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("継承関係にあるクラスにおいては、コメントの継承機能を利用して、作業負荷を軽減する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("サブクラス側にコメントがなければ、自動でスーパークラスのコメントが採用される。").e();
		o.l2("{@inheritDoc}を使用して、スーパークラスのコメントを一部継承する。").e();
	}

	@Test
	public void 記述_外部文書() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ドキュメンテーションコメントで、すべての文書化が完全になるわけではない。").e();
		o.l2("その場合、ドキュメンテーションコメントを補完する、外部文書を作成する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("複数の相互に関連しているクラスから構成される複雑なAPIに対しては、");
		o.__("API全体のアーキテクチャを説明する外部文書でドキュメンテーションコメントを補足する。").e();
		o.l2("ドキュメンテーションコメントから、その外部文書へのリンクがあると、なおよい。");
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ドキュメンテーションコメントは、APIを文書化する方法の、ほぼ唯一の選択肢。").e();
		o.l1("ドキュメンテーションコメントは、書くこと、これ必須。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();
}
