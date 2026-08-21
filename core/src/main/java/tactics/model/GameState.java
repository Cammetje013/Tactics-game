package tactics.model;

public class GameState {

    private Teams currentTeam = Teams.PLAYER;
    private int turnNumber = 1;

    public Teams currentTeam() {
        return currentTeam;
    }

    public int turnNumber() {
        return turnNumber;
    }

    public void endTurn(UnitRoster roster) {
        currentTeam = currentTeam == Teams.PLAYER ? Teams.CPU : Teams.PLAYER;
        if (currentTeam == Teams.PLAYER) {
            turnNumber++;
        }
        for (Unit unit : roster.units()) {
            if (unit.team == currentTeam) {
                unit.resetTurn();
            }
        }
    }
}
