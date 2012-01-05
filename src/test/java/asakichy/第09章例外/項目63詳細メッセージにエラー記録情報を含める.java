package asakichy.第09章例外;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目63詳細メッセージにエラー記録情報を含める {

	@Test
	public void 詳細メッセージとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("例外の文字列表現（toStringの出力）の一部で、特に保守の際、有用な情報となる。").e();
		o.l3("例外の文字列表現＝クラス名＋詳細メッセージ。").e();

		o.l1("【どうして？】").e();
		o.l2("プログラムがキャッチされなかった例外により失敗した場合、");
		o.__("システムは自動的にその例外のスタックトレースを表示する。").e();
		o.l2("スタックトレースには、その例外の文字列表現が含まれており、");
		o.__("これが原因究明の唯一の手がかりになることが多い。").e();

		o.l1("【どうすれば？】").e();
		o.l2("後の分析のために、詳細メッセージにエラー情報を記録しておく。").e();
	}

	@Test
	public void 内容() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("その例外の原因となったすべてのパラメータとフィールドの値を含んでいるべき。").e();

		o.l1("【どうして？】").e();
		o.l2("原因の診断が目的なので、それに纏わった情報はすべて記録する。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.IndexOutOfBoundsExceptionの場合。").e();
		o.l3("下限範囲。").e();
		o.l3("上限範囲。").e();
		o.l3("範囲におさまらなかった実際のインデックスの値。").e();

		o.l1("【どうすれば？】").e();
		o.l2("逆に、不要な情報を含めないこと。").e();
		o.l3("たとえばファイル名や行番号はスタックトレースに任せる。").e();
		o.l2("エンドユーザ用のメッセージとは分けて考える。").e();
		o.l3("保守用なので、わかりやすさより情報の濃さで。").e();
	}

	@Test
	public void 実装() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンストラクタで必要な情報を強要する。").e();

		o.l1("【たとえば？】").e();
		o.l2("オリジナルのIndexOutOfBoundsException。").e();

		// コンストラクタで、エラーの原因となった情報を強要する。
		IndexOutOfBoundsException e = new IndexOutOfBoundsException(0, 10, 11);
		assertThat(e.getLowerBound(), is(0));
		assertThat(e.getUpperBound(), is(10));
		assertThat(e.getIndex(), is(11));
	}

	private static class IndexOutOfBoundsException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		private int lowerBound;
		private int upperBound;
		private int index;

		// エラー原因となった情報を強要するコンストラクタ
		public IndexOutOfBoundsException(int lowerBound, int upperBound, int index) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.index = index;
		}

		// アクセサーを提供するのもよいこと。
		// 特にチェック例外だった場合、復旧に役立つ情報を提供すること。

		public int getLowerBound() {
			return lowerBound;
		}

		public int getUpperBound() {
			return upperBound;
		}

		public int getIndex() {
			return index;
		}

		// 詳細メッセージ組み立て等は省略

	}

	@Test
	public void まとめ() throws Exception {
		o.l1("例外文字列に含まれる詳細メッセージは、リリース後のトラブル解決の命綱。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
