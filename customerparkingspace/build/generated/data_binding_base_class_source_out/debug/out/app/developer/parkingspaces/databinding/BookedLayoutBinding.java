// Generated by view binder compiler. Do not edit!
package app.developer.parkingspaces.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import app.developer.parkingspaces.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BookedLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView dateTimeTV;

  @NonNull
  public final TextView locationTV;

  @NonNull
  public final TextView ownerNameTV;

  @NonNull
  public final TextView phoneTV;

  @NonNull
  public final TextView slotTV;

  @NonNull
  public final TextView vehicleTV;

  private BookedLayoutBinding(@NonNull LinearLayout rootView, @NonNull TextView dateTimeTV,
      @NonNull TextView locationTV, @NonNull TextView ownerNameTV, @NonNull TextView phoneTV,
      @NonNull TextView slotTV, @NonNull TextView vehicleTV) {
    this.rootView = rootView;
    this.dateTimeTV = dateTimeTV;
    this.locationTV = locationTV;
    this.ownerNameTV = ownerNameTV;
    this.phoneTV = phoneTV;
    this.slotTV = slotTV;
    this.vehicleTV = vehicleTV;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static BookedLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BookedLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.booked_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BookedLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.dateTimeTV;
      TextView dateTimeTV = ViewBindings.findChildViewById(rootView, id);
      if (dateTimeTV == null) {
        break missingId;
      }

      id = R.id.locationTV;
      TextView locationTV = ViewBindings.findChildViewById(rootView, id);
      if (locationTV == null) {
        break missingId;
      }

      id = R.id.ownerNameTV;
      TextView ownerNameTV = ViewBindings.findChildViewById(rootView, id);
      if (ownerNameTV == null) {
        break missingId;
      }

      id = R.id.phoneTV;
      TextView phoneTV = ViewBindings.findChildViewById(rootView, id);
      if (phoneTV == null) {
        break missingId;
      }

      id = R.id.slotTV;
      TextView slotTV = ViewBindings.findChildViewById(rootView, id);
      if (slotTV == null) {
        break missingId;
      }

      id = R.id.vehicleTV;
      TextView vehicleTV = ViewBindings.findChildViewById(rootView, id);
      if (vehicleTV == null) {
        break missingId;
      }

      return new BookedLayoutBinding((LinearLayout) rootView, dateTimeTV, locationTV, ownerNameTV,
          phoneTV, slotTV, vehicleTV);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
