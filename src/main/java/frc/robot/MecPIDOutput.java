package frc.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class MecPIDOutput implements PIDOutput
{

    private double rotationCorrection;

    public double getRotationCorrection()
    {
        return rotationCorrection;
    }

    @Override
    public void pidWrite(double output) {

        rotationCorrection=output;
    }

}