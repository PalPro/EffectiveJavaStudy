package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目37型を定義するためにマーカーインタフェースを使用する {

	@Test
	public void マーカーインタフェースとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("メソッド宣言を含んでいないインタフェース。").e();
		o.l2("そのインタフェースを実装しているクラスが、何らかの特性を持っていると単にマークしてる。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.io.Serializableインタフェース。").e();
		o.l2("クラスのインスタンスがObjectOutputStreamへ書き込むことができる");
		o.__("（＝「シリアライズ」できる）ことを示す。").e();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject("String"/* 文字列クラスはSerializable */);
		assertThat(os.toByteArray().length, greaterThan(0));

		o.l1("【どうすれば？】").e();
		o.l2("同様の役割に「マーカーアノテーション」がある。").e();
		o.l2("しかし、これに置き換え可能なものではなく、それぞれ適した状況で使用しなければならない。").e();
	}

	@Test
	public void マーカーインタフェース長所_型定義() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("マークされたクラスのインスタンスが実装している型を定義している。").e();

		o.l1("【どうして？】").e();
		o.l2("マーカーアノテーションを使用したら実行時まで発見できない誤りを、");
		o.__("コンパイル時に見つけることができる。").e();
	}

	@Test
	public void マーカーインタフェース長所_対象特定() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("特定のインターフェイスにのみ適用可能なマーカーとして機能する。").e();
		o.l2("より正確に処理対象を特定できる。").e();

		o.l1("【どうして？】").e();
		o.l2("マーカーインタフェースとしてそのマーカーを定義すると、");
		o.__("そのマーカーインタフェースが適用可能な唯一のインタフェースを、");
		o.__("マーカーインタフェースに拡張させることができる。").e();

		o.l1("【たとえば？】").e();
		o.l2("マーカーアノテーションElementType.TYPE。").e();
		o.l3("どんなクラスやインターフェイスにも付加できる。").e();
		o.l2("制限されたマーカーインターフェイスとしてのjava.util.Set。").e();
		o.l3("Collectionのサブタイプだけに適用可能だが、");
		o.__("Collectionで定義されたメソッド以外のメソッドを追加していない。").e();
	}

	/** 【補】特定のインターフェイスにのみ適用可能なマーカー・具体例 */
	// 要するに、
	// 自分自身は何のメソッドも持たないで、マーカーに徹しているが、
	// 実は別のインターフェイスを親に持っている（extendsしている）パターン。

	// Bは、Bという型マーカでありつつ、（型Aの）methodの実装を保証している。
	interface A {
		void method();
	}

	interface B extends A {
	}

	@Test
	public void マーカーアノテーション長所_拡張性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("デフォルトを持つアノテーション型要素を１つ以上追加することで、");
		o.__("すでに使用された後でもアノテーション型に情報を追加できる。").e();

		o.l1("【どうして？】").e();
		o.l2("単なるマーカーアノテーション型として始まっても、時間の経過に伴い、").e();
		o.__("機能が豊富なアノテーション型へ発展可能。").e();
		o.l2("マーカーインターフェイスにメソッド増やしたら、コンパイルエラーだらけになる。").e();
	}

	@Test
	public void マーカーアノテーション長所_一貫性() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("アノテーションという大きな機構の一部である。").e();

		o.l1("【どうして？】").e();
		o.l2("アノテーションを使うフレームワーク内での一貫性を可能にする。").e();
	}

	@Test
	public void 選択基準() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスやインターフェイス以外の場合はアノテーションしかない。").e();
		o.l2("クラスやインターフェイスのときは、適宜検討。").e();

		o.l1("【どうすれば？】").e();
		o.l2("「このマークを持つオブジェクトだけを受け付けるメソッドを書きたい？」ならインターフェイス。").e();
		o.l3("メソッドのシグニチャを利用してコンパイル検査の恩恵を受けられる。").e();
		o.l2("「このマーカーの使用を、特定のインタフェースの要素に永久に制限したい？」ならインターフェイス。").e();
		o.l3("そのインターフェイスのサブインターフェイスをマーカに。").e();
		o.l2("いずれも「NO」ならアノテーション。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("「型」を定義したいならインターフェイスを使用する。").e();
		o.l1("特に「ElementType.TYPE」なアノテーションを書いているときは、");
		o.__("インターフェイスのほうが適していないかを再検討する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
