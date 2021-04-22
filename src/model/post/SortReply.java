package model.post;

import java.util.Comparator;

// Sort reply by value asc
public class SortReply implements Comparator<Reply> {

	@Override
	public int compare(Reply reply1, Reply reply2) {
		return ((Double) reply1.getValue()).compareTo(reply2.getValue());
		// return Double.compare(reply1.getValue(), reply2.getValue());
	}
}
