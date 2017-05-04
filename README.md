# Android-M-Runtime-Permission-Module
<h1>This is a Module that help you to handle Android M Permission Easily :)</h1>
<h1>Usage</h1>
<h1>1. Copy This Two File in your project</h1>
<h1>2. Extend Your Class With This Two File</h1>

```javascript 
@Override
public class ActivityMain extends AppCompatActivityWithRequestPermission implements AppCompatActivityWithRequestPermission.OnSinglePermissionStatus {
```

<h1>OR</h1>

```javascript 
@Override
public class MyFragment extends FragmentWithRequestPermission implements AppCompatActivityWithRequestPermission.OnSinglePermissionStatus {
```

<h1>3. For Single Check Permission Or Request Permission Do like This:</h1>

```javascript 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    singleRequestPermission(Manifest.permission.CAMERA,
      this,
      "This App Need Some Permission",
      "This App For Continue Working Need Camera Permission.Grant The Requested Permission To App Continue Working",
      "OK",
      "Cancel");
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

<p>This module is the easiest way to handle android M Runtime permission.
just copy this 2 file in your project and extends your class of this two file.
if you use Activity you must extends your class with AppCompatActivityWithRequestPermssion
and if use fragment must extends FragmentWithRequestPermission</p>
