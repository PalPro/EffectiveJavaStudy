package asakichy.第10章並行性;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目72スレッドスケジューラに依存しない {

	@Test
	public void スレツドスケジューラとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複数のスレッドが実行可能である場合、どのスレッドをどのくらいの時間実行させるかを決めるもの。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スレツドスケジューラに依存したプログラムを書いてはいけない。").e();
		o.l2("スケジューラに影響を与えようとするAPIの使用も控える。").e();

		o.l1("【どうして？】").e();
		o.l2("オペレーティングシステムによって、時間配分のポリシーは異なる。").e();
		o.l2("ポリシーに依存すると、移植可能でなくなる。").e();
	}

	@Test
	public void 指針_スレッド数を少なく() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("実行可能なスレッドの平均数が、プロセッサの数よりも大きくならないことを保証する。").e();

		o.l1("【どうして？】").e();
		o.l2("スレッドスケジューラの出番がほとんど無くなるので。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スレッドが有益な処理を行っていない場合に、スレッドを動作させない。").e();
		o.l2("スレッドプールを適切な大きさにして、スレッドのタスクを適度に小さくする。").e();
	}

	@Test
	public void 指針_ビジーウェイトを行わない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("何かが起きるのを待つために、共有されたオブジェクトを繰り返し検査する").e();
		o.l2("「ビジーウェイト」を行わない。").e();

		o.l1("【どうして？】").e();
		o.l2("スケジューラの予想のつかない処理に対してプログラムを脆弱にする").e();
		o.l2("プロセッサへの負荷をかなり増大させて、他のスレッドができる有益な処理の量を減らす。").e();

		o.l1("【たとえば？】").e();
		o.l2("CountDownLatchの再実装であるSlowCountDownLatchクラス。").e();

		final SlowCountDownLatch latch = new SlowCountDownLatch(2);
		new Thread(new Runnable() {
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
				}
				latch.countDown();
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
				}
				latch.countDown();
			}
		}).start();

		latch.await(); // CPUが100％になる。。。

		assertThat(latch.count, is(0));
	}

	private static class SlowCountDownLatch {
		private int count;

		public SlowCountDownLatch(int count) {
			if (count < 0) {
				throw new IllegalArgumentException(count + " < 0");
			}
			this.count = count;
		}

		public void await() {
			while (true) { // 絶え間のない、ビジーウェイト
				synchronized (this) {
					if (count == 0) {
						return;
					}
				}
			}
		}

		public synchronized void countDown() {
			if (count != 0) {
				count--;
			}
		}
	}

	@Test
	public void 指針_yieldを使わない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("あるスレッドが動作しないからといって、Thread#yield()の呼び出しで調整してはならない。").e();

		{
			/** 【補】yieldとは */
			// java.lang.Threadクラスのstaticメソッドで、
			// 現在実行中のスレッドオブジェクトを一時的に休止させ、
			// ほかのスレッドが実行できるようにする目的を持つ。
		}

		o.l1("【どうして？】").e();
		o.l2("JVMの実装に依存し、移植不可能なプログラムとなってしまう。").e();

		o.l1("【どうすれば？】").e();
		o.l2("アプリケーションを再構築して、並行して実行可能になるスレッドの数を減らす。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("スレッドスケジュールの（小手先の）「調整」をしてはいけない。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
