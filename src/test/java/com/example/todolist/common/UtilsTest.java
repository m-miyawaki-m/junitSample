package com.example.todolist.common;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

public class UtilsTest {

    // 正常系テスト
    @Test
    public void testStr2Date_validDate() {
        String validDateString = "2023-08-20";
        Date expectedDate = Date.valueOf(validDateString);
        Date actualDate = Utils.str2date(validDateString);
        assertEquals(expectedDate, actualDate);
    }

    // 異常系テスト: 不正な日付フォーマット
    @Test
    public void testStr2Date_invalidDateFormat() {
        String invalidDateString = "20-08-2023";
        Date actualDate = Utils.str2date(invalidDateString);
        assertNull(actualDate); // 無効なフォーマットの場合は null を期待
    }


    // 異常系テスト: 存在しない日付
    @Test
    public void testStr2Date_nonExistentDate() {
        String invalidDateString = "2023-02-30";
        Date actualDate = Utils.str2date(invalidDateString);
        // 存在しない日付が渡された場合は、nullを期待する
        assertNull(actualDate);
    }


    // 境界値テスト: 空文字列
    @Test
    public void testStr2Date_emptyString() {
        String emptyString = "";
        Date actualDate = Utils.str2date(emptyString);
        assertNull(actualDate); // 修正: 空文字列が渡された場合は null を期待する
    }
    
    @Test
    public void testStr2Date_parseException() {
        String invalidDateString = "invalid-date";
        Date actualDate = Utils.str2date(invalidDateString);
        // 無効な日付フォーマットが渡された場合は、ParseExceptionが発生し、nullが返ることを確認
        assertNull(actualDate);
    }

}