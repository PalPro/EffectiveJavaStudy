package asakichy.第11章シリアライズ;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目75カスタムシリアライズ形式の使用を検討する {

	@Test
	public void シリアライズ形式とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("デフォルトシリアライズ形式。").e();
		o.l2("JVMが、そのオブジェクトを起点とするオブジェクトグラフの物理表現を効率的に符号化したもの。").e();
		o.l2("カスタムシリアライズ形式").e();
		o.l3("手動で、readObject()/writeObject()メソッドを提供し符号化したもの。").e();

		o.l1("【どうすれば？】").e();
		o.l2("検討することなく、デフォルトのシリアライズ形式を受け入れてはいけない。").e();
		o.l2("カスタムシリアライズ形式を設計した場合に選択する符号化と、");
		o.__("デフォルトのシリアライズ形式がほとんど同じ場合にだけ、");
		o.__("デフォルトのシリアライズ形式を受け入れるべき。").e();
	}

	@Test
	public void デフォルトシリアライズ形式の使用() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("論理的内容が物理表現と同じ場合のみ、デフォルトシリアライズ形式を使用。").e();

		o.l1("【どうして？】").e();
		o.l2("シリアライズは、論理的内容のみシリアルするのが目的である。");
		o.l2("デフォルトシリアライズ形式は、オブジェクト内に含まれるデータと、");
		o.__("そのオブジェクトから到達可能なすべてのオブジェクトに含まれるデータを対象としてしまう。").e();

		o.l1("【たとえば？】").e();
		o.l2("デフォルトのシリアライズ形式が適切なNameクラス。").e();

		Name name = new Name("last", "first", "middle");

		// シリアライズ
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(name);
		byte[] serializedName = os.toByteArray();

		// デシリアライズ
		ByteArrayInputStream is = new ByteArrayInputStream(serializedName);
		Name deserializedName = (Name) new ObjectInputStream(is).readObject();

		assertThat(deserializedName.lastName, is("last"));
		assertThat(deserializedName.firstName, is("first"));
		assertThat(deserializedName.middleName, is("middle"));
	}

	private static final class Name implements Serializable {
		private static final long serialVersionUID = 9126353547644252193L;

		public Name(String lastName, String firstName, String middleName) {
			this.lastName = lastName;
			this.firstName = firstName;
			this.middleName = middleName;
		}

		@SuppressWarnings("unused")
		public Name(String lastName, String firstName) {
			this.lastName = lastName;
			this.firstName = firstName;
			this.middleName = null;
		}

		// ※シリアライズ形式を定義しているので、privateのフィールドにもコメントを書く
		/**
		 * ラストネーム。nullではない。
		 * 
		 * @serial
		 */
		private final String lastName;
		/**
		 * ファーストネーム。nullではない。
		 * 
		 * @serial
		 */
		private final String firstName;

		/**
		 * ミドルネーム。なければnull。
		 * 
		 * @serial
		 */
		private final String middleName;
	}

	@Test
	public void カスタムシリアライズ形式の使用() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("論理的内容が物理表現と異なる場合に使用。").e();

		o.l1("【どうして？】").e();
		o.l2("オブジェクトの物理表現とその論理データの内容が実質的に異なる場合、").e();
		o.l2("デフォルトのシリアライズ形式を使用することには、４つの短所がある。").e();
		o.l2("１").s("内部表現に永久拘束。").e();
		o.l3("実装方法を変更できなくなる。").e();
		o.l2("２").s("過剰なリソース消費。").e();
		o.l3("シリアライズに必要のないデータが混入する。").e();
		o.l2("３").s("過剰な時間消費。").e();
		o.l3("コストが高いグラフ探索を行わなければならない。").e();
		o.l2("４").s("スタックオーバーフロー発生。").e();
		o.l3("グラフの再帰的検索を行い、スタックが不足する場合がある。").e();
		o.l3("").e();

		o.l1("【たとえば？】").e();
		o.l2("カスタムシリアライズ形式が適切なクラスStringList。").e();

		StringList stringList = new StringList();
		stringList.add("first");
		stringList.add("second");
		assertThat(stringList.head.data, is("first"));
		assertThat(stringList.head.next.data, is("second"));
		assertThat(stringList.head.next.previous.data, is("first"));
		assertThat(stringList.size, is(2));

		// シリアライズ
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(stringList);
		byte[] serializedStringList = os.toByteArray();

		// デシリアライズ
		ByteArrayInputStream is = new ByteArrayInputStream(serializedStringList);
		StringList deserializedStringList = (StringList) new ObjectInputStream(is).readObject();

		assertThat(deserializedStringList.head.data, is("first"));
		assertThat(deserializedStringList.head.next.data, is("second"));
		assertThat(deserializedStringList.head.next.previous.data, is("first"));
		assertThat(deserializedStringList.size, is(2));
		{
			/** 【補】StringListクラスのシリアライズ検討 */
			// 論理的には、文字列の列を表現している。
			// 物理的には、文字列を双方向リンクリストとして表現している。
			// デフォルトのシリアライズ形式を選択した場合、
			// （文字列データだけが重要なのに、）
			// リンクリスト内のすべてのエントリーおよび双方向のエントリー間のリンクを
			// そのまま反映することになってしまう。
		}
	}

	private static final class StringList implements Serializable {
		private transient int size = 0;
		private transient Entry head = null;

		// このクラスのシリアライズは不要
		private static class Entry {
			String data;
			Entry next;
			Entry previous;
		}

		// 文字列を追加する
		public final void add(String s) {
			size++;

			if (head == null) {
				head = new Entry();
				head.data = s;
				head.next = null;
				head.previous = null;
				return;
			}

			Entry e = head;
			while (e.next != null) {
				e = e.next;
			}

			Entry newEntry = new Entry();
			e.next = newEntry;
			newEntry.data = s;
			newEntry.next = null;
			newEntry.previous = e;
		}

		// privateメソッドだが、シリアライズは公開API相当なので、ドキュメントコメントを書く
		/**
		 * この {@code StringList} インスタンスをシリアライズする。
		 * 
		 * @serialData リストのサイズ（リストが含んでいる文字列の数）を書き出して（{@code int}）、
		 *             適切な順番にすべての要素が続く（個々の要素は{@code String}）。
		 */
		private void writeObject(ObjectOutputStream s) throws IOException {
			s.defaultWriteObject();
			s.writeInt(size);

			// 順に、すべての要素を書き出す
			for (Entry e = head; e != null; e = e.next) {
				s.writeObject(e.data);
			}
		}

		private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
			s.defaultReadObject();
			int numElements = s.readInt();

			// 順に、すべての要素を読み込む
			for (int i = 0; i < numElements; i++) {
				add((String) s.readObject());
			}
		}

		private static final long serialVersionUID = 93248094385L;

		// ... 省略
	}

	@Test
	public void transientの取り扱い() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("フィールドに付加すると、デフォルトシリアライズの対象から外れる。").e();
		o.l2("フィールドに付加すると、defaultWriteObject()の対象から外れる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("論理状態の一部でなければ必ず付加する。").e();
		o.l3("特にカスタム形式であれば、ほぼすべて付けても間違いではない。").e();
		o.l2("これを付加したら、デシリアライズで初期化されることを忘れない。").e();
		o.l3("特にカスタム形式であれば、readObject()のとき、適切な初期値を設定する。").e();

		{
			/** 【補】transientでもdefaultWriteObject()/defaultReadObject()を呼び出す理由 */
			// （StringListクラスのように）すべてのフィールドがtransientだとしても、
			// writeObjectメソッドが最初にdefau1tWriteObjectを呼び出し、
			// readObjectメソッドが最初にdefaultReadObjectを呼び出すことが推奨されている。
			//
			// これは、柔軟性をもたらすためで、後方互換性と前方互換性を保ちながら、
			// 後のリリースでtransientでないインスタンスフィールドを追加することを可能にする。
			//
			// これをしないと、フィールド追加された新しいクラスでシリアライズし、
			// 古いバージョンでデシリアライズすると、StreamCorruptedExceptionで失敗することになる。
		}
	}

	@Test
	public void 同期処理の取り扱い() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("スレッドセーフなオブジェクトでは、シリアライズ処理も（他のメソッド同様）同期が必要。").e();
		o.l2("デフォルトシリアライズ形式か、カスタムシリアライズ形式か、にかかわらず、必要。").e();

		o.l1("【どうすれば？】").e();
		o.l2("synchronizedを付加したwriteObject()を定義する。").e();

		o.l1("【たとえば？】").e();
		o.l2("デフォルトのシリアライズ形式を使用するBarクラスのwriteObject()メソッド。").e();
		Bar bar = new Bar();
		assertThat(bar, anything());
	}

	private static class Bar {
		private synchronized void writeObject(ObjectOutputStream s) throws IOException {
			s.defaultWriteObject();
		}
	}

	@Test
	public void シリアルバージョンUIDの取り扱い() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("作成するすべてのシリアライズ可能なクラスに「明示的な」シリアルバージョンUIDを宣言する。").e();
		o.l2("デフォルトシリアライズ形式か、カスタムシリアライズ形式か、にかかわらず、必要。").e();
		// ex.
		// private static final long serialVersionUID = 1856835860954L;

		o.l1("【どうすれば？】").e();
		o.l2("互換性保ちたいなら、古いクラスのUID値を使い回す。").e();
		o.l2("互換性保ちたくないなら、UIDを変更する。").e();
		o.l3("UIDが合わないと、デシリアライズ時にjava.io.InvalidClassException発生。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("短絡的にデフォルトシリアライズ形式を選択しない。").e();
		o.l1("クラスの論理的内容を考慮して、カスタムシリアライズ形式を検討する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
