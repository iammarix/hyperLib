package org.usfirst.frc.team69.util;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.command.WaitUntilCommand;

/**
 * A builder class to create commands in autonomous.  Currently, it wraps a
 * {@link CommandGroup}, but the implementation may change in future versions.
 * 
 * To build a command, add child commands using {@link #sequential(Command)}
 * and {@link #parallel(Command)}, then get a command using {@link #build()}.
 * 
 * @author James Hagborg
 *
 */
public class CommandBuilder {
    private final CommandGroup m_cmdGroup;
    
    /**
     * Construct a new CommandBuilder
     */
    public CommandBuilder() {
        m_cmdGroup = new CommandGroup();
    }
    
    /**
     * Construct a new CommandBuilder with a given name
     * @param name The name of the command to build
     */
    public CommandBuilder(String name) {
        m_cmdGroup = new CommandGroup(name);
    }
    
    /**
     * Add a command in sequence.  Execution of the next command will not
     * happen until this one finishes.
     * @param command The command to add in sequence
     * @return This CommandBuilder object
     */
    public CommandBuilder sequential(Command command) {
        m_cmdGroup.addSequential(command);
        return this;
    }
    
    /**
     * Add a command in sequence.  Execution of the next command will not
     * happen until this one finishes, or until the timeout expires
     * @param command The command to add in sequence
     * @param timeout The timeout, in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder sequential(Command command, double timeout) {
        m_cmdGroup.addSequential(command, timeout);
        return this;
    }
    
    /**
     * Add a command in parallel.  Execution of the next command will begin
     * immediately, while this one continues to execute in parallel.
     * 
     * Note that the command created will not end until all parallel commands
     * finish.
     * 
     * @param command The command to add in parallel
     * @return This CommandBuilder object
     */
    public CommandBuilder parallel(Command command) {
        m_cmdGroup.addParallel(command);
        return this;
    }
    
    /**
     * Add a command in parallel.  Execution of the next command will begin
     * immediately, while this one continues to execute in parallel.  The
     * command will be killed if the timeout expires.
     * 
     * Note that the command created will not end until all parallel commands
     * finish.
     * 
     * @param command The command to add in parallel
     * @param timeout The timeout, in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder parallel(Command command, double timeout) {
        m_cmdGroup.addParallel(command, timeout);
        return this;
    }
    
    /**
     * Wait for a given duration.
     * 
     * @param seconds The time to wait, in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForDuration(double seconds) {
        m_cmdGroup.addSequential(new WaitCommand(seconds));
        return this;
    }
    
    /**
     * Wait until a given match time.
     * 
     * @param seconds The match time to wait for, in seconds from the start
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForMatchTime(double seconds) {
        m_cmdGroup.addSequential(new WaitUntilCommand(seconds));
        return this;
    }
    
    /**
     * Wait until an arbitrary condition is met.
     * 
     * @param condition The condition to wait for.
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForCondition(BooleanSupplier condition) {
        m_cmdGroup.addSequential(QuickCommand.waitFor(condition));
        return this;
    }
    
    /**
     * Wait until an arbitrary condition is met, or until a timeout expires.
     * 
     * @param condition The condition to wait for
     * @param timeout The timeout in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForCondition(BooleanSupplier condition, double timeout) {
        m_cmdGroup.addSequential(QuickCommand.waitFor(condition), timeout);
        return this;
    }
    
    /**
     * Run a command if and only if a condition is met at runtime.
     * 
     * @param condition The condition to check
     * @param ifTrue The command to run if the condition is true
     * @return This CommandBuilder object
     */
    public CommandBuilder ifThen(BooleanSupplier condition, Command ifTrue) {
        return ifThenElse(condition, ifTrue, new InstantCommand());
    }
    
    /**
     * Run one of two commands, depending on if a condition is true.
     * 
     * @param condition The condition to check
     * @param ifTrue The command to run if the condition is true
     * @param ifFalse The command to run if the condition is false
     * @return This CommandBuilder object
     */
    public CommandBuilder ifThenElse(BooleanSupplier condition, Command ifTrue, Command ifFalse) {
        m_cmdGroup.addSequential(new ConditionalCommand(ifTrue, ifFalse) {
            @Override protected boolean condition() {
                return condition.getAsBoolean();
            }
        });
        return this;
    }
    
    /**
     * Build a command.  Note that for simplicity, calling this method 
     * multiple times will return the same {@link Command} object.  This
     * may change in the future, to match the behavior of other objects
     * following the builder pattern.
     * @return A command created from this builder
     */
    public Command build() {
        return m_cmdGroup;
    }
}