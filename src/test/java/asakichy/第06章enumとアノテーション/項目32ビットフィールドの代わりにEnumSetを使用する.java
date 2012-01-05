package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.EnumSet;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目32ビットフィールドの代わりにEnumSetを使用する {

	@Test
	public void EnumSetとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("単一のenum型から選ばれた値の集合を効率的に表現するクラス。").e();

		o.l1("【どうして？】").e();
		o.l2("enum型のまま、ビット演算が可能。").e();

		o.l1("【たとえば？】").e();
		o.l2("Flag列挙型のビット演算。").e();
		EnumSet<Flag> f1_f3 = EnumSet.of(Flag.F1, Flag.F3);
		assertThat(f1_f3.contains(Flag.F1), is(true));
		assertThat(f1_f3.contains(Flag.F2), is(false));
		assertThat(f1_f3.contains(Flag.F3), is(true));
		assertThat(f1_f3.contains(Flag.F4), is(false));
	}

	enum Flag {
		F1, F2, F3, F4
	}

	@Test
	public void ビットフィールドの代替() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ビット演算を使用して、集合操作を行う場合、ビットフィールドよりEnumSetを利用する。").e();

		o.l1("【どうして？】").e();
		o.l2("従来のint定数ベースの「ビットフィールド」に対する、高品質かつ型保証された代替。").e();
		o.l2("ビット演算を高レベルなAPIで完全に隠蔽。").e();
		o.l2("内部表現がlongなので、64個までならパフォーマンスも遜色ない。").e();

		o.l1("【たとえば？】").e();
		o.l2("テキスト装飾スタイルをもつTextクラス。").e();

		// int定数版
		int boldUnderline = Text.STYLE_BOLD | Text.STYLE_UNDERLLINE;
		assertThat(Text.bold(boldUnderline), is(true));

		// enum版
		EnumSet<Text.Style> boldItalic = EnumSet.of(Text.Style.BOLD, Text.Style.ITALIC);
		assertThat(Text.bold(boldItalic), is(true));

		o.l1("【どうすれば？】").e();
		o.l2("不変なEnumSetは作れないが、java.util.Collections.unmodifiableSetで包めばいいだけ。").e();
	}

	@SuppressWarnings("unused")
	private static class Text {
		// int定数版
		public static final int STYLE_BOLD = 1 << 0; // 1
		public static final int STYLE_ITALIC = 1 << 1; // 2
		public static final int STYLE_UNDERLLINE = 1 << 2; // 4
		public static final int STYLE_STRIKETHROUGH = 1 << 3; // 8

		public static boolean bold(int styles) {
			return (styles & STYLE_BOLD) != 0;
		}

		// enum版
		public enum Style {
			BOLD, ITALIC, UNDERLINE, STRIKETHROUGH
		}

		public static boolean bold(Set<Style> styles) {
			return styles.contains(Style.BOLD);
		}

	}

	@Test
	public void まとめ() throws Exception {
		o.l1("EnumSetは、ビットフィールドの簡潔性とパフォーマンスを、");
		o.__("enumの利点を損なわずに実現する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
