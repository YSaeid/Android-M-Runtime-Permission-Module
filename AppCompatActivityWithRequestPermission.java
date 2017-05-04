package ir.fuzhan.karbon.app.karbon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by saied on 4/24/17.
 */

public class AppCompatActivityWithRequestPermission extends AppCompatActivity {

  private final int SINGLE_REQUEST_PERMISSION_REQUEST_CODE = 100;
  private final int MULTIPLE_REQUEST_PERMISSION_REQUEST_CODE = 101;
  private SharedPreferences permissionStatus;
  private final String SHARED_PREFERENCES_KEY = AppCompatActivityWithRequestPermission.class.getSimpleName();
  private boolean isSingleRequest;
  private boolean sentToSettings;
  private String singlePermissionName;
  private OnSinglePermissionStatus onSinglePermissionStatus;
  private OnMultiplePermissionStatus onMultiplePermissionStatus;
  private String dialogTitle;
  private String dialogMessage;
  private String dialogPositiveButtonText;
  private String dialogNegativeButtonText;
  private String[] permissionNames;


  public void singleRequestPermission(String permissionName, OnSinglePermissionStatus singlePermissionGranted, String dialogTitle, String dialogMessage, String dialogPositiveButtonText, String dialogNegativeButtonText) {
    singlePermissionName = permissionName;
    this.onSinglePermissionStatus = singlePermissionGranted;
    this.dialogTitle = dialogTitle;
    this.dialogMessage = dialogMessage;
    this.dialogPositiveButtonText = dialogPositiveButtonText;
    this.dialogNegativeButtonText = dialogNegativeButtonText;
    permissionStatus = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);
    isSingleRequest = true;
    if (ActivityCompat.checkSelfPermission(this, permissionName) != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
        showPermissionHintDialog(permissionName, dialogTitle, dialogMessage, dialogPositiveButtonText, dialogNegativeButtonText);
      } else if (permissionStatus.getBoolean(permissionName, false)) {
        showOpenAppSetting(dialogTitle, dialogMessage, dialogPositiveButtonText, dialogNegativeButtonText);
      } else {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, SINGLE_REQUEST_PERMISSION_REQUEST_CODE);
      }
      SharedPreferences.Editor editor = permissionStatus.edit();
      editor.putBoolean(permissionName, true);
      editor.apply();
    } else {
      singlePermissionGranted.onPermissionGranted();
    }
  }

  public boolean checkSinglePermissionIsGranted(String permissionName) {
    return ActivityCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED;
  }

  public boolean checkMultiplePermissionIsGranted(String[] permissionNames) {
    ArrayList<Boolean> allowedPermission = new ArrayList<>();
    for (String permissionName : permissionNames) {
      if (ActivityCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
        allowedPermission.add(true);
      }
    }
    return allowedPermission.size() == permissionNames.length;
  }

  public void multipleRequestPermission(String[] permissionNames, OnMultiplePermissionStatus onMultiplePermissionStatus, String dialogTitle, String dialogMessage, String dialogPositiveButtonText, String dialogNegativeButtonText) {
    isSingleRequest = false;
    this.permissionNames = permissionNames;
    this.onMultiplePermissionStatus = onMultiplePermissionStatus;
    this.dialogTitle = dialogTitle;
    this.dialogMessage = dialogMessage;
    this.dialogPositiveButtonText = dialogPositiveButtonText;
    this.dialogNegativeButtonText = dialogNegativeButtonText;
    permissionStatus = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);

    ArrayList<Boolean> allowedPermission = new ArrayList<>();
    for (String permissionName : permissionNames) {
      if (ActivityCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
        allowedPermission.add(true);
      }
    }

    if (allowedPermission.size() == permissionNames.length) {
      onMultiplePermissionStatus.onPermissionGranted();
    } else {
      for (String permissionName : permissionNames) {
        if (ActivityCompat.checkSelfPermission(this, permissionName) != PackageManager.PERMISSION_GRANTED) {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
            showMultiplePermissionHintDialog(permissionNames, dialogTitle, dialogMessage, dialogPositiveButtonText, dialogNegativeButtonText);
          } else if (permissionStatus.getBoolean(permissionName, false)) {
            showOpenAppSetting(dialogTitle, dialogMessage, dialogPositiveButtonText, dialogNegativeButtonText);
          } else {
            ActivityCompat.requestPermissions(this, permissionNames, MULTIPLE_REQUEST_PERMISSION_REQUEST_CODE);
            break;
          }
          SharedPreferences.Editor editor = permissionStatus.edit();
          editor.putBoolean(permissionName, true);
          editor.apply();
          break;
        }
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (isSingleRequest) {
      if (requestCode == SINGLE_REQUEST_PERMISSION_REQUEST_CODE) {
        boolean granted = false;
        for (int grantResult : grantResults) {
          if (grantResult == PackageManager.PERMISSION_GRANTED) {
            granted = true;
          } else {
            granted = false;
            break;
          }
          if (granted) {
            onSinglePermissionStatus.onPermissionGranted();
          } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, singlePermissionName)) {
            showPermissionHintDialog(singlePermissionName, dialogTitle, dialogMessage, dialogPositiveButtonText, dialogNegativeButtonText);
          } else {
            onSinglePermissionStatus.onPermissionNotGranted();
          }
        }
      }
    } else {
      if (requestCode == MULTIPLE_REQUEST_PERMISSION_REQUEST_CODE) {
        ArrayList<Boolean> grantPermissionArrayList = new ArrayList<>();
        for (int grantResult : grantResults) {
          if (grantResult == PackageManager.PERMISSION_GRANTED) {
            grantPermissionArrayList.add(true);
          }
        }
        if (grantPermissionArrayList.size() == permissions.length) {
          onMultiplePermissionStatus.onPermissionGranted();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
          showMultiplePermissionHintDialog(permissions, dialogTitle, dialogMessage, dialogPositiveButtonText, dialogNegativeButtonText);
        } else {
          onMultiplePermissionStatus.onAllPermissionNotGranted();
        }
      }
    }
  }

  public interface OnSinglePermissionStatus {
    void onPermissionGranted();

    void onPermissionNotGranted();
  }

  public interface OnMultiplePermissionStatus {
    void onPermissionGranted();

    void onPermissionNotGranted();

    void onAllPermissionNotGranted();
  }

  private void showPermissionHintDialog(final String permissionName, String dialogTitle, String dialogMessage, String positiveButtonText, String negativeButtonText) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(dialogTitle);
    builder.setMessage(dialogMessage);
    builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        ActivityCompat.requestPermissions(AppCompatActivityWithRequestPermission.this, new String[]{permissionName}, SINGLE_REQUEST_PERMISSION_REQUEST_CODE);
      }
    });
    builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (isSingleRequest) {
          onSinglePermissionStatus.onPermissionNotGranted();
        } else {
          onMultiplePermissionStatus.onPermissionNotGranted();
        }
      }
    });
    builder.show();
  }

  private void showMultiplePermissionHintDialog(final String[] permissionName, String dialogTitle, String dialogMessage, String positiveButtonText, String negativeButtonText) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(dialogTitle);
    builder.setMessage(dialogMessage);
    builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        ActivityCompat.requestPermissions(AppCompatActivityWithRequestPermission.this, permissionName, MULTIPLE_REQUEST_PERMISSION_REQUEST_CODE);
      }
    });
    builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (isSingleRequest) {
          onSinglePermissionStatus.onPermissionNotGranted();
        } else {
          onMultiplePermissionStatus.onPermissionNotGranted();
          onMultiplePermissionStatus.onAllPermissionNotGranted();
        }
      }
    });
    builder.show();
  }

  private void showOpenAppSetting(String dialogTitle, String dialogMessage, String positiveButtonText, String negativeButtonText) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(dialogTitle);
    builder.setMessage(dialogMessage);
    builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        sentToSettings = true;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        if (isSingleRequest) {
          startActivityForResult(intent, SINGLE_REQUEST_PERMISSION_REQUEST_CODE);
        } else {
          startActivityForResult(intent, MULTIPLE_REQUEST_PERMISSION_REQUEST_CODE);
        }
      }
    });
    builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (isSingleRequest) {
          onSinglePermissionStatus.onPermissionNotGranted();
        } else {
          onMultiplePermissionStatus.onPermissionNotGranted();
        }
      }
    });
    builder.show();
  }

  public void openAppSettingForGrantPermission() {
    sentToSettings = true;
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    Uri uri = Uri.fromParts("package", getPackageName(), null);
    intent.setData(uri);
    if (isSingleRequest) {
      startActivityForResult(intent, SINGLE_REQUEST_PERMISSION_REQUEST_CODE);
    } else {
      startActivityForResult(intent, MULTIPLE_REQUEST_PERMISSION_REQUEST_CODE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (isSingleRequest) {
      if (requestCode == SINGLE_REQUEST_PERMISSION_REQUEST_CODE) {
        if (ActivityCompat.checkSelfPermission(this, singlePermissionName) == PackageManager.PERMISSION_GRANTED) {
          onSinglePermissionStatus.onPermissionGranted();
        } else {
          onSinglePermissionStatus.onPermissionNotGranted();
        }
      }
    } else {
      if (requestCode == MULTIPLE_REQUEST_PERMISSION_REQUEST_CODE) {
        ArrayList<Boolean> allowedPermission = new ArrayList<>();
        for (String permissionName : permissionNames) {
          if (ActivityCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
            allowedPermission.add(true);
          }
        }
        if (allowedPermission.size() == permissionNames.length) {
          onMultiplePermissionStatus.onPermissionGranted();
        } else {
          onMultiplePermissionStatus.onAllPermissionNotGranted();
        }
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (isSingleRequest) {
      if (sentToSettings) {
        if (ActivityCompat.checkSelfPermission(this, singlePermissionName) == PackageManager.PERMISSION_GRANTED) {
          onSinglePermissionStatus.onPermissionGranted();
        }
      }
    }
  }
}
