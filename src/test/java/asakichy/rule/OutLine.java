package asakichy.rule;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class OutLine extends TestWatcher {
	private StringBuilder outline;

	public OutLine l1(String line) {
		outline.append("-").append(line);
		return this;
	}

	public OutLine l2(String line) {
		outline.append("--").append(line);
		return this;
	}

	public OutLine l3(String line) {
		outline.append("---").append(line);
		return this;
	}

	public OutLine __(String line) {
		return _(line);
	}

	public OutLine _(String line) {
		outline.append(line);
		return this;
	}

	public OutLine e() {
		outline.append("\n");
		return this;
	}

	public OutLine s(String line) {
		outline.append("：").append(line);
		return this;
	}

	@Override
	protected void starting(Description desc) {
		outline = new StringBuilder();
		outline.append("***").append(desc.getMethodName()).append("\n\n");
	}

	@Override
	protected void finished(Description desc) {
		System.out.println(outline.toString());
	}

}
