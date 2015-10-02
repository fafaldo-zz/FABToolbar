FABToolbar
================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fafaldo/fab-toolbar/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fafaldo/fab-toolbar)

An implementation of Google design, with Floating Action Button transforming into toolbar.

![Illustration of behavior](https://github.com/fafaldo/FABToolbar/blob/master/fabtoolbar.gif "Illustration of behavior")


Features:
--------------

- morphing Floating Action Button (or any other view that extends ImageView) into custom toolbar
- FAB image fade-out animation
- toolbar elements fade-in animation
- toolbar elements translation animation


How to use
----------

Import dependency using Gradle:

```
compile 'com.github.fafaldo:fab-toolbar:1.0.1'
```


In order to use FABToolbar you need to implement following view hierarchy in your XML layout file:

```
|
|->FABToolbarLayout
	|
	|-> FABContainer (id: fabtoolbar_container)
	|	|
	|	|-> Your FAB (or other view that extends ImageView; id: fabtoolbar_fab)
	|	
	|-> Your Toolbar (view that extends ViewGroup; id: fabtoolbar_toolbar)
		|
		|-> Your elements
		...
```

Remember to assign each element a proper ID.

Example implementation:
 
```xml
<com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
	android:id="@+id/fabtoolbar"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	app:showDuration="600"
	app:hideDuration="200"
	app:rightMargin="30dp"
	app:bottomMargin="30dp"
	app:fadeInFraction="0.2">

	...
	
	<com.github.fafaldo.fabtoolbar.widget.FABContainer
		android:id="@+id/fabtoolbar_container"
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:layout_alignParentBottom="true"
		android:layout_alignParentRight="true">

		<android.support.design.widget.FloatingActionButton
			android:id="@+id/fabtoolbar_fab"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			app:fabSize="normal"
			android:src="@drawable/abc_ic_menu_share_mtrl_alpha"/>

	</com.github.fafaldo.fabtoolbar.widget.FABContainer>

	<LinearLayout
		android:id="@+id/fabtoolbar_toolbar"
		android:layout_width="match_parent"
		android:layout_height="70dp"
		android:layout_alignParentBottom="true"
		android:orientation="horizontal">

		<ImageView
			android:id="@+id/one"
			android:layout_width="0dp"
			android:layout_height="match_parent"
			android:layout_weight="1"
			android:scaleType="centerInside"
			android:src="@drawable/test"/>
		...
		
	</LinearLayout>

</com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout>
```

Toolbar will be automatically opened on FAB click. If you want to do it manually use function
```
show();
```

To close toolbar call function
```
hide();
```

In case of other problems with implementation see example included in this repository.


Parameters:
-----

You can control these parameters via XML:

```
<attr name="showDuration" format="integer"/>      //show animation duration (in ms), default: 600 ms
<attr name="hideDuration" format="integer"/>      //hide animation duration (in ms), default: 600 ms
<attr name="bottomMargin" format="dimension"/>    //FAB bottom margin (in dp), default: 100 px
<attr name="rightMargin" format="dimension"/>     //FAB right margin (in dp), default: 100 px
<attr name="fadeInPivotX" format="dimension"/>    //toolbar elements translation animation pivot X (in dp), default: 1/2 toolbar width
<attr name="fadeInPivotY" format="dimension"/>    //toolbar elements translation animation pivot Y (in dp), default: 1/2 toolbar height
<attr name="fadeInFraction" format="float"/>      //percent of translation animation, between element position and pivot point (float 0.0-1.0), default: 0.2
```


Changelog
---------

* 1.0 - initial release


License
----

FABToolbar for Android

The MIT License (MIT)

Copyright (c) 2015 Rafał Tułaza

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.