package asakichy.第02章オブジェクトの生成と消滅;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目07ファイナライザを避ける {

	@Test
	public void ファイナライザとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトがどこからも参照されなくなり、");
		o.__("GCにより破棄される時に、JVMにコールバックされるメソッド。").e();
		o.l2("具体的にはObject#finalize()のこと。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Object#finalize()をオーバライド。").e();
		o.l3("親クラスのファイナライザは自動的には呼ばれない。").e();
		o.l3("「super.finalize()」として、明示的に呼び出すこと。").e();

		o.l1("【たとえば？】").e();
		o.l2("Object#finalize()をオーバライド。").e();
		Object finalizer = new Object() {
			@Override
			protected void finalize() throws Throwable {
				try {
					finalizeCalled = true;
				} finally {
					super.finalize();
				}
			}

			@Override
			public String toString() {
				return "finalizer!";
			}
		};

		assertThat(finalizer.toString(), is("finalizer!"));

		// オブジェクト破棄
		finalizer = null;
		System.gc();
		assertThat(finalizeCalled, anything()); // GC後でも呼び出しは保証されない
	}

	private boolean finalizeCalled = false;

	@Test
	public void ダメな理由_即座に実行されるわけではない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトが到達不能になってから、").e();
		o.l2("ファイナライザが実行されるまでの時間は制御できない。").e();
		o.l3("JVM依存ということ。").e();

		o.l1("【どうすれば？】").e();
		o.l2("リソース開放などに使用してはいけない。").e();
	}

	@Test
	public void ダメな理由_実行されない場合もある() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("そもそも言語仕様的に、実行が保証されていない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("永続性のある情報の更新に使用してはいけない。").e();

		{
			/** 【補】runFinalizersOnExit について */
			// 「System#runFinalizersOnExit()」「Runtime#runFinalizersOnExit()」は、
			// ファイナライザの呼び出しを保証する。
			// しかし、デッドロックを引き起こすなど致命的な欠陥をもつため、
			// このメソッド自身が「Deprecated」である。
		}
	}

	@Test
	public void ダメな理由_パフォーマンスが悪くなる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ファイナライザの使用には重大なパフォーマンスのペナルティがある。").e();

		o.l1("【どうして？】").e();
		o.l2("ファイナライザを持つオブジェクトの生成と解放は、");
		o.l2("ファイナライザを持もたないオブジェクトの場合の430倍遅い。").e();
	}

	@Test
	public void 有効な場合_セーフティネット() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クライアントの契約違反により、");
		o.__("リソースを解放してもらえなかったときの「安全弁」として使う。").e();

		o.l1("【どうすれば？】").e();
		o.l2("リソースの開放とともに、契約違反の旨、ログに出力するのがベター。").e();
	}

	@Test
	public void 有効な場合_ネイティブピアを持つオブジェクト() throws Exception {
		{
			/** 【補】ネイティブピアとは */
			// 普通のオブジェクトが、
			// ネイティブメソッドを通して委譲する、ネイティブオブジェクトのこと。
		}

		o.l1("【どういうこと？】").e();
		o.l2("ネイティブピアは通常のオブジェクトではないので、").e();
		o.l2("通常のオブジェクトが回収される時に、ネイティブピアを一緒に回収できない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("ネイティブピアが重要な資源を保持していないなら、その開放に使ってもよい。").e();
	}

	@Test
	public void イディオム_ファイナライザガーディアン() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("サブクラスの「super.finalize()」呼び忘れ対策の実装方法。").e();

		o.l1("【どうすれば？】").e();
		o.l2("外側のオブジェクトをファイナライズする、無名内部クラスを作成。").e();

		o.l1("【たとえば？】").e();
		o.l2("クラスFooにfinalize()をオーバライドした無名内部クラスのフィールド。").e();
		@SuppressWarnings("unused")
		class Foo {
			private final Object finalizerGuardian = new Object() {
				@Override
				protected void finalize() throws Throwable {
					// 外側のFooオブジェクトのファイナライズを行う
				}
			};
		}

		{
			/** 【補】ファイナライザガーディアンのメカニズム */
			// Fooのサブクラスが、Foo#finalizeを呼び忘れても、
			// エンクロージングインスタンスfinalizerGuardianのファイナライザが、
			// Fooオブジェクト破棄時に呼ばれる。
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("基本、ファイナライザを使用しない。").e();
		o.l1("終了処理の設計は、明示的な解放メソッドを用意して、");
		o.__("try-finally構文でそれを呼び出してもらう方法で。").e();

		// サーバ
		class Foo {
			public void execute() {
				// 実行
			};

			public void terminate() {
				// 終了処理
			};
		}

		// クライアント
		Foo f = new Foo();
		try {
			f.execute();
		} finally {
			f.terminate();
		}
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
