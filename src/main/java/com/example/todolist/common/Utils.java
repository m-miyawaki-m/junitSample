package com.example.todolist.common;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  public static Date str2date(String s) {
	    if (s == null || s.isEmpty()) {
	        return null;
	    }
	    try {
	        java.util.Date parsedDate = sdf.parse(s);
	        if (!s.equals(sdf.format(parsedDate))) {
	            return null; // 入力とパース結果が一致しない場合は null を返す
	        }
	        return new Date(parsedDate.getTime());
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
