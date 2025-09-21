package org.firstinspires.ftc.teamcode.DriverControl;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotClass;

@TeleOp(name = "ServoToggleExample", group = "testing")
public class ServoToggleExample extends OpMode {

    RobotClass robot;

    boolean servoAt90 = false;     // starts at 0°
    boolean lastDpadDown = false;  // for edge detection

    @Override
    public void init() {
        robot = new RobotClass(hardwareMap);

        // Make sure your servo is mapped in RobotClass, e.g.:
        // gun1Fill = hardwareMap.get(Servo.class, "gun1Fill");

        // Start at 0 degrees
        robot.gun1Fill.setPosition(0.0);
    }

    @Override
    public void loop() {
        boolean currentDpadDown = gamepad1.dpad_down;

        // Detect a new press (toggle)
        if (currentDpadDown && !lastDpadDown) {
            servoAt90 = !servoAt90; // flip state

            if (servoAt90) {
                // Move to 90 degrees
                robot.gun1Fill.setPosition(1);
                // 0.5 is halfway for a 180° servo (approx 90°)
            } else {
                // Move back to 0 degrees
                robot.gun1Fill.setPosition(0.0);
            }
        }

        lastDpadDown = currentDpadDown;

        telemetry.addData("Servo at 90°?", servoAt90);
        telemetry.addData("Servo Position", robot.gun1Fill.getPosition());
        telemetry.update();
    }
}
