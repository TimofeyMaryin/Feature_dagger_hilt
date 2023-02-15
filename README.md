# Как пользоваться Dagger Hilt.

### Импорт зависимостей.


> **build.gradle (Module:app)**
```
dependencies {
  ...
  implementation "com.google.dagger:hilt-android:2.40.5"
  kapt "com.google.dagger:hilt-android-compiler:2.40.5"
  implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
  kapt "androidx.hilt:hilt-compiler:1.0.0"
  implementation 'androidx.hilt:hilt-navigation-compose:1.0.0'
  ...
}

plugins {
    ...
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

```

> **build.gradle (Project:...)**
```

  buildscript {
    ...
    
    dependencies {
        classpath "com.google.dagger:hilt-android-gradle-plugin:2.40.5"
    }

  }

  plugins {
    ...
    
    id 'com.google.dagger.hilt.android' version '2.44' apply false
  }
  
  task clean(type: Delete) {
    delete rootProject.buildDir
  }

```



### Пояснение Аннотаций
<sub> источник : https://developer.android.com/static/images/training/dependency-injection/hilt-cheatsheet.png </sub>

> **@HiltAndroidApp** - аннотируется класс приложение (Application) Пример:
```
@HiltAndroidApp
class MyApplication: Application() {}
```

> **@Inject** - инъекция конструктора. Сообщает, какой конструктор использовать для предоставления экземпляров и какие зависимости имеет тип. 
Внедрение поля Заполняет поля в классах аннотаций @AndroidEntryPoint. 
**Поле не может быть приватным.**
```
class AnalyticsAdapter @Inject constructor(
  private val service: AnalyticService
) {}

@AndroidEntryPoint
class MyActivity: AppCompactActivity() {
  @Inject lateinit var adapter: AnalyticsAdapter
}
```

> **@HiltViewModel** - сообщает hilt, как предоставлять экземпляры архитектурного компонента ViewModel.

```
@HiltViewModel
class MyViewModel @Inject constructor(
  private val adapter: AnalyticsAdapter,
  private val state: SaveStateHandle
): ViewModel {}
```

> **@Module** - класс, в котором вы можете добавить привязки для типов, которые нельзя внедрить в конструктор.
```
@InstalIn(SingletonComponent::class)
@Module
class AnalyticsModule() { ... }
```


> **@InstalIn** - указывает, в каких контейнерах DI hilt (SingletonComponent в привязках модуля) кода должен быть доступен.
```
@InstalIn(SingletonComponent::class)
@Module
class AnalyticsModule { ... }
```


> **@Provides** - добавляет привязку для типа, который нельзя внедрить в конструктор:
> - Тип возврата — это тип привязки.
> - Параметры являются зависимостями.
> - Каждый раз, когда требуется экземпляр, выполняется тело функции, если тип не ограничен областью действия.

```

@InstalIn(SingletonComponent::class)
@Module
class AnalyticsModule { 
  @Provides
  fun provideAnalyticsService(converterFactory: GsonConverterFactory): AnalyticsService {
    return Retrofit.Builder()
      .baseUrl("...")
      .addConverterFactory(converterFactory)
      .build()
      .create(AnalyticsAPI::class.java)
  }
}

```



> **@Binds** - сокращение для привязки к типу интерфейса:
> - Метод должен быть в модуле.
> - Аннотированные методы @Binds должны быть абстрактными.
> - Тип возврата — это тип привязки.
> - Параметр - тип реализации.

```
@InstalIn(Singleton::class)
@Module
abstract class AnalyticsModule {
  @Binds
  abstract fun bindAnalyticsService(
    analiticsServiceImpl: AnalyticsServiceImpl
  ): AnalyticsService
}
```


### Пояснение от меня.
<sub> Точнее немного от меня, немного от документации в самих файлах Hilt </sub>

**@Module** - аннотация присваивается Сингл классу (object MyObject), который говорит Hilt, что отсюда надо брать зависимости.

**@InstalIn(SingletonComponent::class)** - говорит Hilt какую область видимости сделать для модуля. Аннотация, которая объявляет, в какой компонент(ы) 
должен быть включен аннотированный класс, когда Hilt создает компоненты. Это можно использовать только с классами, 
аннотированными @dagger.Module или @EntryPoint.

**@Provide** - Аннотирует методы модуля для создания привязки метода поставщика. Тип возвращаемого значения метода привязан к возвращаемому значению. 
Реализация компонента будет передавать зависимости методу в качестве параметров.

**@Singleton** - Идентифицирует тип, экземпляр создает только один раз. Не унаследовано.

**@Named(value = "value")** - идентифицирует @Provide.  Применяется в случае, если в модуле Hilt находятся более одного метода возвращаемые один тип данных.

**@AndroidEntryPoint** - Эта аннотация создаст базовый класс, который аннотированный класс должен расширить либо напрямую, либо через плагин Hilt Gradle. 
Этот базовый класс позаботится о внедрении членов в класс Android, а также об обработке экземпляров соответствующих компонентов Hilt в нужный момент жизненного цикла.
