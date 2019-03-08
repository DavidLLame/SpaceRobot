package frc.robot;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class CameraStream
{

    private UsbCamera camera1;
    private UsbCamera camera2;
    private CvSink cvSink;
    private CvSource outputStream;

    public void initCamera()
    {
        System.out.println("About to init");
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
       // camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        
      System.out.println("It didn't crash");
      camera1.setResolution(320, 240);
      camera1.setFPS(12);
      //  camera2.setResolution(320, 240);
        cvSink = CameraServer.getInstance().getVideo();
        outputStream = CameraServer.getInstance().putVideo("DriverCam", 320, 240);
        outputStream.setFPS(12);
        outputStream.setResolution(320, 240);
        lastSwitch=System.currentTimeMillis();
        cvSink.setSource(camera1);
    }

    private long lastSwitch;
    private boolean cam1=true;
  

}