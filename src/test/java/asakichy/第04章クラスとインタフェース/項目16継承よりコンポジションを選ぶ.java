package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目16継承よりコンポジションを選ぶ {
	@Test
	public void 継承の種類とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("１").s("実装継承。").e();
		o.l3("具象クラスの拡張（extends）。").e();
		o.l3("※本項目の対象はこちら。").e();
		o.l2("２").s("インターフェイス継承。").e();
		o.l3("インターフェイスの実装（implements）。").e();
		o.l3("インターフェイスによるインターフェイスの拡張（extends）。").e();
	}

	@Test
	public void 実装継承の欠点() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("カプセル化を破ってしまう。").e();

		o.l1("【どうして？】").e();
		o.l2("スーパークラスの実装の詳細に依存するから。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.HashSetを実装継承して機能拡張したInstrumentedHashSetクラス。").e();
		// 追加した要素数をカウントする機能を付加
		InstrumentedHashSet<String> s = new InstrumentedHashSet<String>();
		// ３件データ登録
		s.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
		// だが、結果は３件にならない
		assertThat(s.getAddCount(), is(not(3)));
		// ６件になってしまっている
		assertThat(s.getAddCount(), is(6));
	}

	@SuppressWarnings("serial")
	public static class InstrumentedHashSet<E> extends HashSet<E> {
		private int addCount = 0;

		public InstrumentedHashSet() {
		}

		public InstrumentedHashSet(int initCap, float loadFactor) {
			super(initCap, loadFactor);
		}

		@Override
		public boolean add(E e) {
			addCount++;
			return super.add(e);
		}

		@Override
		public boolean addAll(Collection<? extends E> c) {
			addCount += c.size();
			return super.addAll(c);
			// 実は、継承元addAll実装でaddを使用している。
			// addはこのクラスで継承して、その中にもカウント実装があるので、
			// 結果2重カウントされ、実際に追加した数の2倍になる。
		}

		public int getAddCount() {
			return addCount;
		}
	}

	{
		/** 【補】親クラス実装詳細変更の悪影響 */
		// InstrumentedHashSetの例で、
		// オーバライドしたaddAllからメソッドのカウントを取り除けば、いったん正しく動くようになる。
		// しかし、今度は継承元addAll実装でaddが使用されることが前提となってしまう。
		// つまり、親クラスaddAll実装変更で、addを使用しなくなったらカウント漏れでアウト。
		// また、親クラスにadd2など、別の追加メソッドが付加されたら、カウント漏れでアウト。
	}

	{
		/** 【補】親クラスインターフェイス変更の悪影響 */
		// 子クラスが独自のメソッドを持っていた場合、
		// 親クラスの変更で同名のメソッドを追加してしまったらコンパイルエラー。
	}

	@Test
	public void 実装継承の代替_コンポジション() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("機能を拡張する際は、継承ではなくコンポジションで設計する。").e();

		o.l1("【どうして？】").e();
		o.l2("親クラスの実装の詳細に依存しない。").e();
		o.l2("拡張元をより柔軟に選択することができる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("新たなクラスに、既存クラスのインスタンスを保持する。").e();
		o.l2("メソッドが呼び出されたら、拡張処理を施した上で、既存クラスのメソッドを呼び出す。").e();

		{
			/** 【補】コンポジションの用語 */
			// 新たなクラスで、既存クラスのメソッドを呼び出すことを「転送」という。
			// 新たなクラスのメソッドは「転送メソッド」という。
			// 新たなクラスのことを、「ラッパー」「デコレータ」と呼ぶこともある。
		}

		o.l1("【たとえば？】").e();
		o.l2("java.util.HashSetをコンポジションで機能拡張したInstrumentedSetクラス。").e();
		// 追加した要素数をカウントする機能を付加
		InstrumentedSet<String> hs = new InstrumentedSet<String>(new HashSet<String>());
		hs.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
		assertThat(hs.getAddCount(), is(3));

		// TreeSetでも問題なく動作する
		InstrumentedSet<String> ts = new InstrumentedSet<String>(new TreeSet<String>());
		ts.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
		assertThat(ts.getAddCount(), is(3));

		{
			/** 【補】コンポジションの弱点・SELF問題 */
			// コンポジションはコールバックフレームワークには向いていない。

			// ラップされたオブジェクトは、ラッパーを知らない。
			// なので、ラップされたオブジェクトは、
			// コールバックにthisを登録してしまい、ラッパーが無視されてしまう。
		}
	}

	public static class InstrumentedSet<E> extends ForwardingSet<E> {
		private int addCount = 0;

		public InstrumentedSet(Set<E> s) {
			super(s);
		}

		@Override
		public boolean add(E e) {
			addCount++;
			return super.add(e);
		}

		@Override
		public boolean addAll(Collection<? extends E> c) {
			addCount += c.size();
			return super.addAll(c);
		}

		public int getAddCount() {
			return addCount;
		}
	}

	// 再利用可能な転送クラス
	public static class ForwardingSet<E> implements Set<E> {
		private final Set<E> s;

		public ForwardingSet(Set<E> s) {
			this.s = s;
		}

		public void clear() {
			s.clear();
		}

		public boolean contains(Object o) {
			return s.contains(o);
		}

		public boolean isEmpty() {
			return s.isEmpty();
		}

		public int size() {
			return s.size();
		}

		public Iterator<E> iterator() {
			return s.iterator();
		}

		public boolean add(E e) {
			return s.add(e);
		}

		public boolean remove(Object o) {
			return s.remove(o);
		}

		public boolean containsAll(Collection<?> c) {
			return s.containsAll(c);
		}

		public boolean addAll(Collection<? extends E> c) {
			return s.addAll(c);
		}

		public boolean removeAll(Collection<?> c) {
			return s.removeAll(c);
		}

		public boolean retainAll(Collection<?> c) {
			return s.retainAll(c);
		}

		public Object[] toArray() {
			return s.toArray();
		}

		public <T> T[] toArray(T[] a) {
			return s.toArray(a);
		}

		@Override
		public boolean equals(Object o) {
			return s.equals(o);
		}

		@Override
		public int hashCode() {
			return s.hashCode();
		}

		@Override
		public String toString() {
			return s.toString();
		}
	}

	@Test
	public void 実装継承の適用_サブタイプ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("サブクラスが、本当にスーパータイプのサブタイプのときだけ使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("不必要に実装の詳細を外部に公開してしまい、将来のクラスの拡張が制限される。").e();
		o.l2("クライアントが内部に直接アクセスすることを許してしまう。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Stackはjava.util.Vectorを拡張すべきではなかった。").e();
		o.l2("java.util.Propertiesはjava.util.Hashtableを拡張すべきではなかった。").e();

		o.l1("【どうすれば？】").e();
		o.l2("クラスＡを拡張して、クラスＢを作る前に、");
		o.__("「すべてのＢは本当にＡであるか（is-a関係か）？」を自問する。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("実装継承は本当にis-a関係の時だけ使用する。").e();
		o.l1("そうでない場合は、コンポジションを使用する。").e();
		o.l1("コンポジションは実装継承より「頑強」かつ「強力」なので。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
