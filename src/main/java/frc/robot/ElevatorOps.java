package frc.robot;

import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorOps
{

    private double bottomTarget=0;
    private double level1HatchPreset=5.0;
    private double level2HatchPreset=14.7;
    private double level3HatchPreset=31;
    private double level1CargoPreset=10.3;
    private double level2CargoPreset=25.0;
    private double level3CargoPreset=41.0;

    private boolean isAutomatic=true;


    private double elevatorKp=0.1;
    private double elevatorKd=0.0;
    private double elevatorKi=0.0;
    private double elevatorIZone=5;

    private ElevatorPresets currentTarget=ElevatorPresets.LEVEL1HATCH;

    private double currentTargetPosition;
    private double lastManualPosition=0;//The last value it was moved to while in manual mode, or its first starting position
    private double gravityOffsetEstimate=0; //The estimated motor set in order to hold against gravity
  
    private double ELMINSPEED=-0.03;
    private double ORIGINALELMINSPEED=-0.03;
    private double ELMAXSPEED=0.6;
    private double SAFETYOVERRIDEINCREMENT=0.03;//The 

    private double zeroLevel=0.0;//The stored value that represents 0
    private boolean isInitialized=false;

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
        if (isInitialized) return;
        zeroLevel=Io.elevatorEncoder.getPosition();
        currentTargetPosition=0;
        lastManualPosition=0;
        SafetyButtonState=TWO_BUTTONS_CLEARED;
        isInitialized=true;

    }

    public void selectTarget()
    {

  

        

        //Now check for any new preset instruction

        if (UserCom.Level1Cargo())
        {currentTarget=ElevatorPresets.LEVEL1CARGO;}
        else if (UserCom.Level2Cargo())
        {
            currentTarget=ElevatorPresets.LEVEL2CARGO;
        }
        else if (UserCom.Level3Cargo())
        {
            currentTarget=ElevatorPresets.LEVEL3CARGO;
        }
        else if (UserCom.Level1Hatch())     
        {
            currentTarget=ElevatorPresets.LEVEL1HATCH;
        }
        else if (UserCom.Level2Hatch())
        {
            currentTarget=ElevatorPresets.LEVEL2HATCH;
        }
        else if (UserCom.Level3Hatch())
        {
            currentTarget=ElevatorPresets.LEVEL3HATCH;
        }

        if (UserCom.isElevatorAuto())
          {
              isAutomatic=true;
          }
    }

    private double getCurrentTargetPosition()
    {
        switch(currentTarget)
        {
            case LEVEL1HATCH:return level1HatchPreset;
            
            case LEVEL2HATCH: return level2HatchPreset;
            
            case LEVEL3HATCH: return level3HatchPreset;

            case LEVEL1CARGO: return level1CargoPreset;

            case LEVEL2CARGO: return level2CargoPreset;

            case LEVEL3CARGO: return level3CargoPreset;

            case HOLDCURRENT: return lastManualPosition;

            default: return bottomTarget;

        }
    }

    public void teachMode()
    {
        if (UserCom.Level1Hatch())
        {
            level1HatchPreset=Io.elevatorEncoder.getPosition()-zeroLevel;

        }
        else if (UserCom.Level2Hatch())
        {
            level2HatchPreset=Io.elevatorEncoder.getPosition()-zeroLevel;
        }
        else if (UserCom.Level3Hatch())
        {
            level3HatchPreset=Io.elevatorEncoder.getPosition()-zeroLevel;
        }
        else if (UserCom.Level1Cargo())
        {
            level1CargoPreset=Io.elevatorEncoder.getPosition()-zeroLevel;
        }
        else if (UserCom.Level2Cargo())
        {
            level2CargoPreset=Io.elevatorEncoder.getPosition()-zeroLevel;
        }
        else if (UserCom.Level3Cargo())
        {
            level3CargoPreset=Io.elevatorEncoder.getPosition()-zeroLevel;
        }
    }

    public void operateElevator()
    {
        
        selectTarget(); //Also deterines manual or automatic mode
        currentTargetPosition=getCurrentTargetPosition();
        if (UserCom.resetElevatorZero()) zeroLevel=Io.elevatorEncoder.getPosition();
        
        System.out.println("Target   "+currentTargetPosition);
        System.out.println("Current Position"+Io.elevatorEncoder.getPosition());
        SmartDashboard.putNumber("Rotations", Io.elevatorEncoder.getPosition());
        SmartDashboard.putNumber("Elevator Target",currentTargetPosition);

        if (isAutomatic  &&  UserCom.manualElevatorSpeed()==0)
        {
           
            System.out.println("Operating in automatic");
            currentTargetPosition=getCurrentTargetPosition();
            SmartDashboard.putNumber("Current Target",currentTargetPosition+zeroLevel);

            Io.elevatorController.setReference(currentTargetPosition+zeroLevel, ControlType.kPosition);

        }
        else
        {


           Io.elevatorController.setReference(limitedElevator(), ControlType.kDutyCycle);
           lastManualPosition=Io.elevatorEncoder.getPosition();//When it leaes manual mode, it will hold the position.
           currentTarget=ElevatorPresets.HOLDCURRENT;
           
        }

        if (UserCom.elevatorTeachMode())
        {teachMode();
        }
        checkSafetyOverride();

        SmartDashboard.putNumber("Motor output", limitedElevator());
    }


    private double ELEVATORMAXSCALEDSPEED=0.6;
    /**
     * 
     * @return The scaled value
     */
    private double limitedElevator()
    {
        double requestedValue=UserCom.manualElevatorSpeed();//Deadband has been applied
        Io.printDebugMessage("Scaled value:"+Math.signum(requestedValue)*Math.min(Math.abs(requestedValue),ELEVATORMAXSCALEDSPEED));
        return Math.signum(requestedValue)*Math.min(Math.abs(requestedValue),ELEVATORMAXSCALEDSPEED);
    }

    /**
     * This function is called when disabled to allow robot reset during practice sessions.
     * Hopefully, it won't cause any grief during competition.
     */
    public void disableOps()
    {
        isInitialized=false;
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