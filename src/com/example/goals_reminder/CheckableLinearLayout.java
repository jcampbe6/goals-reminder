package com.example.goals_reminder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

public class CheckableLinearLayout
  extends LinearLayout
  implements Checkable
{
  private CheckedTextView _checkbox;
  
  public CheckableLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public boolean isChecked()
  {
    if (this._checkbox != null) {
      return this._checkbox.isChecked();
    }
    return false;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    int i = getChildCount();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return;
      }
      View localView = getChildAt(j);
      if ((localView instanceof CheckedTextView)) {
        this._checkbox = ((CheckedTextView)localView);
      }
    }
  }
  
  public void setChecked(boolean paramBoolean)
  {
    if (this._checkbox != null) {
      this._checkbox.setChecked(paramBoolean);
    }
  }
  
  public void toggle()
  {
    if (this._checkbox != null) {
      this._checkbox.toggle();
    }
  }
}