package onedayapp.com.remotedac212.widgets;

import onedayapp.com.remotedac212.*;

import android.animation.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;

/**
 * Created by dangilbert on 05/08/2014.
 */
public class ProgressCircle extends View
{
  private final RectF mOval = new RectF();
  private float mSweepAngle = 0;// 0
  private int startAngle = 211;
  private int angleGap = 4; // 4
  private Bitmap icon;

  private float mEndAngle = 1.0f;

  private Paint progressPaint = new Paint();
  private Paint progressCursorPaint = new Paint();
  private Paint staticBottomPaint = new Paint();
  private Paint staticTopPaint = new Paint();

  private float strokeWidth = 30.0f;
  private float strokeWidthDynamic = 30.0f;

  private float staticAngle;
  private float selectorWidth;

  public ProgressCircle(Context context, AttributeSet attrs)
  {
    super(context, attrs);

    staticAngle = 234; // Static angle bow
    selectorWidth = 15; // Cursor width

    TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
        R.styleable.ProgressCircle, 0, 0);

    strokeWidth = a.getDimension(R.styleable.ProgressCircle_strokeWidth, 30.0f);
    strokeWidthDynamic = a
        .getDimension(R.styleable.ProgressCircle_strokeWidthDynamic, 35.0f);

    // strokeWidth = 105.0f;
    // strokeWidthDynamic = 115.0f;

    progressPaint.setColor(
        getResources().getColor(R.color.didit_grey_selected_circulair));
    progressPaint.setStrokeWidth(strokeWidthDynamic);
    progressPaint.setStyle(Paint.Style.STROKE);
    progressPaint.setPathEffect(new DashPathEffect(new float[] { 30, 10 }, 0));
    progressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

    progressCursorPaint.setColor(getResources().getColor(R.color.orange));
    progressCursorPaint.setStrokeWidth(strokeWidthDynamic);
    progressCursorPaint.setStyle(Paint.Style.STROKE);
    progressCursorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    progressCursorPaint
        .setColor(getResources().getColor(R.color.didit_red_circulair));

    staticBottomPaint
        .setColor(getResources().getColor(R.color.didit_grey_circulair));
    staticBottomPaint.setStrokeWidth(strokeWidth);
    staticBottomPaint.setStyle(Paint.Style.STROKE);
    staticBottomPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

    staticTopPaint
        .setColor(getResources().getColor(R.color.didit_light_grey_circulair));
    staticTopPaint.setStrokeWidth(strokeWidth);
    staticTopPaint.setStyle(Paint.Style.STROKE);
    staticTopPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

    // icon = BitmapFactory.decodeResource(getResources(),
    // R.drawable.ic_placeholder);
  }

  @Override
  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    float currentAngleGap = (mSweepAngle == 1.0f) || (mSweepAngle == 0) ? 0
        : angleGap;
    mOval.set(strokeWidthDynamic / 2, strokeWidthDynamic / 2,
        getWidth() - (strokeWidthDynamic / 2),
        getWidth() - (strokeWidthDynamic / 2));

    // mOval.set(strokeWidth / 2, strokeWidth / 2, getWidth() - (strokeWidth /
    // 2),
    // getWidth() - (strokeWidth / 2));
    canvas.drawArc(mOval, -startAngle + currentAngleGap + staticAngle, 126,
        false, staticBottomPaint);

    canvas.drawArc(mOval, -startAngle + currentAngleGap, staticAngle, false,
        staticTopPaint);

    canvas.drawArc(mOval, -startAngle + currentAngleGap,
        (mSweepAngle * 360) - currentAngleGap, false, progressPaint);

    canvas.drawArc(mOval,
        (-startAngle + currentAngleGap + (mSweepAngle * 360)) - currentAngleGap,
        selectorWidth, false, progressCursorPaint);

    if (icon != null)
    {
      canvas.drawBitmap(icon, (canvas.getWidth() / 2) - (icon.getWidth() / 2),
          strokeWidth + (canvas.getHeight() / 15), null);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
  }

  public void setProgress(float progress)
  {
    if ((progress > 1.0f) || (progress < 0))
    {
      progress = 0; // Show must go on
      // throw new RuntimeException("Value must be between 0 and 1: " +
      // progress);
    }

    mEndAngle = progress;
    this.invalidate();
  }

  public void startAnimation()
  {
    ValueAnimator anim = ValueAnimator.ofFloat(mSweepAngle, mEndAngle);
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator)
      {
        ProgressCircle.this.mSweepAngle = (Float) valueAnimator
            .getAnimatedValue();
        ProgressCircle.this.invalidate();
      }
    });
    anim.setDuration(50); // Was 500
    anim.setInterpolator(new AccelerateDecelerateInterpolator());
    anim.start();

  }

}
