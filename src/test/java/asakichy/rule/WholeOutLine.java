package asakichy.rule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class WholeOutLine extends TestWatcher {
	@Override
	protected void starting(Description desc) {
		StringBuilder header = new StringBuilder();
		header.append("*[オブジェクト指向プログラミング][EffectiveJava]");
		header.append(title(desc.getClassName())).append("\n");
		header.append("**アウトライン").append("\n");
		System.out.println(header.toString());
	}

	@Override
	protected void finished(Description desc) {
		StringBuilder footer = new StringBuilder();
		footer.append("**テストコード").append("\n\n");
		footer.append(">|java|").append("\n");
		footer.append(extractSourceCode(desc.getClassName()));
		footer.append("||<").append("\n");
		System.out.println(footer.toString());
	}

	private String title(String targetTestFQCN) {
		int index = targetTestFQCN.lastIndexOf('.');
		return targetTestFQCN.substring(index + 1/* dot */+ "項目00".length());
	}

	private String extractSourceCode(String targetTestFQCN) {
		try {
			StringBuilder sourcecode = new StringBuilder();
			File file = new File(sourcCodeName(targetTestFQCN));
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				sourcecode.append(line).append("\n");
			}

			br.close();
			return sourcecode.toString();
		} catch (FileNotFoundException e) {
			System.out.println(e);
			return null;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	private String sourcCodeName(String targetTestFQCN) {
		return "src/test/java/" + targetTestFQCN.replace('.', '/') + ".java";
	}

}
