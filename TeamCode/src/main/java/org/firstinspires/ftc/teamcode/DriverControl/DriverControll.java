package org.firstinspires.ftc.teamcode.DriverControl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.teamcode.RobotClass;

@TeleOp(name = "ONTOS Combined", group = "driving")
public class DriverControll extends OpMode {

    RobotClass robot;

    // Use arrays to manage the state for each gun individually
    boolean[] valveOpen = new boolean[6];
    boolean[] moving = new boolean[6];
    int[] targetPosition = new int[6];
    double[] movePower = new double[6];
    boolean[] servoAt90 = new boolean[6];

    // Last state for each button
    boolean lastAState = false;
    boolean lastBState = false;
    boolean lastXState = false;
    boolean lastYState = false;
    boolean lastRBState = false;
    boolean lastRTState = false;
    boolean lastDpadUpState = false;
    boolean lastDpadDownState = false;
    boolean lastDpadLeftState = false;
    boolean lastDpadRightState = false;
    boolean lastLBState = false;
    boolean lastLTState = false;

    // A single constant for the target encoder value
    static final int OPEN_POSITION = 7 * 28;

    @Override
    public void init() {
        robot = new RobotClass(hardwareMap);

        // Reverse one side of the drivetrain for tank drive
        robot.frontRight.setDirection(CRServo.Direction.REVERSE);
        robot.middleRight.setDirection(CRServo.Direction.REVERSE);
        robot.backRight.setDirection(CRServo.Direction.REVERSE);

        // Initialize all servos to the closed position (0.0)
        for (int i = 0; i < 6; i++) {
            robot.gunFillServos[i].setPosition(0.0);
        }
    }

    @Override
    public void start() {
        // Reset and set the run mode for all gun motors
        for (int i = 0; i < 6; i++) {
            robot.gunFireMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.gunFireMotors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    @Override
    public void loop() {
        // --- Drivetrain Logic ---
        double leftPower = gamepad1.left_stick_y;
        double rightPower = gamepad1.right_stick_y;

        robot.frontLeft.setPower(leftPower);
        robot.middleLeft.setPower(leftPower);
        robot.backLeft.setPower(leftPower);

        robot.frontRight.setPower(rightPower);
        robot.middleRight.setPower(rightPower);
        robot.backRight.setPower(rightPower);

        // --- Gun Logic ---
        // Read gamepad states once per loop
        boolean currentAState = gamepad1.a;
        boolean currentBState = gamepad1.b;
        boolean currentXState = gamepad1.x;
        boolean currentYState = gamepad1.y;
        boolean currentRBState = gamepad1.right_bumper;
        boolean currentRTState = gamepad1.right_trigger > 0.5; // Use a threshold for triggers
        boolean currentDpadUpState = gamepad1.dpad_up;
        boolean currentDpadDownState = gamepad1.dpad_down;
        boolean currentDpadLeftState = gamepad1.dpad_left;
        boolean currentDpadRightState = gamepad1.dpad_right;
        boolean currentLBState = gamepad1.left_bumper;
        boolean currentLTState = gamepad1.left_trigger > 0.5; // Use a threshold for triggers

        // Trigger logic for each gun's fire motor
        handleMotorToggle(0, currentAState, lastAState); // Gun 1 Fire
        handleMotorToggle(1, currentBState, lastBState); // Gun 2 Fire
        handleMotorToggle(2, currentXState, lastXState); // Gun 3 Fire
        handleMotorToggle(3, currentYState, lastYState); // Gun 4 Fire
        handleMotorToggle(4, currentRBState, lastRBState); // Gun 5 Fire
        handleMotorToggle(5, currentRTState, lastRTState); // Gun 6 Fire

        // Trigger logic for each gun's fill servo
        handleServoToggle(0, currentDpadUpState, lastDpadUpState); // Gun 1 Fill
        handleServoToggle(1, currentDpadDownState, lastDpadDownState); // Gun 2 Fill
        handleServoToggle(2, currentDpadLeftState, lastDpadLeftState); // Gun 3 Fill
        handleServoToggle(3, currentDpadRightState, lastDpadRightState); // Gun 4 Fill
        handleServoToggle(4, currentLBState, lastLBState); // Gun 5 Fill
        handleServoToggle(5, currentLTState, lastLTState); // Gun 6 Fill

        // Update the last state for each button
        lastAState = currentAState;
        lastBState = currentBState;
        lastXState = currentXState;
        lastYState = currentYState;
        lastRBState = currentRBState;
        lastRTState = currentRTState;
        lastDpadUpState = currentDpadUpState;
        lastDpadDownState = currentDpadDownState;
        lastDpadLeftState = currentDpadLeftState;
        lastDpadRightState = currentDpadRightState;
        lastLBState = currentLBState;
        lastLTState = currentLTState;

        // Movement and telemetry logic for each gun
        for (int i = 0; i < 6; i++) {
            int position = robot.gunFireMotors[i].getCurrentPosition();

            if (moving[i]) {
                if (valveOpen[i]) {
                    if (position < targetPosition[i]) {
                        robot.gunFireMotors[i].setPower(movePower[i]);
                    } else {
                        robot.gunFireMotors[i].setPower(0);
                        moving[i] = false;
                    }
                } else {
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
                // If a gun is not "firing," it has no manual control
                // You can add manual control for a single gun with a different button here if you want
                robot.gunFireMotors[i].setPower(0);
            }

            telemetry.addData("Gun " + (i + 1) + " Encoder Position", position);
            telemetry.addData("Gun " + (i + 1) + " Servo Position", robot.gunFillServos[i].getPosition());
        }

        // Add drivetrain telemetry
        telemetry.addData("Left Drivetrain Power", leftPower);
        telemetry.addData("Right Drivetrain Power", rightPower);

        telemetry.update();
    }

    private void handleMotorToggle(int gunIndex, boolean currentState, boolean lastState) {
        if (currentState && !lastState) {
            valveOpen[gunIndex] = !valveOpen[gunIndex];
            targetPosition[gunIndex] = valveOpen[gunIndex] ? OPEN_POSITION : 0;
            movePower[gunIndex] = valveOpen[gunIndex] ? 1.0 : -1.0;
            moving[gunIndex] = true;
        }
    }

    private void handleServoToggle(int gunIndex, boolean currentState, boolean lastState) {
        if (currentState && !lastState) {
            servoAt90[gunIndex] = !servoAt90[gunIndex];
            if (servoAt90[gunIndex]) {
                robot.gunFillServos[gunIndex].setPosition(1);
            } else {
                robot.gunFillServos[gunIndex].setPosition(0.0);
            }
        }
    }
}