package frc.robot;

public class Beavertail
{

    public void OperateBeaverTail()
    {
        if (Io.elevatorStick.getRawButton(Io.JSB_BEAVERTAILLOWER))
            Io.beaverTailLower.set(true);
        else
            Io.beaverTailLower.set(false);

        if(Io.elevatorStick.getRawButton(Io.JSB_BEAVERTAILFIRE))
            Io.beaverTailFire.set(true);
        else
            Io.beaverTailFire.set(false);
    }

}