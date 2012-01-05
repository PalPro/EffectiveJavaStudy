package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目17継承のために設計および文書化する_でなければ継承を禁止する {

	@Test
	public void 継承のガイドラインとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("安全にサブクラス化されるための設計と文書化を選択する。").e();
		o.l2("さもなければ、適切に継承を禁止しておく。").e();
	}

	@Test
	public void 継承の文書化_オーバライド可能なメソッドの自己利用() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("finalでない、public/protectedメソッドについて、");
		o.__("クラスがそれを自己利用している場合には、必ずその旨文書化する。").e();

		o.l1("【どうして？】").e();
		o.l2("オーバライドしたときの影響を明示にするため。").e();

		o.l1("【どうすれば？】").e();
		o.l2("慣例として、オーバーライド可能なメソッドを呼び出すメソッドは、");
		o.__("そのドキュメントコメントの終わりに、それらの呼び出しに関する記述を書く。").e();
		o.l2("その記述は、「この実装は」という言葉で始める。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.AbstractCollection#remove()のJavaDocコメント。").e();
		o.l2("イテレータのremoveメソッド使用を明記。").e();

		/**
		 * public boolean remove( Object o )
		 * 
		 * この実装は、指定された要素を探すのにコレクションをイテレートします。
		 * もし、その要素が見つかれば、イテレータのremoveメソッドを使用して、
		 * コレクションからその要素を取り除きます。
		 * 本コレクションのiteratorメソッドから返されたイテレータがremoveメソッドを実装していなければ、
		 * この実装はUnsupportedOperationExceptionをスローすることに注意してください。
		 */

	}

	@Test
	public void 継承の文書化_実装の詳細() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ドキュメントは本来「なにをするか」「なぜするか」を書くところ。").e();
		o.l2("しかし、継承のドキュメントには「どうやって」を書かなければならない。").e();

		o.l1("【どうして？】").e();
		o.l2("継承はカプセル化を破っているため、");
		o.__("ドキュメントのセオリーを破り、実装の詳細を晒さなければならない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.AbstractCollection#removeRange()のJavaDocコメント。").e();
		o.l2("メソッドを使うヒトには関係ない、サブクラスのための実装便宜情報が書かれている。").e();

		/**
		 * protected void removeRange(int fromIndex, int toIndex）
		 * 
		 * このメソッドは、このリストとそのサブリストに対するclear操作により呼び出されます。
		 * リスト実装の内部を利用するためにこのメソッドをオーバーライドすることで、
		 * このリストとそのサブリストに対するclear操作のパフォーマンスを実質的に改善できます。
		 * 
		 * この実装はfromIndexの前の位置のリストイテレータを取得して、範囲全体が取り除かれるまで
		 * ListIterator.nextとその後のListIterator.removeの呼び出しを繰り返します。
		 * 注意：もし、ListIterator.removeが線形時間を要するならば、この実装は２次の時間を要します。
		 */
	}

	@Test
	public void 継承の設計_テストとしてサブクラスを書く() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("設計の妥当性確認のため、リリースする前に、サブクラスを書くことでクラスをテストする。").e();

		o.l1("【どうして？】").e();
		o.l2("この実装上の決定事項に、永久に束縛されることになるので、テストして慎重に決定する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("サブクラスを書けば、欲しいprotectedフィールド・メソッドが判明する。").e();
		o.l2("サブクラスを書けば、不必要なprotectedフィールド・メソッドが判明する。").e();
	}

	@Test
	public void 継承の設計_コンストラクタでオーバーライド可能メソッド呼び出し不可() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンストラクタの中で、オーバライド可能メソッドの自己利用は禁止。").e();

		o.l1("【どうして？】").e();
		o.l2("スーパークラスのコンストラクタは、サブクラスのコンストラクタより前に実行。").e();
		o.l2("スーパークラスで、不十分な状態のサブクラスのインスタンスのメソッド呼ぶことになる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Superクラスのコンストラクタによる、Subクラスのオーバライドメソッド呼び出し。").e();

		//スーパークラスのコンストラクタはサブクラスのコンストラクタより前に実行される。
		//よって、サブクラスでオーバーライドしているメソッドは、
		// サブクラスのコンストラクタが実行される前に呼び出されることになる。
		Sub sub = new Sub();
		assertThat(sub.getSpr(), is(not("sub")));
		assertThat(sub.getSpr(), nullValue());

	}

	public static class Super {
		private final String spr;

		public Super() {
			// 不完全な状態のサブクラスインスタンスを参照してしまう。
			// 具体的には、Subのコンストラクタで行われている「sub = "sub";」より前に、
			// Sub#overrideMe()が呼ばれてしまう。
			spr = overrideMe();
		}

		public String overrideMe() {
			return "super";
		}

		public String getSpr() {
			return spr;
		}

	}

	public static final class Sub extends Super {
		private final String sub;

		public Sub() {
			sub = "sub";
		}

		@Override
		public String overrideMe() {
			return sub;
		}
	}

	@Test
	public void 継承の設計_CloneableやSerializableの実装を回避する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("継承のために設計されているクラスは、極力CloneableやSerializableの実装を避けること。").e();

		o.l1("【どうして？】").e();
		o.l2("クラスを拡張しようとすると、CloneableやSerializable実装には、かなりの負荷がかかるため。").e();

		o.l1("【どうすれば？】").e();
		o.l2("cloneメソッド、readObjectメソッドは、オーバライド可能メソッドを呼んではいけない。").e();
		o.l3("コンストラクタのときと同じ理由。").e();
		o.l2("readResolve/writeReplaceメソッドはprotectedにする。").e();
		o.l3("privateにしてしまうと、サブクラスに黙って無視される。").e();

	}

	@Test
	public void 継承の禁止() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("サブクラス化を禁止する。").e();

		o.l1("【どうして？】").e();
		o.l2("継承のためにクラスを設計することは、そのクラスにかなりの制限を課す。").e();
		o.l2("継承を禁止すれば、圧倒的にシンプルで安全になる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("クラスをfinalに。").e();
		o.l2("コンストラクタをprivateにして、staticファクトリメソッドを提供。").e();
	}

	@Test
	public void 継承の代替() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("実装の拡張をしたいのなら、継承でなくほかの方法を使用する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("コンポジションで設計する。").e();
		o.l2("ヘルパメソッド化する。").e();
		o.l3("オーバライド可能メソッドの中身を、privateなヘルパメソッドへ移動。").e();
		o.l3("オーバライド可能メソッドからヘルパを呼び、かつ自己利用もヘルパのほうを呼ぶ。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("継承を検討する場合、面倒を引き受けるに値する正当な理由があるかを考える。").e();
		o.l1("継承を選んだら、「クラスを使う人」だけでなく、「クラスを拡張する人」に向けても文書化する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
