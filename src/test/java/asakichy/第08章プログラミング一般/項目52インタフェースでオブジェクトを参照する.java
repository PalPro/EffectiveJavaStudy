package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目52インタフェースでオブジェクトを参照する {

	@Test
	public void オブジェクト参照について() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パラメータ、戻り値、変数、およびフィールドは、型を指定して宣言される。").e();
		o.l2("オブジェクトはその型を通して参照され、アクセスされる。").e();
	}

	@Test
	public void 原則_インターフェイス型を使用する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("型宣言にはインターフェイス型を使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("実装を切り替えることができるので、プログラムが柔軟になる。").e();
		o.l2("新しい機能を持った、よりよいパフォーマンスを持った実装が、後から出てくるのは常。").e();

		o.l1("【たとえば？】").e();
		o.l2("コレクションの型。").e();
		{
			// 具象クラスで受けている悪例
			Vector<Subscriber> subscribers = new Vector<Subscriber>();
			assertThat(subscribers.size(), is(0));
		}
		{
			// インターフェイスで受ける
			List<Subscriber> subscribers = new Vector<Subscriber>();
			assertThat(subscribers.size(), is(0));

			// 実装を切り替えても、周りのコードに影響を与えることはない。
			subscribers = new ArrayList<Subscriber>();
			assertThat(subscribers.size(), is(0));
		}

		{
			/** 【補】 実装切り替えの注意点 */
			// 周りのコードが、Vectorの「同期している」という特性に依存していた場合、
			// ArrayListへの置き換えは正しくない。
		}
	}

	private static class Subscriber {
		// dummy
	}

	@Test
	public void 例外_値クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("値クラスは、そもそも、適当なインタフェースが存在しない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Stringなど。").e();
		o.l2("java.util.Randomは、値クラスではないが、この部類に入る。").e();
	}

	@Test
	public void 例外_クラスに基づくフレームワーク() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インタフェースではなく、クラスであるフレームワークに、オブジェクトが属している。").e();

		o.l1("【どうすれば？】").e();
		o.l2("そのクラスの中でも、基底クラスで参照するのがベター。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Timer。").e();
	}

	@Test
	public void 例外_インターフェイスにない機能() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インターフェイスを実装しているが、そこにないメソッドを提供している。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.PriorityQueue。").e();
		o.l3("コレクションだが、キュー機能メソッドを提供している。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("プログラムの意味・意図を考え、インターフェイスで会話する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
