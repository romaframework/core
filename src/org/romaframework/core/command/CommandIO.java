package org.romaframework.core.command;

import java.io.InputStream;
import java.io.PrintStream;

public class CommandIO {

	private InputStream	input;
	private PrintStream	output;
	private PrintStream	error;

	public CommandIO() {
		this.input = System.in;
		this.output = System.out;
		this.error = System.err;
	}

	public CommandIO(InputStream input, PrintStream output, PrintStream error) {
		this.input = input;
		this.output = output;
		this.error = error;
	}

	public PrintStream getError() {
		return error;
	}

	public void setError(PrintStream iStream) {
		error = iStream;
	}

	public PrintStream getOutput() {
		return output;
	}

	public void setOutput(PrintStream iStream) {
		output = iStream;
	}

	public InputStream getInput() {
		return input;
	}

	public void setInput(InputStream input) {
		this.input = input;
	}

	public void line() {
		line(output, 80);
	}

	public void line(PrintStream iStream, int iCols) {
		for (int i = 0; i < iCols; ++i)
			iStream.print("*");
		iStream.println();
	}
}
