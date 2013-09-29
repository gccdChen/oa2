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
		String curdate = null;
		for(int i = 0 ;i < wdrcs.size(); i++){
			Wdrc wdrc = wdrcs.get(i);
			String xdrq = DateUtil.strTostr(wdrc.xdsj);
			if(!curdate.equals(xdrq)){
				if(calenderNote != null)
					calenderNotes.add(calenderNote);
				calenderNote = new CalenderNote(xdrq, new ArrayList<Wdrc>());
				calenderNote.wdrcs.add(wdrc);
				curdate = xdrq;
			}else{
				calenderNote.wdrcs.add(wdrc);
			}
		}
		if(calenderNote != null)
			calenderNotes.add(calenderNote);
		return calenderNotes;
	}
}
