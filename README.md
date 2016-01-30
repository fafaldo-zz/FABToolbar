FABToolbar
================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fafaldo/fab-toolbar/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fafaldo/fab-toolbar) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FABToolbar-green.svg?style=flat)](https://android-arsenal.com/details/1/2592)

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
compile 'com.github.fafaldo:fab-toolbar:1.2.0'
```


In order to use FABToolbar you need to implement following view hierarchy in your XML layout file:

```
|
|->FABToolbarLayout
	|
	|-> Some container (that is, or extends RelativeLayout)
	|	|
	|	|-> Your FAB (or other view that extends ImageView)
	|	
	|-> Your Toolbar (view that extends ViewGroup)
		|
		|-> Your elements
		...
```

Starting from version 1.1.0 views do NOT have to be assigned a static ID. Instead try referencing them in XML attributes in FABToolbarLayout, as shown below.

Example implementation:
 
```xml
<com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
	android:id="@+id/fabtoolbar"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	app:showDuration="600"
	app:hideDuration="200"
	app:horizontalMargin="30dp"
	app:verticalMargin="30dp"
	app:fadeInFraction="0.2"
	app:fabId="@+id/fabtoolbar_fab"
	app:containerId="@+id/fabtoolbar_container"
	app:fabToolbarId="@+id/fabtoolbar_toolbar">

	...
	
	<RelativeLayout
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

	</RelativeLayout>

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

To open toolbar call method
```
show();
```

To close toolbar call method
```
hide();
```

In case of other problems with implementation see example included in this repository.


Parameters:
-----

You can control these parameters via XML:

```
<attr name="showDuration" format="integer"/>      	//show animation duration (in ms), default: 600 ms
<attr name="hideDuration" format="integer"/>      	//hide animation duration (in ms), default: 600 ms
<attr name="verticalMargin" format="dimension"/>    //FAB vertical margin (in dp), default: 100 px
<attr name="horizontalMargin" format="dimension"/>  //FAB horizontal margin (in dp), default: 100 px
<attr name="fadeInPivotX" format="dimension"/>    	//toolbar elements translation animation pivot X (in dp), default: 1/2 toolbar width
<attr name="fadeInPivotY" format="dimension"/>    	//toolbar elements translation animation pivot Y (in dp), default: 1/2 toolbar height
<attr name="fadeInFraction" format="float"/>      	//percent of translation animation, between element position and pivot point (float 0.0-1.0), default: 0.2
<attr name="fabId" format="reference"/>			  	//reference to the FAB view
<attr name="containerId" format="reference"/>	  	//reference to the FAB container view
<attr name="fabToolbarId" format="reference"/>    	//reference to the FAB toolbar view
<attr name="fabDrawableAnimationEnabled" format="boolean"/> //enable or disable FAB cross-fade animation, default: true
```


Changelog
---------

* 1.2.0 - most of the issues reported by users fixed - toolbar shadow, wrap_content height, side alignment, OnClickListener etc.
* 1.1.0 - view id constraints removed, removed container layout, fixed FAB shadow problems
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