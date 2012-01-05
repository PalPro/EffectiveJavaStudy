package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目50他の型が適切な場所では文字列を避ける {

	@Test
	public void 文字列型とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("テキスト情報を表現する型。").e();
		o.l2("便利な型なので、本来の目的以外に使われることが多い。").e();

		o.l1("【どうすれば？】").e();
		o.l2("万能な文字列を使用するのではなく、適材適所な型を使用する。").e();
	}

	@Test
	public void 不適切_他の値型の代替() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("他の値型に対する代替としては貧弱。").e();

		o.l1("【どうして？】").e();
		o.l2("データは文字として入力されることが多いので、そのまま文字列で扱いがち。").e();
		o.l2("しかし、プログラム内の型は、そのデータの本質を表現するべき。").e();

		o.l1("【どうすれば？】").e();
		o.l2("データが数値なら、intやfloatに。").e();
		o.l2("データが「はい」「いいえ」なら、booleanに。").e();
	}

	@Test
	public void 不適切_列挙型の代替() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("文字列定数は、列挙型の代替としては貧弱すぎる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("enum型を使用する。").e();

		{
			/** 【補】 JDK7におけるswitch文String評価 */
			// JDK6までは、caseにintかenumしか使えなかったが、
			// JDK7からは文字列も使用できるようになった。

			// switch( str ) {
			// case "string": ....
		}
	}

	@Test
	public void 不適切_集合型の代替() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("文字列は、集合型に対する代替としては貧弱。").e();

		o.l1("【どうして？】").e();
		o.l2("解析に時間がかかる。").e();
		o.l2("バグ混入の可能性が高くなる。").e();
		o.l2("オブジェクト共通の機能が提供できなくなる。").e();
		o.l3("equals()やtoString()など。").e();

		o.l1("【たとえば？】").e();
		o.l2("「#」区切りの文字列で集合を表現。").e();
		String person = "asakichy#Japan"; // name#from
		String[] attr = person.split("#");
		assertThat(attr[0], is("asakichy"));
		assertThat(attr[1], is("Japan"));

		o.l1("【どうすれば？】").e();
		o.l2("クラスを使用する。").e();
		o.l2("コレクションフレームワークを使用する。").e();
	}

	@Test
	public void 不適切_ケイパビリティの代替() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("文字列は、ある機能やデータへのアクセスを得るためのキーの代替としては貧弱。").e();

		o.l1("【たとえば？】").e();
		o.l2("スレッドローカル変数クラスを自作することを考える。").e();

		o.l2("キーとして文字列を使用したThreadLocalStringクラス。").e();
		o.l3("一意性を保つのが大変。").e();
		o.l3("セキュリティも甘い。").e();

		o.l2("キーとしてケイパビリティを使用したThreadLocalCapabilityクラス。").e();
		o.l3("一意の偽造できないキーが生成できる。").e();

		{
			/** 【補】 スレッドローカル変数クラスの更なる改良 */
			// 実は、staticのメソッドは、もはや本当は必要ない。
			// それらは、代わりに、キーに対するインスタンスメソッドになることができるし、
			// その時点でキーはもはやスレッドローカル変数に対するキーではない。
			// つまり、それこそがスレッドローカル変数である。

			@SuppressWarnings("unused")
			class ThreadLocal {
				public ThreadLocal() {
				};

				public void set(Object value) {
					// ....
				};

				public Object get() {
					// ....
					return null;
				};
			}

			// そして、これをジェネリック化したものが、
			// ほぼjava.lang.ThreadLocalそのものである。
		}
	}

	@SuppressWarnings("unused")
	private static class ThreadLocalString {
		// インスタンス化不可能
		private ThreadLocalString() {
		};

		public static void set(String key, Object value) {
			// ....
		};

		public static Object get(String key) {
			// ....
			return null;
		};
	}

	@SuppressWarnings("unused")
	private static class ThreadLocalCapability {
		// インスタンス化不可能
		private ThreadLocalCapability() {
		};

		// ケイパビリティ
		public static class Key {
			Key() {
			};
		}

		// 一意の偽造できないキー生成
		public static Key getKey() {
			return new Key();
		}

		public static void set(Key key, Object value) {
			// ....
		};

		public static Object get(Key key) {
			// ....
			return null;
		};
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("思考停止で文字列型を選択しないで、より適切な型を検討する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
