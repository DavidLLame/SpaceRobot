package frc.robot;

import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorOps
{
    //obviously, the values will have to be changed
    private double level1HatchPreset=5.0;
    private double level2HatchPreset=10.0;
    private double level3HatchPreset=15.0;
    private double level1CargoPreset=20.0;
    private double level2CargoPreset=25.0;
    private double level3CargoPreset=30.0;

    private boolean isAutomatic=false;


    private double elevatorKp=0.1;
    private double elevatorKd=0.0;
    private double elevatorKi=0.0;
    private double elevatorIZone=5;

    private ElevatorPresets currentTarget=ElevatorPresets.LEVEL1HATCH;

    private double currentTargetPosition;
    private double gravityOffsetEstimate=0; //The estimated motor set in order to hold against gravity

    private double encoderZero;  //The stored value that represents 0
    private double ELMINSPEED=-0.2;
    private double ELMAXSPEED=0.2;

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

    public void selectTarget()
    {

        //Check for any requests to change from maunal to automatic, or vice versa
        if (Io.fightStick.getRawButton(Io.MANUALOVERRIDEELEVATOR))
        {
            isAutomatic=false;
        }
        else if (Io.fightStick.getRawButton(Io.AUTOMATICOPERATIONELEVATOR))
        {
            isAutomatic=true;
        }

        //Now check for any new preset instruction

        if (Io.fightStick.getRawButton(Io.JSB_LEVEL1CARGO))
        {currentTarget=ElevatorPresets.LEVEL1CARGO;}
        else if (Io.fightStick.getRawButton(Io.JSB_LEVEL2CARGO))
        {
            currentTarget=ElevatorPresets.LEVEL2CARGO;
        }
        else if (Io.fightStick.getRawButton(Io.JSB_LEVEL3CARGO))
        {
            currentTarget=ElevatorPresets.LEVEL3CARGO;
        }
        else if (Io.fightStick.getRawButton(Io.JSB_LEVEL1HATCH))
        {
            currentTarget=ElevatorPresets.LEVEL1HATCH;
        }
        else if (Io.fightStick.getRawButton(Io.JSB_LEVEL2HATCH))
        {
            currentTarget=ElevatorPresets.LEVEL2HATCH;
        }
        else if (Io.fightStick.getRawButton(Io.JSB_LEVEL3HATCH))
        {
            currentTarget=ElevatorPresets.LEVEL3HATCH;
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

            default: return level1HatchPreset;

        }
    }

    public void operateElevator()
    {
        selectTarget(); //Also deterines manual or automatic mode
        getCurrentTargetPosition();
        System.out.println("Target   "+currentTargetPosition);
        System.out.println("Current Position"+Io.elevatorEncoder.getPosition());
        SmartDashboard.putNumber("Rotations", Io.elevatorEncoder.getPosition());

        if (isAutomatic)
        {
           
           
            currentTargetPosition=getCurrentTargetPosition();

            Io.elevatorController.setReference(currentTargetPosition, ControlType.kPosition);
            

            

           
        }
        else
        {

            //Todo:  This might not work at all.  I'm not sure you can turn this "off"
           // Io.elevator.set(Io.elevatorStick.getRawAxis(Io.MANUALAXISELEVATOR));
           System.out.println("Raw stick: "+Io.elevatorStick.getRawAxis(Io.MANUALAXISELEVATOR));
           System.out.println("Scaled stick: "+joystickScaling(Io.elevatorStick.getRawAxis(Io.MANUALAXISELEVATOR)));
           Io.elevatorController.setReference(joystickScaling(Io.elevatorStick.getRawAxis(Io.MANUALAXISELEVATOR)), ControlType.kDutyCycle);
        }
    }

    private double ELEVATORDEADBAND=0.3;
    private double ELEVATORMAXSCALEDSPEED=0.2;
    /**
     * if the stick is in the deadband, output 0
     * If the stick is at 1, output ELEVATORMAX SPEED
     * Between the deadband limit and 1, scale linearly
     * 
     * @param rawAxis The raw joystick value
     * @return The scaled value
     */
    private double joystickScaling(double rawAxis)
    {if (Math.abs(rawAxis)<ELEVATORDEADBAND)
        return 0;
     else
          return Math.signum(rawAxis)*ELEVATORMAXSCALEDSPEED*(Math.abs(rawAxis)-ELEVATORDEADBAND)/(1-ELEVATORDEADBAND);
    }


}