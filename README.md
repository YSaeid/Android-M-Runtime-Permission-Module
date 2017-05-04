# Android-M-Runtime-Permission-Module
<h1>This is a Module that help you to handle Android M Permission Easily :)</h1>
<h1>Usage</h1>
<h1>1. Copy This Two File in your project</h1>
<h1>2. Extend Your Class With This Two File</h1>
<h1>2.1 For Single request or check permission</h1>

```javascript 
@Override
public class ActivityMain extends AppCompatActivityWithRequestPermission implements AppCompatActivityWithRequestPermission.OnSinglePermissionStatus {
```

<h1>2.2 For Multiple request or check permission</h1>

```javascript 
@Override
public class ActivityMain extends AppCompatActivityWithRequestPermission implements AppCompatActivityWithRequestPermission.OnMultiplePermissionStatus {
```

<h1>2.3 Single check or request for Fragment</h1>

```javascript 
@Override
public class MyFragment extends FragmentWithRequestPermission implements AppCompatActivityWithRequestPermission.OnSinglePermissionStatus {
```

<h1>2.4 Multiple check or request For Fragment</h1>

```javascript 
@Override
public class MyFragment extends FragmentWithRequestPermission implements AppCompatActivityWithRequestPermission.OnMultiplePermissionStatus {
```

<h1>3. For Single Check Permission Or Request Permission Do like This:</h1>

```javascript 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      singleRequestPermission(Manifest.permission.CAMERA,
        this,
        "This App Need Some Permission",
        "This App For Continue Working Need Camera Permission.Grant The Requested Permission To App Continue Working",
        "OK",
        "Cancel");
    }
  }
```

<h1>3.1 Do Your Work Here After Permission Grant Or Not:</h1>

```javascript
  @Override
  public void onPermissionGranted() {
    // do Your work here for granted permission
  }

  @Override
  public void onPermissionNotGranted() {
    // do Your work here for permission not granted
  }
```

<h1>3.2 For Multiple Request Permssion or Check Multiple Permssion:</h1>

```javascript
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    String[] permissions = new String[]{
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.READ_CONTACTS,
      Manifest.permission.RECORD_AUDIO};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      multipleRequestPermission(permissions, this, "Need Permission", "This App Need Some Permission.", "OK", "Cancel");
    }
    
  }
```

<h1>3.3 Do Your Work Here After All Permission Grant Or Not:</h1>

```javascript
  @Override
  public void onPermissionGranted() {
    Toast.makeText(this, "All Granted", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onPermissionNotGranted() {

  }

  @Override
  public void onAllPermissionNotGranted() {
    Toast.makeText(this, "All Not Granted", Toast.LENGTH_SHORT).show();
  }
```

<h1>4 Just Check Single Permission is granted in activity or fragment do like this:</h1>

```javascript
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      checkSinglePermissionIsGranted(Manifest.permission.CAMERA);
    }
```

<h1>4.1 Just Check Multiple Permission is granted in activity or fragment do like this:</h1>

```javascript
    String[] permissions = new String[]{
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.READ_CONTACTS,
      Manifest.permission.RECORD_AUDIO};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      checkMultiplePermissionIsGranted(permissions);
    }
```

<h1>HAVE FUN :)</h1>


<p>This module is the easiest way to handle android M Runtime permission.
just copy this 2 file in your project and extends your class of this two file.
if you use Activity you must extends your class with AppCompatActivityWithRequestPermssion
and if use fragment must extends FragmentWithRequestPermission</p>
