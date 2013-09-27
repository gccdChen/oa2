package scau.duolian.oa.model;

import java.util.List;

public class CalenderNote {
	public String date;
	public List<Wdrc> wdrcs;
	public CalenderNote(String date, List<Wdrc> wdrcs) {
		super();
		this.date = date;
		this.wdrcs = wdrcs;
	}
}
