package frc.robot;

public class Beavertail
{

    public void OperateBeaverTail()
    {
        if (UserCom.beaverTailLower())
            Io.beaverTailLower.set(true);
        else
            Io.beaverTailLower.set(false);

        if(UserCom.beaverTailFire())
            Io.beaverTailFire.set(true);
        else
            Io.beaverTailFire.set(false);
    }

}