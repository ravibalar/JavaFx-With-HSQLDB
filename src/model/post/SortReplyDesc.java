package model.post;

import java.util.Comparator;

// Sort reply by value desc
public class SortReplyDesc implements Comparator<Reply> {

	@Override
	public int compare(Reply reply1, Reply reply2) {
		return ((Double) reply2.getValue()).compareTo(reply1.getValue());
		// return Double.compare(reply2.getValue(), reply1.getValue());
	}
}
