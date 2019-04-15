# ClickableStyckyHeader
Improved sticky List in https://medium.com/@saber.solooki/sticky-header-for-recyclerview-c0eb551c3f68

## Main Idea
The idea is intercept touch in the part where StickyHeader is drown.

I implemented a custom recyclerview

``` Kotlin
StickyRecyclerView: RecyclerView, GestureDetector.OnGestureListener
```

in which I want to intercept tap

```kotlin
override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {

        return recyclerSticky?.let {

            if(e?.y?:0.0f in x..(x+(recyclerSticky?.mStickyHeaderHeight?:0))){
                true
            }else {
                super.onInterceptTouchEvent(e)
            }
        }?: kotlin.run {
            return super.onInterceptTouchEvent(e)
        }

    }
```

so, in this way, i can propagate the tap to user

``` kotlin

override fun onTouchEvent(e: MotionEvent?): Boolean {
        performClick()
        gestureDetector?.onTouchEvent(e)
        return super.onTouchEvent(e)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        recyclerSticky?.propagateTap()
        return true
    }
```

## RecyclerSticky
I had to make changes to RecyclerSticky and StickyHeaderInterface:

``` kotlin
public interface StickyHeaderInterface {

        int getHeaderPositionForItem(int itemPosition);

        int getHeaderLayout(int headerPosition);

        void bindHeaderData(View header, int headerPosition);

        boolean isHeader(int itemPosition);
        
        boolean propatageTap(int headerPosition);
    }
```

In the RecyclerSticky implementation I made this add

``` kotlin
public void propagateTap() {
        mListener.propatageTap(headerPos);
    }
```

so to know last header position I had to save it in a variable

```kotlin
 headerPos = mListener.getHeaderPositionForItem(topChildPosition);
        View currentHeader = getHeaderViewForItem(headerPos, parent);
```

in the `onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)` method.

The improvements are so simple, so I hope you'll enjoy them.

