package asakichy.第10章並行性;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目66共有された可変データへのアクセスを同期する {

	@Test
	public void 同期とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("不整合なオブジェクトの状態を見ないことを保証。").e();
		o.l2("同じロックで保護されていた以前のすべての変更の結果を見えることを保証。").e();

		o.l1("【どうすれば？】").e();
		o.l2("言語に組み込まれている同期機構である「synchronized」を使用する。").e();
		o.l3("１つのスレッドだけがある時点で１つのメソッドやブロックを実行していることを保証。").e();
		o.l2("メモリモデルを理解する。").e();
		o.l3("スレッドによる変更が、他のスレッドにいつどのように見えるか規定している言語仕様。").e();
	}

	@Test
	public void アトミック変数() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複数のスレッドにより同期なしで変数が並行して変更されたとしても、");
		o.__("どれかのスレッドによりその変数に保存された値を返すことが保証される。").e();
		o.l2("longとdouble以外のプリミティブ変数はアトミック。").e();

		o.l1("【どうすれば？】").e();
		o.l2("アトミック変数にも同期は「必要」。").e();
		o.l2("同期は、スレッド間での「相互排他」だけでなく、「信頼性のある通信」のために必要。").e();

		o.l1("【どうして？】").e();
		o.l2("アトミックは、読み出し時、スレッドがランダムな値を読み出さないことを保証している。").e();
		o.l2("しかし、１つのスレッドが書き込んだ値が、他のスレッドからも見えることは保証していない。").e();

		o.l1("【たとえば？】").e();
		o.l2("同期化せず、スレッドが停止しないNonStopThreadクラス。").e();
		o.l3("「活性エラー（liveness failure）」が発生。").e();
		// NonStopThread t = new NonStopThread();
		// t.execute();
		o.l2("同期化して、スレッドが停止するStopThreadクラス。").e();
		StopThread t = new StopThread();
		t.execute();
		assertThat(t.stopRequested(), is(true));
	}

	public static class NonStopThread {
		private boolean stopRequested;

		public void execute() throws InterruptedException {
			Thread backgroundThread = new Thread(new Runnable() {
				public void run() {
					int i = 0;
					while (!stopRequested) {
						i++;
					}
					// このWhile文が同期化されていないため、
					// 仮想マシンにより、以下のように最適化される。

					/*
					 * if( !stopRequested ){
					 * 　while(true){
					 * 　　i++;
					 * 　}
					 * }
					 */
					// （この最適化は、「巻き上げ」という。）

					// 同期化されていないことから、
					// JVM的には、まさか他からstopRequested変更されるとは思わず、
					// 変数チェックの回数を減らすため、巻き上げてしまった。

					// 結果永久ループに入った後、stopRequestedがどうなろうが、
					// それを見ることはなくなってしまった。
				}
			});
			backgroundThread.start();

			TimeUnit.SECONDS.sleep(1);
			stopRequested = true;
		}

	}

	public static class StopThread {
		private boolean stopRequested;

		// 変数への書き込みと、読み込みを両方同期化する。
		// （両方を同期化しないと、同期できないので注意！）

		private synchronized void requestStop() {
			stopRequested = true;
		}

		private synchronized boolean stopRequested() {
			return stopRequested;
		}

		public void execute() throws InterruptedException {
			Thread backgroundThread = new Thread(new Runnable() {
				public void run() {
					int i = 0;
					while (!stopRequested()) {
						i++;
					}
				}
			});
			backgroundThread.start();

			TimeUnit.SECONDS.sleep(1);
			requestStop();
		}

	}

	@Test
	public void volatile修飾子付き変数() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("変数アクセスの相互排他は行わないが、最後に書き込まれた値が見えることを保証する。").e();

		o.l1("【どうして？】").e();
		o.l2("同期処理が冗長な場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("アトミックにより相互排他が保証され、通信効果のみ必要なときに使用。").e();

		o.l1("【たとえば？】").e();
		o.l2("同期化せず、スレッドが停止するStopThreadSpeedyクラス。").e();
		StopThreadSpeedy t = new StopThreadSpeedy();
		t.execute();
		assertThat(t.stopRequested, is(true));
	}

	public static class StopThreadSpeedy {
		private volatile boolean stopRequested;

		public void execute() throws InterruptedException {
			Thread backgroundThread = new Thread(new Runnable() {
				public void run() {
					int i = 0;
					while (!stopRequested) {
						i++;
					}
				}
			});
			backgroundThread.start();

			TimeUnit.SECONDS.sleep(1);
			stopRequested = true;
		}

	}

	@Test
	public void 演算付き変数() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("アトミック変数＋volatile修飾子でも、演算が入ると同期が必要。").e();

		o.l1("【どうして？】").e();
		o.l2("演算そのものがアトミックではない。").e();

		o.l1("【たとえば？】").e();
		o.l2("スレッドセーフでないSerialNumberUnsafetyクラス。").e();
		o.l3("「安全性エラー（safety failure）」が発生。").e();
		SerialNumberUnsafety serialNumberUnsafety = new SerialNumberUnsafety();
		assertThat(serialNumberUnsafety.generate(), is(0));
		assertThat(serialNumberUnsafety.generate(), is(1));

		o.l2("同期化してスレッドセーフなSerialNumberSafetyクラス。").e();
		SerialNumberSafety serialNumberSafety = new SerialNumberSafety();
		assertThat(serialNumberSafety.generate(), is(0));
		assertThat(serialNumberSafety.generate(), is(1));

		o.l2("java.util.concurrent.atomic.AtomicLongを使用して、");
		o.__("スレッドセーフかつパフォーマンスもよいSerialNumberSafetySpeedyクラス。").e();
		SerialNumberSafetySpeedy serialNumberSafetySpeedy = new SerialNumberSafetySpeedy();
		assertThat(serialNumberSafetySpeedy.generate(), is(0L));
		assertThat(serialNumberSafetySpeedy.generate(), is(1L));
	}

	private static class SerialNumberUnsafety {
		private volatile int nextSerialNumber = 0;

		public int generate() {
			// ＋＋がアトミックではない。
			// 「読み出し」と「加算」２つの処理を含んでいる。
			// この隙間に別スレッドがくると安全でなくなる。
			return nextSerialNumber++;
		}
	}

	private static class SerialNumberSafety {
		private int nextSerialNumber = 0; // 同期化したのでvolatileは必要なし

		public synchronized int generate() { // メソッドを同期化
			return nextSerialNumber++;
		}
	}

	private static class SerialNumberSafetySpeedy {
		private final AtomicLong nextSerialNumber = new AtomicLong();

		public synchronized long generate() {
			return nextSerialNumber.getAndIncrement();
		}
	}

	@Test
	public void 可変データを共有しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("同期の問題を避けるために、不変データのみ共有するか、一切共有しないかどちらかにする。").e();

		o.l1("【どうすれば？】").e();
		o.l2("可変データを単一スレッドに閉じ込める。").e();
		o.l2("この方針を採用した場合、その旨文書化して通知する。").e();

		{
			/** 【補】「事実上不変なオブジェクト」とは？ */
			// スレッドがデータオブジェクトを変更してから、
			// オブジェクト参照を共有する処理だけを同期しているオブジェクト。
			// そのデータオブジェクトを他のスレッドと共有するのは問題ない。
			// それ以降オブジェクトが変更されない限り、
			// 他のスレッドはさらに同期することなくオブジェクトを読み出せる。
		}

		{
			/** 【補】「安全な公開」とは？ */
			// オブジェクト参照などを、スレッドから他のスレッドへ転送することを「公開」という。
			// 安全に公開するには、以下のような方法がある。

			// ・クラス初期化でstaticフィールド保存
			// ・volatileフィールド
			// ・finalフィールド
			// ・普通のロックでアクセスするフィールド
			// ・コンカレントコレクション
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("複数のスレツドが可変データを共有する場合には、");
		o.__("そのデータを読み書きするスレッドは同期を行わなければならない。").e();
		o.l1("並行プログラミングの障害原因調査は困難なので、実装方法はより安全志向で選択。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
