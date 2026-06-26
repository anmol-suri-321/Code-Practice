package Practice.LLD.DesignPatterns.StrategyDesign;

import Practice.LLD.DesignPatterns.StrategyDesign.Strategy.SportsDriveStrategy;

public class SportsVehicle extends Vehicle{

    public SportsVehicle() {
        super(new SportsDriveStrategy());
    }
}
