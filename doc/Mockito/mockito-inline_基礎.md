`Mockito-inline`は、標準のMockitoライブラリに静的・最終メソッドのモック化を可能にする機能を追加したバージョンです。通常のMockitoでは`final`や`static`メソッドのモック化はできませんが、`mockito-inline`を利用することでそれが可能になります。ここでは、`Mockito-inline`で行える機能と、よく利用されるケースを紹介します。

### 1. **`final`クラス・`final`メソッドのモック化**

Mockito-inlineでは、`final`なクラスやメソッドをモック化できます。これにより、特定のライブラリやクラスが`final`修飾されている場合でも、それらをモック化してテストを行うことが可能です。

#### 例: `final`メソッドのモック化

```java
public class FinalClass {
    public final String finalMethod() {
        return "Original Value";
    }
}

// テスト
import static org.mockito.Mockito.*;

public class FinalClassTest {

    @Test
    public void testFinalMethod() {
        FinalClass finalClass = mock(FinalClass.class);

        // finalメソッドをモック化
        when(finalClass.finalMethod()).thenReturn("Mocked Value");

        // テスト
        assertEquals("Mocked Value", finalClass.finalMethod());
    }
}
```

### 2. **`static`メソッドのモック化**

Mockito-inlineでは、静的メソッドのモック化もサポートしています。通常のMockitoでは静的メソッドをモックするにはPowerMockitoが必要でしたが、Mockito-inlineを使用することで、静的メソッドのモック化が可能です。

#### 例: `static`メソッドのモック化

```java
public class StaticClass {
    public static String staticMethod() {
        return "Original Value";
    }
}

// テスト
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

public class StaticClassTest {

    @Test
    public void testStaticMethod() {
        try (MockedStatic<StaticClass> mockedStatic = mockStatic(StaticClass.class)) {
            // staticメソッドをモック化
            mockedStatic.when(StaticClass::staticMethod).thenReturn("Mocked Value");

            // テスト
            assertEquals("Mocked Value", StaticClass.staticMethod());
        }
    }
}
```

### 3. **コンストラクタのモック化**

Mockito-inlineでは、クラスのインスタンス化をモック化することができ、特定のコンストラクタをモック化することも可能です。これにより、クラスのコンストラクタが呼ばれた際に特定の振る舞いを強制できます。

#### 例: コンストラクタのモック化

```java
public class ClassWithConstructor {
    public ClassWithConstructor(String value) {
        // コンストラクタのロジック
    }

    public String someMethod() {
        return "Real Value";
    }
}

// テスト
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockConstruction;

public class ConstructorMockTest {

    @Test
    public void testConstructorMock() {
        try (MockedConstruction<ClassWithConstructor> mocked = mockConstruction(ClassWithConstructor.class)) {
            ClassWithConstructor instance = new ClassWithConstructor("mocked");
            when(instance.someMethod()).thenReturn("Mocked Value");

            // テスト
            assertEquals("Mocked Value", instance.someMethod());
        }
    }
}
```

### 4. **`Object`クラスのメソッドのモック化**

`Mockito-inline`を使うと、`equals`、`hashCode`、`toString`などの`Object`クラスに定義されたメソッドもモック化することができます。これは、通常のMockitoではサポートされていない機能です。

#### 例: `toString`メソッドのモック化

```java
import static org.mockito.Mockito.*;

public class ObjectMethodMockTest {

    @Test
    public void testToStringMethod() {
        Object obj = mock(Object.class);

        // toStringメソッドをモック化
        when(obj.toString()).thenReturn("Mocked toString");

        // テスト
        assertEquals("Mocked toString", obj.toString());
    }
}
```

### 5. **スパイオブジェクト (`Spy`) の改善されたサポート**

Mockito-inlineでは、`Spy`（部分的なモック）の機能も強化されています。スパイオブジェクトは通常のインスタンスの一部のメソッドを実際の動作で、他のメソッドをモック化しますが、Mockito-inlineでは`final`クラスやメソッドもスパイできます。

#### 例: `final`メソッドを含むスパイ

```java
public class FinalClass {
    public final String finalMethod() {
        return "Original Value";
    }

    public String regularMethod() {
        return "Real Value";
    }
}

// テスト
import static org.mockito.Mockito.*;

public class FinalClassSpyTest {

    @Test
    public void testFinalMethodInSpy() {
        FinalClass finalClass = spy(new FinalClass());

        // finalメソッドをモック化
        when(finalClass.finalMethod()).thenReturn("Mocked Value");

        // 通常のメソッドは実際の動作を行う
        assertEquals("Real Value", finalClass.regularMethod());

        // finalメソッドはモックされた動作
        assertEquals("Mocked Value", finalClass.finalMethod());
    }
}
```

### 6. **`Mocking Details`を使用したモックの詳細情報の取得**

Mockito-inlineでは、`MockingDetails`を使用して、モックオブジェクトに関する詳細な情報を取得することができます。これは、デバッグやモックがどのように使用されているかを確認する際に役立ちます。

#### 例: モックの詳細情報を取得

```java
import static org.mockito.Mockito.*;

public class MockingDetailsTest {

    @Test
    public void testMockingDetails() {
        List<String> mockList = mock(List.class);

        // モックの呼び出し
        mockList.add("item");

        // MockingDetailsを使用してモックの詳細を取得
        MockingDetails details = mockingDetails(mockList);

        // モックが呼び出されたかの確認
        assertTrue(details.isMock());
        assertTrue(details.getInvocations().size() > 0);
    }
}
```

### まとめ

`Mockito-inline`で利用できる、よく使われる機能のまとめは以下の通りです:

1. **`final`クラス・メソッドのモック化**
2. **`static`メソッドのモック化**
3. **コンストラクタのモック化**
4. **`Object`クラスのメソッドのモック化（`equals`、`hashCode`、`toString`）**
5. **スパイオブジェクトのサポート (`final`メソッド含む)**
6. **`MockingDetails`を使ったモックの詳細情報の取得**

これらの機能により、`mockito-inline`は`final`や`static`な要素を含むクラスのテストや、スパイオブジェクトのテストにおいて非常に強力なツールとなります。


通常、Javaでは`final`フィールドは一度だけ代入が許可され、その後に値を変更することはできません。`final`フィールドは基本的に不変として扱われ、コンストラクタ内やフィールドの初期化時にのみ代入可能です。

しかし、テストや特殊な状況で`final`フィールドに値を代入する必要がある場合には、リフレクションを使用して無理やり値を変更することができます。これによって通常のルールを回避できますが、この手法はリフレクションの使用による副作用を伴うため、通常の設計やユニットテストでは推奨されません。

### 1. **通常の`final`フィールドの初期化**

通常、`final`フィールドはクラス定義時やコンストラクタ内で初期化され、再代入はできません。

```java
public class MyClass {
    private final String finalField;

    public MyClass(String value) {
        this.finalField = value;  // コンストラクタで初期化
    }

    public String getFinalField() {
        return finalField;
    }
}
```

このように、`final`フィールドはクラスのインスタンス化時にしか値を設定できません。

### 2. **リフレクションを使った`final`フィールドへの代入**

`final`フィールドに代入する必要がある場合、Javaのリフレクションを使って強制的に変更することが可能です。リフレクションを使用すると、Javaのアクセス制限や`final`の特性を無視して、フィールドに直接アクセスできます。

#### 例: リフレクションを使った`final`フィールドへの代入

```java
import java.lang.reflect.Field;

public class MyClass {
    private final String finalField;

    public MyClass(String value) {
        this.finalField = value;
    }

    public String getFinalField() {
        return finalField;
    }
}

public class FinalFieldTest {
    public static void main(String[] args) throws Exception {
        MyClass obj = new MyClass("Initial Value");

        // リフレクションを使用してfinalフィールドにアクセス
        Field field = MyClass.class.getDeclaredField("finalField");
        field.setAccessible(true);

        // final修飾子を無視して値を変更
        field.set(obj, "Modified Value");

        // 値を確認
        System.out.println(obj.getFinalField());  // "Modified Value"
    }
}
```

### 3. **リフレクションを使う際の注意点**

リフレクションを使って`final`フィールドに値を代入することは可能ですが、以下の点に注意する必要があります。

1. **セキュリティリスク**: リフレクションはJavaのセキュリティモデルをバイパスするため、不必要に使用するとセキュリティリスクが高まる可能性があります。プロダクションコードでは避けるべきです。
   
2. **メンテナンス性の低下**: リフレクションを使用するとコードの可読性や保守性が低下します。コードを読む開発者にとって意図が分かりにくくなるため、通常の状況では避けるべきです。

3. **最適化の問題**: JVMは`final`フィールドを最適化するため、リフレクションで変更された値が必ずしも正しく反映されるとは限りません。最適化の影響を受ける可能性があるため、予期せぬ動作を引き起こす可能性があります。

### まとめ

- **通常の状況では、`final`フィールドには一度しか値を設定できず、再代入は不可です。**
- **リフレクションを使用することで、`final`フィールドに強制的に値を代入することができますが、この方法は推奨されません。**
- リフレクションは、テストや特殊な状況でのみ使用し、通常のコードでは使わない方がよいです。設計上、`final`フィールドの値を変更する必要がある場合は、設計そのものを見直す方が理想的です。