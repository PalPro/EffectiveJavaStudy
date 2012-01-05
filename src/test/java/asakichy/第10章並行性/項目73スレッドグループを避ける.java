package asakichy.第10章並行性;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目73スレッドグループを避ける {

	@Test
	public void スレッドグループとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("スレッドのグループ化を行うことにより、");
		o.__("そのスレッドグループに含まれるスレッドを一元管理することができるようになる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("java.lang.ThreadGroupを使用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("スレッドグループの作成と、スレッドの登録。").e();
		// threadGroupというスレッドグループの作成
		ThreadGroup threadGroup = new ThreadGroup("threadGroup");
		// ExThreadスレッドを生成し、ExThradGroupグループに含める。
		new Thread(threadGroup, "memberThread");
		assertThat(threadGroup.activeCount(), anything());
		assertThat(threadGroup.getName(), is("threadGroup"));
	}

	@Test
	public void 原則_使用しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("スレッドグループは「廃れている」ので、もう使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("提供されている操作は、非推奨か、滅多に使われないか。").e();
		o.l2("安全性が貧弱。").e();
		o.l3("スレッド一覧enumerateやactiveCountはスレッドをこぼす可能性アリ。").e();
		o.l3("サブグループ一覧にも欠陥あるが、修正されずに放置。").e();
		o.l2("ThreadGroup#uncaughtExeptionは有用だが、JDK5からはThread#setUncaughtExeptionHandlerにもある。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("ThreadGroupはもう無視してかまわない。").e();
		o.l1("グループ化したいならスレッドプールエグゼキュータを使えばよい。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
