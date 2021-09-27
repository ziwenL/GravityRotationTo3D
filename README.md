# GravityRotationTo3D
<p>本项目是基于 <b> SensorManager + Scroller </b> 实现高仿自如裸眼 3D 效果 </p>

:star:<a href="https://blog.csdn.net/lzw398756924/article/details/120492750">博客地址</a>:star:

 ### 效果比对
![自如效果](https://img-blog.csdnimg.cn/8be7e502428141a5b7a428f1a4327814.gif)
![仿现效果](https://img-blog.csdnimg.cn/cfabc31357aa4e428ed2dd6e41c57a20.gif)
<h6>(:star:左为自如效果，:star:右为该项目高仿效果)</h6>

### 具体实现
<ul>
<li>
<p>将普通 2D 图层分割为三层：前景、中景及后景，（本项目示例中使用到的图层元素均来自自如 APP，接口拦截得到的）</p>
</li>
<li>
<p>主要是通过 <b>加速度传感器</b> 和 <b>磁场传感器</b> 感知设置倾斜角度，计算出前景和后景 View 在 X 、 Y 轴上应该移动的距离，中景保持不动，从而增加景深，实现伪 3D 效果</p>
</li>
<li>
<p>创建工具类 <b>GravityRotationHelper</b> 在此类中通过 <b>Lifecycle</b> 更便捷的在上下文对象的生命周期中注册与反注册传感器监听，得到需要的传感器数值</p>
</li>
<li>
<p>自定义 <b>GravityRotationImageView</b> 处理传感器数值，并判断当前 View 为前景还是后景，再通过 <b>Scroller</b> 辅助进行对应方向上的滚动</p>
</li>
</ul>

### 使用步骤
<ul>
<li>
<p>1.复制类 <a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/library/src/main/java/com/ziwenl/library/GravityRotationHelper.kt" rel="nofollow">GravityRotationHelper</a> 和 <a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/library/src/main/java/com/ziwenl/library/GravityRotationImageView.kt" rel="nofollow">GravityRotationImageView</a> 到项目中，并将其<a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/library/src/main/res/values/attrs.xml" rel="nofollow"> 自定义属性 </a>复制至 attrs 中</p>
</li>
<li>
<p>2.在布局中使用 <b>GravityRotationImageView</b> 装填前景及后景图片（将 GravityRotationImageView 当 ImageView 用即可）</p>

```
给后景设置 isBack 属性为 true 或在代码中进行设置
```
</li>
<li>
<p>3.创建 <b>GravityRotationHelpe</b>r 对象并绑定前后景 View : </p>

```
GravityRotationHelper(this).attachViews(viewBinding.ivFront, viewBinding.ivBack)
```
</li>
</ul>

### 使用参考
<ul>
<li>
<p>页面内只有一组图象需要实现该效果：<a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/app/src/main/java/com/ziwenl/gravityrotationto3d/ui/SinglePageActivity.kt" rel="nofollow">SinglePageActivity</a> 、 <a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/app/src/main/res/layout/activity_singlepage.xml" rel="nofollow">activity_singlepage</a></p>
</li>
<li>
<p>在 banner 中实现该效果：<a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/app/src/main/java/com/ziwenl/gravityrotationto3d/ui/BannerActivity.kt" rel="nofollow">BannerActivity</a> 、 <a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/app/src/main/res/layout/activity_banner.xml" rel="nofollow">activity_banner</a> 、 <a href="https://github.com/ziwenL/GravityRotationTo3D/blob/master/app/src/main/res/layout/item_banner.xml" rel="nofollow">item_banner</a> </p>
</li>
</ul>

### About Me
<ul>
<li>
<p>Email: ziwen.lan@foxmail.com</p>
</li>
</ul>
