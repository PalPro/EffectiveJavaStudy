package asakichy.第03章すべてのオブジェクトに共通のメソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目10toStringを常にオーバーライドする {

	@Test
	public void toStringとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトの内容を文字列で表現するメソッド。").e();
		o.l2("すべてのクラスがこのメソッドを実装することが推奨されている。").e();

		o.l1("【どうして？】").e();
		o.l2("toString()は呼ばれる頻度が多い。").e();
		o.l3("＋（文字列結合演算子）。").e();
		o.l3("printf()/println()。").e();
		o.l3("assert()。").e();
		o.l3("デバッガの表示。").e();
		o.l3("エラーの表示。").e();
		o.l2("よって、簡潔でわかりやすい文字情報を返すtoString()を持つクラスは、使い勝手が良い。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Object#toString()をオーバライド。").e();

		{
			/** 【補】Object#toStringのデフォルト実装 */
			// 出力内容は「クラス名＠ハッシュコード16進表現」となる。
			// 出力例：java.lang.Object@19fcc69
			assertThat(new Object(), hasToString(startsWith("java.lang.Object@")));
		}
	}

	@Test
	public void 実装原則() throws Exception {
		o.l1("【どういうこと？】").e();

		o.l2("１").s("クライアントの興味のあると思われる情報は「すべて」含める。").e();
		o.l3("あまりに巨大な情報の場合のみ要約を検討する。").e();

		o.l2("２").s("意図を明確にドキュメントに書く。").e();
		o.l3("書式情報を含めるかどうか？を検討する。").e();
		o.l3("利点").s("入出力に使える明確な表現形式として応用できる。").e();
		o.l3("欠点").s("一度公開すると、形式を変更できなくなる。").e();

		o.l2("３").s("表示する情報へのアクセサを提供する。").e();
		o.l3("toString()でしか取れない情報があると、");
		o.__("クライアントは無理矢理パースして手に入れようとする。").e();

		o.l1("【たとえば？】").e();
		o.l2("PhoneNumberクラス。").e();
		PhoneNumber phoneNumber = new PhoneNumber(707, 867, 5309);
		assertThat(phoneNumber, hasToString("(707) 867-5309"));
	}

	public static final class PhoneNumber {
		private final short areaCode;
		private final short prefix;
		private final short lineNumber;

		public PhoneNumber(int areaCode, int prefix, int lineNumber) {
			rangeCheck(areaCode, 999, "area code");
			rangeCheck(prefix, 999, "prefix");
			rangeCheck(lineNumber, 9999, "line number");
			this.areaCode = (short) areaCode;
			this.prefix = (short) prefix;
			this.lineNumber = (short) lineNumber;
		}

		private static void rangeCheck(int arg, int max, String name) {
			if (arg < 0 || arg > max) {
				throw new IllegalArgumentException(name + ": " + arg);
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof PhoneNumber)) {
				return false;
			}
			PhoneNumber pn = (PhoneNumber) o;
			return pn.lineNumber == lineNumber && pn.prefix == prefix && pn.areaCode == areaCode;
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + areaCode;
			result = 31 * result + prefix;
			result = 31 * result + lineNumber;
			return result;
		}

		// 【原則２】意図を明確にしたコメント
		/**
		 * この電話番号の文字列表現を返します。
		 * 
		 * 文字列は、14文字で構成されて、その形式は (AAA) BBB-CCCC です。
		 * AAAは市外局番で、BBB-CCCCは市内局番です。（各大文字は、1桁の数字を表しています｡）
		 * 
		 * この電話番号の3つの部分のどれかが、そのフィールドを埋めるには桁が少ない場合には、
		 * そのフィールドの先頭が0で埋められます。
		 * たとえば、最後の4桁部分の番号が123だとしたら、文字列表現の最後の4文字は、0123となります。
		 * 
		 * 市外局番の括弧の後に、市内局番と区切るために空白が1つあることに注意してください。
		 */
		@Override
		public String toString() {
			// 【原則１】全情報を返す
			return String.format("(%03d) %03d-%04d", areaCode, prefix, lineNumber);
		}

		// 【原則３】アクセサを提供
		public short getAreaCode() {
			return areaCode;
		}

		public short getPrefix() {
			return prefix;
		}

		public short getLineNumber() {
			return lineNumber;
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("問題解決の手がかりとしてリアルに役立つので、");
		o.__("toString()は必ず実装しておくこと。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
