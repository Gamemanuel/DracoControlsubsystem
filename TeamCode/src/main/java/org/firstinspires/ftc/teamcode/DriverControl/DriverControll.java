package org.firstinspires.ftc.teamcode.DriverControl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RobotClass;

@TeleOp(name = "ONTOS", group = "driving")
public class DriverControll extends OpMode {

    RobotClass robot;

    // Use arrays to manage the state for each gun individually
    boolean[] valveOpen = new boolean[6];
    boolean[] moving = new boolean[6];
    int[] targetPosition = new int[6];
    double[] movePower = new double[6];
    boolean[] servoAt90 = new boolean[6];

    // Single boolean for gamepad button states
    boolean lastAState = false;
    boolean lastDpadDown = false;

    // A single constant for the target encoder value
    static final int OPEN_POSITION = 7 * 20;

    @Override
    public void init() {
        robot = new RobotClass(hardwareMap);
        // Initialize all servos to the closed position (0.0)
        for (int i = 0; i < 6; i++) {
            robot.gunFillServos[i].setPosition(0.0);
        }
    }

    @Override
    public void start() {
        // Reset and set the run mode for all motors
        for (int i = 0; i < 6; i++) {
            robot.gunFireMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.gunFireMotors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    @Override
    public void loop() {
        // Read gamepad states once per loop
        boolean currentAState = gamepad1.a;
        boolean currentDpadDown = gamepad1.dpad_down;

        // --- Toggle logic for all guns with button presses ---
        // A button toggles the motors
        if (currentAState && !lastAState) {
            for (int i = 0; i < 6; i++) {
                valveOpen[i] = !valveOpen[i];
                targetPosition[i] = valveOpen[i] ? OPEN_POSITION : 0;
                movePower[i] = valveOpen[i] ? 1.0 : -1.0;
                moving[i] = true;
            }
        }
        lastAState = currentAState;

        // D-pad Down toggles the servos
        if (currentDpadDown && !lastDpadDown) {
            for (int i = 0; i < 6; i++) {
                servoAt90[i] = !servoAt90[i];
                if (servoAt90[i]) {
                    robot.gunFillServos[i].setPosition(1);
                } else {
                    robot.gunFillServos[i].setPosition(0.0);
                }
            }
        }
        lastDpadDown = currentDpadDown;

        // --- Movement and telemetry logic for each gun ---
        for (int i = 0; i < 6; i++) {
            int position = robot.gunFireMotors[i].getCurrentPosition();

            if (moving[i]) {
                if (valveOpen[i]) {
                    // Opening: move forward until we reach or exceed target
                    if (position < targetPosition[i]) {
                        robot.gunFireMotors[i].setPower(movePower[i]);
                    } else {
                        robot.gunFireMotors[i].setPower(0);
                        moving[i] = false;
                    }
                } else {
                    // Closing: move backward until we reach or go below target
                    if (position > targetPosition[i]) {
                        robot.gunFireMotors[i].setPower(movePower[i]);
                    } else {
                        robot.gunFireMotors[i].setPower(0);
                        moving[i] = false;
                        robot.gunFireMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        robot.gunFireMotors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    }
                }
            } else {
                // Manual control for all guns
                robot.gunFireMotors[i].setPower(gamepad1.right_stick_y);
            }

            // Telemetry for each individual gun (for debugging)
            telemetry.addData("Gun " + (i + 1) + " Encoder Position", position);
            telemetry.addData("Gun " + (i + 1) + " Target", targetPosition[i]);
            telemetry.addData("Gun " + (i + 1) + " Valve Open?", valveOpen[i]);
            telemetry.addData("Gun " + (i + 1) + " Moving?", moving[i]);
            telemetry.addData("Gun " + (i + 1) + " Servo Position", robot.gunFillServos[i].getPosition());
        }

        telemetry.update();
    }
}