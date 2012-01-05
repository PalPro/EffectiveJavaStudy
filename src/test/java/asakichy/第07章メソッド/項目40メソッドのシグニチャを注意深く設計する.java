package asakichy.第07章メソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目40メソッドのシグニチャを注意深く設計する {

	@Test
	public void メソッドシグニチャの設計とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッドの名前、引数、戻り値の設計のこと。").e();

		o.l1("【どうして？】").e();
		o.l2("使いやすい、エラーになりにくいAPIを設計し、コードの品質を高める。").e();
	}

	@Test
	public void メソッド名を注意深く選ぶ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("理解可能で、同じパッケージ内の他の名前と矛盾のない名前を選ぶこと。").e();
		o.l2("存在する広範囲のコンセンサスと矛盾がない名前を選ぶこと。").e();

		o.l1("【どうして？】").e();
		o.l2("直感的にわかりやすい名前にして、APIを使いやすくする。").e();

		o.l1("【どうすれば？】").e();
		o.l2("JDKを参考にする。").e();
	}

	@Test
	public void 便利なメソッドを提供し過ぎない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("型は余計なメソッドを提供せず、「自分の役割」のみを果たすこと。").e();

		o.l1("【どうして？】").e();
		o.l2("多すぎるメソッドは、クラスの学習・使用・文書化・テスト・保守を困難にする。").e();
		o.l2("インターフェイスの多すぎるメソッドは、実装者に負荷をかける。").e();

		o.l1("【どうすれば？】").e();
		o.l2("メソッドをシンプルに保つ。").e();
		o.l2("「疑わしきは、提供せず。」").e();
	}

	@Test
	public void 長いパラメータのリストは避ける() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パラメータの多いメソッドは使いにくい。").e();
		o.l2("４個以下を目標とする。").e();

		o.l1("【どうして？】").e();
		o.l2("ドキュメントを参照しながらでないと使用できない。").e();
		o.l2("同じ型のパラメータが続くと、順番間違えてもコンパイル通るので、特に有害。").e();

		o.l1("【どうすれば？】").e();
		o.l2("１").s("メソッド分割。").e();
		o.l3("複数メソッドに分割すれば、各メソッドはパラメータのサブセットだけが必要となる。").e();
		o.l2("２").s("ヘルパクラス。").e();
		o.l3("パラメータの集まりを保持するクラスを作成。").e();
		o.l2("３").s("ビルダーパターン。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Listはメソッド分割を利用している。").e();
		o.l3("indexOfメソッドについて、").e();
		o.l3("indexOf(検索対象オブジェクト, fromIndex, toIndex)というメソッドではなく、").e();
		o.l3("「subList(fromIndex, toIndex)」と「indexOf(検索対象オブジェクト)」に分割されている。").e();
		List<String> strings = Arrays.asList("zero", "one", "two", "three", "four", "five");
		int index = strings.subList(0, 4).indexOf("three");
		assertThat(index, is(3));
	}

	@Test
	public void パラメータ型はクラスよりインタフェース() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パラメータの型には、インタフェースを実装したクラスより、そのインタフェースを使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("クライアントを実装に縛り付けない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.HashMapではなく、java.util.Mapをパラメータの型にする。").e();
		o.l2("クライアントはjava.util.TreeMapでも、java.util.Hashtableでも、メソッドを使用できるようになる。").e();
	}

	@Test
	public void パラメータはbooleanよりenum型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パラメータには、booleanより「2つの要素を持つenum型」を使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("コードが読みやすい。").e();
		o.l2("後で三値以上になっても問題なく拡張できる。").e();
		o.l2("IDEの入力補助を得られる。").e();

		o.l1("【たとえば？】").e();
		o.l2("ThermometerクラスのnewInstanceメソッドの引数。").e();
		// Thermometer.newInstance(true)よりわかりやすい
		Thermometer instance = Thermometer.newInstance(TemperatureScale.FAHRENHEIT);
		assertThat(instance, anything());
	}

	private static class Thermometer {
		static Thermometer newInstance(TemperatureScale t) {
			return null; // 省略
		}
	}

	enum TemperatureScale {
		FAHRENHEIT, CELSIUS
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("メソッドは、視点を切り替えて見直してみること。").e();
		o.l2("使う人。").e();
		o.l2("読む人。").e();
		o.l2("保守する人。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
