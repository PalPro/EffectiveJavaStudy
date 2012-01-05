package asakichy.第02章オブジェクトの生成と消滅;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目06廃れたオブジェクト参照を取り除く {
	@Test
	public void 廃れた参照とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("参照されることのないオブジェクトの参照を保持していること。").e();
		o.l2("メモリリークの原因となる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Stackクラスにおける廃れた参照。").e();

		stack = new Stack();
		Object o1 = new Object();
		Object o2 = new Object();
		stack.push(o1);
		stack.push(o2);
		assertThat(stack.pop(), is(o2));
		assertThat(stack.pop(), is(o1));
		// （外部動作は問題なし）
	}

	private Stack stack;

	private static class Stack {
		protected Object[] elements; // 便宜上 protected
		protected int size = 0;// 便宜上 protected
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
			return elements[--size]; // elements[size] が廃れた！
		}

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}
	}

	@Test
	public void 原因_独自のメモリ管理() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("配列を使って独自のメモリ管理を行う。").e();
		o.l2("配列の要素が無効かどうかプログラマにしかわからず、JVMが回収できない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("nullを代入。").e();

		o.l1("【たとえば？】").e();
		o.l2("Stackクラスにおける廃れた参照の改善。").e();
		stack = new Stack() {
			@Override
			public Object pop() {
				if (size == 0)
					throw new IllegalStateException();
				Object result = elements[--size];
				elements[size] = null; // 廃れた参照を取り除く！
				return result;
			};
		};
		Object o1 = new Object();
		Object o2 = new Object();
		stack.push(o1);
		stack.push(o2);
		assertThat(stack.pop(), is(o2));
		assertThat(stack.pop(), is(o1));
	}

	@Test
	public void 原因_キャッシュ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("キャッシュに保管したオブジェクトが、");
		o.__("キャッシュ外で意味がなくなったあとも、");
		o.__("キャッシュ内に居残り続ける。").e();

		o.l1("【どうすれば？】").e();
		o.l2("java.util.WeakHashMapで実装する。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.WeakHashMapによるキャッシュ機能。").e();
		Map<Integer, String> cash = new WeakHashMap<Integer, String>();
		Integer[] keys = new Integer[2];
		keys[0] = new Integer(0);
		keys[1] = new Integer(1);
		cash.put(keys[0], "val0");
		cash.put(keys[1], "val1");
		assertThat(cash.get(0), is("val0"));
		assertThat(cash.get(1), is("val1"));

		keys[0] = null; // 廃れた！
		System.gc();

		assertThat(cash.get(0), is(nullValue())); // 廃れたキーのデータは自動削除
		assertThat(cash.get(1), is("val1"));
	}

	@Test
	public void 原因_リスナやコールバック() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コールバックを登録し、解除し忘れて蓄積してしまう。").e();

		o.l1("【どうすれば？】").e();
		o.l2("java.util.WeakHashMapで実装する。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("メモリリークはJavaでも発生しうる。").e();
		o.l1("JVMの挙動を理解し、JDKの機能を使い、メモリリークを未然に防ぐこと。").e();
		o.l1("メモリリークの発見は、レビューやプロファイラで。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
