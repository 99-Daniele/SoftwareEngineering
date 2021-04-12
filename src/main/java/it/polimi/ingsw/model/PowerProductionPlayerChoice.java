package it.polimi.ingsw.model;

public class PowerProductionPlayerChoice {

    private boolean firstPower;
    private boolean secondPower;
    private boolean thirdPower;
    private boolean basicPower;
    private Resource[] basicResources;
    private boolean firstAdditionalPower;
    private Resource additionalResource1;
    private boolean secondAdditionalPower;
    private Resource additionalResource2;
    private int choice;

    public boolean isFirstPower() {
        return firstPower;
    }

    public void setFirstPower() {
        this.firstPower = true;
    }

    public boolean isSecondPower() {
        return secondPower;
    }

    public void setSecondPower() {
        this.secondPower = true;
    }

    public boolean isThirdPower() {
        return thirdPower;
    }

    public void setThirdPower() {
        this.thirdPower = true;
    }

    public boolean isBasicPower() {
        return basicPower;
    }

    public Resource[] getResources(){
        return basicResources;
    }

    public void setBasicPower(Resource r1, Resource r2, Resource r3){
        basicResources = new Resource[3];
        basicPower = true;
        basicResources[0] = r1;
        basicResources[1] = r2;
        basicResources[2] = r3;
    }

    public boolean isFirstAdditionalPower() {
        return firstAdditionalPower;
    }

    public Resource getAdditionalResource1() {
        return additionalResource1;
    }

    public void setFirstAdditionalPower(Resource resource){
        firstAdditionalPower = true;
        additionalResource1 = resource;
    }

    public boolean isSecondAdditionalPower() {
        return secondAdditionalPower;
    }

    public Resource getAdditionalResource2() {
        return additionalResource2;
    }

    public void setSecondAdditionalPower(Resource resource){
        secondAdditionalPower = true;
        additionalResource2 = resource;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
