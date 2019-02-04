package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleTest
{

    private ShuffleboardTab mainTab;
    private long lastUpdateTime;
    private int gValue=0;

    public void Init()
    {
        //mainTab=Shuffleboard.getTab("Main");
        SmartDashboard.putNumber("GyroThingie",0);
        lastUpdateTime=System.currentTimeMillis();
    }

    public void increment()
    {
        if (System.currentTimeMillis()-lastUpdateTime>1000)
        {
            gValue++;
            if (gValue>=360)
            {
                gValue=0;
            }
            SmartDashboard.putNumber("GyroThingie",gValue);
            lastUpdateTime=System.currentTimeMillis();
        }
    }

    
}