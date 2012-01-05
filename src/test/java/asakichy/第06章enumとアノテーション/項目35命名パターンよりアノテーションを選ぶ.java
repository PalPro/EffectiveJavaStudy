package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目35命名パターンよりアノテーションを選ぶ {

	@Test
	public void アノテーションとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("プログラム（構文）要素に付加する「マーカー」。").e();
		o.l2("他のツールへの通知に使用する。").e();
		o.l3("それ自身が何かを行うのではなく、他のツールが読み取って、何らかの処理を行う。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Override。").e();
		o.l2("スーパークラスのメソッドをオーバーライドしている「はず」であることを明示。").e();
		o.l2("オーバーライドしていなければ、コンパイラがそれを読み取り、エラーを出力。").e();
		new Object() {
			@Override
			public String toString() {
				return super.toString();
			}
		};
	}

	@Test
	public void ツールへの通知_命名パターン() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ツールやフレームワークへの特別な処理の要求に、命名規則を使用する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("短所が多いので使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("誤脱字を検知しない。").e();
		o.l2("適切なプログラム（構文）要素にだけ適用することを保証する術がない。").e();
		o.l2("プログラム（構文）要素にパラメータを関連付ける術がない。").e();

		o.l1("【たとえば？】").e();
		o.l2("テスティングフレームワークJUnit3。").e();
		o.l2("「test」で開始されるメソッド名をテスト対象メソッドとする。").e();
		o.l2("JUnit4でアノテーションに変更された。").e();
	}

	@Test
	public void ツールへの通知_アノテーション() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ツールやフレームワークへの特別な処理の要求に、アノテーションを使用する。").e();

		o.l1("【どうすれば？】").e();
		o.l2("命名規則は使用せず、アノテーションを使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("命名規則の短所を、すべて改善している。").e();

		o.l1("【たとえば？】").e();
		o.l2("JUnitの@Testを模した、@MyTest/@MyExceptionTest。").e();
		MyTestResult result;

		result = runMyTest(MyTestTarget.class);
		assertThat(result.passed, is(1));
		assertThat(result.failed, is(3));

		{
			/** 【補】 マーカーアノテーション */
			// @MyTestのように、パラメータを持っておらず、
			// 単にアノテーションが付けられた要素を「マーク」しているだけのアノテーションは、
			// マーカーアノテーションと呼ばれる。
		}

		result = runMyTest(MyTestExceptionTarget.class);
		assertThat(result.passed, is(2));
		assertThat(result.failed, is(2));
	}

	@SuppressWarnings("unused")
	private static class MyTestTarget {
		@MyTest
		public static void m1() {
			// 成功1
		}

		public static void m2() {
			// 対象外1
		}

		@MyTest
		public static void m3() {
			// 失敗1
			throw new RuntimeException("Boom");
		}

		public static void m4() {
			// 対象外2
		}

		@MyTest
		public void m5() {
			// 失敗2（不正：インスタンスメソッドに付加している）
		}

		public static void m6() {
			// 対象外3
		}

		@MyTest
		public static void m7() { // Test should fail
			// 失敗3
			throw new RuntimeException("Crash");
		}

		public static void m8() {
			// 対象外4
		}
	}

	@SuppressWarnings("unused")
	private static class MyTestExceptionTarget {
		@MyExceptionTest(ArithmeticException.class)
		public static void m1() {
			// 成功1
			int i = 0;
			i = i / i;
		}

		@MyExceptionTest(ArithmeticException.class)
		public static void m2() {
			// 失敗1（Exceptionが異なる）
			int[] a = new int[0];
			int i = a[1];
		}

		@MyExceptionTest(ArithmeticException.class)
		public static void m3() {
			// 失敗2（Exceptionが発生しない）
		}

		@MyExceptionTest({ IndexOutOfBoundsException.class, NullPointerException.class })
		public static void doublyBad() {
			// 成功2
			// （仕様として、IndexOutOfBoundsExceptionかNullPointerExceptionが許可されている）
			List<String> list = new ArrayList<String>();
			list.addAll(5, null);
		}
	}

	// アノテーション仕様
	// パラメータなしのstaticのメソッドに対してだけ使用可。
	// （「static」という部分はコンパイル時に制御できず、あくまで「仕様」。）

	@Retention(RetentionPolicy.RUNTIME)
	// 実行時に保持
	@Target(ElementType.METHOD)
	// メソッドのみに付与
	public @interface MyTest {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MyExceptionTest {
		// パラメータ付き
		// @MyExceptionTest(ArithmeticException.class)のように書くと、それが取得できる。
		Class<? extends Exception>[] value();
	}

	private static MyTestResult runMyTest(Class<?> testClass) throws ClassNotFoundException {
		int tests = 0;
		int passed = 0;
		for (Method m : testClass.getDeclaredMethods()) {
			if (m.isAnnotationPresent(MyTest.class)) {
				tests++;
				try {
					m.invoke(null);
					passed++;
				} catch (InvocationTargetException wrappedExc) {
					// テスト失敗
				} catch (Exception exc) {
					// 不正なテスト
				}
			}

			if (m.isAnnotationPresent(MyExceptionTest.class)) {
				tests++;
				try {
					m.invoke(null);
					// ここを通過してもテスト失敗（例外が発生しなかった）
				} catch (Throwable wrappedExc) {
					Throwable exc = wrappedExc.getCause();
					Class<? extends Exception>[] excTypes = m.getAnnotation(MyExceptionTest.class).value();
					int oldPassed = passed;
					for (Class<? extends Exception> excType : excTypes) {
						if (excType.isInstance(exc)) {
							passed++;
							break;
						}
					}
					if (passed == oldPassed) {
						// テスト失敗（正しい例外が発生しなかった）
					}
				}
			}
		}
		return new MyTestResult(passed, tests - passed);
	}

	private static class MyTestResult {
		public final int passed;
		public final int failed;

		public MyTestResult(int passed, int failed) {
			this.passed = passed;
			this.failed = failed;
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("まず、積極的にアノテーションの利用者になる。").e();
		o.l1("ツール開発時であれば、利用者とのコミュニケーション方法に、アノテーションを検討する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
