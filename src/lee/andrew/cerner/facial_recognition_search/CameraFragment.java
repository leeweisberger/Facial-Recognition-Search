package lee.andrew.cerner.facial_recognition_search;


import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraFragment extends Fragment {
    private Camera mCamera=null;
    private CameraPreview mPreview;
    private FrameLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Lee", "view made");
        View v = inflater.inflate(R.layout.fragment_camera, container,false);
        inflater.inflate(R.layout.fragment_camera, container,false);
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        layout = (FrameLayout) v.findViewById(R.id.camera_preview);
        layout.addView(mPreview);
        Button captureButton = (Button) v.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, mPreview.getPictureCallback());
                        mCamera.startPreview();
                    }
                }
                );
        return v;
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
            Log.d("Lee", "camera opened");
        }
        catch (Exception e){}
        return c;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mCamera!=null){
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();  
            mCamera=null;
            Log.d("Lee", "Camera Released");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCamera==null){
            initializeCameraPreview();
        }
    }

    private void initializeCameraPreview() {
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        layout.addView(mPreview);
    }
}
