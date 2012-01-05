package asakichy.第08章プログラミング一般;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目54ネイティブメソッドを注意して使用する {

	@Test
	public void ネイティブメソッドとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ＣやＣ++などのネイティブのプログラミング言語で書かれたメソッド。").e();
		o.l2("Javaからも、そのネイティブメソッドを呼び出すことができる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("JNI（Java Native Interface）を使用する。").e();
	}

	@Test
	public void 用途_プラットフォーム機構() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("レジストリーやファイルロックなどのプラットフォーム固有の機構へのアクセス。").e();

		o.l1("【どうすれば？】").e();
		o.l2("JDKの進化とともに、サポートされている機能もあるので、そちらを使うようにする。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.prefsパッケージは、レジストリへのアクセスを提供。").e();
		o.l2("java.awt.SystemTrayは、デスクトップのシステムトレイ領域へのアクセスを提供").e();
	}

	@Test
	public void 用途_パフォーマンス改善() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パフォーマンスが重要な部分をネイティブ言語で書く。").e();

		o.l1("【どうすれば？】").e();
		o.l2("現状、この用途での使用はまったく勧められない。").e();

		o.l1("【どうして？】").e();
		o.l2("JVMはすでに十分に高速である。").e();
		o.l3("ネイティブコードへの入出力のオーバーヘッドで、逆に遅くなるケースすらある。").e();
		o.l2("ネイティブコードは、プログラム全体に影響を与えるメモリ破壊の危険性がある。").e();
		o.l2("ネイティブコードは、プラットフォーム依存なので、せっかくJava使っているのに移植性が悪くなる。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("どうしても使わなければならない古いライブラリがあるときだけ、JNIを使うようにする。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
