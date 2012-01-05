package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目33序数インデックスの代わりにEnumMapを使用する {

	@Test
	public void EnumMapとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("enumをキーとして使用するように設計された非常に高速なMap実装。").e();

		o.l1("【たとえば？】").e();
		o.l2("HttpStatus列挙型をキーにしたEnumMap。").e();

		Map<HttpStatus, String> messages = new EnumMap<HttpStatus, String>(HttpStatus.class);
		messages.put(HttpStatus.OK, "Success(200)");
		messages.put(HttpStatus.NOTFOUND, "Not Found(404)");
		messages.put(HttpStatus.SERVER_ERROR, "Internal Server Error(500)");

		assertThat(messages.get(HttpStatus.OK), is("Success(200)"));
		assertThat(messages.get(HttpStatus.NOTFOUND), is("Not Found(404)"));
		assertThat(messages.get(HttpStatus.SERVER_ERROR), is("Internal Server Error(500)"));
	}

	enum HttpStatus {
		OK, NOTFOUND, SERVER_ERROR
	}

	@Test
	public void EnumMap_序数インデックスの代替() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("配列のindexにenumの序数を使用してはいけない。").e();

		o.l1("【どうして？】").e();
		o.l2("intは型安全を提供しない。").e();
		o.l2("順番がずれたら、「黙って間違ったことをする」か「ArrayIndexOutOfBoundsException」発生。").e();

		o.l1("【どうすれば？】").e();
		o.l2("代わりにEnumMapを使用する。").e();
		o.l3("キーにenumをそのまま使える。").e();
		o.l2("内部的には配列使っているので、パフォーマンスも良い。").e();

		o.l1("【たとえば？】").e();
		o.l2("例１").s("種類（Herb.Type列挙型）ごとのHerb集合を管理するEnumMap。").e();

		// 庭にあるハーブ
		Herb[] garden = { new Herb("Basil", Herb.Type.ANNUAL), new Herb("Carroway", Herb.Type.BIENNIAL),
				new Herb("Dill", Herb.Type.ANNUAL), new Herb("Lavendar", Herb.Type.PERENNIAL),
				new Herb("Parsley", Herb.Type.BIENNIAL), new Herb("Rosemary", Herb.Type.PERENNIAL) };

		// 種別ごとに管理（Herb.Type.classは境界型トークン）
		Map<Herb.Type, Set<Herb>> herbsByType = new EnumMap<Herb.Type, Set<Herb>>(Herb.Type.class);

		for (Herb.Type t : Herb.Type.values()) {
			herbsByType.put(t, new HashSet<Herb>());
		}

		for (Herb h : garden) {
			herbsByType.get(h.type).add(h);
		}

		assertThat(herbsByType.get(Herb.Type.ANNUAL), containsInAnyOrder(garden[0], garden[2]));

		o.l2("例２").s("物質三相（Phase列挙型）とその相転移（Transition列挙型）を管理するEnumMap。").e();
		// 気体から液体は「溶解」
		assertThat(Phase.Transition.from(Phase.SOLID, Phase.LIQUID), is(Phase.Transition.MELT));
	}

	private static class Herb {
		public enum Type {
			// 一年生
			ANNUAL,
			// 多年生
			PERENNIAL,
			// 越年生
			BIENNIAL
		}

		private final String name;
		private final Type type;

		Herb(String name, Type type) {
			this.name = name;
			this.type = type;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// 物質三相
	enum Phase {
		SOLID, // 固体
		LIQUID, // 液体
		GAS; // 気体

		// 相転移
		enum Transition {
			MELT(SOLID, LIQUID), // 溶解
			FREEZE(LIQUID, SOLID), // 凍結
			BOIL(LIQUID, GAS), // 気化
			CONDENSE(GAS, LIQUID), // 液化
			SUBLIME(SOLID, GAS), // 昇華
			DEPOSIT(GAS, SOLID); // 凝固

			private final Phase src;
			private final Phase dst;

			Transition(Phase src, Phase dst) {
				this.src = src;
				this.dst = dst;
			}

			// 相ごとの転移管理
			private static final Map<Phase, Map<Phase, Transition>> m = new EnumMap<Phase, Map<Phase, Transition>>(Phase.class);

			static {
				for (Phase p : Phase.values()) {
					// キーは、転移元相
					// 値のEnumMapは、転移先相がキーで、相転移が値
					m.put(p, new EnumMap<Phase, Transition>(Phase.class));
				}
				for (Transition trans : Transition.values()) {
					m.get(trans.src).put(trans.dst, trans);
				}
			}

			public static Transition from(Phase src, Phase dst) {
				// 転移元->転移先から相転移を取得
				return m.get(src).get(dst);
			}
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("データのMap管理の必要がある場合、");
		o.__("キーの数があらかじめ把握できているなら、");
		o.__("キーをenum型にして、EnumMapを検討する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
