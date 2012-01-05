package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目36常にOverrideアノテーションを使用する {

	@Test
	public void Overrideアノテーションとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッド宣言が、スーパータイプの宣言をオーバーライドしていることを示す。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Object#toString()のオーバライド。").e();
		new Object() {
			@Override
			public String toString() {
				return super.toString();
			}
		};
	}

	@Test
	public void スーパークラスメソッドをオーバーライド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("スーパークラスのメソッドをオーバライドするときは「もれなく」付加する。").e();

		o.l1("【どうして？】").e();
		o.l2("実行時ではなく、コンパイル時に不具合を発見するため。").e();
		o.l2("IDEであれば、コードインスペクションで警告してくれる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Bigramクラス。").e();
		Set<Bigram> s = new HashSet<Bigram>();
		for (int i = 0; i < 10; i++) {
			for (char ch = 'a'; ch <= 'z'; ch++) {
				s.add(new Bigram(ch, ch));
			}
		}
		// 重複がはじかれ、26個のハズが、260個登録されている。
		// equalsのオーバーライドが、オーバーロードになっていて、正しく呼ばれていない。
		assertThat(s.size(), is(not(26)));
		assertThat(s.size(), is(260));
	}

	@SuppressWarnings("unused")
	private static class Bigram {
		// Bigram：二重音字：アルファベット2文字が重なって別の音になること。thなど。
		private final char first;
		private final char second;

		public Bigram(char first, char second) {
			this.first = first;
			this.second = second;
		}

		// オーバーロードになっている
		public boolean equals(Bigram b) {
			return b.first == first && b.second == second;
		}

		// 正解
		// @Override
		// public boolean equals(Object o) {
		// Bigram b = (Bigram) o;
		// return b.first == first && b.second == second;
		// }

		public int hashCode() {
			return 31 * first + second;
		}

	}

	@Test
	public void 抽象メソッドをオーバーライド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("具象クラスが抽象メソッドをオーバーライドしているときは付加しなくてよい。").e();

		o.l1("【どうして？】").e();
		o.l2("具象クラスで、抽象メソッドをオーバライドしなければ、どのみちコンパイルエラー。").e();
		o.l2("障害発見が実行時まで遅延することはない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("とはいえ、コメント的な意味合いで付加してもよい。").e();
	}

	@Test
	public void インターフェイスをオーバーライド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("JDK6よりインターフェイスのオーバーライドに付加できるようになった。").e();
		o.l2("インターフェイスを実装している抽象クラスやインターフェイスでは付加する。").e();

		o.l1("【どうして？】").e();
		o.l2("具象クラスは、オーバーライドしなければコンパイルエラーになる。").e();
		o.l2("抽象クラスやインターフェイスでは、「新しいメソッドを追加していない」");
		o.__("ということ明確にするため、");
		o.__("スーパーインタフェースのメソッドをオーバーライドしているすべてのメソッドに");
		o.__("アノテーションを付ける価値がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Setインターフェイスは、java.util.Collectionインターフェイスに").e();
		o.l2("新たなメソッドを追加していないので、").e();
		o.l2("Collectionインタフェースに新たなメソッドを誤って追加しないことを保証するために、").e();
		o.l2("そのすべてのメソッド宣言にOverrideアノテーションを付けている。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("Overrideアノテーションは、つける必要のないケースもあるが、");
		o.__("盲目的に付けてしまい、実行時エラーを防ぐほうが得策。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
