package Practice.LLD.DesignPatterns.StrategyDesign;

import Practice.LLD.DesignPatterns.StrategyDesign.Strategy.PassesngerDriveStrategy;

public class NormalVehicle extends Vehicle{

    public NormalVehicle() {
        super(new PassesngerDriveStrategy());
    }
}
