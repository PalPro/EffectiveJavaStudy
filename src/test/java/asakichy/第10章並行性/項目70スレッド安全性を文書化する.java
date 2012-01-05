package asakichy.第10章並行性;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目70スレッド安全性を文書化する {

	@Test
	public void スレッド安全性とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("マルチスレッドで安全に使用してもらうため、");
		o.__("クラスはサポートするスレッド安全性を明確に文書化しなければならない。").e();

		o.l1("【どうして？】").e();
		o.l2("synchronized修飾子の有無は、実装の詳細であり、スレッドの安全性を判断できるものではない。").e();
		o.l2("実際、スレッドの安全性にはレベルがある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("安全性のレベルを文書に明記する。").e();
		o.l2("不変。").e();
		o.l3("インスタンスは定数のよう。").e();
		o.l3("外部同期必要なし。").e();
		o.l3("java.lang.String、java.lang.Integer、java.lang.BigIntegerなど。").e();
		o.l2("無条件スレッドセーフ。").e();
		o.l3("インスタンスは可変だが、外部同期を必要とすることなく並行使用できる内部同期を含む。").e();
		o.l3("java.util.Random、java.util.concurrent.ConcurrentHashMapなど。").e();
		o.l2("条件付きスレッドセーフ。").e();
		o.l3("メソッドのいくつかが外部同期を必要とする。").e();
		o.l3("java.util.Collections#synchronizedCollection()が返すコレクションの ");
		o.__("イテレータは外部同期が必要。").e();
		o.l2("スレッドセーフでない。").e();
		o.l3("外部同期が必要。").e();
		o.l3("java.util.ArrayList、java.util.HashMapなど。").e();
		o.l2("スレッド敵対。").e();
		o.l3("外部同期したとしても、並行使用は安全ではない。").e();
		o.l3("大抵staticを同期なし並行使用に起因。").e();
		o.l3("System#runFinalizerOnExit()など。").e();

		{
			/** 【補】スレッド安全性アノテーション */
			// 書籍「Java並行処理プログラミング」の付録Aで紹介されている
			// （クラスレベルの）スレッド安全性アノテーションは、
			// 上述スレッド安全性と大まかに対応している。

			// ・@Innmutable（不変）
			// ・@ThreadSafe（無条件スレッドセーフ、条件付きスレッドセーフ）
			// ・@NotThreadSafe（スレッドセーフでない、スレッド敵対）
		}
	}

	@Test
	public void 留意点_条件付きスレッドセーフ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("どの一連のメソッド呼び出しが外部同期を必要とし、");
		o.__("どのロックを獲得しないといけないのか、を文書化。").e();

		o.l1("【どうして？】").e();
		o.l2("ロックは、一般には自分（this）だが、そうでない場合もある。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Collections.#synchronizedMap()のドキュメントの例。").e();

		/**
		 * コレクションのビューに対してイテレートする場合には、
		 * 返されたマップに対してユーザは同期を取る必要があります。
		 * 
		 * Map<K,V> m = Collections.synchroziedMap(new HashMap<K,V>());
		 * Set<K> s = m.keySet(); // ここは同期しなくていい。
		 * synchronized(m){ // sではなくmに対して同期！
		 * 　for( K key : s ) {
		 * 　　key.f();
		 * 　}
		 * }
		 * 
		 * これを行わない場合、動作は保証されません。
		 * 
		 */
	}

	@Test
	public void 留意点_無条件スレッドセーフ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("誰もがアクセス可能なロックをクラスが使用することで、");
		o.__("クライアントが一連のメソッド呼び出しをアトミックに行うことを可能にできる。").e();
		o.l2("ただし、その柔軟性には代償が伴う。").e();

		o.l1("【どうして？】").e();
		o.l2("コンカレントコレクションが持つ高パフォーマンスの内部並行処理と互換性がなくなる。").e();
		o.l2("ロックを長い期間保持することで、サービス拒否攻撃を行うことが可能になる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("プライベートロックオブジェクトイデオムを使用する。").e();
		o.l3("privateなので外部から見えないし、finalなので不用意な変更もない。").e();
		o.l2("サブクラスからの干渉も防ぐので、継承にも効果的。").e();
		o.l2("無条件スレッドセーフのみで使用。").e();
		o.l3("条件付きだと、クライアントが使うロックを文書化しないといけない。").e();

		// コードイメージ
		/*
		 * private final Object lock = new Object();
		 * 
		 * public void foo() {
		 * 　synchronized( lock ){
		 * 　　....
		 * 　}
		 * }
		 */
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("すべてのクラスは、そのスレッド安全性の特性を文書化する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
