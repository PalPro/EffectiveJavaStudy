package asakichy.第08章プログラミング一般;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目46従来のforループよりforeachループを選ぶ {

	@Test
	public void foreachループとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("イテレータ変数とインデックス変数を完全に隠蔽したループ構文。").e();

		o.l1("【どうして？】").e();
		o.l2("for文は、while文よりはマシだが、イテレータ変数とインデックス変数によって、コードが散らかる。").e();
		o.l2("foreachによって、この変数にまつわるバグの可能性がなくなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("foreachループ文。").e();
		List<String> strings = Arrays.asList("a", "b", "c");
		StringBuilder concat = new StringBuilder();
		for (String string : strings) {
			concat.append(string);
		}
		assertThat(concat.toString(), is("abc"));
	}

	@Test
	public void ネストしたループ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("foreachのシンプルさの効果は、ネストしたループで顕著に現れる。").e();

		o.l1("【たとえば？】").e();
		Collection<Suit> suits = Arrays.asList(Suit.values());
		Collection<Rank> ranks = Arrays.asList(Rank.values());

		o.l2("バグのあるネストしたforループ文。").e();
		// トランプのカードデッキ作成
		List<Card> deck = new ArrayList<Card>();
		try {
			for (Iterator<Suit> i = suits.iterator(); i.hasNext();) {
				for (Iterator<Rank> j = ranks.iterator(); j.hasNext();) {
					// iだけが進みすぎてしまう。
					deck.add(new Card(i.next(), j.next()));
				}
			}
		} catch (NoSuchElementException e) {
			// iの要素が枯渇する！
		}

		// こうすれば動作するようになる、、、が後述foreachの方が美しい
		deck = new ArrayList<Card>();
		for (Iterator<Suit> i = suits.iterator(); i.hasNext();) {
			Suit suit = i.next();// suitを一時保管
			for (Iterator<Rank> j = ranks.iterator(); j.hasNext();) {
				deck.add(new Card(suit, j.next()));
			}
		}
		assertThat(deck.size(), is(13 * 4));

		o.l2("ネストしたforeachループ文による修正。").e();
		deck = new ArrayList<Card>();
		// シンプルかつ問題なし。
		for (Suit suit : suits) {
			for (Rank rank : ranks) {
				deck.add(new Card(suit, rank));
			}
		}
		assertThat(deck.size(), is(13 * 4));
	}

	enum Suit {
		CLUB, DIAMOND, HEART, SPADE
	}

	enum Rank {
		ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
	}

	@SuppressWarnings("unused")
	private static class Card {
		final Suit suit;
		final Rank rank;

		Card(Suit suit, Rank rank) {
			this.suit = suit;
			this.rank = rank;
		}
	}

	@Test
	public void Iterableを実装する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("自分で使うのみならず、クライアントにforeach文を使用してもらう。").e();

		o.l1("【どうして？】").e();
		o.l2("foreachはIterableを実装しているすべてのクラスで使用できる。").e();

		o.l2("複数要素を含む型を書いている場合、Collectionを実装しないとしても、");
		o.__("Iterableインターフェイス実装を検討する。").e();
	}

	@Test
	public void 適用できない場合() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("foreach文に向いていないケースがある。").e();
		o.l2("１").s("フィルタリング。").e();
		o.l3("ある要素を削除する。").e();
		o.l2("２").s("変換。").e();
		o.l3("ある要素を置換する。").e();
		o.l2("３").s("並列イテレーション。").e();
		o.l3("複数のコレクションを、明示的にインデックスして、並列にイテレートする。").e();

		o.l1("【どうすれば？】").e();
		o.l2("従来のfor文を、気をつけて使用する。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("while文よりfor文、for文よりforeach文を使用する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
