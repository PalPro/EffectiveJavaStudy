package asakichy.第05章ジェネリックス;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目26ジェネリック型を使用する {

	@Test
	public void ジェネリック化とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Object型を使用した汎用クラスがあれば、ジェネリックに改造する。").e();

		o.l1("【どうして？】").e();
		o.l2("値を取り出したとき、キャストが要らない。").e();
		o.l2("型安全が保証される。").e();
		o.l2("型パラメータには、どんな型でも渡せる。").e();
		o.l2("逆に、型パラメータの型を制限することもできる。").e();

		{
			/** 【補】型パラメータの型制限 */
			// java.util.concurrent.DelayQueueの例。
			//
			// class DelayQueue<E extends Delayed> implements BlockingQueue<E>
			//
			// 実型パラメータがjava.util.concurrent.Delayのサブタイプじゃないとダメなことを表現。
			// 制限があるかわりに、ジェネリック型内部で、EはDelayedのメソッドを使用できる。
			//
			// ちなみに、<E super Delayed>とは書けない。
		}

		o.l1("【たとえば？】").e();
		o.l2("ジェネリック化改造候補の、Object配列を用いたStackクラス。").e();
		Stack stack = new Stack();
		stack.push("string");
		// キャストが必要。。。
		String s = (String) stack.pop();
		assertThat(s, is("string"));

	}

	private static class Stack {
		private Object[] elements;
		private int size = 0;
		private static final int DEFAULT_INITIAL_CAPACITY = 16;

		public Stack() {
			elements = new Object[DEFAULT_INITIAL_CAPACITY];
		}

		public void push(Object e) {
			ensureCapacity();
			elements[size++] = e;
		}

		public Object pop() {
			if (size == 0) {
				throw new IllegalStateException();
			}
			Object result = elements[--size];
			elements[size] = null;
			return result;
		}

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}
	}

	@Test
	public void 問題点_具象化不可能() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ジェネリック型にして、Objectを単純に仮型パラメータにしても、コンパイルエラーになる。").e();

		o.l1("【どうして？】").e();
		o.l2("仮型パラメータは具象化不可能型なので。").e();

		o.l1("【たとえば？】").e();
		o.l2("StackWrongクラス。").e();
	}

	@SuppressWarnings("unused")
	private static class StackWrong<E> {
		private E[] elements;
		private int size = 0;
		private static final int DEFAULT_INITIAL_CAPACITY = 16;

		public StackWrong() {
			// コンパイルエラー
			// elements = new E[DEFAULT_INITIAL_CAPACITY];
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

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}
	}

	@Test
	public void 解決策_配列をキャスト() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Object型の配列を生成し、それを仮パラメータ配列でキャスト。").e();
		o.l2("無検査キャスト警告を抑制。").e();

		o.l1("【どうして？】").e();
		o.l2("配列には、push(E)から来る要素Eしか入らないので、安全。").e();

		o.l1("【たとえば？】").e();
		o.l2("StackCastArray<E>クラス。").e();
		StackCastArray<String> stack = new StackCastArray<String>();
		stack.push("string");
		// キャスト不要
		String s = stack.pop();
		assertThat(s, is("string"));

		o.l1("【どうすれば？】").e();
		o.l2("配列型に@SuppressWarningsを使うのは危険性が増す。").e();
		o.l2("ただし、キャストは１回でパフォーマンスがよい。").e();
	}

	private static class StackCastArray<E> {
		private E[] elements;
		private int size = 0;
		private static final int DEFAULT_INITIAL_CAPACITY = 16;

		@SuppressWarnings("unchecked")
		public StackCastArray() {
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

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}
	}

	@Test
	public void 解決策_要素をキャスト() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Object型の配列を生成し、Object配列のまま保管。").e();
		o.l2("popした要素をEにキャストして返す。").e();

		o.l1("【どうして？】").e();
		o.l2("配列には、push(E)から来る要素Eしか入らないので、安全。").e();

		o.l1("【たとえば？】").e();
		o.l2("StackCastElement<E>クラス。").e();

		StackCastElement<String> stack = new StackCastElement<String>();
		stack.push("string");
		// キャスト不要
		String s = stack.pop();
		assertThat(s, is("string"));

		o.l1("【どうすれば？】").e();
		o.l2("スカラー型のみに@SuppressWarningsを使っているので危険が少ない。").e();
		o.l2("ただし、キャストは毎回でパフォーマンスがわるい。").e();
	}

	private static class StackCastElement<E> {
		private Object[] elements;
		private int size = 0;
		private static final int DEFAULT_INITIAL_CAPACITY = 16;

		public StackCastElement() {
			elements = new Object[DEFAULT_INITIAL_CAPACITY];
		}

		public void push(E e) {
			ensureCapacity();
			elements[size++] = e;
		}

		public E pop() {
			if (size == 0) {
				throw new IllegalStateException();
			}
			@SuppressWarnings("unchecked")
			E result = (E) elements[--size];
			elements[size] = null;
			return result;
		}

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("既存の型をジェネリック化して、安全性と使いやすさをクライアントに提供すること。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
