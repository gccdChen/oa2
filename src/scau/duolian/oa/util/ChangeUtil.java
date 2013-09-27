package scau.duolian.oa.util;

import java.util.ArrayList;
import java.util.List;

import scau.duolian.oa.model.CalenderNote;
import scau.duolian.oa.model.Wdrc;

/**
 * 公用转换类
 * 	wdrc-》calenderNote
 * @author gccd
 *
 */
public class ChangeUtil {
	public static List<CalenderNote> Wdrcs2Cals(List<Wdrc> wdrcs){
		List<CalenderNote> calenderNotes = new  ArrayList<CalenderNote>();
		CalenderNote calenderNote = null;
		for(int i = 0 ;i < wdrcs.size(); i++){
			Wdrc wdrc = wdrcs.get(i);
			String curdate = null;
			String xdrq = DateUtil.strTostr(wdrc.xdsj);
			if(!xdrq.equals(curdate)){
				calenderNote = new CalenderNote(xdrq, new ArrayList<Wdrc>());
				calenderNote.wdrcs.add(wdrc);
			}
			else if(DateUtil.strTostr(wdrc.xdsj).equals(curdate)){
				calenderNote.wdrcs.add(wdrc);
			}
		}
		return calenderNotes;
	}
}
