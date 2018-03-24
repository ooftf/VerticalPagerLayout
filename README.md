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
    compile 'com.github.ooftf:VerticalPagerLayout:1.0.1'
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
* 如果Item布局包含Vertical滚动布局，为了解决触摸冲突问题，需要设置Item根布局为ScrollEdgeLayout,并设置scrollId，如果还是无法解决触摸冲突需要自己实现EdgeWrapper
```xml
<com.ooftf.vertical.ScrollEdgeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:scrollId="@+id/recyclerView">
    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</com.ooftf.vertical.ScrollEdgeLayout>
```
* Java部分
```kotlin
verticalPagerLayout.setAdapter(PagerAdapter())
```
## ScrollEdgeLayout XML属性
|属性名|描述|默认值|
|---|---|---|
|并设置scrollId|指定滚动布局Id(必填)|无|
## VerticalPagerLayout 方法
|方法名|描述|
|---|---|
|setAdapter|设置适配器|
|setOffscreenPageLimit|和ViewPager相同，设置上下每侧缓冲Item个数，默认1|
|setCurrentItem|显示指定Item，需要设置Adapter之后调用才有效果|

