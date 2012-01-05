package asakichy.第05章ジェネリックス;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目29型安全な異種コンテナーを検討する {

	@Test
	public void 型安全異種コンテナーとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ひとつのコンテナで、複数の型の要素を、型安全を保証しつつ取り扱う。").e();

		o.l1("【どうして？】").e();
		o.l2("単一要素を取り扱うコンテナではまかないきれない、柔軟性が必要な場合がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("データベースの行は任意の数の多くの列を持っており、");
		o.__("型安全な方法でそれらすべての列をアクセスしたい。").e();
	}

	@Test
	public void 実装() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンテナーの代わりにキーをパラメータ化する。").e();
		o.l2("値の出し入れのため、コンテナーに対して、パラメータ化されたキーを渡す。").e();
		o.l2("値の型がそのキーと一致することを保証するために、ジェネリックを使用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("Favoritesクラス。").e();
		Favorites f = new Favorites();
		f.putFavorite(String.class, "Java");
		f.putFavorite(Integer.class, 0xcafebabe);
		f.putFavorite(Class.class, Favorites.class);

		String favoriteString = f.getFavorite(String.class);
		int favoriteInteger = f.getFavorite(Integer.class);
		Class<?> favoriteClass = f.getFavorite(Class.class);

		assertThat(favoriteString, is("Java"));
		assertThat(favoriteInteger, is(0xcafebabe));
		assertThat(favoriteClass.getName(), is(Favorites.class.getName()));

		{
			/** 【補】型トークンとは */
			// クラスリテラルが、コンパイル時と実行時の型情報を伝えるため、
			// メソッドに渡された場合のこと。
			// クラスリテラルString.classの型はClass<String>で、
			// クラスリテラルInterger.classの型はClass<Integer>になる。
		}
	}

	private static class Favorites {
		// 「キーの型」がワイルドカード
		// Class<String>でも、Class<Integer>でも、キーに使える
		protected Map<Class<?>, Object> favorites = new HashMap<Class<?>, Object>();

		public <T> void putFavorite(Class<T> type, T instance) {
			if (type == null) {
				throw new NullPointerException("Type is null");
			}
			favorites.put(type, instance);
		}

		public <T> T getFavorite(Class<T> type) {
			// 値はObject型。
			// だけど、キーが、「キー」であり、かつ「値の型情報」を持っているので、
			// 値を安全にキャストできる。
			return type.cast(favorites.get(type));
		}
		// ちなみにcastメソッドのシグニチャ
		// Class<T>{
		// T cast( Object o );
		// }
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void 制限_原型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Classオブジェクトを原型で使用すれば、型危険になる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Favoritesクラスに原型キーで登録。").e();
		Favorites f = new Favorites();

		// クライアントが悪意を持てば、型安全は壊せる。
		// ただし、警告はきちんと出ているので、
		// 原型使用したときのコレクションと実装は違わない。
		Class StringClass = String.class;
		f.putFavorite(StringClass, 0xcafebabe);

		{
			/** 【補】実行時型安全の保証 */
			// Favorites#putFavorite()の時点で動的キャストを実行すれば、
			// コストはかかるが、実行時型安全が保証される。
			@SuppressWarnings("unused")
			Favorites fav = new Favorites() {
				@Override
				public <T> void putFavorite(Class<T> type, T instance) {
					if (type == null) {
						throw new NullPointerException("Type is null");
					}
					favorites.put(type, type.cast(instance));
				};
			};
			// この方法は、java.util.Collections#checkedMap()などで実際使用されている。
		}
	}

	@Test
	public void 制限_具現化不可能型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("具現化不可能クラスを使用できない。").e();
		o.l2("StringやString[]は登録できるが、List<String>は登録できない。>").e();

		o.l1("【どうして？】").e();
		o.l2("List<String>のClassオブジェクトは取得できない。").e();
		o.l2("List<String>.classは文法エラーになる。").e();
	}

	@Test
	public void 型を制御() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッドに渡す型を制限したい場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("境界型トークンを導入する。").e();
		o.l2("境界型パラメータか、境界ワイルドカードを利用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("アノテーションAPIは境界型トークンを多用。").e();
		o.l2("java.lang.reflect.AnnotatedElement#getAnnotation()など。").e();

		{
			/** 【補】 getAnnotation()のシグニチャ */
			// AnnotatedElementはインターフェイスで、getAnnotation()シグニチャは以下の通り。
			// <T extends Annotation> T getAnnotation(Class<T> )

			// 引数annotationClassは、アノテーション型を表す境界型トークン。
			// 戻りは、要素に付けられているその型のアノテーション。
		}

		{
			/** 【補】 Class<?> を 境界型トークンに渡す方法 */
			// Class#asSubclass()を使用して、Classオブジェクトを、
			// 引数で表されるクラスのサブクラスにキャストする。
			// サンプル：指定されたアノテーションを探すsearchAnnotation()関数。

			// クラスそのものに＠Deprecatedが付与されているクラス。
			// http://java.sun.com/javase/ja/6/docs/ja/api/deprecated-list.html
			String className = "java.rmi.ServerRuntimeException";
			// 探すアノテーションはDeprecated
			String annotationTypeName = "java.lang.Deprecated";

			Class<?> klass = Class.forName(className);
			Annotation annotation = searchAnnotation(klass, annotationTypeName);
			assertThat(annotation, is(instanceOf(Deprecated.class)));
		}
	}

	private static Annotation searchAnnotation(AnnotatedElement element, String annotationTypeName) {
		Class<?> annotationType = null;
		try {
			annotationType = Class.forName(annotationTypeName);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
		return element.getAnnotation(annotationType.asSubclass(Annotation.class));
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ひとつのコンテナで、複数の型を安全に管理できる。").e();
		o.l1("そのための方法は、キーにClassオブジェクト（＝型トークン）を使用することである。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
