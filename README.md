# VerticalPagerLayout
* 类似一个竖向的ViewPager
* 支持内部控件滚动
# 注意事项
* VerticalPagerLayout本身没有实现点击事件，如果需要设置点击事件可对Item单独设置
# 效果图
![](https://github.com/ooftf/VerticalPagerLayout/raw/master/art/verticalPagerLayout.gif)
# Gradle配置
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    compile 'com.github.ooftf:VerticalPagerLayout:1.0.0'
}
```
# 使用方式
```xml
 <com.ooftf.spiale.SpialeLayout
        app:scrollMillis="1000"
        app:showMillis="3000"
        android:layout_marginTop="36dp"
        android:id="@+id/spialeLayout"
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:background="#FFFFFF">
    </com.ooftf.spiale.SpialeLayout>
```
```kotlin
    BaseAdapter adapter = SpialeAdapter(this)
    spialeLayout.adapter  = adapter
    spialeLayout.setOnItemClickListener{ position, _, itemData ->
                itemData as Bean
                Toast.makeText(this,"$position :: ${itemData.text}",Toast.LENGTH_SHORT).show()
    }
```
# XML属性
|属性名|描述|默认值|
|---|---|---|
|scrollMillis|滚动动画时间（毫秒）|2000|
|showMillis|停止展示时间（毫秒）|2000|
# SpialeLayout方法
|方法名|描述|
|---|---|
|setAdapter|设置适配器|
|setOnItemClickListener|设置Item点击时间|
