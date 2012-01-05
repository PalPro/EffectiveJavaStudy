package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目47ライブラリーを知りライブラリーを使う {

	@Test
	public void ライブラリを使用する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("標準ライブラリを使用すること。").e();

		o.l1("【どうして？】").e();
		o.l2("ライブラリを提供した専門家の知識と、それを使用した人々の経験を利用することができる。").e();
		o.l2("（場当たり的な）ライブラリ作成に費やす時間を、アプリケーションに費やせる。").e();
		o.l2("（自分では何もしなくても、）時間とともにバグや機能やパフォーマンスが改善されていく。").e();
		o.l2("ライブラリに基礎処理を任せられれば、ビジネスロジックが読みやすくなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("問題のある自作乱数関数random()。").e();
		// １．nが２の小さな累乗であれば、生成される乱数列はかなり短い期間の後に繰り返される。
		// ２．nが２の累乗でなければ、平均していくつかの数が他の数よりも頻繁に返される。
		// ３．万が一、nextInt()がInterger.MIN_VALUEを返した場合、意図しない結果になる。
		assertThat(random(100), anything());

		{
			/** 【補】Math.abs(Interger.MIN_VALUE)の結果 */
			// Interger.MIN_VALUEは「-2147483648」で、その絶対値は「2147483648」となるハズ。
			// しかし、intの範囲は-2147483648～2147483647なので、「2147483648」は範囲外。
			// よって、Math.abs(Interger.MIN_VALUE)の結果は、仕様にあるとおり、
			// そのままInterger.MIN_VALUEが返ってくる。
			// http://java.sun.com/javase/ja/6/docs/ja/api/java/lang/Math.html#abs%28int%29
		}

		o.l2("専門家の知識が詰まったjava.util.Random#nextInt()。").e();
		// 擬似乱数発生法、整数論、２の補数算術、etc... を「とりあえず」知る必要はない
		assertThat(rnd.nextInt(100), anything());
	}

	private static final Random rnd = new Random();

	private static int random(int n) {
		return Math.abs(rnd.nextInt()) % n;
	}

	@Test
	public void ライブラリを知る() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("標準ライブラリを知っていること。").e();

		o.l1("【どうして？】").e();
		o.l2("知らないものは使えない。").e();
		o.l2("意図しない形で車輪の再発明をしてしまう。").e();

		o.l1("【どうすれば？】").e();
		o.l2("最低限「java.lang」「java.util」「java.io」パッケージは抑えておく。").e();
		o.l2("主要リリース毎に、ライブラリに追加されたものを調べるようにする。").e();

		o.l1("【たとえば？】").e();
		o.l2("JDKの進化とともに追加になった以下の機能は、プログラマの道具箱に常備すべき。").e();
		o.l3("java.utilパッケージのコレクションフレームワーク。").e();
		o.l3("java.util.concurrentパッケージのコンカレンシーユーティリティ。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("正しい努力をするために、まずライブラリを知っておく。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
