// Generated by view binder compiler. Do not edit!
package com.developer.adminparkingspace.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.developer.adminparkingspace.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AreaLayoutBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView areaDesTV;

  @NonNull
  public final TextView areaID;

  @NonNull
  public final CircleImageView areaIV;

  @NonNull
  public final TextView areaTitleTV;

  private AreaLayoutBinding(@NonNull CardView rootView, @NonNull TextView areaDesTV,
      @NonNull TextView areaID, @NonNull CircleImageView areaIV, @NonNull TextView areaTitleTV) {
    this.rootView = rootView;
    this.areaDesTV = areaDesTV;
    this.areaID = areaID;
    this.areaIV = areaIV;
    this.areaTitleTV = areaTitleTV;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static AreaLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AreaLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.area_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AreaLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.areaDes_TV;
      TextView areaDesTV = ViewBindings.findChildViewById(rootView, id);
      if (areaDesTV == null) {
        break missingId;
      }

      id = R.id.area_ID;
      TextView areaID = ViewBindings.findChildViewById(rootView, id);
      if (areaID == null) {
        break missingId;
      }

      id = R.id.area_IV;
      CircleImageView areaIV = ViewBindings.findChildViewById(rootView, id);
      if (areaIV == null) {
        break missingId;
      }

      id = R.id.areaTitle_TV;
      TextView areaTitleTV = ViewBindings.findChildViewById(rootView, id);
      if (areaTitleTV == null) {
        break missingId;
      }

      return new AreaLayoutBinding((CardView) rootView, areaDesTV, areaID, areaIV, areaTitleTV);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}