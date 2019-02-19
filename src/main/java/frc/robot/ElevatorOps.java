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

    private boolean isAutomatic=false;


    private double elevatorKp=0.1;
    private double elevatorKd=0.0;
    private double elevatorKi=0.0;
    private double elevatorIZone=5;

    private ElevatorPresets currentTarget=ElevatorPresets.LEVEL1HATCH;

    private double currentTargetPosition;
    private double lastManualPosition=0;//The last value it was moved to while in manual mode, or its first starting position
    private double gravityOffsetEstimate=0; //The estimated motor set in order to hold against gravity
  
    private double ELMINSPEED=-0.1;
    private double ELMAXSPEED=0.6;

    private double zeroLevel=0.0;//The stored value that represents 0

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
            currentTargetPosition=getCurrentTargetPosition()-zeroLevel;
            SmartDashboard.putNumber("Current Target",currentTargetPosition);

            Io.elevatorController.setReference(currentTargetPosition, ControlType.kPosition);
            

            

           
        }
        else
        {

           System.out.println("Raw stick: "+UserCom.manualElevatorSpeed());
           System.out.println("Scaled stick: "+limitedElevator());
           SmartDashboard.putNumber("ScaledStick",limitedElevator());
           Io.elevatorController.setReference(limitedElevator(), ControlType.kDutyCycle);
           lastManualPosition=Io.elevatorEncoder.getPosition();//When it leaes manual mode, it will hold the position.
           currentTarget=ElevatorPresets.HOLDCURRENT;
           
        }
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


}