package asakichy.第03章すべてのオブジェクトに共通のメソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目11cloneを注意してオーバーライドする {

	@Test
	public void cloneとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクト複製の仕組み。").e();

		o.l1("【どうすれば？】").e();
		o.l2("構成要素１").s("Object#clone()。").e();
		o.l3("オブジェクトの複製機能を持つ、Objectの「protected」メソッド。").e();
		o.l3("オーバライドして可視性を広げ、クライアントに使ってもらう。").e();
		o.l2("構成要素２").s("java.lang.Clonableインターフェイス。").e();
		o.l3("オブジェクトが複製を許可していることを宣伝する、マーカーインターフェイス。").e();
		o.l3("Object#clone()をオーバライドする場合は、このインターフェイスの実装は必須。").e();
		o.l3("clone()はあくまでObjectのメソッドであり、このインターフェイスにメソッドはない。").e();
	}

	@Test
	public void 実装手順() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("１").s("インターフェイスCloneableを実装。").e();
		o.l2("２").s("Object#clone()をオーバーライド。").e();
		o.l2("２").s("a").s("可視性をpublicに広げる。").e();
		o.l2("２").s("b").s("Object#clone()を呼び出す。").e();
		o.l2("２").s("c").s("戻り値をObjectから自身の型に狭める。（共変戻り値型）").e();

		{
			/** 【補】Object#clone()の実装 */
			// オブジェクトを複製する実処理は、自分で書かなくても、
			// Object#clone()を呼び出せば、勝手にやってくれる。
		}
		{
			/** 【補】共変戻り値型について */
			// オーバライド時、戻り値の型にサブクラスを指定できる。
			class NumbersImpl implements Numbers {
				// 共変なので、Numberのサブクラス（今回はInteger）が指定可能
				@Override
				public Integer get() {
					return Integer.valueOf(1);
				}
			}
			assertThat(new NumbersImpl().get(), isA(Integer.class));
		}

		o.l1("【たとえば？】").e();
		o.l2("Personクラス。").e();
		Person clonable = new Person(20);
		Person cloned = clonable.clone();
		assertThat(cloned.getAge(), is(20));
	}

	interface Numbers {
		Number get();
	}

	class Person implements Cloneable {

		private int age;

		public int getAge() {
			return age;
		}

		public Person(int age) {
			this.age = age;
		}

		// Obejct#cloneのシグニチャ
		// protected Object clone() throws CloneNotSupportedException
		// この可視性、例外を緩和する

		@Override
		public Person clone() {
			try {
				return (Person) super.clone();// 複製実処理はObject#cloneに任せる
			} catch (CloneNotSupportedException e) {
				throw new AssertionError();// 絶対発生しない
			}
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + age;
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Person)) {
				return false;
			}
			Person p = (Person) o;
			return p.age == age;
		}

	}

	@Test
	public void 一般契約_別インスタンス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複製先は、別のオブジェクトでなければならない。").e();
		o.l2("x.clone() != x。").e();

		o.l1("【たとえば？】").e();
		o.l2("Personクラス。").e();
		Person org = new Person(20);
		Person cloned = org.clone();
		assertThat(cloned, is(not(sameInstance(org))));
	}

	@Test
	public void 一般契約_同クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複製先は、同じクラスのオブジェクトでなければならない。").e();
		o.l2("x.clone().getClass() == x.getClass()。").e();

		o.l1("【たとえば？】").e();
		o.l2("Personクラス。").e();
		Person org = new Person(20);
		Person cloned = org.clone();
		assertThat(cloned.getClass().getName(), is(org.getClass().getName()));
	}

	@Test
	public void 一般契約_同値() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複製先は、論理的に同値のオブジェクトでなければならない。").e();
		o.l2("x.clone().equals(x)").e();

		o.l1("【たとえば？】").e();
		o.l2("Personクラス。").e();
		Person org = new Person(20);
		Person cloned = org.clone();
		assertThat(cloned, is(org));
	}

	@Test(expected = CloneNotSupportedException.class)
	public void 定石_Cloneable実装とcloneオーバライドはセット() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("cloneオーバライドしたら、必ずCloneableを実装しなければならない。").e();

		o.l1("【どうして？】").e();
		o.l2("Clonableを実装し忘れたクラスは、");
		o.__("cloneが呼び出されると、例外が発行されてしまう。").e();

		/** 【例】 */
		o.l1("【たとえば？】").e();
		o.l2("PersonForgotClonableクラス。").e();
		PersonForgotClonable noClonable = new PersonForgotClonable(20);
		PersonForgotClonable cloned = noClonable.clone(); // 例外発生
		assertThat(cloned.getAge(), is(20));
	}

	class PersonForgotClonable /* implements Cloneable */{

		private int age;

		public int getAge() {
			return age;
		}

		public PersonForgotClonable(int age) {
			this.age = age;
		}

		@Override
		public PersonForgotClonable clone() throws CloneNotSupportedException {
			return (PersonForgotClonable) super.clone();
		}
	}

	@Test
	public void 定石_finalでないクラスならsuperのcloneを返す() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("継承を許す設計のクラスは、super#clone()を呼び出すこと。").e();

		o.l1("【どうして？】").e();
		o.l2("super#clone()を呼び出すことこそが、");
		o.__("スーパークラスがサブクラスのインスタンスを作成する唯一無二の方法。").e();

		o.l1("【どうすれば？】").e();
		o.l2("super#cloneの呼び出しをチェインして、最終的にObject#cloneを呼びだす。").e();
		o.l2("クラス階層の途中で、cloneの連鎖を連鎖を断ち切らないこと。").e();
		o.l2("自分勝手にコンストラクタでオブジェクトを生成するクラスがいちゃダメだし、");
		o.__("Clonableを持つクラス階層にcloneをオーバライドしていないクラスがいちゃダメ。").e();
	}

	@Test
	public void 定石_可変オブジェクト参照含むなら深いコピー() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトが可変オブジェクトを参照しているフィールドを保持していたら、");
		o.__("cloneがオブジェクトの不変式を壊してしまう。").e();

		o.l1("【たとえば？】").e();
		o.l2("Stackクラスはそのままだと浅いコピー。").e();
		String input = "a";
		String output = "";
		Stack stack = null;

		stack = new Stack() {
			@Override
			public Stack clone() {
				return (Stack) super.clone();
			}
		};
		stack.push(input);
		Stack clonedStack = stack.clone();

		// オリジナルをテスト
		assertThat(stack.isEmpty(), is(false));
		output = (String) stack.pop();
		assertThat(output, is(input));
		assertThat(stack.isEmpty(), is(true));

		// 複製にも同じテスト
		assertThat(clonedStack.isEmpty(), is(false));
		output = (String) clonedStack.pop();
		// assertThat(pop, is(input));
		assertThat(output, is(not(input)));// 不変式が破壊！
		assertThat(clonedStack.isEmpty(), is(true));

		o.l2("改造して、再帰的cloneを行う。").e();
		stack = new Stack() {
			@Override
			public Stack clone() {
				Stack result = (Stack) super.clone();
				// 再帰的clone
				// （ただし、フィールドがfinalだとこの方法使えない。）
				result.elements = elements.clone();
				return result;
			}
		};
		stack.push(input);
		clonedStack = stack.clone();

		// オリジナルからpop
		assertThat(stack.isEmpty(), is(false));
		output = (String) stack.pop();
		assertThat(output, is(input));
		assertThat(stack.isEmpty(), is(true));

		// 複製からpop
		assertThat(clonedStack.isEmpty(), is(false));
		output = (String) clonedStack.pop();
		assertThat(output, is(input));
		assertThat(clonedStack.isEmpty(), is(true));
	}

	private static class Stack implements Cloneable {
		protected Object[] elements;
		protected int size = 0;
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
		};

		public boolean isEmpty() {
			return size == 0;
		}

		@Override
		public Stack clone() {
			try {
				return (Stack) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new AssertionError();
			}
		}

		private void ensureCapacity() {
			if (elements.length == size) {
				elements = Arrays.copyOf(elements, 2 * size + 1);
			}
		}

	}

	@Test
	public void 定石_可変オブジェクト参照をネストして含むならさらに深いコピー() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("再帰的cloneを行っても、");
		o.__("その先に更なる可変オブジェクトへの参照があった場合は、");
		o.__("やはり、cloneがオブジェクトの不変式を壊してしまう。").e();

		o.l1("【どうすれば？】").e();
		o.l2("cloneは「もうひとつのコンストラクタ」なので、以下を保証しなくてはならない。").e();
		o.l3("元のオブジェクトに対して何も害を及ぼしていないこと。").e();
		o.l3("複製先に対して状態の不変式を適切に確立していること。").e();
		o.l2("サブクラスのオーバライドで不整合になる可能性が高いので、");
		o.__("コンストラクタ同様、finalでないメソッドを呼び出すべきではない。").e();

		o.l1("【たとえば？】").e();
		o.l2("HashTableクラスにおいて、");
		o.__("仮にバケットの配列をコピーしても、配列の要素はLinkedListなので、");
		o.__("リンク先情報は複製元と複製先で共有してしまう。").e();
		o.l2("よって、リンクリストまで個別にコピー。").e();
		@SuppressWarnings("unused")
		class HashTable implements Cloneable {
			private Entry[] buckets = new Entry[10];

			class Entry {
				final Object key;
				Object value;
				Entry next;

				public Entry(Object key, Object value, Entry next) {
					this.key = key;
					this.value = value;
					this.next = next;
				}

				Entry deepCopy() {
					// このエントリからつながっているリンクを再帰的にコピー
					return new Entry(key, value, next == null ? null : next.deepCopy());
				}
			}

			@Override
			public HashTable clone() {
				try {
					HashTable result = (HashTable) super.clone();
					result.buckets = new Entry[buckets.length];
					for (int i = 0; i < buckets.length; i++) {
						if (buckets[i] != null) {
							result.buckets[i] = buckets[i].deepCopy();
						}
					}
					return result;
				} catch (CloneNotSupportedException e) {
					throw new AssertionError();
				}
			}
		}
	}

	@Test
	public void 定石_例外は隠蔽() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("cloneのオーバライド時には、元のシグニチャにあるCloneNotSupportedExceptionを削る。").e();

		o.l1("【どうして？】").e();
		o.l2("チェック例外を持たないメソッドの方が使いやすい。").e();

		o.l1("【どうすれば？】").e();
		o.l2("ただし、finalじゃないなら、Object#cloneのシグニチャを完コピして、");
		o.__("継承先に委ねるのがベター。").e();
	}

	@Test
	public void 定石_クラスがスレッドセーフなら追随する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Object#cloneはスレッドセーフではない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スレッドセーフ要件なクラスでは、cloneの実装も適切に同期する。").e();
	}

	@Test
	public void 定石_IDやシリアル番号は変更() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複製したオブジェクトにユニーク必須の情報があれば、");
		o.__("そこまで完全にコピーしてはシステムに不整合が生じる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("別途採番したものを格納してから返すことを忘れずに。").e();
	}

	@Test
	public void 代替_コピーコンストラクタないしstaticファクトリメソッド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("フレームワークに要求されている等、特別な要件がなければ、");
		o.__("コピーコンストラクタやstaticファクトリメソッドを使用する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("比較にならないほどシンプルになる。").e();

		o.l1("【たとえば？】").e();
		Book org = new Book("Effective Java");
		Book byCopyConstructor = new Book(org);
		Book byStaticFactory = Book.newInstance(org);
		assertThat(byCopyConstructor.getTitle(), is(org.getTitle()));
		assertThat(byStaticFactory.getTitle(), is(org.getTitle()));
	}

	private static class Book {
		private String title;

		public String getTitle() {
			return title;
		}

		public Book(String title) {
			this.title = title;
		}

		// コピーコンストラクタ
		public Book(Book book) {
			title = book.title;
		}

		// staticファクトリメソッド
		public static Book newInstance(Book book) {
			return new Book(book);
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("cloneによる複製の実現は「とてつもなく複雑」である。").e();
		o.l1("代替手段を使うか、複製の手段を提供しないほうが賢明である。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
