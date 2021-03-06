[![](https://jitpack.io/v/ooftf/VerticalPagerLayout.svg)](https://jitpack.io/#ooftf/VerticalPagerLayout)
# VerticalPagerLayout
* 类似竖向ViewPager控件
* 支持内部滚动控件
## 注意事项
* VerticalPagerLayout本身没有实现点击事件，如果需要设置点击事件可对Item单独设置
## 效果图
![](https://github.com/ooftf/VerticalPagerLayout/raw/master/art/verticalPagerLayout.gif)
## Gradle配置
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    compile 'com.github.ooftf:VerticalPagerLayout:1.2.2'
    //根据自己项目设置support-v4版本
    compile 'com.android.support:support-v4:26.1.0'
}
```
## 使用方式
* XML布局
```xml
 <com.ooftf.vertical.VerticalPagerLayout
         android:id="@+id/verticalViewPager"
         android:layout_width="match_parent"
         android:layout_height="match_parent"/>
```
* 如果Item布局包含Vertical滚动布局，为了解决触摸冲突问题，需要自己调用VerticalPagerLayout.setScrollEdgeAnalyzer()设置边界分析器
```kotlin
    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>


    viewPager.setScrollEdgeAnalyzer { i, view ->
           ScrollEdgeEngine(viewGroup.findViewById(R.id.recyclerView))
        }

```
* Java部分
```kotlin
verticalPagerLayout.setAdapter(PagerAdapter())
```
## 使用方式2
* XML布局
```xml
 <com.ooftf.vertical.nested.NestedVerticalPagerLayout
         android:id="@+id/verticalViewPager"
         android:layout_width="match_parent"
         android:layout_height="match_parent"/>
```
* 如果Item布局包含Vertical滚动布局，为了解决触摸冲突问题，需要是Nested系列控件

* Java部分
```kotlin
verticalPagerLayout.setAdapter(PagerAdapter())
```
## VerticalPagerLayout 方法
|方法名|描述|
|---|---|
|setAdapter|设置适配器|
|setOffscreenPageLimit|和ViewPager相同，设置上下每侧缓冲Item个数，默认1|
|setCurrentItem|显示指定Item，需要设置Adapter之后调用才有效果|
|setScrollEdgeAnalyzer|设置边界分析器|

