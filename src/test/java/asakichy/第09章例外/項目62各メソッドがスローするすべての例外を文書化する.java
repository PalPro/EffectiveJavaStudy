package asakichy.第09章例外;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目62各メソッドがスローするすべての例外を文書化する {

	@Test
	public void メソッド例外の文書化とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッドがスローする例外の説明は、").e();
		o.l2("そのメソッドを適切に使用するために、重要度の高い情報。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Javadocの@throwsを利用する。").e();
	}

	@Test
	public void チェックされる例外() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("チェックされる例外を「個々に」宣言し、スローされる条件を正確に文書化する。").e();

		o.l1("【どうして？】").e();
		o.l2("メソッドを使用する側が、正確にプログラミングするための情報を出す。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スローされる例外を決して「丸め」ない。").e();
		o.l2("throw Exception/throw Throwableは避ける。").e();
	}

	@Test
	public void チェックされない例外() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("チェックされない例外でも、チェックされる例外同様の文書化を行う。").e();
		o.l2("チェックされない例外を、インターフェイスにおいて文書化することは特に重要。").e();

		o.l1("【どうして？】").e();
		o.l2("事前条件の記述になる。").e();
		o.l2("一般契約の記述になる。").e();
		o.l2("プログラミングエラーの回避につながる情報。").e();

		o.l1("【どうすれば？】").e();
		o.l2("throws宣言には含めず、文書化だけ行う。").e();
		o.l3("APIのユーザが、チェックされる例外とチェックされない例外を、すぐに判別できるようにするため。").e();
	}

	@Test
	public void 簡便化_クラスコメントへ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ある例外が、同じ理由でクラス内の多くのメソッドによりスローされる場合には、");
		o.__("クラスのドキュメンテーションコメントにその例外を文書化してもよい。").e();

		o.l1("【たとえば？】").e();
		o.l2("よくある例は「java.lang.NullPointerException」。").e();
		/**
		 * このクラスのすべてのメソッドは、
		 * nullオブジェクト参照がいかなるパラメータに渡されても
		 * NullPointerExceptionをスローします。
		 */
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("各メソッドにおいて、スローする可能性のあるすべての例外を、個々に文書化する。").e();
		o.l1("チェックされる例外も、チェックされない例外も、すべて。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
