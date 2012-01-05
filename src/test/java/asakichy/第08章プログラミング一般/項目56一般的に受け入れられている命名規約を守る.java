package asakichy.第08章プログラミング一般;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目56一般的に受け入れられている命名規約を守る {

	@Test
	public void 命名規約とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("活字的命名規約と文法的命名規約がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Java言語仕様を参照する。").e();
		o.l2("http://www.oracle.com/technetwork/java/codeconv-138413.html").e();
	}

	@Test
	public void 活字的命名規約() throws Exception {
		o.l1("【どういうこと？】").e();

		o.l2("パッケージ名。").e();
		o.l3("ピリオド区切りの階層。").e();
		o.l3("小文字。").e();
		o.l3("8文字以内目安。").e();
		o.l3("認知された省略形、頭文字は可。").e();

		o.l2("クラス名・インターフェイス名・enum名・アノテーション名。").e();
		o.l3("最初大文字").e();
		o.l3("キャメルが一般的。").e();
		o.l3("省略形は避ける。").e();

		o.l2("メソッド名・フィールド名。").e();
		o.l3("最初小文字。").e();
		o.l3("ローカル変数は省略形可。").e();
		o.l3("コンテキストがしっかりしてれば短い名前可。").e();
		o.l3("定数フィールドは、全部大文字で、アンダースコア区切り。").e();

		o.l2("型パラメータ名。").e();
		o.l3("T").s("任意。").e();
		o.l3("K/V").s("マップのキーと値。").e();
		o.l3("X").s("例外。").e();

		o.l1("【たとえば？】").e();
		o.l2("パッケージ").s("com.google.inject、org.joda.time.format。").e();
		o.l2("クラス・インタフェース").s("Timer、FutureTask、LinkedHashMap、HttpServlet。").e();
		o.l2("メソッド、フィールド").s("remove、ensureCapacity、getCrc。").e();
		o.l2("定数フィールド").s("VALUES、NEGATIVE_INFINITY。").e();
		o.l2("ローカル変数").s("i、xref、houseNumber。").e();
		o.l2("型パラメータ").s("T、E、K、V、X、T1、T2。").e();
	}

	@Test
	public void 文法的命名規約() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラス名・enum名。").e();
		o.l3("名詞。").e();
		o.l2("インターフェイス名。").e();
		o.l3("名詞、形容詞。").e();
		o.l2("アノテーション名。").e();
		o.l3("自由。").e();
		o.l2("メソッド名。").e();
		o.l3("動詞。").e();
		o.l2("フィールド名。").e();
		o.l3("（公開されていないので）自由。").e();

		o.l1("【どうすれば？】").e();
		o.l2("特にメソッド名は慣例が多い。").e();
		o.l3("boolean返すヤツは、is～/has～。").e();
		o.l3("属性を返すヤツは、get～が多いが、名詞だけの場合もある。").e();
		o.l3("変換メソッドは、to～。").e();
		o.l3("ビューを返すヤツは、as～。").e();
		o.l3("基本型返すヤツは、～Value。").e();
		o.l3("staticファクトリは、valueOf/of/getInstance/newInstance。").e();

		o.l1("【たとえば？】").e();
		o.l2("クラス").s("Timer、BufferedWriter、Chess。").e();
		o.l2("インターフェイス").s("Collection、Comparator、Runable、Iterable、Accessible。").e();
		o.l2("アノテーション").s("BindingAnnotation、Inject、ImplementedBy、Singleton。").e();
		o.l2("メソッド").s("append、isDigit、hasSiblings、getTime、setTime、size、toString、asList、intValue、valueOf。").e();
		o.l2("フィールド").s("initialized、composite、height、digits、bodyStyle。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("標準の命名規約を知り、みんなに読みやすい違和感のないプログラムへ。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
