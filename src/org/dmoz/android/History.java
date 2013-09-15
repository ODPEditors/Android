package org.dmoz.android;

import java.util.Stack;

public class History {

	private static boolean	mDebug	= false;

	private Stack<String>		back		= new Stack<String>();
	private Stack<String>		forward	= new Stack<String>();

	public void change(String to) {
		Shared.log("History().change() adding to history: " + to, mDebug);
		push(back, forward, to);
	}

	private void push(Stack<String> Object, Stack<String> aOpositeObject, String aHistoryObj) {
		Shared.log("History().push(): " + aHistoryObj, mDebug);

		if (!Object.empty()) {
			if (!Object.peek().equals(aHistoryObj)) {
				if (aOpositeObject.empty())
					Object.push(aHistoryObj);
				else if (!aOpositeObject.peek().equals(aHistoryObj))
					Object.push(aHistoryObj);
			}

		} else {
			if (aOpositeObject.empty())
				Object.push(aHistoryObj);
			else if (!aOpositeObject.peek().equals(aHistoryObj))
				Object.push(aHistoryObj);
		}
	}

	public String goBack(String from) {
		Shared.log("History().goBack()$from" + from, mDebug);

		push(forward, back, from);
		String to = back.pop();

		while (from.equals(to) && canGoBack("")) {
			push(back, forward, to);
			to = back.pop();
		}

		Shared.log("History().goBack() go back in history to: " + to, mDebug);
		return to;
	}

	public String goForward(String from) {
		Shared.log("History().goForward()$from" + from, mDebug);

		push(back, forward, from);
		String to = forward.pop();

		while (from.equals(to) && canGoForward("")) {
			back.push(to);
			to = forward.pop();
		}

		Shared.log("History().goForward() go forward in history to: " + to, mDebug);
		return to;
	}

	public boolean canGoBack(String from) {
		while (!back.empty() && from.equals(back.peek()))
			back.pop();
		return !back.empty();
	}

	public boolean canGoForward(String from) {
		while (!forward.empty() && from.equals(forward.peek()))
			forward.pop();
		return !forward.empty();
	}

	public void reset() {
		back = new Stack<String>();
		forward = new Stack<String>();
	}
}
