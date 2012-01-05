package asakichy.第10章並行性;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目67過剰な同期は避ける {

	@Test
	public void エイリアンメソッドとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("同期されたメソッドやブロック内に、異質なメソッドが混入すること。").e();

		o.l1("【どうして？】").e();
		o.l2("メソッドの処理によっては、パフォーマンス低下、デッドロック、").e();
		o.l2("あるいは予想がつかない振る舞いを起こす可能性がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("、オーバーライドされるように設計されているメソッドや、").e();
		o.l2("関数オブジェクト（項目２１）の形式でクライアントが提供しているメソッド").e();

		o.l1("【どうすれば？】").e();
		o.l2("同期されたメソッドやブロック内で、決して制御をクライアントに譲ってはいけない。").e();
	}

	@Test
	public void 問題_例外発生() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("エイリアンメソッドが、同期例外を引き起こすことがある。").e();

		o.l1("【たとえば？】").e();
		o.l2("被観察者ObservableSetクラスと問題のない観察者Concatクラス。").e();

		ObservableSet<Integer> set;
		SetObserver<Integer> observer;

		set = new ObservableSet<Integer>(new HashSet<Integer>());
		observer = new Concat();
		set.addObserver(observer);

		// 100回コールバック
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}

		// まったく問題ない
		assertThat(observer.result().length(), is(190));

		o.l2("被観察者ObservableSetクラスとエイリアンを含む観察者ConcatAndRemoveクラス。").e();
		set = new ObservableSet<Integer>(new HashSet<Integer>());
		observer = new ConcatAndRemove();
		set.addObserver(observer);

		// 23までコールバックされ、オブザーバーが解除される
		try {
			for (int i = 0; i < 100; i++) {
				set.add(i);
			}
			assertThat(observer.result().length(), is(38)); // 23まで
		} catch (ConcurrentModificationException e) {
			// しかし、例外発生！
		}

		{
			/** 【補】 例外発生のメカニズム */
			// addの中で、notifyElementAddedがコールされる。
			// notifyElementAddedは、全オブザーバobserversに対し、それぞれのaddedをコールバックする。
			// つまり、addedのコールバックの最中は、observersがイテレート中である。
			// addedは、エイリアンとして、
			// イテレート中のobserversに対して、removeObserverでリストの要素を削除しようとしたので、
			// ConcurrentModificationException例外が発生した。
			// removeObserverはobserversで同期化されているが、
			// notifyElementAddedの最初でobserversの同期は取得しているので、問題なくコールされる。
		}
	}

	interface SetObserver<E> {
		// ObservableSetに要素が追加されたら、この関数がコールバックされる
		void added(ObservableSet<E> set, E element);

		// 処理結果の文字列を返す。
		String result();
	}

	private static class Concat implements SetObserver<Integer> {

		private final StringBuilder strings = new StringBuilder();

		// 追加された数字をすべて文字として連結
		@Override
		public void added(ObservableSet<Integer> s, Integer e) {
			strings.append(e);
		}

		@Override
		public String result() {
			return strings.toString();
		}

	}

	private static class ConcatAndRemove implements SetObserver<Integer> {

		private final StringBuilder strings = new StringBuilder();

		// 追加された数字をすべて文字として連結
		// かつ、数字が23のときだけ自分自身のオブザーバ登録を解除
		@Override
		public void added(ObservableSet<Integer> s, Integer e) {
			strings.append(e);
			if (e == 23) {
				s.removeObserver(this);
			}
		}

		@Override
		public String result() {
			return strings.toString();
		}

	}

	public class ObservableSet<E> extends ForwardingSet<E> {
		public ObservableSet(Set<E> set) {
			super(set);
		}

		protected final List<SetObserver<E>> observers = new ArrayList<SetObserver<E>>();

		// 通知登録
		public void addObserver(SetObserver<E> observer) {
			synchronized (observers) {
				observers.add(observer);
			}
		}

		// 通知解除
		public boolean removeObserver(SetObserver<E> observer) {
			synchronized (observers) {
				return observers.remove(observer);
			}
		}

		protected void notifyElementAdded(E element) {
			synchronized (observers) {
				for (SetObserver<E> observer : observers) {
					observer.added(this, element); // エイリアンメソッド呼び出し
				}
			}
		}

		@Override
		public boolean add(E element) {
			boolean added = super.add(element);
			if (added) {
				notifyElementAdded(element);
			}
			return added;
		}

		@Override
		public boolean addAll(Collection<? extends E> c) {
			boolean result = false;
			for (E element : c) {
				result |= add(element);
			}
			return result;
		}
	}

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
	public void 問題_デッドロック() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("エイリアンメソッドが、デッドロックを引き起こすことがある。").e();

		o.l1("【たとえば？】").e();
		o.l2("被観察者ObservableSetクラスとエイリアンを含む観察者ConcatAndRemoveByExecutorクラス。").e();

		ObservableSet<Integer> set = new ObservableSet<Integer>(new HashSet<Integer>());
		SetObserver<Integer> observer = new ConcatAndRemoveByExecutor();
		set.addObserver(observer);

		// 自身が登録解除されるi=23までコールバックされるが、そこでデッドロック
		for (int i = 0; i < 100; i++) {
			// デッドロックするのでコメント
			// set.add(i);
		}

		{
			/** 【補】デッドロックのメカニズム */
			// バックグラウンドスレッドによるremoveObserverの呼び出しは、
			// observersをロックしようとしするが、
			// メインスレッドがすでにロックを獲得しているため、
			// ロックを獲得できず、バックグラウンドスレッドは待ちになる。
			// 一方、メインスレッドは、バックグラウンドスレッドが
			// オブザーバーの削除を完了するのを待っている。
			// よってデッドロックが発生。
		}
	}

	private static class ConcatAndRemoveByExecutor implements SetObserver<Integer> {

		private final StringBuilder strings = new StringBuilder();

		// 追加された数字をすべて文字として連結
		// かつ、数字が23のときだけ、自分自身のオブザーバ登録を解除
		// ただし、今度は直接解除を呼び出すのではなく、エゼキューターサービスを利用
		@Override
		public void added(ObservableSet<Integer> s, Integer e) {
			strings.append(e);
			if (e == 23) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				final SetObserver<Integer> observer = this;
				final ObservableSet<Integer> set = s;
				try {
					executor.submit(new Runnable() {
						public void run() {
							set.removeObserver(observer);
						}
					}).get();
				} catch (ExecutionException ex) {
					throw new AssertionError(ex.getCause());
				} catch (InterruptedException ex) {
					throw new AssertionError(ex.getCause());
				} finally {
					executor.shutdown();
				}
			}
		}

		@Override
		public String result() {
			return strings.toString();
		}

	}

	@Test
	public void 問題_不整合() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("エイリアンメソッドが、不整合を引き起こすことがある。").e();

		o.l1("【どうして？】").e();
		o.l2("同期された領域により保護されている不変式が一時的に不正になっている間に、").e();
		o.l2("同期された領域内からエイリアンメソッドを呼び出すと、処理の失敗が壊滅的結果につながる。").e();
	}

	@Test
	public void 解法_オープンコール() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("異質なメソッドの呼び出しを、同期されたブロックの外に移動させる。").e();

		o.l1("【どうして？】").e();
		o.l2("例外やデットロック防ぐとともに、並行性をあげる。").e();

		o.l1("【たとえば？】").e();
		o.l2("オープンコールを使用した被観察者ObservableSetクラスと観察者ConcatAndRemoveクラス。").e();

		ObservableSet<Integer> set = new ObservableSet<Integer>(new HashSet<Integer>()) {
			@Override
			protected void notifyElementAdded(Integer element) {
				// ロックなしで操作できるスナップショットを取る。
				List<SetObserver<Integer>> snapshot = null;
				synchronized (observers) {
					snapshot = new ArrayList<SetObserver<Integer>>(observers);
				}
				for (SetObserver<Integer> observer : snapshot) {
					observer.added(this, element);
				}
			}

		};
		SetObserver<Integer> observer = new ConcatAndRemove();
		set.addObserver(observer);

		// 自身が登録解除されるi=23までコールバックされる
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}
		// 今度は、例外が発生しない
		assertThat(observer.result().length(), is(38));

		o.l2("オープンコールを使用した被観察者ObservableSetクラスと");
		o.__("観察者ConcatAndRemoveByExecutorクラス。").e();
		set.clear();
		observer = new ConcatAndRemoveByExecutor();
		set.addObserver(observer);

		// 自身が登録解除されるi=23までコールバックされる
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}
		// 今度は、デッドロックが発生しない
		assertThat(observer.result().length(), is(38));
	}

	@Test
	public void 解法_コンカレントコレクション() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("JDKで提供されているコンカレントコレクションを利用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.concurrent.CopyOnWriteArrayListを使用した被観察者ObservableSetクラスと");
		o.__("観察者ConcatAndRemoveクラス。").e();
		ObservableSet<Integer> set = new ObservableSet<Integer>(new HashSet<Integer>()) {

			// コンカレントコレクション使用
			private final List<SetObserver<Integer>> observers = new CopyOnWriteArrayList<SetObserver<Integer>>();

			@Override
			public void addObserver(SetObserver<Integer> observer) {
				// もはやこの処理に同期の必要なし
				observers.add(observer);
			}

			@Override
			public boolean removeObserver(SetObserver<Integer> observer) {
				// もはやこの処理に同期の必要なし
				return observers.remove(observer);
			}

			@Override
			protected void notifyElementAdded(Integer element) {
				// もはやこの処理に同期の必要なし
				for (SetObserver<Integer> observer : observers) {
					observer.added(this, element);
				}
			}

		};

		SetObserver<Integer> observer = new ConcatAndRemove();
		set.addObserver(observer);

		// 自身が登録解除されるi=23までコールバックされる
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}
		// 今度は、例外が発生しない
		assertThat(observer.result().length(), is(38));

		o.l2("java.util.concurrent.CopyOnWriteArrayListを使用した被観察者ObservableSetクラスと");
		o.__("観察者ConcatAndRemoveByExecutorクラス。").e();
		set.clear();
		observer = new ConcatAndRemoveByExecutor();
		set.addObserver(observer);

		// 自身が登録解除されるi=23までコールバックされる
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}
		// 今度は、デッドロックが発生しない
		assertThat(observer.result().length(), is(38));
	}

	@Test
	public void パフォーマンス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("過剰な同期は、パフォーマンスを損なう。").e();

		o.l1("【どうして？】").e();
		o.l2("並行処理を行う機会を損失。").e();
		o.l2("すべてのコアがメモリの一貫したビューを持つことを保証する必要から生じる遅延。").e();
		o.l2("コード実行を最適化するJVMの能力を制限する可能性。").e();

		o.l1("【どうすれば？】").e();
		o.l2("オブジェクト全体を外部から同期せず、内部的に同期する。").e();
		o.l3("この場合、高い並行性を達成するための様々な技法を検討する。").e();
		o.l3("具体的には「ロック分割」「ロックストライピング」「非ブロッキング並行性制御」など。").e();
		o.l2("オブジェクト内部で同期をせず、同期をしていない旨文書化する。").e();
		o.l3("クライアントがそれを見て、必要があれば、外部から同期させる。").e();

		{
			/** 【補】staticフィールドを変更するメソッドの同期化 */
			// メソッドがstaticのフィールドを変更するのであれば、
			// 通常は単一スレッドからだけそのメソッドが使用されるとしても、
			// そのフィールドへのアクセスを同期しなければならない。

			// なぜなら、関係のないクライアントすべてが同じ同期を行う保証はないため、
			// クライアントの外部同期は不可能である。
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("マルチコア時代には、過剰に同期しないことがこれまで以上に重要である。").e();
		o.l1("同期の有無を適切に選択し、その決定を文書化して明瞭にする。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
