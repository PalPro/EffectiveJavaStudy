package asakichy.第05章ジェネリックス;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目28APIの柔軟性向上のために境界ワイルドカードを使用する {

	@SuppressWarnings("unused")
	@Test
	public void 境界ワイルドカードとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パラメータ化された型に、");
		o.__("それが何らかのサブタイプ、ないしスーパータイプであることを指定できる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("パラメータ化された型にワイルドカード（？）を指定し、");
		o.__("かつ境界を組み合わせ、上限（extends）・下限（super）を指定する。").e();

		o.l1("【どうして？】").e();
		o.l2("ジェネリック型は非変で、親子関係を引きずらない。").e();
		o.l2("現実には、ジェネリック型の稼動範囲を継承方向に広げ、");
		o.__("柔軟性を持たせたい場合がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("非境界型のスタッククラスStackUnbounded#pushAll()/popAll()メソッド。").e();
		o.l2("非境界型だと、渡せる型の範囲が狭く、使いにくい。").e();

		StackUnbounded<Number> stack = new StackUnbounded<Number>();
		Collection<Integer> integers = new ArrayList<Integer>();
		Collection<Object> objects = new ArrayList<Object>();

		// ジェネリック型は「非変」なので、コンパイルエラー
		// stack.pushAll(integers);
		// stack.popAll(objects);

		{
			/** 【補】非変とは？ */
			// IntegerはNumberのサブタイプだが、
			// Collection<Integer>はCollection<Number>のサブタイプではない。
			// ObjectはNumberのスーパータイプだが、
			// Collection<Object>はCollection<Number>のスーパータイプではない。
		}

	}

	private static class StackUnbounded<E> {
		private E[] elements;
		private int size = 0;
		private static final int DEFAULT_INITIAL_CAPACITY = 16;

		@SuppressWarnings("unchecked")
		public StackUnbounded() {
			elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
		}

		public void push(E e) {
			ensureCapacity();
			elements[size++] = e;
		}

		public E pop() {
			if (size == 0) {
				throw new IllegalStateException();
			}
			E result = elements[--size];
			elements[size] = null;
			return result;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		// pushAll - 非境界型
		@SuppressWarnings("unused")
		public void pushAll(Iterable<E> src) {
			for (E e : src) {
				push(e);
			}
		}

		// popAll - 非境界型
		@SuppressWarnings("unused")
		public void popAll(Collection<E> dst) {
			while (!isEmpty())
				dst.add(pop());
		}

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}
	}

	@Test
	public void ワイルドカード化_GetとPutの原則() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Get and Put Principle。").e();
		o.l2("プロデューサー（生産者）かコンシューマー（消費者）を表す入力パラメータに対して、");
		o.__("境界ワイルドカード型を使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("APIの柔軟性を最大化できる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("「PECS（Producer-extends、Consumer-super）」を指針に。").e();
		o.l3("パラメータ化された型Ｔがプロデューサーを表していれば、＜? extends T＞を使用。").e();
		o.l3("パラメータ化された型Ｔがコンシューマーを表していれば、＜? super T＞を使用。").e();
		o.l3("パラメータ化された型Ｔがプロデューサー/コンシューマー両方を表していれば、ワイルドカードを使用しない。").e();

		o.l1("【たとえば？】").e();
		o.l2("境界ワイルドカードを使用したスタッククラスStack。").e();
		o.l2("pushAll()では、上限境界ワイルドカード型を使用し、局所的な「共変」を導入。").e();
		o.l2("popAll()では、下限境界ワイルドカード型を使用し、局所的な「反変」を導入。").e();
		Stack<Number> stack = new Stack<Number>();
		Collection<Integer> integers = new ArrayList<Integer>();
		Collection<Object> objects = new ArrayList<Object>();

		stack.pushAll(integers);
		stack.popAll(objects);
		assertThat(stack.size, is(0));

		{
			/** 【補】共変、反変とは？ */
			// IntegerはNumberのサブタイプで、
			// Collection<Integer>に対して、
			// Collection<? extends Number>は共変してサブタイプ。

			// IntegerはNumberのサブタイプで、
			// Collection<Integer>に対して、
			// Collection<? super Number>は反変してスーパータイプ。
		}
	}

	private static class Stack<E> {
		private E[] elements;
		private int size = 0;
		private static final int DEFAULT_INITIAL_CAPACITY = 16;

		@SuppressWarnings("unchecked")
		public Stack() {
			elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
		}

		public void push(E e) {
			ensureCapacity();
			elements[size++] = e;
		}

		public E pop() {
			if (size == 0) {
				throw new IllegalStateException();
			}
			E result = elements[--size];
			elements[size] = null;
			return result;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		// pushAll - プロデューサー
		// 「Eの何らかのサブタイプのIterable」
		public void pushAll(Iterable<? extends E> src) {
			// Eを取得するのみ
			for (E e : src) {
				push(e);
			}
		}

		// popAll - コンシューマー
		// 「Eの何らかのスーパータイプのCollection」
		public void popAll(Collection<? super E> dst) {
			// Eを消費するのみ
			while (!isEmpty()) {
				dst.add(pop());
			}
		}

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}
	}

	@Test
	public void ワイルドカード化_明示的型パラメータ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("望んでいる型を、コンパイラが推論しない場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("型パラメータの型を、明示的に指定する。").e();

		o.l1("【たとえば？】").e();
		o.l2("Union#union()メソッド。").e();

		Set<Integer> integers = new HashSet<Integer>(Arrays.asList(1, 2, 3));
		Set<Double> doubles = new HashSet<Double>(Arrays.asList(4D, 5D, 6D));

		// あいまいなためコンパイルエラー
		// Set<Number> unionSet = Union.union(integers, doubles);

		// 型パラメータを明示的に指定
		Set<Number> unionSet = Union.<Number>/*ここ*/ union(integers, doubles);
		assertThat(unionSet.size(), is(6));
	}

	private static class Union {
		private static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
			Set<E> result = new HashSet<E>(s1);
			result.addAll(s2);
			return result;
		}
	}

	@Test
	public void ワイルドカード化_型パラメータリストにワイルドカード() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("型変数での変数宣言部分だけでなく、型パラメータリスト部分にもワイルドカードが使用できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("リストの最大値を計算する関数max()。").e();

		List<ScheduledFuture<?>> scheduleFutures = new ArrayList<ScheduledFuture<?>>();
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> feature = service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// dummy
			}
		}, 3, 1, TimeUnit.SECONDS);
		scheduleFutures.add(feature);

		ScheduledFuture<?> max = max(scheduleFutures);
		assertThat(max, isA(ScheduledFuture.class));

		{
			/** 【補】 maxでScheduledFutureが使用できる理由 */
			// ScheduledFutureはComparable<ScheduledFuture>を実装していない。
			// もし、maxの型パラメータが単純に<T extends Comparable<T>>だったら、
			// コンパイルエラー。

			// しかし、ScheduledFutureがComparable<Delayed>を実装していて
			// （DelayedはScheduledFutureのスーパータイプ）、
			// かつ、maxの型パラメータがComparable<? super T>となっているため、
			// コンパイルエラーが発生していない。
		}
	}

	// 型パラメータ部分に境界ワイルドカードを使用
	private static <T extends Comparable<? super T>> T max(List<? extends T> list) {
		Iterator<? extends T> i = list.iterator();
		T result = i.next();
		while (i.hasNext()) {
			T t = i.next();
			if (t.compareTo(result) > 0) {
				result = t;
			}
		}
		return result;
	}

	@Test
	public void ワイルドカード化_型パラメータとワイルドカードの二重性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("同じ意味のメソッドを、型パラメータとワイルドカードどちらでも定義できる場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("型パラメータがメソッド宣言中に一度しか現れないのであれば、それをワイルドカードで置き換える。").e();
		o.l3("非境界型パラメータならば、非境界ワイルドカードで置き換える。").e();
		o.l3("境界型パラメータならば、境界ワイルドカードで置き換える。").e();

		o.l1("【どうして？】").e();
		o.l2("インターフェイスがシンプルになる。").e();

		o.l1("【たとえば？】").e();
		o.l2("リスト中の２つのインデックスで指定される項目を交換するstaticメソッド。").e();
		o.l2("型パラメータ版swapByTypeParam()とワイルドカード版swapByWildCard()。").e();

		List<String> strings = Arrays.asList("a", "b", "c");

		swapByTypeParam(strings, 0, 1);
		assertThat(strings, contains("b", "a", "c"));

		swapByWildCard(strings, 1, 2);
		assertThat(strings, contains("b", "c", "a"));
	}

	// 型パラメータ版
	public static <E> void swapByTypeParam(List<E> list, int i, int j) {
		list.set(i, list.set(j, list.get(i)));
	}

	// ワイルドカード版
	public static void swapByWildCard(List<?> list, int i, int j) {
		// ワイルドカードを使用すると、そのリストにはnull以外入れられない。
		// 以下はコンパイルエラー。
		// list.set(i, list.set(j, list.get(i)));

		// ワイルドカードを捕捉して、ヘルパーメソッドに実装を逃がす。
		// （「ワイルドカードキャプチャ」として知られる機能。）
		swapHelper(list, i, j);
	}

	// 結局、型パラメータ版のメソッドだが、この複雑なAPIをユーザから隠蔽できる。
	private static <E> void swapHelper(List<E> list, int i, int j) {
		list.set(i, list.set(j, list.get(i)));
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("広く使用されるAPIには、使い勝手を考慮し、ワイルドカード型を適用する。").e();
		o.l1("適用の際はPECS基本原則に倣うこと。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
