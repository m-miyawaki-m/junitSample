import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.todolist.common.UtilsInnerClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilsInnerClass.class})
public class UtilsInnerClassTest {

    private UtilsInnerClass utilsInnerClass;

    @Before
    public void setUp() throws Exception {
        // シングルトンインスタンスをモック
        PowerMockito.mockStatic(UtilsInnerClass.class);
        utilsInnerClass = PowerMockito.mock(UtilsInnerClass.class);

        // getInstanceをモックして、モックインスタンスを返すように設定
        when(UtilsInnerClass.getInstance()).thenReturn(utilsInnerClass);
    }

    @Test
    public void testUserUtils() throws Exception {
        // リフレクションを使ってプライベートなUserクラスのインスタンスを作成
        Class<?> userClass = Class.forName("com.example.todolist.common.UtilsInnerClass$User");
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
        Object userInstance = userClass.getDeclaredConstructor(String.class, Date.class).newInstance("Alice", birthDate);

        // プライベートフィールドにアクセス
        Field nameField = userClass.getDeclaredField("name");
        nameField.setAccessible(true);
        assertEquals("Alice", nameField.get(userInstance));

        Field birthDateField = userClass.getDeclaredField("birthDate");
        birthDateField.setAccessible(true);
        assertEquals(birthDate, birthDateField.get(userInstance));
    }

    @Test
    public void testStr2date() throws Exception {
        // 内部Utilsクラスのインスタンスをリフレクションで取得
        Class<?> utilsClass = Class.forName("com.example.todolist.common.UtilsInnerClass$Utils");
        Object utilsInstance = utilsClass.getDeclaredConstructor(UtilsInnerClass.class).newInstance(utilsInnerClass);

        // str2dateメソッドにアクセスして実行
        Method str2dateMethod = utilsClass.getDeclaredMethod("str2date", String.class);
        str2dateMethod.setAccessible(true);

        Date date = (Date) str2dateMethod.invoke(utilsInstance, "2021-01-01");
        assertNotNull(date);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01"), date);
    }
}
