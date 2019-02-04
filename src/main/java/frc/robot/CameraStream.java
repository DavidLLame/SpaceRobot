package frc.robot;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class CameraStream
{

    private UsbCamera camera;
    private CvSink cvSink;
    private CvSource outputStream;

    public void initCamera()
    {
    //    System.out.println("About to init");
    //    camera = CameraServer.getInstance().startAutomaticCapture();
    //    System.out.println("It didn't crash");
   //     camera.setResolution(320, 240);
   //     cvSink = CameraServer.getInstance().getVideo();
   //     outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);
    }

    public void grabFrame()
    {

      //  Mat source = new Mat();
      //  cvSink.grabFrame(source);
      //  outputStream.putFrame(source);

    }

}