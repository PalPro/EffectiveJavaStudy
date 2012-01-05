package asakichy.第09章例外;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目65例外を無視しない {

	@Test
	public void 例外の無視とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("空のcatchブロックを作ること。").e();
		o.l2("これは例外の「目的」をまったく果たしていないことになる。").e();
		o.l3("目的とは、「例外的状態を処理させることを強制する」こと。").e();

		o.l1("【どうして？】").e();
		o.l2("火災警報を無視して、警報を切ってしまうのと同じこと。").e();

		o.l1("【どうすれば？】").e();
		o.l2("無視しない。").e();
		o.l2("無視するなら、その理由をコメントに書く。").e();

		{
			/** 【補】 例外を無視してもよい場合 */
			// FileInputStreamのクローズ処理の例外は無視しても問題ない。
			// ファイルの状態を変更していなければ、回復処理の必要はないし、
			// 必要な情報を読み終わっている後なので、その後の処理を中断する理由もない。

			// ただし、このような場合でも、例外を記録することは有用である。
			// 例外が頻繁に発生していれば、問題に気づいて調査できるようになる。
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("例外を無視して、副作用的なエラーが他の場所で発生すると、本当の原因がわからなくなる。").e();
		o.l2("例外を外側に投げるだけでも、情報がどこかで出ることになるので、握りつぶすよりはマシ。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
