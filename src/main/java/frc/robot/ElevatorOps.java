package frc.robot;

public class ElevatorOps
{
    //obviously, the values will have to be changed
    private double level1HatchPreset=5.0;
    private double level2HatchPreset=10.0;
    private double level3HatchPreset=15.0;
    private double level1CargoPreset=20.0;
    private double level2CargoPreset=25.0;
    private double level3CargoPreset=30.0;

    private boolean isAutomatic=true;

    private ElevatorPresets currentTarget=ElevatorPresets.LEVEL1HATCH;

    private double currentTargetPosition;
    private double gravityOffsetEstimate=0; //The estimated motor set in order to hold against gravity

    private double encoderZero;  //The stored value that represents 0

    public void selectTarget()
    {

        //Check for any requests to change from maunal to automatic, or vice versa
        if (Io.elevatorStick.getRawButton(Io.MANUALOVERRIDEELEVATOR))
        {
            isAutomatic=false;
        }
        else if (Io.elevatorStick.getRawButton(Io.AUTOMATICOPERATIONELEVATOR))
        {
            isAutomatic=true;
        }

        //Now check for any new preset instruction

        if (Io.elevatorStick.getRawButton(Io.JSB_LEVEL1CARGO))
        {currentTarget=ElevatorPresets.LEVEL1CARGO;}
        else if (Io.elevatorStick.getRawButton(Io.JSB_LEVEL2CARGO))
        {
            currentTarget=ElevatorPresets.LEVEL2CARGO;
        }
        else if (Io.elevatorStick.getRawButton(Io.JSB_LEVEL3CARGO))
        {
            currentTarget=ElevatorPresets.LEVEL3CARGO;
        }
        else if (Io.elevatorStick.getRawButton(Io.JSB_LEVEL1HATCH))
        {
            currentTarget=ElevatorPresets.LEVEL1HATCH;
        }
        else if (Io.elevatorStick.getRawButton(Io.JSB_LEVEL2HATCH))
        {
            currentTarget=ElevatorPresets.LEVEL2HATCH;
        }
        else if (Io.elevatorStick.getRawButton(Io.JSB_LEVEL3HATCH))
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

        if (isAutomatic)
        {
            currentTargetPosition=getCurrentTargetPosition();

            double error=currentTargetPosition-Io.elevatorEncoder.getPosition();

            //TODO:  Figure out closed loop control on spark max with neo

           
        }
        else
        {
            Io.elevator.set(Io.elevatorStick.getRawAxis(Io.MANUALAXISELEVATOR));
        }
    }


}