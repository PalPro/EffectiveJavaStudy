package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目53リフレクションよりインタフェースを選ぶ {

	@Test
	public void リフレクションとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("java.lang.reflectパッケージ。").e();
		o.l2("ロードされたクラスに関する情報について、プログラムによるアクセスが可能になる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("あるClassインスタンスが与えられれば、");
		o.__("そのクラスのコンストラクタ・メソッド・フィールドを表している");
		o.__("Constructorインスタンス・Methodインスタンス・Fieldインスタンスを取得できる。").e();
		o.l3("そこから、クラスのメンバー名・フィールド型・メソッドのシグニチャなどの情報が取得できる。").e();
		o.l3("そこから、実際の対象物を反射的に実行できる。");

		o.l1("【たとえば？】").e();
		o.l2("String#length()のリフレクション経由呼び出し。").e();

		String message = "message";
		Method method = message.getClass().getMethod("length", (Class[]) null);
		Object ret = method.invoke(message, (Object[]) null);
		assertThat(ret.toString(), is("7"));
	}

	@Test
	public void 代償_コンパイル時の型検査() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("例外の検査も含めて、コンパイル時の型検査の恩恵をすべて失う。").e();

		o.l1("【どうして？】").e();
		o.l2("実行時例外の危険性が高まる。").e();
	}

	@Test
	public void 代償_保守性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("リフレクションによるアクセスを行うコードは、ぎこちなく、冗長。").e();

		o.l1("【どうして？】").e();
		o.l2("書くのも読むのも大変。").e();
	}

	@Test
	public void 代償_パフォーマンス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パフォーマンスが悪くなる。").e();

		o.l1("【どうして？】").e();
		o.l2("通常のメソッド呼び出しより、かなり多くのオーバヘッドがある。").e();
	}

	@Test
	public void 使用_デザインツール() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンポーネントに基づくアプリケーションビルダーツールで使用。").e();

		o.l1("【どうして？】").e();
		o.l2("リフレクションは、そもそもこの設計ツールのための機能。").e();
	}

	@Test
	public void 使用_サービスプロバイダ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンパイル時に存在しないクラスを、実行時にロードするために使用。").e();
		o.l2("実行時にリフレクションを使用する、ほとんど唯一といっていいケース。").e();

		o.l1("【どうすれば？】").e();
		o.l2("インターフェイスを使用してロジックを書いておく。").e();
		o.l2("そのインターフェイスの実装クラスを、実行時リフレクションでロードする。").e();

		o.l1("【たとえば？】").e();
		o.l2("実行時にコレクション（Set）実装を選択できるcreateStringSet()関数。").e();
		Set<String> hashSet = createStringSet("java.util.HashSet", "2", "1", "3", "1");
		assertThat(hashSet, containsInAnyOrder("1", "2", "3"));
		Set<String> treeSet = createStringSet("java.util.TreeSet", "2", "1", "3", "1");
		assertThat(treeSet, contains("1", "2", "3"));
	}

	@SuppressWarnings("unchecked")
	private static Set<String> createStringSet(String setImpl, String... strings) {
		Class<?> cl = null;
		try {
			cl = Class.forName(setImpl);
		} catch (ClassNotFoundException e) {
			return null;
		}

		Set<String> s = null;
		try {
			s = (Set<String>) cl.newInstance();
		} catch (IllegalAccessException e) {
			return null;
		} catch (InstantiationException e) {
			return null;
		}

		s.addAll(Arrays.asList(strings));
		return s;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("リフレクションは強力だが、パフォーマンス悪化や例外発生の危険性を考慮して濫用しない。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
