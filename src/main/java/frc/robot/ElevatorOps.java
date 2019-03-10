package frc.robot;

import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorOps
{

    private double bottomTarget=0;
    private double level1HatchPreset=0;
    private double level2HatchPreset=20.2;
    private double level3HatchPreset=37.2;
    private double level1CargoPreset=16.8;
    private double level2CargoPreset=32.64;
    private double level3CargoPreset=46.0;
    private double hatchPickupLift=8.76;
    private double ballpickup=22.3;


    private boolean isAutomatic=true;


    private double elevatorKp=0.11;
    private double elevatorKd=0.0;
    private double elevatorKi=0.0;
    private double elevatorIZone=5;

    private ElevatorPresets currentTargetPRESET=ElevatorPresets.LEVEL1HATCH;

    private double currentTargetPosition;
    private double lastManualPosition=0;//The last value it was moved to while in manual mode, or its first starting position
    private double gravityOffsetEstimate=0; //The estimated motor set in order to hold against gravity
  
    private double ELMINSPEED=-0.03;
    private double ORIGINALELMINSPEED=-0.03;
    private double ELMAXSPEED=0.75;
    private double SAFETYOVERRIDEINCREMENT=0.03;//The 

    private double zeroLevel=0.0;//The stored value that represents 

    public  ElevatorOps()
    {

        Io.elevatorController=Io.elevator.getPIDController();
        Io.elevatorEncoder=Io.elevator.getEncoder();


        Io.elevatorController.setP(elevatorKp);
        Io.elevatorController.setD(elevatorKd);
        Io.elevatorController.setI(elevatorKi);
        Io.elevatorController.setIZone(elevatorIZone);
        Io.elevatorController.setOutputRange(ELMINSPEED, ELMAXSPEED);
        
    }

    public void Init()
    {

        zeroLevel=Io.elevatorEncoder.getPosition();
        currentTargetPosition=0;
        lastManualPosition=0;
        SafetyButtonState=TWO_BUTTONS_CLEARED;


    }

    public void selectTarget()
    {

  

        

        //Now check for any new preset instruction

        if (UserCom.Level1Cargo())
        {currentTargetPRESET=ElevatorPresets.LEVEL1CARGO;}
        else if (UserCom.Level2Cargo())
        {
            currentTargetPRESET=ElevatorPresets.LEVEL2CARGO;
        }
        else if (UserCom.Level3Cargo())
        {
            currentTargetPRESET=ElevatorPresets.LEVEL3CARGO;
        }
        else if (UserCom.Level1Hatch())     
        {
            currentTargetPRESET=ElevatorPresets.LEVEL1HATCH;
        }
        else if (UserCom.Level2Hatch())
        {
            currentTargetPRESET=ElevatorPresets.LEVEL2HATCH;
        }
        else if (UserCom.Level3Hatch())
        {
            currentTargetPRESET=ElevatorPresets.LEVEL3HATCH;
        }
        else if (UserCom.HatchPickupLift())
        {
            currentTargetPRESET=ElevatorPresets.HATCHPICKUPLIFT;
        }


    }

    private double getCurrentTargetPosition()
    {
        switch(currentTargetPRESET)
        {
            case LEVEL1HATCH:return level1HatchPreset;
            
            case LEVEL2HATCH: return level2HatchPreset;
            
            case LEVEL3HATCH: return level3HatchPreset;

            case LEVEL1CARGO: return level1CargoPreset;

            case LEVEL2CARGO: return level2CargoPreset;

            case LEVEL3CARGO: return level3CargoPreset;

            case HOLDCURRENT: return lastManualPosition;

            case HATCHPICKUPLIFT: return hatchPickupLift;

            default: return bottomTarget;

        }
    }


    public void operateElevator()
    {
     /*
        SmartDashboard.putString("Current Preset", currentTargetPRESET.toString());
        SmartDashboard.putNumber("Current Target",currentTargetPosition+zeroLevel);
        SmartDashboard.putNumber("Rotations", Io.elevatorEncoder.getPosition());
        SmartDashboard.putNumber("Zero Postion", zeroLevel);
       */

        selectTarget(); //Also deterines manual or automatic mode
        currentTargetPosition=getCurrentTargetPosition();
        if (UserCom.resetElevatorZero()) zeroLevel=Io.elevatorEncoder.getPosition();
        
    
     
        if (isAutomatic  &&  UserCom.manualElevatorSpeed()==0)
        {
           
    
            currentTargetPosition=getCurrentTargetPosition();
           
            Io.elevatorController.setReference(currentTargetPosition+zeroLevel, ControlType.kPosition);

        }
        else
        {


           Io.elevatorController.setReference(limitedElevator(), ControlType.kDutyCycle);
           lastManualPosition=Io.elevatorEncoder.getPosition();//When it leaes manual mode, it will hold the position.
           currentTargetPRESET=ElevatorPresets.HOLDCURRENT;
           
        }


        checkSafetyOverride();


    }


    private double ELEVATORMAXSCALEDSPEED=0.6;
    /**
     * 
     * @return The scaled value
     */
    private double limitedElevator()
    {
        double requestedValue=UserCom.manualElevatorSpeed();//Deadband has been applied
        double outval =Math.signum(requestedValue)*Math.min(Math.abs(requestedValue),ELEVATORMAXSCALEDSPEED);
        return Math.max(outval,-0.03);
    }

    /**
     * This function is called when disabled to allow robot reset during practice sessions.
     * Hopefully, it won't cause any grief during competition.
     */
    public void disableOps()
    {

    }




    //Safety override


    private final int TWO_BUTTONS_PRESSED=1;
    private final int TWO_BUTTONS_CLEARED=2;

    private int SafetyButtonState=TWO_BUTTONS_CLEARED;
    /**
     * This function allows the operators to override the maximum downward force
     * of the spark max controller, allowing a much more rapid descent.
     * 
     * To use it, a button on each of the drivers' sticks must be held simultaneously
     * To increase again, both must be released, and then pressed again and held simultaneously
     * 
     * USE WITH CAUTION
     */
    private void checkSafetyOverride()
    {
        if(SafetyButtonState==TWO_BUTTONS_CLEARED)
        {
            if (UserCom.elevatorSafetyOverRideDriver1()&&UserCom.elevatorSafetyOverRideDriver2())
            {
                SafetyButtonState=TWO_BUTTONS_PRESSED;
                ELMINSPEED-=SAFETYOVERRIDEINCREMENT;
                Io.elevatorController.setOutputRange(ELMINSPEED, ELMAXSPEED);
            }
        }
        if (!UserCom.elevatorSafetyOverRideDriver1()&&
            !UserCom.elevatorSafetyOverRideDriver2())
            {
                SafetyButtonState=TWO_BUTTONS_CLEARED;
            }

        if (UserCom.restoreSafety())
        {
             Io.elevatorController.setOutputRange(ORIGINALELMINSPEED,ELMAXSPEED);
        }
    }

    



}