package asakichy.第02章オブジェクトの生成と消滅;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目05不必要なオブジェクトの生成を避ける {
	@Test
	public void オブジェクトの生成とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトの生成は、コスト（時間・メモリ）がかかる場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("必要に応じて、再利用などして、無駄な生成を省く。").e();
	}

	@Test
	public void オブジェクトを再利用_不変ならランタイムに任せる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("不変であれば、オブジェクトは再利用できる。").e();
		o.l2("特に文字リテラルは、JVMが再利用を保証してくれる。").e();

		o.l1("【どうして？】").e();
		o.l2("文字リテラル引数に文字コンストラクトは、無駄なインスタンス生成。").e();
		o.l2("同じ文字列があればJVMが再利用してくれる特典付。").e();

		o.l1("【どうすれば？】").e();
		o.l2("素直に文字リテラルだけでよい。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.String。").e();

		// 悪い例
		String sBad = new String("immutable");
		assertThat(sBad, not(sameInstance("immutable")));

		{
			/** 【補】Stringのコピーコンストラクタ */
			// Stringリテラルを引数にするStringコンストラクトという形式は、
			// いわゆる「コピーコンストラクタ」にあたる。
			// ただ、不変オブジェクトにコピーコンストラクタは通常不要なハズ。
		}

		// 良い例
		String sGood = "immutable";
		assertThat(sGood, sameInstance("immutable"));
		assertThat(sGood, sameInstance(immutable()));
	}

	private String immutable() {
		return "immutable"; // コレもJVMが共有
	}

	@Test
	public void オブジェクトを再利用_不変ならファクトリに任せる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンストラクタよりstaticファクトリメソッドを使用して、");
		o.__("不必要なオブジェクト生成を回避。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Boolean#valueOf()。").e();

		Boolean bBad = new Boolean("true");
		assertThat(bBad, not(sameInstance(Boolean.TRUE)));
		Boolean bGood = Boolean.valueOf("true");
		assertThat(bGood, sameInstance(Boolean.TRUE));
	}

	@Test
	public void オブジェクトを再利用_可変でも変更されないなら使い回す() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("変更のない値は、最初に計算してstaticのfinalに保管して使い回し。").e();

		o.l1("【たとえば？】").e();
		o.l2("Personクラス。").e();
		Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtCal.set(1950, Calendar.JANUARY, 1, 0, 0, 0);
		Date birthDay = gmtCal.getTime();
		assertThat(new Person_ムダ生成(birthDay).isBabyBoomer(), is(true));
		assertThat(new Person_再利用(birthDay).isBabyBoomer(), is(true));
	}

	private static class Person_ムダ生成 {
		private final Date birthDate;

		public Person_ムダ生成(Date birthDate) {
			this.birthDate = new Date(birthDate.getTime());
		}

		public boolean isBabyBoomer() {
			// ベビーブームの開始・終了を毎回生成するのはムダ！
			Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
			Date boomStart = gmtCal.getTime();
			gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
			Date boomEnd = gmtCal.getTime();
			return birthDate.compareTo(boomStart) >= 0 && birthDate.compareTo(boomEnd) < 0;
		}
	}

	private static class Person_再利用 {
		private final Date birthDate;

		public Person_再利用(Date birthDate) {
			this.birthDate = new Date(birthDate.getTime());
		}

		// 変更のないオブジェクトは再利用
		private static final Date BOOM_START;
		private static final Date BOOM_END;

		static {
			// 最初に計算
			Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
			BOOM_START = gmtCal.getTime();
			gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
			BOOM_END = gmtCal.getTime();
			// （遅延初期化という手もあるが、複雑になるし、効果も薄いので非推奨）
		}

		public boolean isBabyBoomer() {
			return birthDate.compareTo(BOOM_START) >= 0 && birthDate.compareTo(BOOM_END) < 0;
		}
	}

	@Test
	public void オブジェクトを再利用_Adapterを再利用() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Adapteeが差し換わるだけなら、Adapterは１つでいい。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Map#keySet()。").e();
		o.l2("Mapオブジェクトのkey集合（=Adaptee）を、Setビュー（=Adapter）で返す。").e();

		Map<String, String> m = new HashMap<String, String>();

		m.put("key1", "value1");
		Set<String> keySet1 = m.keySet();
		assertThat(keySet1.size(), is(1));

		m.put("key2", "value2");
		Set<String> keySet2 = m.keySet();
		assertThat(keySet2.size(), is(2));

		// keySetの中身が変わっているのに、keySetインスタンスは共通
		assertThat(keySet1, sameInstance(keySet2));
	}

	@Test
	public void オブジェクトを再利用_オブジェクトプールは多用しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトの生成が重いときに、オブジェクトのキャッシュという方法がある。").e();
		o.l2("しかし、これは多用すべきではない。").e();

		o.l1("【どうして？】").e();
		o.l2("基本「悪い」アイデア。").e();
		o.l3("コードが複雑になるし、メモリも食う。").e();
		o.l3("高度に最適化されたガベージコレクターを持つ、JVMに任せたほうがいい。").e();

		o.l1("【どうすれば？】").e();
		o.l2("DBコネクションぐらいコストがかかる場合に検討する程度でよい。").e();
	}

	@Test
	public void オブジェクトを作らない_自動ボクシングによるラッパーインスタンス生成回避() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("意図しないボクシング・アンボクシングが発生している場合がある。").e();

		o.l1("【たとえば？】").e();
		o.l2("単純な加算関数sumSlowly()。").e();
		assertThat(sumSlowly(10L), is(55L));

		o.l1("【どうすれば？】").e();
		o.l2("ボクシングされた基本データ型よりも、基本データ型を選ぶ。").e();
		o.l2("意図しない自動ボクシングに注意する。").e();
	}

	private long sumSlowly(long limit) {
		Long/* longの誤字 */total = 0L; // ここをlongプリミティブにすると、6倍以上速くなる
		for (long i = 0; i <= limit; i++) {
			total += i; // 自動ボクシングにより、Longインスタンスを毎回生成
		}
		return total;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("不要なオブジェクトの検討を忘れてはいけない。").e();
		o.l1("が、必要なオブジェクトの生成は躊躇してはいけない。").e();
		o.l2("たとえば、「防御的コピー」。").e();
		o.l1("あくまで、可読性・安全性重視で。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
