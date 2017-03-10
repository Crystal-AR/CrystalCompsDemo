package com.crystal_ar.crystalcompsdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.crystal_ar.crystal_ar.CrystalAR;
import com.crystal_ar.crystal_ar.IntPair;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Frederik on 2/16/17.
 * Credit for camera code: https://inducesmile.com/android/android-camera2-api-example-tutorial/
 */

public class ModelActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "ModelActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    private String cameraId;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Context context = this;
    private TextView clickText;
    private Float clickX;
    private Float clickY;
    private CrystalAR crystalAR;
    private ListView modelListView;
    private ModelRenderer modelRenderer;
    private RajawaliSurfaceView rajSurface;
    private TextureView textureView;
    private Size imageDimension;
    private ImageReader imageReader;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private Bitmap photo;
    private Thread cornerThread;
    private TableView tableView;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z, change_x, change_y, change_z;
    private static final int SHAKE_THRESHOLD = 800;
    private Number3d mAccVals = new Number3d();

    // Filenames for obj/awd files.
    // Do not include file extensions for awd files.
    // OBJ files do not have a file extension (convert .obj to _obj).
    String[] modelFileList = new String[] {
            "multiobjects_obj",
            "bumpsphere_obj",
            "bumptorus_obj",
            "dark_fighter", // awd
            "space_cruiser", // awd
            "teapot_obj"
    };

    // The strings that appear in our list.
    String[] modelNameList = new String[] {
            "Multiple objects",
            "Earth",
            "Torus",
            "Dark fighter",
            "Space cruiser",
            "Teapot"
    };

    // Filenames for textures.
    // Do not include file extensions.
    String[] modelTextureList = new String[] {
            null, // no texture.
            "earthtruecolor_nasa_big",
            "torus_texture",
            "dark_fighter_6_color",
            "space_cruiser_4_color_1",
            null // no texture.
    };

    // Types of model files.
    // Only obj and awd are supported currently.
    String[] modelTypeList = new String[]{
            "obj",
            "obj",
            "obj",
            "awd",
            "awd",
            "obj"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        crystalAR = new CrystalAR(getApplicationContext());
        this.tableView = (TableView) findViewById(R.id.tableView);
        this.clickText = (TextView) findViewById(R.id.cornerOverlayText);
        // Setup click and surface texture listeners for texture view.
        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);


        //Setup the sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        textureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clickX = event.getX();
                    clickY = event.getY();

                    // This is where we would use clickX and clickY to calculate where the model
                    // should be.

                    // Swap views.
                    swapViews();
                }
                return true;
            }
        });

        // Setup RajawaliSurfaceView and create ModelRenderer.
        rajSurface = new RajawaliSurfaceView(this);
        rajSurface.setFrameRate(60.0);
        rajSurface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
        rajSurface.setTransparent(true);
        rajSurface.setZOrderOnTop(true);
        addContentView(rajSurface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
        this.modelRenderer = new ModelRenderer(this);
        rajSurface.setSurfaceRenderer(modelRenderer);

        createListView();
    }

    protected void onStop() {
        super.onStop();
        senSensorManager.unregisterListener(this);
        cornerThread.interrupt();
        cornerThread = null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 1000) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {

                }

                    change_x = last_x - x;
                    change_y = last_y - y;
                    change_z = last_z - z;

                    last_x = x;
                    last_y = y;
                    last_z = z;
                    double angleX = Math.atan2(-y, z) / (Math.PI / 180);
                    double angleY = Math.atan2(-z, x) / (Math.PI / 180);
                    double angleZ = Math.atan2(y, x) / (Math.PI / 180);

                    //modelRenderer.setRotations(new Vector3(angleX,angleY,angleZ), new Vector3(x,y,z));
                    modelRenderer.setRotations(
                        new Vector3(angleX, angleY, angleZ),
                        new Vector3(change_x, change_y, change_z),
                        new Vector3(x,y,z)
                    );
                }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    // Setup surface texture listener.
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            // Open the camera.
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    // openCamera().
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // If the user has not given permission to use the camera, ask for it.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ModelActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }

            // Open the camera.
            manager.openCamera(cameraId, stateCallback, null);

            // Setup image height and width.
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            // Setup image reader for getting individual frames.
            imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 2);
            imageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    };

    // Define state callback. This starts a preview or closes the camera.
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    // Define capture callback.
    private final CameraCaptureSession.CaptureCallback mCaptureCallback =
        new CameraCaptureSession.CaptureCallback() {
            private void process(CaptureResult result) {
            }

            @Override
            public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
                process(partialResult);
            }

            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                       TotalCaptureResult result) {
                process(result);
            }
        };

    // Define ImageReader.OnImageAvailableListener. This is where we access each frame.
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener =
        new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image readImage = reader.acquireLatestImage();

                // Only find corners if we are done processing the frame we're currently at.
//                if(photo == null || photo.isRecycled()) {
//                    ByteBuffer buffer = readImage.getPlanes()[0].getBuffer();
//                    buffer.rewind();
//                    byte[] jpegByteData = new byte[buffer.capacity()];
//                    buffer.get(jpegByteData);
//                    photo = BitmapFactory.decodeByteArray(jpegByteData, 0, jpegByteData.length, null);
//
//                    // Find corners using a handler.
//                    if (photo != null) {
//                        initiateCornerHandler();
//                    }
//                }

                // Close image when done.
                readImage.close();
            }
        };

    public void initiateCornerHandler() {
        cornerThread = new Thread(crystalAR.findCornersRunnable(new cornerHandler(photo), photo));
        cornerThread.setDaemon(true);
        cornerThread.start();
        // Is is better to do it on the background thread?
//        mBackgroundHandler.post(crystalAR.findCornersRunnable(new cornerHandler(photo), photo));
    }

    private class cornerHandler extends Handler {
        Bitmap img;

        public cornerHandler(Bitmap img) {
            this.img = img;
        }
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case CrystalAR.CORNERS_FOUND:
                    // message.obj = null if no corners were found.
                    tableView.drawCorners((IntPair[]) message.obj, this.img.getWidth(), this.img.getHeight());

                    this.img.recycle();
                    this.img = null;
                    // Try to force garbage collection.
                    //System.gc();
                    break;
            }
        }
    }

    // createCameraPreview(). Sets up the targets for our capture request and initiates a capture
    // session.
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface mImageSurface = imageReader.getSurface();
            captureRequestBuilder.addTarget(mImageSurface);
            // Might need to remove this surface when we find corners, as we would have alternative
            // bitmaps to display.
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(mImageSurface, surface), new CameraCaptureSession.StateCallback(){
//            cameraDevice.createCaptureSession(Arrays.asList(mImageSurface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    // The camera is already closed.
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(ModelActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // updatePreview(). Setup repeated requests fo the capture callback.
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // onRequestPermissionsResult(). Handles permission denied for the camera.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(ModelActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    // onResume().
    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    // onPause().
    @Override
    protected void onPause() {
        //closeCamera();
        stopBackgroundThread();
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    // startBackgroundThread().
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    // stopBackgroundThread().
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // creatListView(). Adds the different models to the list view.
    private void createListView() {
        modelListView = (ListView) findViewById(R.id.modelList);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, modelNameList);
        modelListView.setAdapter(adapter);

        // Create onclick functionality for each list item.
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                // Get the model from the resource folder.
                String modelFile = modelFileList[position];
                int model = context.getResources().getIdentifier(modelFile, "raw", context.getPackageName());

                // Get the texture from the resource folder.
                String textureFile = modelTextureList[position];
                Integer texture = textureFile == null
                    ? null
                    : context.getResources().getIdentifier(textureFile, "drawable", context.getPackageName());

                // Render model.
                modelRenderer.renderModel(model, texture, modelTypeList[position]);
                modelRenderer.setPosition(0.0, 0.0, 0.0);
                //modelRenderer.setPosition((double) clickX,(double) clickY, 1.0);

                // Swap views.
                swapViews();
            }
        };

        modelListView.setOnItemClickListener(itemClickListener);
    }

    // swapViews(). Swaps the views displayed: clickText, modelListView, rajSurface.
    private void swapViews() {
        if (modelListView.getVisibility() == View.INVISIBLE) {
            // Show list, hide rajSurface, hide click text.
            clickText.setVisibility(View.INVISIBLE);
            modelListView.setVisibility(View.VISIBLE);
            rajSurface.setVisibility(View.INVISIBLE);
        } else {
            // Hide list, show rajSurface, show click text.
            rajSurface.setVisibility(View.VISIBLE);
            rajSurface.setTransparent(true);
            clickText.setVisibility(View.VISIBLE);
            modelListView.setVisibility(View.INVISIBLE);
        }
    }
}
