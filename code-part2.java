    interface Command {
    void execute();
    void undo();
}

// Receivers
class Light {
    public void turnOn() {
        System.out.println("[Light] Turning ON");
    }

    public void turnOff() {
        System.out.println("[Light] Turning OFF");
    }
}

class Thermostat {
    private int temperature = 20;

    public void setTemperature(int temp) {
        System.out.println("[Thermostat] Setting temperature to " + temp + "°C");
        temperature = temp;
    }

    public int getTemperature() {
        return temperature;
    }
}

// Concrete Commands
class TurnOnLightCommand implements Command {
    private Light light;

    public TurnOnLightCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOn();
    }

    public void undo() {
        light.turnOff();
    }
}

class SetThermostatCommand implements Command {
    private Thermostat thermostat;
    private int prevTemp;
    private int newTemp;

    public SetThermostatCommand(Thermostat thermostat, int newTemp) {
        this.thermostat = thermostat;
        this.newTemp = newTemp;
    }

    public void execute() {
        prevTemp = thermostat.getTemperature();
        thermostat.setTemperature(newTemp);
    }

    public void undo() {
        System.out.println("[Thermostat] Reverting to previous temperature");
        thermostat.setTemperature(prevTemp);
    }
}

// Invoker
class SmartHomeRemoteControl {
    private Map<String, Command> slots = new HashMap<>();
    private Command lastCommand;

    public void setCommand(String slot, Command command) {
        slots.put(slot, command);
    }

    public void pressButton(String slot) {
        if (slots.containsKey(slot)) {
            slots.get(slot).execute();
            lastCommand = slots.get(slot);
        }
    }

    public void undoButton() {
        if (lastCommand != null) {
            lastCommand.undo();
            lastCommand = null;
        }
    }
}

// Client
public class SmartHome {
    public static void main(String[] args) {
        Light light = new Light();
        Thermostat thermostat = new Thermostat();

        Command lightOn = new TurnOnLightCommand(light);
        Command setThermo = new SetThermostatCommand(thermostat, 22);

        SmartHomeRemoteControl remote = new SmartHomeRemoteControl();
        remote.setCommand("light", lightOn);
        remote.setCommand("thermo", setThermo);

        remote.pressButton("light");
        remote.pressButton("thermo");
        remote.undoButton();
    }
}


