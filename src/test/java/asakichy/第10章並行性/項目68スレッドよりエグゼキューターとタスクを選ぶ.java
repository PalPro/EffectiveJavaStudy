package asakichy.第10章並行性;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目68スレッドよりエグゼキューターとタスクを選ぶ {

	@Test
	public void エグゼキューターフレームワークとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("柔軟なインタフェースに基づく、並行処理によるタスク実行機構。").e();

		o.l1("【どうすれば？】").e();
		o.l2("処理の単位に、「スレッド」という抽象は使用せず、「タスク」という抽象を使用する。").e();
		o.l3("java.lang.Runnableインターフェイス（値を返さないとき）。").e();
		o.l3("java.lang.Callableインターフェイス（値を返すとき）。").e();
		o.l2("「タスク」を実行する機構に、「エグゼキューターサービス」を使用する。").e();
	}

	@Test
	public void 用途_ワークキュー() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("キューイングされたタスクを、順にデキューして、順に別のスレッドで実行。").e();

		o.l1("【どうすれば？】").e();
		o.l2("java.util.concurrent.Executors.newSingleThreadExecutor()ファクトリメソッドを使用。").e();

		o.l1("【たとえば？】").e();
		o.l2("newSingleThreadExecutor()でワークキューを作成。").e();

		o.l3("タスクがRunnableの場合").e();
		NameDecorateRunner runTask1 = new NameDecorateRunner("task1", 2);
		NameDecorateRunner runTask2 = new NameDecorateRunner("task2", 1);

		// executeされたタスクは1つの別スレッドで順々に処理される。
		ExecutorService executor = Executors.newSingleThreadExecutor();

		// 呼び出し順に処理が行われる
		executor.execute(runTask1);
		executor.execute(runTask2);

		// （ゆるやかな）終了指示
		executor.shutdown();

		// エグゼキューターの完了を待つ
		executor.awaitTermination(10/* timeout */, TimeUnit.SECONDS);

		assertThat(runTask1.name, is("***task1***"));
		assertThat(runTask2.name, is("***task2***"));

		o.l3("タスクがCallableの場合").e();
		NameDecorateCaller callTask1 = new NameDecorateCaller("task1", 2);
		NameDecorateCaller callTask2 = new NameDecorateCaller("task2", 1);

		executor = Executors.newSingleThreadExecutor();

		Future<String> future1 = executor.submit(callTask1);
		Future<String> future2 = executor.submit(callTask2);

		executor.shutdown();

		// 処理の完了を待つ
		String result1 = future1.get();
		String result2 = future2.get();

		assertThat(result1, is("###task1###"));
		assertThat(result2, is("###task2###"));

		{
			/** 【補】invokeAllメソッドとinvokeAnyメソッド */
			// あるタスクの集まりの完了を待つことができるメソッド。
			// invokeAllメソッドは、タスクをすべて実行し、すべての完了を待つ。
			// invokeAnyメソッドは、タスクをすべて実行し、どれか１つでも完了することを待つ。
		}
	}

	private static class NameDecorateRunner implements Runnable {

		private String name;
		private long delay;

		public NameDecorateRunner(String name, long delay) {
			this.name = name;
			this.delay = delay;
		}

		@Override
		public void run() {
			try {
				TimeUnit.SECONDS.sleep(delay);
			} catch (InterruptedException e) {
			}
			name = "***" + name + "***";
			System.out.println(name);
		}
	}

	private static class NameDecorateCaller implements Callable<String> {

		final private String name;
		final private long delay;

		public NameDecorateCaller(String name, long delay) {
			this.name = name;
			this.delay = delay;
		}

		@Override
		public String call() throws Exception {
			try {
				TimeUnit.SECONDS.sleep(delay);
			} catch (InterruptedException e) {
			}
			String decoratedName = "###" + name + "###";
			System.out.println(decoratedName);
			return decoratedName;
		}
	}

	@Test
	public void 用途_ワークキュー_スレッド複数() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("タスクを実行するスレッドを複数持つワークキュー。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スレッドプールを利用する。").e();
		o.l2("java.util.concurrent.Executors.newCachedTreadPool()ファクトリメソッドを使用。").e();
		o.l3("タスクが割り当てられるたびにスレッド生成して、しばらく使いまわす。").e();
		o.l3("一定時間使われなかったスレッドは消滅する。").e();
		o.l2("java.util.concurrent.Executors.newFixedTreadPool()ファクトリメソッドを使用。").e();
		o.l3("指定された数のスレッドを常時保持するスレッドプールを作成。").e();
		o.l3("空きスレッドがあれば、直ちにタスクが割り当てられる。").e();
		o.l3("スレッド生成のオーバヘッドを避ることができるため、性能が要求されるアプリケーションで使用する。").e();

		o.l1("【たとえば？】").e();
		o.l2("newCachedTreadPool()でスレッドプール付ワークキューを作成。").e();

		// タスク
		NameDecorateRunner runTask1 = new NameDecorateRunner("task1", 2);
		NameDecorateRunner runTask2 = new NameDecorateRunner("task2", 1);

		// executeされたタスクはそれぞれ個々にスレッドが割り当てられ、並行で処理される。
		ExecutorService executor = Executors.newCachedThreadPool();

		// 並行に処理が行われ、後から実行されるが遅延の少ないtask2が先に終了する
		executor.execute(runTask1);
		executor.execute(runTask2);

		// 終了指示
		executor.shutdown();

		// エグゼキューターの完了を待つ
		executor.awaitTermination(10/* timeout */, TimeUnit.SECONDS);

		assertThat(runTask1.name, is("***task1***"));
		assertThat(runTask2.name, is("***task2***"));

		{
			/** 【補】ExecutorCompletionServiceについて */
			// タスクが完了するごとに、１つずつタスクの結果を取り出すことができる。

			executor = Executors.newFixedThreadPool(2);
			ExecutorCompletionService<String> service = new ExecutorCompletionService<String>(executor);

			// 遅延を調整し、1の方が遅く終わるように
			NameDecorateCaller callTask1 = new NameDecorateCaller("task1", 3);
			NameDecorateCaller callTask2 = new NameDecorateCaller("task2", 1);

			service.submit(callTask1);
			service.submit(callTask2);

			executor.shutdown();

			String result2 = service.take().get();
			assertThat(result2, is("###task2###"));

			String result1 = service.take().get();
			assertThat(result1, is("###task1###"));
		}
	}

	@Test
	public void 用途_タイマ_スレッド複数() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("タスクを実行するスレッドを複数持つタイマ。").e();

		o.l1("【どうして？】").e();
		o.l2("java.util.Timerは単一スレッド。").e();

		o.l1("【どうすれば？】").e();
		o.l2("スレッドプールを利用する。").e();
		o.l2("java.util.concurrent.Executors.newScheduledThreadPool()ファクトリメソッドを使用。").e();

		o.l1("【たとえば？】").e();
		o.l2("newScheduledThreadPool()でスレッドプール付タイマを作成。").e();

		NameDecorateRunner runTask1 = new NameDecorateRunner("task1", 0);
		NameDecorateRunner runTask2 = new NameDecorateRunner("task2", 0);

		// 複数スレッドをプールするタイマ
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

		// タイマタスクを並行実行
		executor.schedule(runTask1, 3, TimeUnit.SECONDS);
		executor.schedule(runTask2, 3, TimeUnit.SECONDS);

		// すぐに終了指示
		// なので、タイマの繰り返しは1回
		executor.shutdown();

		// エグゼキューターの完了を待つ
		executor.awaitTermination(10/* timeout */, TimeUnit.SECONDS);

		assertThat(runTask1.name, is("***task1***"));
		assertThat(runTask2.name, is("***task2***"));

		{
			/** 【補】scheduleAtFixedRateメソッドとscheduleWithFixedDelayメソッド */
			// ScheduledExecutorServiceには、scheduleの他に、
			// scheduleAtFixedRateメソッドとscheduleWithFixedDelayメソッドがある。

			// scheduleAtFixedRateは、タスク開始と次のタスク開始の間隔を指定できる。
			// scheduleWithFixedDelayは、タスク終了と次のタスク開始の間隔を指定できる。
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("品質、書き易さ、読み易さ、すべての点において、");
		o.__("スレッドよりエグゼキューターがすぐれている。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
