# DbTestUtil
DB test utility by spring and mybatis

## setup

### gradle
#### リポジトリの追加
```
repositories {
    mavenCentral()
    maven {
        url 'https://naosim.github.io/DbTestUtil/'
    }
}
```

#### dependenciesに追加
```
testCompile group: 'com.naosim', name: 'dbtestutil', version: '1.0.0'
```

#### その他dependencies追加  
バージョンはなんでもいいです
```
compile group: 'org.springframework', name: 'spring-context', version: '5.0.5.RELEASE'
compile group: 'org.mybatis', name: 'mybatis', version: '3.4.6'
compile group: 'org.mybatis', name: 'mybatis-spring', version: '1.3.2'
```