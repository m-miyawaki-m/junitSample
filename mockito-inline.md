オフラインでビルドする際には、Mavenの`-o`（オフラインモード）オプションを使用して、既にローカルにダウンロードされた依存関係のみを使用してビルドを行います。オフラインモードでビルドを行うためのコマンドは以下の通りです。

### オフラインビルドコマンド
```bash
mvn clean install -o
```

### コマンドの詳細
- `clean`: 以前のビルド成果物（`target`ディレクトリなど）を削除します。
- `install`: プロジェクトをコンパイルし、テストを実行し、成果物をローカルリポジトリにインストールします（通常のMavenビルド）。
- `-o`: オフラインモードを指定し、ローカルリポジトリに既にダウンロードされている依存関係のみを使用します。

### 事前準備
オフラインビルドを実行する前に、すべての依存関係がローカルリポジトリに存在している必要があります。そのため、初回はインターネット接続がある状態で以下のコマンドを実行して、依存関係をダウンロードしておくことが重要です。

#### 依存関係の事前ダウンロード
```bash
mvn dependency:go-offline
```

このコマンドにより、プロジェクトで必要なすべての依存関係がダウンロードされ、ローカルリポジトリにキャッシュされます。その後、オフラインでビルドを行うことが可能になります。

```pom.xml
        <!-- JUnit 4 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <!-- <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>2.28.2</version>
            <scope>test</scope> -->
            <groupId>org.mockito</groupId>
            <artifactId>mockito-inline</artifactId>
            <version>2.28.2</version> <!-- もしくは最新のバージョンを指定 -->
            <scope>test</scope>
        </dependency>
```

```テスト対象クラス
package com.example.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SampleDao {
    public String getSample() {
        final Log logger = LogFactory.getLog(this.getClass());
        if (logger.isDebugEnabled()) {
            logger.debug("getSample() called");
        }
        return "sample";
    }
}

```

```テストクラス
package com.example.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.mockito.MockedStatic;

public class SampleDaoTest {

    @Test
    public void testGetSample() {
        // モック化されたLogを用意する
        Log mockLog = mock(Log.class);
        when(mockLog.isDebugEnabled()).thenReturn(true);

        // LogFactory.getLog()のモック化
        try (MockedStatic<LogFactory> mockedStaticLogFactory = mockStatic(LogFactory.class)) {
            mockedStaticLogFactory.when(() -> LogFactory.getLog(SampleDao.class)).thenReturn(mockLog);

            // テスト対象のインスタンスを生成
            SampleDao sampleDao = new SampleDao();

            // getSample()を実行
            String result = sampleDao.getSample();

            // 期待される結果を検証
            assertEquals("sample", result);

            // デバッグメッセージが出力されたかを確認
            verify(mockLog).debug("getSample() called");
        }
    }

    @Test
    public void testGetSampleWhenDebugDisabled() {
        // モック化されたLogを用意する
        Log mockLog = mock(Log.class);
        when(mockLog.isDebugEnabled()).thenReturn(false); // デバッグログが無効の場合

        // LogFactory.getLog()のモック化
        try (MockedStatic<LogFactory> mockedStaticLogFactory = mockStatic(LogFactory.class)) {
            mockedStaticLogFactory.when(() -> LogFactory.getLog(SampleDao.class)).thenReturn(mockLog);

            // テスト対象のインスタンスを生成
            SampleDao sampleDao = new SampleDao();

            // getSample()を実行
            String result = sampleDao.getSample();

            // 期待される結果を検証
            assertEquals("sample", result);

            // デバッグメッセージが出力されないことを確認
            verify(mockLog, never()).debug("getSample() called");
        }
    }
}
```
